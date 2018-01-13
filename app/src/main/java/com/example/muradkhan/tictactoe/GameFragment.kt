package com.example.muradkhan.tictactoe

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

import java.util.HashSet
import java.util.Set

import com.example.muradkhan.tictactoe.Tile

class GameFragment: Fragment() {
    val mLargeIds = arrayOf(R.id.large0, R.id.large1, R.id.large2, R.id.large3, R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8)
    val mSmallIds = arrayOf(R.id.small0, R.id.small1, R.id.small2, R.id.small3, R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8)
    private var mEntireBoard = Tile(this)
    private var mLargeTiles: Array<Tile?> = arrayOfNulls<Tile>(9)
    private var mSmallTiles: Array<Array<Tile?>> = arrayOf<Array<Tile?>>(arrayOfNulls<Tile?>(9), arrayOfNulls<Tile?>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9))
    private var mPlayer: Tile.Owner = Tile.Owner.X
    private var mAvailable = HashSet<Tile>
    private var mLastLarge: Int? = null
    private var mLastSmall: Int? = null
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
    public fun initGame() {
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
        setAvailableFromLastMove(mLastSmall)
    }
    private fun initViews(rootView: View) {
        mEntireBoard.mView = rootView
        for (i in 0..8) {
            var outer: View = rootView.findViewById(mLargeIds[i])
            mLargeTiles.mView = outer
            for (j in 0..8) {
                var inner: ImageButton = outer.findViewById(mSmallIds[j])
                val fSmall = j
                val fLarge = i
                val smallTile = mSmallTiles[i][j]
                smallTile.mView = inner
                inner.setOnClickListener(View.OnClickListener { view ->
                    if (isAvailable(smallTile)) {
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
        smallTile.mOwner = mPlayer
        setAvailableTileFromLastMove(small)
        val oldWinner: Tile.Owner = largeTile.mOwner
        var winner: Tile.Owner = largeTile.findWinner()
        if (winner != oldWinner) largeTile.mOwner = winner
        winner = mEntireBoard.findWinner()
        mEntireBoard.mOwner = winner
        updateAllTiles()
        if (winner != Tile.Owner.NEITHER) GameActivity.reportWinner(winner)
    }
    private fun switchTurns() { mPlayer = if (mPlayer == Tile.Owner.X) Tile.Owner.O else Tile.Owner.X }
    private fun restartGame() {
        initGame()
        initViews(view)
        updateAllTiles()
    }

    private fun clearAvailable() { mAvailable.clear() }
    private fun addAvailableTile(tile: Tile) { mAvailable.add(tile) }
    fun isAvailable(tile: Tile): Boolean { mAvailable.contains(tile) }
    private fun setAvailableFromLastMove(small: Int) {
        clearAvailable()
        if (small != -1) {
            for (dest in 0..8) {
                val tile: Tile = mSmallTiles[small][dest]
                if (tile.mOwner == Tile.Owner.NEITHER) addAvailableTile(tile)
            }
        }
        if (mAvailable.isEmpty()) setAllAvailable()
    }
    private fun setAllAvailable() {
        for (i in 0..8) {
            for (j in 0..8) {
                val tile: Tile = mSmallTiles[i][j]
                if (tile.mOwner == Tile.Owner.NEITHER) mAvailable.add(tile)
            }
        }
    }
    fun getState(): String {
        var sBuilder = StringBuilder()
        sBuilder.append(mLastLarge)
        sBuilder.append(',')
        sBuilder.append(mLastSmall)
        sBuilder.append(',')
        for (i in 0..8) {
            for (j in 0..8) {
                sBuilder.append(mSmallTiles[i][j].mOwner.name())
                sBuilder.append(',')
            }
        }
        return sBuilder.toString()
    }
    fun putState(gameData: String) {
        var fields = gameData.split(',')
        var index = 0
        mLastLarge = fields[index++].toInt()
        mLastSmall = fields[index++].toInt()
        for (i in 0..8) {
            for (j in 0..8) {
                val owner: Tile.Owner = Tile.Owner.valueOf(fields[index++])
                mSmallTiles[i][j].mOwner = owner
            }
        }
        setAvailableFromLastMove(mLastSmall as Int)
        updateAllTiles()
    }
    fun updateAllTiles() {
        mEntireBoard.updateDrawableState()
        for (i in 0..8) {
            mLargeTiles[i].updateDrawableState()
            for (j in 0..8) {
                mSmallTiles[i][j].updateDrawableState()
            }
        }
    }
}