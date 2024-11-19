package dany.nav

import androidx.compose.runtime.Composable
import kotlin.reflect.KClass
import androidx.navigation3.Record

inline fun <reified T : Any> provideRecord(
    noinline content: @Composable (T) -> Unit,
): Pair<KClass<out Any>, (Any) -> Record<Any>> =
    (T::class as KClass<out Any>) to
            ({ key: T -> Record(key = key, content = content) } as (Any) -> Record<Any>)