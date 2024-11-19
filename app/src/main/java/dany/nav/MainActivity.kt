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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.NavDisplay
import androidx.navigation3.NavWrapperManager
import androidx.navigation3.Record
import androidx.navigation3.rememberNavWrapperManager
import dany.nav.colorscreen.ColorScreen
import dany.nav.di.AppComponent
import dany.nav.di.LetterScreen
import dany.nav.di.MainComponent
import dany.nav.di.create
import dany.nav.ui.theme.DanyNavDemoTheme
import kotlin.reflect.KClass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DanyNavDemoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
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
                        MainContent()
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent() {
    val records = (getAppComponent() as MainComponent).recordMap
    val backStack = remember { mutableStateListOf<Any>("Main") }
    NavDisplay(
        backstack = backStack,
        wrapperManager = rememberNavWrapperManager(emptyList()),
        onBack = { backStack.removeAt(backStack.lastIndex) },
    ) { key ->
        if (key == "Main") {
            Record("Main") {
                Column {
                    Text("List of screens available (press button to nav):")
                    Button(
                        onClick = { backStack.add(LetterScreen("A")) },
                    ) {
                        Text(text = "Nav to letter")
                    }
                    Button(
                        onClick = { backStack.add(ColorScreen) },
                    ) {
                        Text(text = "Nav to colors")
                    }
                }
            }
        } else {
            records.getValue(key::class).invoke(key)
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