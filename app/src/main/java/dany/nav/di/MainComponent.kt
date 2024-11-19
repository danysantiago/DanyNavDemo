package dany.nav.di

import androidx.compose.material3.Text
import androidx.navigation3.Record
import dany.nav.AppScope
import dany.nav.provideRecord
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import kotlin.reflect.KClass

@ContributesTo(AppScope::class)
interface MainComponent {
    val recordMap: Map<KClass<out Any>, (Any) -> Record<Any>>

//    val navWrappers: Set<NavContentWrapper>

    @Provides
    @IntoMap
    fun provideLetterScreen() =
        provideRecord<LetterScreen> { letterScreen ->
            Text("My favorite letter is '${letterScreen.letter}' for 'Tom' the Cat!")
        }
}

data class LetterScreen(val letter: String) {
    override fun toString(): String {
        return "Letter Screen"
    }
}
