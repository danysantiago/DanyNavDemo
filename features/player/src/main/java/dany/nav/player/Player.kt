package dany.nav.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import dany.nav.network.HttpClient
import dany.nav.player.di.PlayerScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(PlayerScope::class)
class ExoPlayer(private val httpClient: HttpClient)

@Inject
@SingleIn(PlayerScope::class)
class PlayerState(
    private val exoPlayer: ExoPlayer,
    private val lifecycle: Lifecycle
) {
    private var ticker: Job? = null

    private var _isPlaying = mutableStateOf(false)
    val isPlaying: Boolean by _isPlaying

    private var _durationTime = mutableFloatStateOf(200f)
    val durationTime: Float by _durationTime

    private var _elapsedTime = mutableFloatStateOf(0f)
    val elapsedTime: Float by _elapsedTime

    fun togglePlayback() {
        _isPlaying.value = !_isPlaying.value
        if (_isPlaying.value) {
            ticker = lifecycle.coroutineScope.launch {
                while (true) {
                    delay(1000)
                    _elapsedTime.value += 1f
                }
            }
        } else {
            ticker?.cancel()
        }
    }
}

interface PlayerController {
    val playerState: PlayerState
    fun onPlayPause()
}

@Inject
@SingleIn(PlayerScope::class)
@ContributesBinding(PlayerScope::class)
class PlayerControllerImpl(
    lifecycle: Lifecycle,
    override val playerState: PlayerState
) : PlayerController {

    init {
        lifecycle.addObserver(
            object : androidx.lifecycle.DefaultLifecycleObserver {
                override fun onPause(owner: LifecycleOwner) {
                    if (playerState.isPlaying) {
                        playerState.togglePlayback()
                    }
                }
            }
        )
    }

    override fun onPlayPause() {
        playerState.togglePlayback()
    }
}