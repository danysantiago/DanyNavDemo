package dany.nav.player

import android.util.Log
import androidx.lifecycle.ViewModel
import dany.nav.network.HttpClient
import me.tatarka.inject.annotations.Inject

@Inject
class PlayerViewModel(
    private val httpClient: HttpClient,
) : ViewModel() {
    init {
        Log.d("PlayerViewModel", "init")
    }
}