package dany.nav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.NavDisplay
import androidx.navigation3.NavWrapperManager
import androidx.navigation3.Record
import androidx.navigation3.rememberNavWrapperManager
import dany.nav.ui.theme.DanyNavDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DanyNavDemoTheme {
                val superBackStack = SuperBackStack()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomBarContent(superBackStack) }
                ) { innerPadding ->
                    Column(
                        Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    ) {
                        Text("Dany's Nav Demo")
                        Spacer(
                            Modifier
                                .padding(4.dp)
                        )
                        MainContent(superBackStack)
                    }
                }
            }
        }
    }
}

class SuperBackStack {
    private var currentKey = mutableStateOf("A")
    val key: String
        get() = currentKey.value

    val current: List<String>
        get() = theBackstack.getValue(key)

    private val theBackstack = mutableMapOf<String, MutableList<String>>(
        "A" to mutableStateListOf("A"),
        "B" to mutableStateListOf("B"),
        "C" to mutableStateListOf("C")
    )

    fun onBack() {
        val current = theBackstack.getValue(key)
        current.removeAt(current.lastIndex)
    }

    fun swap(key: String) {
        currentKey.value = key
    }

    fun navTo(key: String) {
        val current = theBackstack.getValue(key)
        current.add(key)
    }


}

@Composable
fun MainContent(backStack: SuperBackStack) {
    SingleTypeNavDisplay(
        backstack = remember(backStack.key) { backStack.current },
        wrapperManager = rememberNavWrapperManager(emptyList()),
        onBack = { backStack.onBack() },
    ) { key ->
        when (key) {
            "A", "C" -> Record(key) {
                ScreenNameText(key)
            }
            "B" -> Record(key) {
                Button(
                    onClick = { backStack.navTo("DeepB") }
                ) {
                    Text("I am 'Screen B'")
                }
            }
            "DeepB" -> Record(key) {
                Button(
                    onClick = { backStack.navTo("DeepDeepB") }
                ) {
                    Text("Deeper in B")
                }
            }
            "DeepDeepB" -> Record(key) {
                Text("At the bottom of B")
            }
            else -> error("Unknown key: $key")
        }
    }
}

@Composable
fun BottomBarContent(backStack: SuperBackStack) {
    Row(
        modifier = Modifier.background(Color.LightGray).padding(4.dp)
    ) {
        fun navigateTo(key: String) {
            backStack.swap(key)
        }
        Button(
            modifier = Modifier.weight(1f),
            onClick = { navigateTo("A") }
        ) {
            Text("Screen A")
        }
        Button(
            modifier = Modifier.weight(1f),
            onClick = { navigateTo("B") }
        ) {
            Text("Screen B")
        }
        Button(
            modifier = Modifier.weight(1f),
            onClick = { navigateTo("C") }
        ) {
            Text("Screen C")
        }
    }
}

@Composable
inline fun <reified T : Any> SingleTypeNavDisplay(
    backstack: List<T>,
    wrapperManager: NavWrapperManager,
    modifier: Modifier = Modifier,
    noinline onBack: () -> Unit = {},
    crossinline recordProvider: (key: T) -> Record<T>
) {
    NavDisplay(backstack, wrapperManager, modifier, onBack) {
        check(it is T) {
            "Expected key '$it' to be of type ${T::class.simpleName} but got ${it::class.simpleName}"
        }
        recordProvider(it)
    }
}

@Composable
fun ScreenNameText(letter: String) {
    Text(
        color = when (letter) {
            "A" -> Color.Red
            "B" -> Color.Magenta
            "C" -> Color.Blue
            else -> error("Unknown key: $letter")
        },
        text = "I am 'Screen $letter'"
    )
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    DanyNavDemoTheme {
        MainContent(backStack = SuperBackStack())
    }
}