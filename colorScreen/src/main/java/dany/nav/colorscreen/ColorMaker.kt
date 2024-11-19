package dany.nav.colorscreen

import androidx.compose.ui.graphics.Color
import me.tatarka.inject.annotations.Inject
import kotlin.random.Random

@Inject
class ColorMaker {
    private val random = Random

    fun makeColor() = Color(
        red = random.nextInt(256),
        green = random.nextInt(256),
        blue = random.nextInt(256)
    )
}