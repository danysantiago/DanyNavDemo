package dany.nav.di

import androidx.activity.ComponentActivity
import androidx.navigation3.NavRecord
import dany.nav.SavableMutableStateNavListFactory
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import me.tatarka.inject.annotations.IntoMap
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

    val navKeysSerializer: Map<KClass<Any>, KSerializer<Any>>

    val savableMutableStateNavListFactory: SavableMutableStateNavListFactory

    @Provides
    fun provideLifecycle(activity: ComponentActivity) = activity.lifecycle

    @Provides
    @IntoMap
    fun provideStringSerializer() = provideSerializer<String>()

    @Provides
    fun provideModule(
    ) = SerializersModule {
        polymorphic(baseClass = Any::class) {
            navKeysSerializer.entries.forEach { (kclass, serializer) ->
                subclass(kclass, serializer)
            }
        }
    }
}



//object ActivityComponentNavContentWrapper : NavContentWrapper {
//    @Composable
//    override fun WrapContent(record: Record) {
//        val appComponent = findAppComponent()
//        val activityComponent = ActivityComponent::class.create(appComponent, LocalContext.current as ComponentActivity)
//    }
//}