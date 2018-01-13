package com.example.muradkhan.tictactoe

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

import java.util.HashSet

class GameFragment: Fragment() {
    val mLargeIds = arrayOf(R.id.large0, R.id.large1, R.id.large2, R.id.large3, R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8)
    val mSmallIds = arrayOf(R.id.small0, R.id.small1, R.id.small2, R.id.small3, R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8)
    private var mEntireBoard = Tile(this)
    private var mPlayer: Tile.Owner = Tile.Owner.X
    private var mAvailable = HashSet<Tile>()
    var mLargeTiles: Array<Tile?> = arrayOfNulls<Tile>(9)
    var mSmallTiles: Array<Array<Tile?>> = arrayOf<Array<Tile?>>(arrayOfNulls<Tile?>(9), arrayOfNulls<Tile?>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9))
    var mLastLarge: Int? = null
    var mLastSmall: Int? = null

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
        initGame()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View {
        val rootView = inflater.inflate(R.layout.large_board, container, false)
        initViews(rootView)
        updateAllTiles()
        return rootView
    }
    fun initGame() {
        Log.d("initGame", "Creating game")
        mEntireBoard = Tile(this)
        for (i in 0..8) {
            mLargeTiles[i] = Tile(this)
            for (j in 0..8) mSmallTiles[i][j] = Tile(this)
            mLargeTiles[i]?.subTiles = mSmallTiles[i]
        }
        mEntireBoard.subTiles = mLargeTiles
        mLastSmall = -1
        mLastLarge = -1
        setAvailableFromLastMove(mLastSmall as Int)
    }
    private fun initViews(rootView: View) {
        mEntireBoard.mView = rootView
        for (i in 0..8) {
            var outer: View = rootView.findViewById(mLargeIds[i])
            mLargeTiles[i]?.mView = outer
            for (j in 0..8) {
                var inner: ImageButton = outer.findViewById(mSmallIds[j])
                val fSmall = j
                val fLarge = i
                val smallTile = mSmallTiles[i][j]
                smallTile?.mView = inner
                inner.setOnClickListener(View.OnClickListener { view ->
                    if (smallTile != null && isAvailable(smallTile)) {
                        makeMove(fLarge, fSmall)
                        switchTurns()
                    }
                })
            }
        }
    }
    private fun makeMove(large: Int, small: Int) {
        mLastLarge = large
        mLastSmall = small
        val smallTile = mSmallTiles[large][small]
        val largeTile = mLargeTiles[large]
        smallTile?.mOwner = mPlayer
        setAvailableFromLastMove(small)
        val oldWinner: Tile.Owner = largeTile!!.mOwner
        var winner: Tile.Owner = largeTile!!.findWinner()
        if (winner != oldWinner) largeTile.mOwner = winner
        winner = mEntireBoard.findWinner()
        mEntireBoard.mOwner = winner
        updateAllTiles()
        if (winner != Tile.Owner.NEITHER) (activity as GameActivity).reportWinner(winner.name)
    }
    private fun switchTurns() { mPlayer = if (mPlayer == Tile.Owner.X) Tile.Owner.O else Tile.Owner.X }
    fun restartGame() {
        initGame()
        initViews(view)
        updateAllTiles()
    }

    private fun clearAvailable() { mAvailable.clear() }
    private fun addAvailableTile(tile: Tile) { mAvailable.add(tile) }
    fun isAvailable(tile: Tile): Boolean { return mAvailable.contains(tile) }
    fun setAvailableFromLastMove(small: Int) {
        clearAvailable()
        if (small != -1) {
            for (dest in 0..8) {
                // Elvis to remove error
                val tile: Tile = mSmallTiles[small][dest] ?: Tile(this)
                if (tile.mOwner == Tile.Owner.NEITHER) addAvailableTile(tile)
            }
        }
        if (mAvailable.isEmpty()) setAllAvailable()
    }
    private fun setAllAvailable() {
        for (i in 0..8) {
            for (j in 0..8) {
                // Shouldn't cause issue but might (had to add Elvis to get rid of error)
                val tile: Tile = mSmallTiles[i][j] ?: Tile(this)
                if (tile.mOwner == Tile.Owner.NEITHER) mAvailable.add(tile)
            }
        }
    }
    fun putState(gameData: String) {

    }
    fun updateAllTiles() {
        mEntireBoard.updateDrawableState()
        for (i in 0..8) {
            mLargeTiles[i]?.updateDrawableState()
            for (j in 0..8) {
                mSmallTiles[i][j]?.updateDrawableState()
            }
        }
    }
}

// The idiomatic way of making static functions in Kotlin
// These can't access private variables
fun GameFragment.getState(): String {
    var sBuilder = StringBuilder()
    sBuilder.append(mLastLarge)
    sBuilder.append(',')
    sBuilder.append(mLastSmall)
    sBuilder.append(',')
    for (i in 0..8) {
        for (j in 0..8) {
            sBuilder.append(mSmallTiles[i][j]?.mOwner?.name)
            sBuilder.append(',')
        }
    }
    return sBuilder.toString()
}
fun GameFragment.putState(gameData: String) {
    var fields = gameData.split(',')
    var index = 0
    mLastLarge = fields[index++].toInt()
    mLastSmall = fields[index++].toInt()
    for (i in 0..8) {
        for (j in 0..8) {
            val owner: Tile.Owner = Tile.Owner.valueOf(fields[index++])
            mSmallTiles[i][j]?.mOwner = owner
        }
    }
    setAvailableFromLastMove(mLastSmall as Int)
    updateAllTiles()
}