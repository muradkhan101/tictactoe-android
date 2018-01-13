package com.example.muradkhan.tictactoe

import android.os.Bundle
import android.util.Log
import android.support.v7.app.AppCompatActivity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface

class GameActivity: AppCompatActivity() {
    val KEY_RESTORE = "key_restore"
    val PREF_RESTORE = "pref_restore"
    var mGameFragment = fragmentManager.findFragmentById(R.id.fragment_game)
    override fun onCreate(savedInstanceState: Bundle) {
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
    override fun onPause() {
        super.onPause()
        // class functions would work here, just had to do cast first
        // even utility functions didnt work without cast
        val gameData: String = (mGameFragment as GameFragment).getState()
        getPreferences(Context.MODE_PRIVATE)
            .edit()
            .putString(PREF_RESTORE, gameData)
            .commit()
        Log.d("GameActivity", "state: " + gameData)
    }
    fun restartGame() {
        (mGameFragment as GameFragment).restartGame()
    }
    fun reportWinner(winner: String) {
        var builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.declare_winner, winner))
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.ok_label,
                DialogInterface.OnClickListener{_: DialogInterface, _: Int ->
                    finish()
                }
        )
        builder.create().show()
        (mGameFragment as GameFragment).initGame()
    }
}