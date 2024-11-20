package dany.nav.di

import androidx.compose.runtime.Composable
import androidx.navigation3.NavRecord
import kotlin.reflect.KClass

inline fun <reified T : Any> provideRecord(
    noinline content: @Composable (T) -> Unit,
): Pair<KClass<out Any>, RecordFactory> =
    (T::class as KClass<out Any>) to
            ({ key: T -> NavRecord(key = key, content = content) } as RecordFactory)