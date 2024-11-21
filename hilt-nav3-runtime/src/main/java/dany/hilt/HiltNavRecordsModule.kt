package dany.hilt

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.Multibinds
import kotlin.reflect.KClass

@Module
@InstallIn(ActivityComponent::class)
interface HiltNavRecordsModule {
    @Multibinds
    fun provideRecords(): Map<String, @JvmSuppressWildcards HiltNavRecord<*>>
}