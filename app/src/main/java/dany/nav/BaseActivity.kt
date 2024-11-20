package dany.nav

import androidx.activity.ComponentActivity

abstract class BaseActivity : ComponentActivity() {

    val activityComponent by lazy {
        val parent = (application as DanyApplication).component
        parent.activityComponentFactory.createActivityComponent(this)
    }

}