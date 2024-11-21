package dany.nav.di

import androidx.compose.runtime.Composable
import androidx.navigation3.NavRecord
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

inline fun <reified T : Any> provideRecord(
    noinline content: @Composable (T) -> Unit,
): Pair<KClass<out Any>, RecordFactory> =
    (T::class as KClass<out Any>) to
            ({ key: T -> NavRecord(key = key, content = content) } as RecordFactory)

inline fun <reified T : Any> provideSerializer(): Pair<KClass<Any>, KSerializer<Any>> =
    (T::class as KClass<Any>) to serializer<T>() as KSerializer<Any>