package com.example.muradkhan.tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MainActivity", "Creating main view")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
