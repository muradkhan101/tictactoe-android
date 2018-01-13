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
            savedInstanceState: Bundle?
    ): View {
        Log.i("FragmentView", "Creating fragment view")
        // Need to use R for layout XML I think?
        val rootView: View = inflater.inflate(R.layout.fragment_main, container, false)
        val aboutButton: View = rootView.findViewById(R.id.about_button)
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
        new_game_button.setOnClickListener(View.OnClickListener { view: View ->
            Log.d("FragmentView", "New Game Button click listener")
            val intent = Intent(activity, GameActivity::class.java)
            activity.startActivity(intent)
        })
        continue_button.setOnClickListener(View.OnClickListener { view: View ->
            Log.d("FragmentView", "Continue button click listener")
            val intent = Intent(activity, GameActivity::class.java)
            intent.putExtra((activity as GameActivity).KEY_RESTORE, true)
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