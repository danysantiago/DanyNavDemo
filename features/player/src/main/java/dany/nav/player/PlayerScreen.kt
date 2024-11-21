package dany.nav.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import dany.nav.di.ActivityScope
import dany.nav.di.provideRecord
import dany.nav.player.di.PlayerComponent
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

data class PlayerScreen(val songId: Int)

@ContributesTo(ActivityScope::class)
interface PlayerScreenContributor {
    @Provides
    @IntoMap
    fun providePlayerScreen(
        playerComponentFactory: PlayerComponent.Factory,
        viewModelCreator: () -> PlayerViewModel,
    ) = provideRecord<PlayerScreen> {
        val playerComponent = remember { playerComponentFactory.createPlayerComponent() }
        val vm = viewModel { viewModelCreator.invoke() }
        PlayerScreenContent(it, playerComponent.controller, vm)
    }
}

@Composable
private fun PlayerScreenContent(
    playerScreen: PlayerScreen,
    controller: PlayerController,
    viewModel: PlayerViewModel,
) {
    Column {
        Text("Song #${playerScreen.songId}")
        Row {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterVertically),
                text = controller.playerState.elapsedTime.toSecondsString())
            Slider(
                modifier = Modifier.weight(1f),
                value = controller.playerState.elapsedTime,
                onValueChange = { },
                enabled = false,
                valueRange = 0f..controller.playerState.durationTime
            )
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterVertically),
                text = controller.playerState.durationTime.toSecondsString()
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { controller.onPlayPause() }
            ) {
                val buttonText = if (controller.playerState.isPlaying) "Pause" else "Play"
                Text(buttonText)
            }
        }
    }
}

private fun Float.toSecondsString(): String {
    val minutes = this.toInt() / 60
    val seconds = this.toInt() % 60
    return "%02d:%02d".format(minutes, seconds)
}