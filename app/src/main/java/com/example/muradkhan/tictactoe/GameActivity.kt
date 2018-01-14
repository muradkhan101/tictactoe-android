package com.example.muradkhan.tictactoe

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.media.MediaPlayer
import android.os.Handler

class GameActivity : Activity() {
    val KEY_RESTORE: String = "key_restore"
    val PREF_RESTORE: String = "pref_restore"
    var mGameFragment = fragmentManager.findFragmentById(R.id.fragment_game)
    private lateinit var mediaPlayer: MediaPlayer
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?): Unit {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        mGameFragment = fragmentManager.findFragmentById(R.id.fragment_game)
        val restore = intent.getBooleanExtra(KEY_RESTORE, false)
        if (restore) {
            val gameData = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_RESTORE, null)
            if (gameData != null) (mGameFragment as GameFragment).putState(gameData)
        }
        Log.d("GameActivity", "restore: " + restore)
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer = MediaPlayer.create(this, R.raw.game)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        // class functions would work here, just had to do cast first
        // even utility functions didnt work without cast
        val gameData: String = (mGameFragment as GameFragment).getState()
        getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putString(PREF_RESTORE, gameData)
                .apply()
        Log.d("GameActivity", "state: " + gameData)

        mediaPlayer.stop()
        handler.removeCallbacks(null)
        mediaPlayer.reset()
        mediaPlayer.release()
    }

    fun restartGame() {
        (mGameFragment as GameFragment).restartGame()
    }

    fun reportWinner(winner: Tile.Owner) {
        var builder = AlertDialog.Builder(this)
        if (mediaPlayer?.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()
        }
        builder.setMessage(getString(R.string.declare_winner, winner))
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.ok_label,
                DialogInterface.OnClickListener { _: DialogInterface, _: Int ->
                    finish()
                }
        )
        val dialog = builder.create()

        handler.postDelayed({
            mediaPlayer = MediaPlayer.create(this, if (winner == Tile.Owner.X) R.raw.charm else R.raw.failfare)
            mediaPlayer.start()
            dialog.show()
        }, 500)
        (mGameFragment as GameFragment).initGame()
    }

    fun startThinking() {
        val thinking: View = findViewById<View>(R.id.thinking)
        thinking.visibility = View.VISIBLE
    }

    fun stopThinking() {
        val thinking: View = findViewById<View>(R.id.thinking)
        thinking.visibility = View.GONE
    }
}