package com.example.muradkhan.tictactoe

import android.os.Bundle
import android.app.Fragment
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.v7.app.AlertDialog
import android.util.Log
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    var mDialog: AlertDialog? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            // Container can be null too so needs ?
            container: ViewGroup?,
            // Bundle can be null, needs ?
            savedInstanceState: Bundle?
    ): View {
        Log.d("FragmentView", "Creating fragment view")

        // Need to use R for layout XML I think?
        val rootView: View = inflater.inflate(R.layout.fragment_main, container, false)
        val aboutButton: View = rootView.findViewById(R.id.about_button)

        Log.d("FragmentView", "aboutButton click listener")

        // Get NullPointerException if don't use R to find View
        aboutButton.setOnClickListener(View.OnClickListener { view: View ->
            Log.d("FragmentView", "Set click listener")
            // The onClick method didn't work so had to SAM w/ lambda
//            fun onClick(view: View): Unit {
            var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            builder.setTitle(R.string.about_title)
            builder.setMessage(R.string.about_text)
            builder.setCancelable(false)
            builder.setPositiveButton(R.string.ok_label,
                    DialogInterface.OnClickListener {_: DialogInterface, _: Int -> 1 })
            mDialog = builder.show()
//            }
        })

        Log.d("FragmentView", "NewGameButton click listener")
        val newGameButton: View = rootView.findViewById(R.id.new_game_button)
        newGameButton.setOnClickListener(View.OnClickListener { view: View ->
            Log.d("FragmentView", "New Game Button click listener")
            val intent = Intent(activity, GameActivity::class.java)
            activity.startActivity(intent)
        })

        Log.d("FragmentView", "ContinueButton click listener")

        val continueButton: View = rootView.findViewById(R.id.continue_button)
        continueButton.setOnClickListener(View.OnClickListener { view: View ->
            Log.d("FragmentView", "Continue button click listener")
            val intent = Intent(activity, GameActivity::class.java)
            intent.putExtra(GameActivity::KEY_RESTORE.toString(), true)
            activity.startActivity(intent)
        })
//        val newGame: View = rootView.findViewById(R.id.new_game_button)
        return rootView
    }
    override fun onPause() {
        super.onPause()
        mDialog?.dismiss()
    }
}