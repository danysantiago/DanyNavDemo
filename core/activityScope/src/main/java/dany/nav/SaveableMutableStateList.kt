package dany.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.savedstate.read
import androidx.savedstate.serialization.decodeFromSavedState
import androidx.savedstate.serialization.encodeToSavedState
import androidx.savedstate.write
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import me.tatarka.inject.annotations.Inject
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
fun <T> snapshotStateListSaver(
    listSaver: Saver<List<T>, out Any> = autoSaver()
): Saver<SnapshotStateList<T>, Any> =
    with(listSaver as Saver<List<T>, Any>) {
        Saver(
            save = { state ->
                // We use toMutableList() here to ensure that save() is
                // sent a list that is saveable by default (e.g., something
                // that autoSaver() can handle)
                save(state.toList().toMutableList())
            },
            restore = { state -> restore(state)?.toMutableStateList() }
        )
    }

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun savableMutableStateListOf(
    serializers: Map<KClass<Any>, KSerializer<Any>>,
    module: SerializersModule,
    vararg elements: Any
): SnapshotStateList<Any> {
    return rememberSaveable(
        saver = snapshotStateListSaver(
            listSaver = listSaver(
                save = { list ->
                    list.map { element ->
                        val serializer = module.getPolymorphic(Any::class, element)!!
                        encodeToSavedState(serializer, element).apply {
                            write { putString("#class", element::class.java.canonicalName!!) }
                        }
                    }
                },
                restore = { list ->
                    list.map { element ->
                        val fqn = element.read { getString("#class") }
                        val serializer = serializers.getValue(Class.forName(fqn).kotlin as KClass<Any>)
                        decodeFromSavedState(serializer, element.apply { write { remove("#class") }})
                    }
                }
            )
        )
    ) {
        elements.toList().toMutableStateList()
    }
}

@Inject
class SavableMutableStateNavListFactory(
    private val navKeysSerializer: Map<KClass<Any>, KSerializer<Any>>,
    private val module: SerializersModule
) {
    @Composable
    fun of(vararg elements: Any) = savableMutableStateListOf(
        serializers = navKeysSerializer,
        module = module,
        *elements)
}