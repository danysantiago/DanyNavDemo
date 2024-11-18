package dany.nav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.NavDisplay
import androidx.navigation3.Record
import androidx.navigation3.rememberNavWrapperManager
import dany.nav.ui.theme.DanyNavDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DanyNavDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                        MainContent()
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent() {
    val backStack = remember { mutableStateListOf("A") }
    val manager = rememberNavWrapperManager(emptyList())
    NavDisplay(
        backstack = backStack,
        wrapperManager = manager,
        onBack = { backStack.removeAt(backStack.lastIndex) },
    ) { key ->
        when (key) {
            "A" -> Record("A") {
                Button(
                    onClick = { backStack.add("B") }
                ) {
                    Text("One way button to 'Screen B'")
                }
            }
            "B" -> Record("B") {
                Text("I am 'Screen B', no button here. Use the back button...")
            }
            else -> error("Unknown key: $key")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    DanyNavDemoTheme {
        MainContent()
    }
}