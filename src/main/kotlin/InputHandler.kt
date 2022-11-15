package com.mojang.escape

import java.awt.event.*
import java.util.*

class InputHandler: KeyListener, FocusListener, MouseListener, MouseMotionListener {
    val keys = BooleanArray(Keys.values().size)

    override fun mouseDragged(e: MouseEvent?) {
    }

    override fun mouseMoved(e: MouseEvent?) {
    }

    override fun mouseClicked(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }

    override fun mousePressed(e: MouseEvent?) {
    }

    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun focusGained(e: FocusEvent?) {
    }

    override fun focusLost(e: FocusEvent?) {
        Arrays.fill(keys, false)
    }

    override fun keyPressed(e: KeyEvent?) {
        if (e != null) {
            for (key in Keys.values()) {
                if (key.code == e.keyCode) {
                    keys[key.ordinal] = true
                    break
                }
            }
        }
    }

    override fun keyReleased(e: KeyEvent?) {
        if (e != null) {
            for (key in Keys.values()) {
                if (key.code == e.keyCode) {
                    keys[key.ordinal] = false
                    break
                }
            }
        }
    }

    override fun keyTyped(e: KeyEvent?) {
    }
}