package dany.nav.player.di

import dany.nav.di.ActivityScope
import dany.nav.player.PlayerController
import software.amazon.lastmile.kotlin.inject.anvil.ContributesSubcomponent
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesSubcomponent(PlayerScope::class)
@SingleIn(PlayerScope::class)
interface PlayerComponent {

    @ContributesSubcomponent.Factory(ActivityScope::class)
    interface Factory {
        fun createPlayerComponent(): PlayerComponent
    }

    val controller: PlayerController
}

@ContributesTo(ActivityScope::class)
interface PlayerComponentFactoryContributor {
    val playerComponentFactory: PlayerComponent.Factory
}

