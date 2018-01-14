package com.example.muradkhan.tictactoe

import android.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.media.AudioManager
import android.media.SoundPool
import java.util.HashSet

class GameFragment : Fragment() {
    val mLargeIds = arrayOf(R.id.large0, R.id.large1, R.id.large2, R.id.large3, R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8)
    val mSmallIds = arrayOf(R.id.small0, R.id.small1, R.id.small2, R.id.small3, R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8)
    private var mEntireBoard = Tile(this)
    private var mPlayer: Tile.Owner = Tile.Owner.X
    private var mAvailable = HashSet<Tile>()
    var mLargeTiles: Array<Tile?> = arrayOfNulls<Tile>(9)
    var mSmallTiles: Array<Array<Tile?>> = arrayOf<Array<Tile?>>(arrayOfNulls<Tile?>(9), arrayOfNulls<Tile?>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9), arrayOfNulls<Tile>(9))
    var mLastLarge: Int? = null
    var mLastSmall: Int? = null
    var mHandler = Handler()

    private lateinit var soundPool: SoundPool
    // IDs for sounds
    private var soundClick: Int = 0
    private var soundMiss: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
        soundPool = SoundPool(3, AudioManager.STREAM_MUSIC, 0)
        soundClick = soundPool.load(activity, R.raw.click, 1)
        soundMiss = soundPool.load(activity, R.raw.failfare, 1)
        initGame()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.large_board, container, false)
        initViews(rootView)
        updateAllTiles()
        return rootView
    }

    fun initGame() {
        Log.d("initGame", "Creating game")
//        (controlFragment as ControlFragment).updateTurnDisplay("X")
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
                inner.setOnClickListener({ _ ->
                    if (smallTile != null && isAvailable(smallTile)) {
                        makeMove(fLarge, fSmall)
                        soundPool.play(soundClick, 0.5f, 0.5f, 0, 0, 1f)
                        think()
                    } else {
                        soundPool.play(soundMiss, 0.5f, 0.5f, 0, 0, 1f)
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
        var winner: Tile.Owner = largeTile.findWinner()
        if (winner != oldWinner) largeTile.mOwner = winner
        winner = mEntireBoard.findWinner()
        mEntireBoard.mOwner = winner
        updateAllTiles()
        if (winner != Tile.Owner.NEITHER) (activity as GameActivity).reportWinner(winner)
    }

    private fun switchTurns() {
        if (mPlayer == Tile.Owner.X) {
            mPlayer = Tile.Owner.O
//            (controlFragment as ControlFragment).updateTurnDisplay("O")
        } else {
            mPlayer = Tile.Owner.X
//            (controlFragment as ControlFragment).updateTurnDisplay("X")
        }
    }

    fun restartGame() {
        initGame()
        initViews(view)
        updateAllTiles()
    }

    private fun clearAvailable() {
        mAvailable.clear()
    }

    private fun addAvailableTile(tile: Tile) {
        mAvailable.add(tile)
    }

    fun isAvailable(tile: Tile): Boolean {
        return mAvailable.contains(tile)
    }

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

    fun updateAllTiles() {
        mEntireBoard.updateDrawableState()
        for (i in 0..8) {
            mLargeTiles[i]?.updateDrawableState()
            for (j in 0..8) {
                mSmallTiles[i][j]?.updateDrawableState()
            }
        }
    }

    private fun think() {
        (activity as GameActivity).startThinking()
        mHandler.postDelayed({
            if (activity != null) {
                if (mEntireBoard.mOwner == Tile.Owner.NEITHER) {
                    var move = pickMove()
                    if (move[0] != -1 && move[1] != -1) {
                        switchTurns()
                        makeMove(move[0], move[1])
                        switchTurns()
                    }
                }
            }
            (activity as GameActivity).stopThinking()
        }, 2000)
    }

    private fun pickMove(): Array<Int> {

        // Variables used in inner functions have to be declared b4 inner function
        var copiedBoard = mEntireBoard.deepCopy()

        // Always starts w/ mPlayer = O (CPU)
        fun inner(moveScore: MoveScore,
                        depth: Int): MoveScore {
            if (depth == 5) return moveScore
            mPlayer = if (mPlayer == Tile.Owner.X) Tile.Owner.O else Tile.Owner.X
            var bestLarge = -1
            var bestSmall = -1
            var bestValue = if (mPlayer == Tile.Owner.X) Int.MAX_VALUE else Int.MIN_VALUE
            for (i in 0..8) {
                for (j in 0..8) {
                    val smallTile = mSmallTiles[i][j]
                    if (smallTile != null && isAvailable(smallTile)) {
                        copiedBoard.subTiles[i]!!.subTiles[j]?.mOwner = mPlayer
                        var value = inner(
                                MoveScore(copiedBoard.evaluate(), arrayOf(i, j)),
                                depth + 1)
                        copiedBoard.subTiles[i]!!.subTiles[j]?.mOwner = Tile.Owner.NEITHER
                        if ( (mPlayer == Tile.Owner.X && value.score < bestValue) ||
                             (mPlayer == Tile.Owner.O && value.score > bestValue) ) {
                            bestValue = value.score
                            bestSmall = value.move[1]
                            bestLarge = value.move[0]
                        }
                    }
                }
            }
            return MoveScore(bestValue, arrayOf(bestLarge, bestSmall))
        }
        val startingPlayer = if (mPlayer == Tile.Owner.X) Tile.Owner.X else Tile.Owner.O
        val result = inner(
                MoveScore(Int.MAX_VALUE, arrayOf(-1, -1)),
                0).move
        mPlayer = startingPlayer
        return result
    }

    class MoveScore(var score: Int, var move: Array<Int>) {}
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