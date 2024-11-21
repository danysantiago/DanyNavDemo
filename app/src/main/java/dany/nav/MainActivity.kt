package dany.nav

import android.os.Bundle
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.ViewModelStoreNavContentWrapper
import androidx.navigation3.NavDisplay
import androidx.navigation3.NavRecord
import androidx.navigation3.SavedStateNavContentWrapper
import androidx.navigation3.rememberNavWrapperManager
import dany.nav.di.ActivityComponent
import dany.nav.player.PlayerScreen
import dany.nav.ui.theme.DanyNavDemoTheme

class MainActivity : BaseActivity() {
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
                        MainContent(activityComponent)
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent(activityComponent: ActivityComponent) {
    val navWrappers = activityComponent.navWrappers
    val recordFactories = activityComponent.navRecordFactories
    val backstack = remember { mutableStateListOf<Any>("MainScreen") }
    NavDisplay(
        backstack = backstack,
        wrapperManager = rememberNavWrapperManager(navWrappers.toList()),
        onBack = { backstack.removeAt(backstack.lastIndex) }
    ) { key ->
        if (key == "MainScreen") {
            NavRecord(Unit) {
                MainScreen(backstack)
            }
        } else {
            recordFactories.getValue(key::class).invoke(key)
        }
    }
}

@Composable
fun MainScreen(backstack: MutableList<Any>) {
    Column {
        Text("Main Screen")
        Button(
            onClick = { backstack.add(ProfileScreen) }
        ) {
            Text("Go to profile")
        }
        repeat(10) { index ->
            val id = index + 1
            Button(
                onClick = { backstack.add(PlayerScreen(id)) }
            ) {
                Text("Song $id")
            }
        }
    }
}
