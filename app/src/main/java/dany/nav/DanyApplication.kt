package dany.nav

import android.app.Application
import dany.nav.di.AppComponent
import dany.nav.di.create

class DanyApplication : Application() {

    val component by lazy {
        AppComponent::class.create()
    }
}
