package dany.nav

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dany.nav.di.AppComponent
import dany.nav.di.create

class DanyApplication : Application() {

    val appComponent by lazy { AppComponent::class.create() }
}

@Composable
fun getAppComponent(): AppComponent =
    (LocalContext.current.applicationContext as DanyApplication).appComponent