package dany.nav.hilt

import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class HiltNavRecordProcessor(
    val env: SymbolProcessorEnvironment
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val recordClassDeclarations = resolver
            .getSymbolsWithAnnotation("dany.hilt.HiltRecord")
            .filterIsInstance<KSClassDeclaration>()
            .mapNotNull {
                processRecordDeclaration(resolver, it)
            }
            .forEach {
                writeModule(it)
            }
        return emptyList()
    }

    private fun processRecordDeclaration(
        resolver: Resolver,
        recordDeclaration: KSClassDeclaration
    ): RecordModule? {
        val hasInjectConstructor = recordDeclaration
            .getConstructors()
            .firstOrNull { constructors ->
                constructors.annotations.firstOrNull {
                    it.shortName.asString() == "Inject"
                } != null
            } != null
        if (!hasInjectConstructor) {
            env.logger.error(
                "Class annotated with @HiltRecord must have at least one @Inject constructor.",
                recordDeclaration
            )
        }

        val superDeclaration = recordDeclaration.superTypes
            .firstOrNull {
                val declaration = it.resolve().declaration
                declaration.qualifiedName?.asString() == "dany.hilt.HiltNavRecord"
            }
        if (superDeclaration == null) {
            env.logger.error(
                "Class annotated with @HiltRecord must implement HiltNavRecord.",
                recordDeclaration
            )
            return null
        }

        val typeArg = superDeclaration.resolve().arguments.single()
        return RecordModule(
            originating = recordDeclaration.containingFile!!,
            keyQualifiedName = typeArg.toTypeName(),
            recordQualifiedName = recordDeclaration.toClassName()
        )
    }

    private fun writeModule(recordModule: RecordModule) {
        val moduleSimpleName =
            recordModule.recordQualifiedName.simpleNames.joinToString(separator = "_") + "_HiltModule"
        val moduleName = ClassName(recordModule.recordQualifiedName.packageName, moduleSimpleName)

        val typeSpec = TypeSpec.interfaceBuilder(moduleName)
            .addAnnotation(ClassName("dagger", "Module"))
            .addAnnotation(
                AnnotationSpec.builder(ClassName("dagger.hilt", "InstallIn"))
                    .addMember("%T::class", ClassName("dagger.hilt.android.components", "ActivityComponent"))
                    .build()
            )
            .addFunction(
                FunSpec.builder("provide")
                    .addAnnotation(ClassName("dagger", "Binds"))
                    .addAnnotation(ClassName("dagger.multibindings", "IntoMap"))
                    .addAnnotation(
                        AnnotationSpec.builder(ClassName("dagger.multibindings", "StringKey"))
                            .addMember("%S", recordModule.keyQualifiedName.toString())
                            .build()
                    )
                    .addModifiers(KModifier.ABSTRACT)
                    .addParameter("record", recordModule.recordQualifiedName)
                    .returns(
                        ClassName("dany.hilt", "HiltNavRecord")
                            .parameterizedBy(STAR)
                    )
                    .build()
            )
            .build()

        val fileSpec = FileSpec.builder(moduleName)
            .addType(typeSpec)
            .build()

        fileSpec.writeTo(env.codeGenerator, false, listOf(recordModule.originating))
    }

    data class RecordModule(
        val originating: KSFile,
        val keyQualifiedName: TypeName,
        val recordQualifiedName: ClassName,
    )

    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            return HiltNavRecordProcessor(environment)
        }
    }
}