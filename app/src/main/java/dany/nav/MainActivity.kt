package dany.nav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.NavDisplay
import androidx.navigation3.NavRecord
import androidx.navigation3.rememberNavWrapperManager
import dagger.hilt.android.AndroidEntryPoint
import dany.hilt.HiltNavRecord
import dany.hilt.HiltNavRecordProvider
import dany.hilt.HiltRecord
import dany.nav.ui.theme.DanyNavDemoTheme
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var hiltNavRecordProvider: HiltNavRecordProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DanyNavDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val backstack = remember { mutableStateListOf<Any>("MainScreen") }
                    NavDisplay(
                        backstack = backstack,
                        modifier = Modifier.padding(innerPadding).consumeWindowInsets(innerPadding),
                        wrapperManager = rememberNavWrapperManager(emptyList()),
                        onBack = { backstack.removeAt(backstack.lastIndex) },
                    ) { key ->
                        if (key == "MainScreen") {
                            NavRecord(key) {
                                MainContent(backstack)
                            }
                        } else {
                            hiltNavRecordProvider.provideRecord(key)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent(backStack: MutableList<Any>) {
    Column {
        Button(
            onClick = { backStack.add(SecondScreen) }
        ) {
            Text("Go to second screen")
        }
    }
}

object SecondScreen

@HiltRecord
class SecondScreenHiltRecord @Inject constructor(
    private val colorMaker: ColorMaker
) : HiltNavRecord<SecondScreen> {
    @Composable
    override fun content(key: SecondScreen) {
        Column {
            Text("Navigated to a second screen!")
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
}

class ColorMaker @Inject constructor() {
    private val random = Random

    fun makeColor() = Color(
        red = random.nextInt(256),
        green = random.nextInt(256),
        blue = random.nextInt(256)
    )
}