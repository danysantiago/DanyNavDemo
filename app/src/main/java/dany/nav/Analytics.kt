package dany.nav

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation3.NavContentWrapper
import androidx.navigation3.NavRecord
import dany.nav.di.ActivityScope
import dany.nav.network.HttpClient
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo


@ContributesTo(ActivityScope::class)
interface AnalyticsWrapperContributor {
    @Provides
    @IntoSet
    fun provideAnalysticsWrapper(okHttpClient: HttpClient): NavContentWrapper =
        object : NavContentWrapper {
            @Composable
            override fun <T : Any> WrapContent(record: NavRecord<T>) {
                DisposableEffect(record.key) {
                    okHttpClient.sendAnalytic(record.key)
                    onDispose {

                    }
                }
                record.content.invoke(record.key)
            }
        }
}

fun HttpClient.sendAnalytic(key: Any) {
    Log.d("Analytics", "Sending analytics for $key")
}
