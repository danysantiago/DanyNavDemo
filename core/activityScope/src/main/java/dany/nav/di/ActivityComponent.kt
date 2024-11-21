package dany.nav.di

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.ViewModelStoreNavContentWrapper
import androidx.navigation3.NavContentWrapper
import androidx.navigation3.NavRecord
import androidx.navigation3.SavedStateNavContentWrapper
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesSubcomponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.reflect.KClass

typealias RecordFactory = (key: Any) -> NavRecord<Any>

@ContributesSubcomponent(ActivityScope::class)
@SingleIn(ActivityScope::class)
interface ActivityComponent {

    @ContributesSubcomponent.Factory(AppScope::class)
    interface Factory {
        fun createActivityComponent(activity: ComponentActivity): ActivityComponent
    }

    val navRecordFactories: Map<KClass<out Any>, RecordFactory>

    val navWrappers: Set<NavContentWrapper>

    @Provides
    fun provideLifecycle(activity: ComponentActivity) = activity.lifecycle

    @Provides
    @IntoSet
    fun provideSavedStateWrapper(): NavContentWrapper = SavedStateNavContentWrapper

    @Provides
    @IntoSet
    fun provideViewModelStateWrapper(): NavContentWrapper = ViewModelStoreNavContentWrapper
}

//object ActivityComponentNavContentWrapper : NavContentWrapper {
//    @Composable
//    override fun WrapContent(record: Record) {
//        val appComponent = findAppComponent()
//        val activityComponent = ActivityComponent::class.create(appComponent, LocalContext.current as ComponentActivity)
//    }
//}