package com.example.muradkhan.tictactoe

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ControlFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_control, container, false)
        val buttonMain: View = rootView.findViewById(R.id.button_main)
        buttonMain.setOnClickListener(View.OnClickListener { _ -> activity.finish() })
        // Not sure how below is different from static function call
        val restartButton: View = rootView.findViewById(R.id.button_restart)
        restartButton.setOnClickListener(View.OnClickListener { _ -> (activity as GameActivity).restartGame() })
        return rootView
    }
}