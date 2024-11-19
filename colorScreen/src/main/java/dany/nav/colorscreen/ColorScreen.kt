package dany.nav.colorscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dany.nav.AppScope
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import androidx.navigation3.Record
import dany.nav.provideRecord

@ContributesTo(AppScope::class)
interface ColorComponent {
    @Provides
    @IntoMap
    fun provideColorScreen(colorMaker: ColorMaker) = provideRecord<ColorScreen> {
        ColorScreen(colorMaker)
    }
}

object ColorScreen

@Composable
fun ColorScreen(colorMaker: ColorMaker) {
    Column {
        Text("Navigated to Color Screen!")
        val color = colorMaker.makeColor()
        Text("... also here is a random color!")
        Box(
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
                .background(color)
        )
    }
}