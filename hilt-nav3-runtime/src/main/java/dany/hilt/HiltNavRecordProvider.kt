package dany.hilt

import androidx.compose.runtime.Composable
import androidx.navigation3.NavRecord
import dagger.hilt.GeneratesRootInput
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

@ActivityScoped
class HiltNavRecordProvider @Inject internal constructor(
    private val navRecords: Map<String, @JvmSuppressWildcards Provider<HiltNavRecord<*>>>
) {
    fun <T : Any> provideRecord(key: T): NavRecord<T> {
        val fqn = key::class.qualifiedName!!
        val hiltNavRecord = navRecords.getValue(fqn).get() as HiltNavRecord<T>
        return NavRecord(key) { hiltNavRecord.content(it) }
    }
}

interface HiltNavRecord<T : Any> {
    @Composable
    fun content(key: T): Unit
}

@GeneratesRootInput
@Target(AnnotationTarget.CLASS)
annotation class HiltRecord