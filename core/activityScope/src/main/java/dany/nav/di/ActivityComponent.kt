package dany.nav.di

import androidx.activity.ComponentActivity
import androidx.navigation3.NavRecord
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

    @Provides
    fun provideLifecycle(activity: ComponentActivity) = activity.lifecycle
}

//object ActivityComponentNavContentWrapper : NavContentWrapper {
//    @Composable
//    override fun WrapContent(record: Record) {
//        val appComponent = findAppComponent()
//        val activityComponent = ActivityComponent::class.create(appComponent, LocalContext.current as ComponentActivity)
//    }
//}