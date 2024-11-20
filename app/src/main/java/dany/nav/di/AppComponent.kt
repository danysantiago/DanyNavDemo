package dany.nav.di

import dany.nav.network.HttpClient
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
interface AppComponent {

    val activityComponentFactory: ActivityComponent.Factory

    @Provides
    @SingleIn(AppScope::class)
    fun provideHttp() = HttpClient()
}