package com.example.muradkhan.tictactoe

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageButton

class Tile(val mGame: GameFragment) {
    public enum class Owner {
        X, O, NEITHER, BOTH
    }
    private val LEVEL_X = 0
    private val LEVEL_O = 1
    private val LEVEL_BLANK = 2
    private val LEVEL_AVAILABLE = 3
    private val LEVEL_TIE = 4
    var mOwner = Owner.NEITHER
    var mView: View? = null
    var subTiles: Array<Tile>? = null

    private fun getDrawableStates() {
        var level = getLevel()
        mView?.getBackground()?.setLevel(level) ?: return
        // Usually this would smart cast variable as ImageButton
        // BUT since mView is var property, it can't be certain
        // that it hasn't changed so we have to explicitly cast it
        if (mView is ImageButton) {
            val imageB: ImageButton = mView as ImageButton
            val drawable: Drawable = imageB.getDrawable()
            drawable.setLevel(level)
        }
    }
    private fun getLevel(): Int =
        when (mOwner) {
            Owner.X -> LEVEL_X
            Owner.O -> LEVEL_O
            Owner.BOTH -> LEVEL_TIE
            Owner.NEITHER -> if (mGame.isAvailable(this)) LEVEL_AVAILABLE else LEVEL_BLANK
        }
    fun findWinner(): Owner {
        if (mOwner != Owner.NEITHER) return mOwner
        var totalX = arrayOfNulls<Int>(4)
        var totalO = arrayOfNulls<Int>(4)
        countCaptures(totalX, totalO)
        if (totalX[3] > 0) return Owner.X
        if (totalO[3] > 0) return Owner.O
        var total = 0
        for (row in 0..2) {
            for (col in 0..2) {
                val owner = subTiles[3*row + col].mOwner
                if (owner != Owner.NEITHER) total++
            }
        }
        return if (total == 9) Owner.BOTH else Owner.NEITHER
    }
}