package com.example.muradkhan.tictactoe

import android.os.Bundle
import android.app.Fragment
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.TextView
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
            Log.i("FragmentView", "Set click listener")
            // The onClick method didn't work so had to SAM w/ lambda
//            fun onClick(view: View): Unit {
            var builder: AlertDialog.Builder = AlertDialog.Builder(getActivity())
            builder.setTitle(R.string.about_title)
            builder.setMessage(R.string.about_text)
            builder.setCancelable(false)
            builder.setPositiveButton(R.string.ok_label,
                    DialogInterface.OnClickListener {_: DialogInterface, _: Int -> 1 })
            mDialog = builder.show()
//            }
        })
        return rootView
    }
    override fun onPause() {
        super.onPause()
        mDialog?.dismiss()
    }
}