package dany.nav

import androidx.compose.material3.Text
import dany.nav.di.ActivityScope
import dany.nav.di.provideRecord
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(ActivityScope::class)
interface ProfileScreenRecordContributor {
    @Provides
    @IntoMap
    fun provideProfileScreenRecord() = provideRecord<ProfileScreen> {
        Text("Profile Screen")
    }
}

object ProfileScreen