Experimental butecode checker

We scan classes in the application to find
potentially illegal access to protected members.

See https://youtrack.jetbrains.com/issue/KT-42012


The project is a dirty utility, so some paths can be hardcoded


## LICENSE
MIT


## Tool run

We start the tool to check protected access from all Kotlin classes
in the IntelliJ, where a protected declaration is not from the
same package as the caller class. 

We run the test to check if there are more issues related to the
https://youtrack.jetbrains.com/issue/KT-42012 issue. 

The tool works as follows:
- take 1: Filter Setup 
     - We use JPS model to collect runtime classpath of all projet modules
     - We turn all .jar files into NIO2 ZipFilesystem Paths
     - we list all .class files
     - we make ASM to scan for all protected members of each
     - if there is no protected member is found, we fallback to the JVM Classpath 
- take 2: Scanning
     - for each .class file in the output                                               
     - we check if the file has Kotlin internal attribute (there is no need to scan non-Kotlin files)
     - we list all non-static member/properties access
     - we check our findings with the filter
     - `<init>` methods are excluded
     
 The following output is collected:
 
 ```
     Collected 3882 roots
     Collected 429088 classes
     Collected 229905 protected members

     class: com/intellij/frameworks/starters/local/wizard/StarterLibrariesStep
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in updateIncludedLibraries
     class: com/jetbrains/cidr/lang/symbols/symtable/serialization/OCSerializer$OCKryo
       Method [com/esotericsoftware/kryo/Kryo].newInstantiator @ (Ljava/lang/Class;)Lorg/objenesis/instantiator/ObjectInstantiator; in newInstantiator
     class: com/intellij/ssh/impl/sshj/tunnels/LocalTunnelServer$serve$channel$1
       Method [net/schmizz/sshj/connection/channel/direct/AbstractDirectChannel].buildOpenReq @ ()Lnet/schmizz/sshj/common/SSHPacket; in buildOpenReq
     class: com/jetbrains/python/sdk/PythonAnalysisToolSdk
       Method [com/intellij/openapi/util/UserDataHolderBase].clone @ ()Ljava/lang/Object; in clone
     class: org/jetbrains/kotlin/codegen/NoriaCodegenExtension
       Method [org/jetbrains/kotlin/codegen/intrinsics/IntrinsicMethod].toCallable @ (Lorg/jetbrains/kotlin/descriptors/FunctionDescriptor;ZLorg/jetbrains/kotlin/resolve/calls/model/ResolvedCall;Lorg/jetbrains/kotlin/codegen/ExpressionCodegen;)Lorg/jetbrains/kotlin/codegen/Callable; in resolveToCallable
     class: com/intellij/serialization/xml/KotlinAwareBeanBinding
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in serializeBaseStateInto
     class: com/intellij/serialization/stateProperties/MyMap
       Method [gnu/trove/THashMap].removeAt @ (I)V in removeAt
     class: com/jetbrains/cidr/cpp/toolchains/CPPToolchainsPanel$ToolchainPanel
       Method [javax/swing/JComboBox].isEditable @ ()Z in getSelectedComboBoxPathValue
       Method [javax/swing/JComboBox].isEditable @ ()Z in setSelectedComboBoxValue
     class: com/intellij/featureStatistics/fusCollectors/EventsIdentityThrottle
       Method [it/unimi/dsi/fastutil/ints/Int2LongOpenHashMap].size @ ()I in size
       Method [it/unimi/dsi/fastutil/ints/Int2LongOpenHashMap].size @ ()I in tryPass
     class: com/intellij/diagnostic/hprof/analysis/GCRootPathsTree$RootNode
       Method [it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap].values @ ()Ljava/util/Collection; in calculateTotalInstanceCount
       Method [it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap].values @ ()Ljava/util/Collection; in collectDisposedDominatorNodes
       Method [it/unimi/dsi/fastutil/ints/IntOpenHashSet].size @ ()I in createHotPathReport
     class: com/intellij/diagnostic/hprof/analysis/GCRootPathsTree
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in registerObject
     class: com/jetbrains/builtInHelp/search/HelpSearch$Companion
       Method [org/apache/lucene/search/IndexSearcher].search @ (Lorg/apache/lucene/search/Query;Lorg/apache/lucene/search/Collector;)V in search
     class: com/intellij/diagnostic/hprof/analysis/AnalyzeDisposer
       Method [it/unimi/dsi/fastutil/ints/IntOpenHashSet].size @ ()I in prepareDisposedObjectsSection
     class: com/intellij/diagnostic/hprof/analysis/AnalyzeGraph
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in traverseInstanceGraph
       Method [it/unimi/dsi/fastutil/longs/LongArrayList].size @ ()I in traverseInstanceGraph
     class: com/intellij/diagnostic/hprof/analysis/AnalyzeDisposer$InstanceStats
       Method [it/unimi/dsi/fastutil/longs/LongArrayList].size @ ()I in objectCount
       Method [it/unimi/dsi/fastutil/longs/LongOpenHashSet].size @ ()I in parentCount
       Method [it/unimi/dsi/fastutil/longs/LongOpenHashSet].size @ ()I in rootCount
     class: com/intellij/diagnostic/hprof/navigator/ObjectNavigatorOnAuxFiles
       Method [it/unimi/dsi/fastutil/longs/LongArrayList].size @ ()I in preloadInstance
     class: org/intellij/plugins/markdown/lang/parser/GFMCommentAwareMarkerProcessor
       Method [org/intellij/markdown/flavours/commonmark/CommonMarkMarkerProcessor].getMarkerBlockProviders @ ()Ljava/util/List; in <init>
       Method [org/intellij/markdown/flavours/commonmark/CommonMarkMarkerProcessor].populateConstraintsTokens @ (Lorg/intellij/markdown/parser/LookaheadText$Position;Lorg/intellij/markdown/parser/constraints/MarkdownConstraints;Lorg/intellij/markdown/parser/ProductionHolder;)V in populateConstraintsTokens
     class: org/jetbrains/r/rmarkdown/RMarkdownMarkerProcessor
       Method [org/intellij/markdown/flavours/commonmark/CommonMarkMarkerProcessor].populateConstraintsTokens @ (Lorg/intellij/markdown/parser/LookaheadText$Position;Lorg/intellij/markdown/parser/constraints/MarkdownConstraints;Lorg/intellij/markdown/parser/ProductionHolder;)V in populateConstraintsTokens
     class: com/intellij/testGuiFramework/framework/GuiTestLocalRunner
       Method [org/junit/runners/BlockJUnit4ClassRunner].describeChild @ (Lorg/junit/runners/model/FrameworkMethod;)Lorg/junit/runner/Description; in describeChild
       Method [org/junit/runners/BlockJUnit4ClassRunner].runChild @ (Lorg/junit/runners/model/FrameworkMethod;Lorg/junit/runner/notification/RunNotifier;)V in doRunChild
     class: com/intellij/testGuiFramework/framework/GuiTestSuiteRunner
       Method [org/junit/runners/Suite].runChild @ (Lorg/junit/runner/Runner;Lorg/junit/runner/notification/RunNotifier;)V in runChild
     class: com/intellij/javascript/debugger/console/LineAnnotationMaps
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in add
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in get
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in getLast
     log4j:WARN No appenders could be found for logger (io.netty.util.internal.logging.InternalLoggerFactory).
     log4j:WARN Please initialize the log4j system properly.
     log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
     class: com/intellij/profiler/ultimate/hprof/impl/HprofDump
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in getShortestPaths
       Method [it/unimi/dsi/fastutil/longs/Long2ObjectOpenHashMap].size @ ()I in getRootCount
       Method [it/unimi/dsi/fastutil/longs/Long2ObjectOpenHashMap].size @ ()I in getShortestPaths
       Method [it/unimi/dsi/fastutil/longs/Long2ObjectOpenHashMap].size @ ()I in getTypeCount
       Method [it/unimi/dsi/fastutil/longs/Long2ObjectOpenHashMap].values @ ()Ljava/util/Collection; in forEachType
       Method [it/unimi/dsi/fastutil/longs/Long2ObjectOpenHashMap].values @ ()Ljava/util/Collection; in getTypeByRegex
       Method [it/unimi/dsi/fastutil/longs/LongOpenHashSet].size @ ()I in getClassLoaderCount
     class: noria/TraceReader
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in scopeEndSlotIndex
     class: com/intellij/profiler/ultimate/hprof/impl/HprofDump$getTypeByName$1
       Method [it/unimi/dsi/fastutil/longs/Long2ObjectOpenHashMap].values @ ()Ljava/util/Collection; in apply
     class: noria/Trace
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in validate
     class: com/intellij/profiler/ultimate/hprof/visitors/IndexFileVisitor
       Method [it/unimi/dsi/fastutil/longs/Long2ObjectOpenHashMap].size @ ()I in postVisit
     class: org/jetbrains/io/fastCgi/FastCgiDecoder
       Method [it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap].values @ ()Ljava/util/Collection; in channelInactive
     class: com/intellij/util/JdomKt$getSpecialSaxBuilder$1
       Method [org/jdom/input/SAXBuilder].configureParser @ (Lorg/xml/sax/XMLReader;Lorg/jdom/input/sax/SAXHandler;)V in configureParser
     class: org/jetbrains/plugins/notebooks/jupyter/python/JupyterStatementParsing
       Method [com/jetbrains/python/parsing/console/PyConsoleParsingContext$ConsoleStatementParsing].continueParseIPythonCommand @ ()Z in continueParseIPythonCommand
     class: org/jetbrains/kotlin/android/configure/KotlinAndroidGradleModuleConfigurator
       Method [org/jetbrains/kotlin/idea/configuration/KotlinWithGradleConfigurator].getStdlibArtifactName @ (Lcom/intellij/openapi/projectRoots/Sdk;Ljava/lang/String;)Ljava/lang/String; in getStdlibArtifactName
     class: com/intellij/internal/performance/LatencyRecord
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in getAverageLatency
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in percentile
     class: com/intellij/sql/dialects/mongo/js/parser/MongoJSExternalParser$1
       Method [com/intellij/lang/javascript/parsing/StatementParser].doParseStatement @ (Z)V in doParseStatement
     class: org/jetbrains/android/dom/converters/ResourceNamespaceReference
       Method [com/intellij/psi/PsiReferenceBase].calculateDefaultRangeInElement @ ()Lcom/intellij/openapi/util/TextRange; in calculateDefaultRangeInElement
     class: com/android/tools/idea/sqlite/ui/mainView/DatabaseInspectorViewImpl
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in findTreeNode
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in updateDatabase
     class: com/android/tools/property/panel/impl/ui/InspectorLayoutManager
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in layoutContainer
     class: com/intellij/aws/cloudformation/tests/MyToStringStyle
       Method [org/apache/commons/lang/builder/ToStringStyle].appendDetail @ (Ljava/lang/StringBuffer;Ljava/lang/String;Ljava/lang/Object;)V in appendDetail
     class: com/intellij/execution/console/PrefixHistoryModel
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in getHistoryPrev
     class: com/jetbrains/cidr/xcode/settings/XCUnloadedResolveContextsPreview$Node
       Field [javax/swing/tree/DefaultMutableTreeNode].children @ Ljava/util/Vector; in getChildNodes
       Field [javax/swing/tree/DefaultMutableTreeNode].parent @ Ljavax/swing/tree/MutableTreeNode; in getParentNode
     class: com/jetbrains/cidr/execution/debugger/memory/AddressSpaceTest
       Method [junit/framework/TestCase].setUp @ ()V in setUp
     class: org/jetbrains/pycharm/ds/env/plugins/notebooks/JupyterRemoteFileSystemLocalRootRule
       Method [org/junit/rules/TemporaryFolder].after @ ()V in after
       Method [org/junit/rules/TemporaryFolder].before @ ()V in before
     class: com/intellij/ui/layout/MigLayoutDebugDumperKt$filter$2$1
       Method [org/yaml/snakeyaml/representer/Representer].representJavaBeanProperty @ (Ljava/lang/Object;Lorg/yaml/snakeyaml/introspector/Property;Ljava/lang/Object;Lorg/yaml/snakeyaml/nodes/Tag;)Lorg/yaml/snakeyaml/nodes/NodeTuple; in representJavaBeanProperty
     class: com/intellij/openapi/application/impl/ServerApplicationTest
       Method [junit/framework/TestCase].setUp @ ()V in setUp
     class: com/intellij/configurationStore/ModuleStoreTest
       Method [it/unimi/dsi/fastutil/objects/Object2IntOpenHashMap].size @ ()I in one batch update session if several modules changed
     class: com/jetbrains/django/fixtures/EdtRunner
       Method [org/junit/runners/BlockJUnit4ClassRunner].runChild @ (Lorg/junit/runners/model/FrameworkMethod;Lorg/junit/runner/notification/RunNotifier;)V in access$runChild$s357308197
     class: com/intellij/ui/svg/MyTranscoder
       Method [org/apache/batik/transcoder/image/ImageTranscoder].createRenderer @ ()Lorg/apache/batik/gvt/renderer/ImageRenderer; in createRenderer
     class: com/intellij/util/io/jna/DisposableMemory
       Method [com/sun/jna/Memory].dispose @ ()V in dispose
     class: com/intellij/execution/impl/ExecutionManagerImpl$MyProcessHandler
       Method [com/intellij/execution/process/ProcessHandler].notifyProcessTerminated @ (I)V in notifyProcessTerminated
     class: com/jetbrains/swift/symbols/impl/SwiftProjectOwnerSerializer
       Method [com/esotericsoftware/kryo/serializers/FieldSerializer].create @ (Lcom/esotericsoftware/kryo/Kryo;Lcom/esotericsoftware/kryo/io/Input;Ljava/lang/Class;)Ljava/lang/Object; in create
       Method [com/esotericsoftware/kryo/serializers/FieldSerializer].createCopy @ (Lcom/esotericsoftware/kryo/Kryo;Ljava/lang/Object;)Ljava/lang/Object; in createCopy
     class: com/jetbrains/swift/symbols/impl/SwiftProjectAndFileOwnerSerializer
       Method [com/esotericsoftware/kryo/serializers/FieldSerializer].create @ (Lcom/esotericsoftware/kryo/Kryo;Lcom/esotericsoftware/kryo/io/Input;Ljava/lang/Class;)Ljava/lang/Object; in create
       Method [com/esotericsoftware/kryo/serializers/FieldSerializer].createCopy @ (Lcom/esotericsoftware/kryo/Kryo;Ljava/lang/Object;)Ljava/lang/Object; in createCopy
     WARNING: An illegal reflective access operation has occurred
     WARNING: Illegal reflective access by org.codehaus.groovy.reflection.CachedClass (file:/Users/jonnyzzz/.m2/repository/org/codehaus/groovy/groovy/2.5.11/groovy-2.5.11.jar) to method java.lang.Object.finalize()
     WARNING: Please consider reporting this to the maintainers of org.codehaus.groovy.reflection.CachedClass
     WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
     WARNING: All illegal access operations will be denied in a future release
     class: com/android/tools/idea/sqlite/fileType/SqliteFileHandlerTest
       Method [junit/framework/TestCase].setUp @ ()V in setUp
     class: com/intellij/configurationStore/BinaryXmlWriter
       Method [it/unimi/dsi/fastutil/objects/Object2IntOpenHashMap].size @ ()I in writeString
     class: com/intellij/workspaceModel/storage/impl/containers/MutableNonNegativeIntIntMultiMap
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in size
     class: com/intellij/workspaceModel/storage/impl/containers/MutableNonNegativeIntIntMultiMap$ByList
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in toImmutable
     class: com/intellij/workspaceModel/storage/impl/VirtualFileUrlManagerImpl
       Method [it/unimi/dsi/fastutil/ints/IntArrayList].size @ ()I in getUrlById
     class: com/intellij/startupPerformanceTests/TestContextFactoryRule
       Method [org/junit/rules/ExternalResource].after @ ()V in after
     class: com/jetbrains/cidr/lang/refactoring/move/ui/OCMoveTopLevelDialog
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in selectNodeInNamespaceTree
     class: org/jetbrains/settingsRepository/git/GitExKt
       Method [org/eclipse/jgit/revwalk/RevWalk].reset @ ()V in getAheadCommitsCount
     class: com/jetbrains/python/run/PyVirtualEnvReader
       Method [com/intellij/util/EnvironmentUtil$ShellEnvReader].getShell @ ()Ljava/lang/String; in getShell
       Method [com/intellij/util/EnvironmentUtil$ShellEnvReader].getShellProcessCommand @ ()Ljava/util/List; in getShellProcessCommand
       Method [com/intellij/util/EnvironmentUtil$ShellEnvReader].readShellEnv @ (Ljava/util/Map;)Ljava/util/Map; in readPythonEnv
     class: com/jetbrains/swift/highlighting/SwiftSyntaxHighlighterTest
       Method [junit/framework/TestCase].setUp @ ()V in setUp
     class: com/jetbrains/liveEdit/elementsInspector/ElementsViewState$scheduleReformat$reformatTask$1$2$1$1
       Method [it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap].values @ ()Ljava/util/Collection; in run
     class: com/jetbrains/liveEdit/elementsInspector/ElementsViewState$scheduleReformat$reformatTask$1
       Method [it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap].size @ ()I in run
     class: com/intellij/diff/DiffTestCase
       Method [junit/framework/TestCase].setUp @ ()V in setUp
       Method [junit/framework/TestCase].tearDown @ ()V in tearDown
     class: fleet/app/measure/ServiceReporterKt
       Method [it/unimi/dsi/fastutil/objects/Object2ObjectOpenHashMap].values @ ()Ljava/util/Collection; in computeOwnTime
     class: com/intellij/diff/util/DiffPerformanceTest
       Method [junit/framework/TestCase].setUp @ ()V in setUp
     class: com/jetbrains/rdserver/ui/converters/JComboBoxConverter
       Method [javax/swing/JComboBox].isEditable @ ()Z in convert
     class: com/android/tools/idea/gradle/structure/configurables/variables/VariablesTableTestKt
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in getAppModuleChild
     class: com/android/tools/idea/gradle/structure/configurables/variables/VariablesTableTest
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in assertVariableValue
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in testAddAndEditBuildscriptSimpleVariable
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in testAddAndEditSimpleVariable
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in testBooleanVariableNodeDisplay
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in testListNodeDisplay
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in testListNodeRename
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in testMapNodeDisplay
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in testStringVariableNodeDisplay
       Method [javax/swing/tree/DefaultMutableTreeNode].children @ ()Ljava/util/Enumeration; in testVariableVariableNodeDisplay
     class: com/android/tools/idea/diagnostics/ExceptionRegistryTest
       Method [junit/framework/TestCase].setUp @ ()V in setUp
       Method [junit/framework/TestCase].tearDown @ ()V in tearDown
```   

After a short optimization (where we tell method from field) we've got
a report that was easy to test:

```
  Collected 3882 roots
  Collected 429088 classes
  Collected 229905 protected members
  class: com/intellij/sisyphus/ui/analyzer/IssueTypeListAction$FilterAction$shouldRequestFocus$1
    Method [javax/swing/JComponent].requestFocusInWindow @ ()Z in ancestorAdded
  class: com/intellij/refactoring/suggested/ParameterValuesPage
    Method [javax/swing/JComponent].requestFocusInWindow @ ()Z in defaultFocus
  class: com/android/tools/idea/common/editor/SplitEditor$SplitEditorAction
    Method [javax/swing/JComponent].requestFocusInWindow @ ()Z in setSelected
  class: com/intellij/serialization/PolymorphicBinding
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in doDeserialize
  class: com/intellij/ui/colorpicker/ColorPickerBuilder$build$panel$1
    Method [javax/swing/JComponent].requestFocusInWindow @ ()Z in requestFocusInWindow
  class: com/intellij/ui/DumpFocusableComponentHierarchyAction
    Method [javax/swing/PopupFactory].getPopup @ (Ljava/awt/Component;Ljava/awt/Component;II)Ljavax/swing/Popup; in actionPerformed
  class: com/intellij/workspaceModel/ide/impl/WorkspaceModelCacheImpl$PluginAwareEntityTypesResolver
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in resolveClass
  class: com/android/tools/idea/uibuilder/visual/CustomConfigurationAttributeCreationPalette
    Method [javax/swing/JComponent].requestFocusInWindow @ ()Z in requestFocusInWindow
  class: org/jetbrains/kotlin/codegen/NoriaCodegenExtension
    Method [org/jetbrains/kotlin/codegen/intrinsics/IntrinsicMethod].toCallable @ (Lorg/jetbrains/kotlin/descriptors/FunctionDescriptor;ZLorg/jetbrains/kotlin/resolve/calls/model/ResolvedCall;Lorg/jetbrains/kotlin/codegen/ExpressionCodegen;)Lorg/jetbrains/kotlin/codegen/Callable; in resolveToCallable
  class: com/android/tools/idea/uibuilder/property2/support/ColorSelectionAction
    Method [java/awt/Component].requestFocus @ ()V in restoreFocus
  class: com/jetbrains/builtInHelp/search/HelpSearch$Companion
    Method [org/apache/lucene/search/IndexSearcher].search @ (Lorg/apache/lucene/search/Query;Lorg/apache/lucene/search/Collector;)V in search
  class: fleet/plugins/client/PluginRegistry
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in instantiateImmediately
  log4j:WARN No appenders could be found for logger (io.netty.util.internal.logging.InternalLoggerFactory).
  log4j:WARN Please initialize the log4j system properly.
  log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
  class: com/intellij/workspaceModel/storage/TestEntityTypesResolver
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in resolveClass
  class: com/intellij/openapi/wm/impl/ToolWindowManagerImpl
    Method [java/awt/Component].requestFocusInWindow @ ()Z in hideIfNeededAndShowAfterTask
  class: com/intellij/openapi/wm/impl/WindowInfoImpl
    Method [java/lang/Object].clone @ ()Ljava/lang/Object; in clone
  class: com/jetbrains/rhizomedb/impl/traits/ByteBuddyExtensionsKt
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in defineDefaultMethod
  class: com/jetbrains/cidr/fileTypes/ImageSetFileType
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in getIcon
  class: com/intellij/openapi/ui/messages/MessagesService$Companion
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in getInstance
  class: com/intellij/ide/util/treeView/TreeSmartSelectTest
    Method [javax/swing/tree/DefaultTreeModel].getPathToRoot @ (Ljavax/swing/tree/TreeNode;)[Ljavax/swing/tree/TreeNode; in select
  class: com/intellij/openapi/externalSystem/model/SerializationKt$createCacheReadConfiguration$1
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in invoke
  class: com/intellij/openapi/externalSystem/model/SerializationKt$createDataClassResolver$1
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in invoke
  class: com/android/tools/property/panel/impl/ui/FlagPropertyPanel
    Method [java/awt/Component].requestFocus @ ()V in hideBalloonAndRestoreFocusOnEditor
  class: com/android/tools/property/panel/impl/ui/PropertyTextFieldWithLeftButton
    Method [javax/swing/JComponent].requestFocusInWindow @ ()Z in requestFocusInWindow
  class: com/android/tools/property/ptable2/impl/PFormTableImpl
    Method [java/awt/Component].requestFocusInWindow @ ()Z in transferFocusToLastEditor
  class: com/jetbrains/rdclient/ui/bindableUi/views/listControl/BeTreeAndTableListenersKt
    Method [javax/swing/JComponent].requestFocusInWindow @ ()Z in startEditing
  class: com/android/tools/adtui/LabelWithEditButtonKt$focusInWindowIf$1
    Method [javax/swing/JComponent].requestFocusInWindow @ ()Z in invoke
  class: com/android/tools/adtui/LabelWithEditButtonKt
    Method [javax/swing/JComponent].requestFocusInWindow @ ()Z in focusInWindowIf
  class: com/android/tools/idea/ui/resourcemanager/explorer/ResourceDetailView
    Method [java/awt/Component].requestFocusInWindow @ ()Z in <init>
    Method [java/awt/Component].requestFocusInWindow @ ()Z in requestFocusInWindow
  class: com/android/tools/idea/ui/resourcemanager/explorer/ResourceDetailView$focusRequestMouseAdapter$1
    Method [javax/swing/JComponent].requestFocusInWindow @ ()Z in mousePressed
  class: com/android/tools/idea/ui/resourcechooser/colorpicker2/ColorPickerBuilder$build$panel$1
    Method [javax/swing/JComponent].requestFocusInWindow @ ()Z in requestFocusInWindow
  class: com/android/tools/idea/ui/resourcechooser/HorizontalTabbedPanelBuilder$build$1
    Method [java/awt/Component].requestFocusInWindow @ ()Z in stateChanged
  class: com/android/tools/idea/ui/resourcechooser/MyTabbedPane
    Method [java/awt/Component].requestFocusInWindow @ ()Z in requestFocusInWindow
  class: com/jetbrains/rd/platform/ui/bedsl/extensions/BeControlExtensionsKt$bindFocus$1$1$1
    Method [javax/swing/JComponent].requestFocusInWindow @ ()Z in run
  WARNING: An illegal reflective access operation has occurred
  WARNING: Illegal reflective access by org.codehaus.groovy.reflection.CachedClass (file:/Users/jonnyzzz/.m2/repository/org/codehaus/groovy/groovy/2.5.11/groovy-2.5.11.jar) to method java.lang.Object.finalize()
  WARNING: Please consider reporting this to the maintainers of org.codehaus.groovy.reflection.CachedClass
  WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
  WARNING: All illegal access operations will be denied in a future release
  class: com/intellij/openapi/components/ComponentManagerConfigurationTest
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in loadClasses
  class: com/intellij/testGuiFramework/impl/GuiTestThread
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in loadClassFromPlugins
  class: com/intellij/testGuiFramework/launcher/GuiTestLocalLauncher
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in getClassFile
  class: com/intellij/testGuiFramework/driver/ExtendedJTreePathFinder$getPathToNode$path$1
    Method [javax/swing/tree/DefaultTreeModel].getPathToRoot @ (Ljavax/swing/tree/TreeNode;)[Ljavax/swing/tree/TreeNode; in invoke
  class: com/intellij/testGuiFramework/recorder/compile/LocalCompiler
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in run
  class: com/android/tools/property/ptable2/impl/PTableImplTest
    Method [java/awt/Component].requestFocusInWindow @ ()Z in testNavigateBackwardsThroughTable
    Method [java/awt/Component].requestFocusInWindow @ ()Z in testNavigateForwardsThroughTable
  class: com/intellij/testGuiFramework/fixtures/ComboBoxActionFixture$Companion$ourComboBoxButtonClass$2
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in invoke
  class: com/intellij/testGuiFramework/generators/Generators
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in getGenerators
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in getGlobalContextGenerators
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in getLocalContextCodeGenerator
  class: com/intellij/openapi/externalSystem/model/DataNodeTest$ProjectSystemIds are re-used after deserialization$deserializedList$1
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in invoke
  class: com/intellij/openapi/externalSystem/model/DataNodeTest
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in instance of class from a classloader can be deserialized
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in instance of class from a our own classloader can be deserialized
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in proxy instance can be deserialized
  class: com/intellij/javascript/debugger/execution/JavaScriptDebugConfigurationProducer
    Method [java/lang/Object].clone @ ()Ljava/lang/Object; in clone
  class: com/intellij/workspaceModel/storage/impl/SimpleEntityTypesResolver
    Method [java/lang/ClassLoader].loadClass @ (Ljava/lang/String;)Ljava/lang/Class; in resolveClass
  class: com/intellij/workspaceModel/storage/impl/containers/MutableNonNegativeIntIntMultiMap
    Method [java/lang/Object].clone @ ()Ljava/lang/Object; in startWrite
  class: org/jetbrains/settingsRepository/git/GitExKt
    Method [org/eclipse/jgit/revwalk/RevWalk].reset @ ()V in getAheadCommitsCount
  
  Process finished with exit code 0

``` 
  
To make sure the tool works, I've reverted the real bug to test if it
is included:

... 
class: com/android/tools/idea/gradle/structure/configurables/variables/VariablesTable$NameCellEditor
  Field [javax/swing/JTable].selectionBackground @ Ljava/awt/Color; in getTableCellEditorComponent
```                                                                                               

We can hope the tool works as expected
        

