package com.mojang.escape.menu

import com.mojang.escape.*
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.Bitmap

class LoseMenu(private val player: Player, lastMenu: Menu? = null): Menu(lastMenu) {
    /**
     * The number of ticks remaining until the Continue button is displayed.
     */
    private var tickDelay = 30

    override fun render(target: Bitmap) {
        target.draw(Art.logo, 0, 10, 0, 39, 160, 23, Art.getCol(0xFFFFFF))

        val seconds = (player.time / 60) % 60
        val minutes = (player.time / 60) / 60

        target.draw("gui.menu.lose.trinkets".translatable and ("" + player.loot + "/12"), 40, 45 + 10 * 0, Art.getCol(0x909090))
        target.draw("gui.menu.lose.time".translatable and "%d:%02d".format(minutes, seconds), 40, 45 + 10 * 1, Art.getCol(0x909090))

        if (tickDelay == 0) {
            target.draw("-> " and "gui.menu.lose.buttonContinue".translatable, 40, target.height - 40, Art.getCol(0xFFFF80))
        }
    }

    override fun tick(game: Game, keys: BooleanArray, up: Boolean, down: Boolean, left: Boolean, right: Boolean, use: Boolean) {
        if (tickDelay > 0) {
            tickDelay--
        } else if (use) {
            Sound.click1.play()
            game.menu = TitleMenu()
        }
    }

}