package com.example.muradkhan.tictactoe

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.media.MediaPlayer

class MainActivity : Activity() {
    lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MainActivity", "Creating main view")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onResume() {
        super.onResume()
        mediaPlayer = MediaPlayer.create(this, R.raw.intro)
        mediaPlayer.setVolume(0.5f, 0.5f)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.release()
    }
}
