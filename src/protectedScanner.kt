package aha

import org.objectweb.asm.*
import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

fun main() {

    val protectedLookup = collectProtectedMembers()
//    val protectedLookup = ClassPathProtected

    val home = Paths.get("/Users/jonnyzzz/Work/intellij-kt14/out")
//    val home = Paths.get("out")
    val classes = Files.walk(home)
        .parallel()
        .filter { Files.isReadable(it) && it.fileName.toString().endsWith(".class") }
        .toList()

    classes.stream().parallel().forEach {
        readClass(it, protectedLookup)
    }
}

class StopParsing : RuntimeException()

fun readClass(file: Path, protectedLookup: ProtectedMemberLookup) {
    try {
        readClassInt(file, protectedLookup)
    } catch (p: StopParsing) {
        //NOP
    }
}

fun isSamePackage(clazzA: String, clazzB : String) : Boolean {
    val iA = clazzA.lastIndexOf('/')
    val iB = clazzB.lastIndexOf('/')

    if (iA == iB && iA >= 0 && iB >= 0) {
        return clazzA.subSequence(0, iA) == clazzB.subSequence(0, iB)
    }
    return false
}

private fun readClassInt(file: Path, protectedLookup: ProtectedMemberLookup) {
    val reader = ClassReader(Files.readAllBytes(file))

    val className = reader.className
    val accessFields = mutableListOf<String>()

    fun isSkipClass(clazz: String, member: String) : Boolean {
        if (clazz == "java/lang/StringBuilder") return true
        if (clazz.startsWith("kotlin/")) return true
        if (clazz == className) return true
        if (isSamePackage(className, clazz)) return true
        if (!protectedLookup.isProtectedMember(clazz, member)) return true
        return false
    }

    reader.accept(object : ClassVisitor(Opcodes.ASM8) {
        var isKotlinClass = false

        override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
            isKotlinClass = isKotlinClass || descriptor == "Lkotlin/Metadata;"
            return null
        }

        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor? {
            if (!isKotlinClass) throw StopParsing()

            return object : MethodVisitor(Opcodes.ASM8) {
                val methodName = name

                override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
                    if (opcode == Opcodes.GETSTATIC) return
                    if (opcode == Opcodes.PUTSTATIC) return
                    if (isSkipClass(owner, name)) return
                    accessFields += "Field [$owner].$name @ $descriptor in $methodName"
                }

                override fun visitMethodInsn(
                    opcode: Int,
                    owner: String,
                    name: String,
                    descriptor: String,
                    isInterface: Boolean
                ) {
                    if (opcode == Opcodes.INVOKEINTERFACE) return
                    if (opcode == Opcodes.INVOKESTATIC) return
                    if (opcode == Opcodes.INVOKESTATIC) return
                    if (name == "<init>") return
                    if (isSkipClass(owner, name)) return
                    accessFields += "Method [$owner].$name @ $descriptor in $methodName"
                }
            }
        }
    }, 0)

    if (accessFields.isEmpty()) return

    println("class: $className")
    for (accessField in accessFields.toSortedSet()) {
        println("  $accessField")
    }
}
