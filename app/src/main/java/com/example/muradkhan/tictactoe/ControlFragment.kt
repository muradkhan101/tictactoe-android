package com.example.muradkhan.tictactoe

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_control.*

class ControlFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup,
                              savedInstanceState: Bundle): View {
        val rootView = inflater.inflate(R.layout.fragment_control, container, false)
        button_main.setOnClickListener(View.OnClickListener { _ -> activity.finish() })
        // Not sure how below is different from static function call
        button_restart.setOnClickListener(View.OnClickListener { _ -> (activity as GameActivity).restartGame() })
        return rootView
    }
}