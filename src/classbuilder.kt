package aha

import com.intellij.openapi.util.io.FileUtil
import com.intellij.util.SystemProperties
import org.jetbrains.jps.model.JpsElementFactory
import org.jetbrains.jps.model.java.JpsJavaExtensionService
import org.jetbrains.jps.model.serialization.JpsModelSerializationDataService
import org.jetbrains.jps.model.serialization.JpsProjectLoader
import org.objectweb.asm.*
import java.io.File
import java.lang.reflect.Modifier
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream
import kotlin.streams.toList


fun main() {
    collectProtectedMembers()
}

interface ProtectedMemberLookup {
    fun isProtectedMember(member: ProtectedMember) : Boolean
}

object AllProtected : ProtectedMemberLookup {
    override fun isProtectedMember(member: ProtectedMember) = true
}

object ClassPathProtected: ProtectedMemberLookup {
    private val cache = ConcurrentHashMap<ProtectedMember, Boolean>()
    override fun isProtectedMember(member: ProtectedMember) =
        cache.computeIfAbsent(member) {
            runCatching {
                with(member) {
                    member in listProtectedMembers(ClassReader(className))
                }
            }.getOrNull() == true
        }
}

fun collectProtectedMembers() :ProtectedMemberLookup{
    val model = JpsElementFactory.getInstance().createModel() ?: throw Exception("Couldn't create JpsModel")

    val pathVariablesConfiguration =
        JpsModelSerializationDataService.getOrCreatePathVariablesConfiguration(model.global)

    val m2HomePath = File(SystemProperties.getUserHome())
        .resolve(".m2")
        .resolve("repository")
    pathVariablesConfiguration.addPathVariable("MAVEN_REPOSITORY", m2HomePath.canonicalPath)

    val root = Paths.get("/Users/jonnyzzz/Work/intellij-kt14")
    val kotlinPath = root.resolve("community/build/dependencies/build/kotlin/Kotlin/kotlinc")
    pathVariablesConfiguration.addPathVariable("KOTLIN_BUNDLED", kotlinPath.toString())

    val pathVariables = JpsModelSerializationDataService.computeAllPathVariables(model.global)
    JpsProjectLoader.loadProject(model.project, pathVariables, root.toString())

    JpsJavaExtensionService.getInstance().getProjectExtension(model.project)!!.outputUrl =
        "file://${FileUtil.toSystemIndependentName(root.resolve("out").toString())}"

    val allRoots = model.project.modules.parallelStream().flatMap { module ->
        JpsJavaExtensionService
            .dependencies(module)
            .recursively()
            .classes()
            .roots
            .stream()
    }.distinct().toList()
    println("Collected ${allRoots.size} roots")

    val allClasses = allRoots.parallelStream().flatMap {
        when {
            it.isDirectory -> Files.walk(it.toPath())
            it.isFile && it.name.endsWith(".jar") -> FileSystems
                .newFileSystem(URI("jar:" + it.toURI()), mapOf<String, Any>())
                .rootDirectories
                .toList()
                .stream()
                .flatMap { Files.walk(it) }

            else -> Stream.empty()
        }
            .parallel()
            .filter {
                Files.isRegularFile(it) && it.fileName.toString().endsWith(".class")
            }
    }.toList().distinct()

    println("Collected ${allClasses.size} classes")

    val allProtectedMembers = allClasses
        .parallelStream()
        .flatMap { listProtectedMembers(it).stream()}
        .toList()
        .filterNotNull()

    println("Collected ${allProtectedMembers.size} protected members")

    val allMembersSet = allProtectedMembers.groupBy { it }
    return object : ProtectedMemberLookup {
        override fun isProtectedMember(member: ProtectedMember): Boolean {
            if (member in allMembersSet) return true
            return ClassPathProtected.isProtectedMember(member)
        }
    }
}

sealed class ProtectedMember {
    abstract val className: String
    abstract val memberName: String

    var extra: String = ""

    data class Method(
        override val className: String,
        override val memberName: String
    ) : ProtectedMember()

    data class Field(
        override val className: String,
        override val memberName: String
    ) : ProtectedMember()
}


private fun listProtectedMembers(file: Path) : List<ProtectedMember> {
    val reader = ClassReader(Files.readAllBytes(file))
    return listProtectedMembers(reader)
}

private fun listProtectedMembers(reader: ClassReader) : List<ProtectedMember> {
    val protectedMembers = mutableListOf<ProtectedMember>()

    val className = reader.className

    reader.accept(object: ClassVisitor(Opcodes.ASM8) {
        override fun visitField(
            access: Int,
            name: String,
            descriptor: String?,
            signature: String?,
            value: Any?
        ): FieldVisitor? = null.apply {
            if (access and Opcodes.ACC_PROTECTED == Opcodes.ACC_PROTECTED) {
                protectedMembers += ProtectedMember.Field(className, name).also { it.extra = "Field: $name @ $descriptor" }
            }
        }

        override fun visitMethod(
            access: Int,
            name: String,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor? = null.apply {
            if (access and Opcodes.ACC_PROTECTED == Opcodes.ACC_PROTECTED) {
                protectedMembers += ProtectedMember.Method(className, name).also { it.extra = "Member: $name @ $descriptor" }
            }
        }
    }, ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)

    return protectedMembers
}
