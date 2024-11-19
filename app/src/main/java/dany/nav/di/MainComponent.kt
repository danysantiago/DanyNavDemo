package dany.nav.di

import androidx.compose.material3.Text
import androidx.navigation3.NavContentWrapper
import androidx.navigation3.Record
import dany.nav.AppScope
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface MainComponent {
    val recordMap: Map<String, Record<String>>

//    val navWrappers: Set<NavContentWrapper>

    @Provides
    @IntoMap
    fun provideLetterScreen() = "A Letter".let { key ->
        key to Record(key) {
            Text("My favorite letter is 'T' for 'Tom' the Cat!")
        }
    }
}
