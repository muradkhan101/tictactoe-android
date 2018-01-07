package com.example.muradkhan.tictactoe

import android.os.Bundle
import android.app.Fragment
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    lateinit var mDialog: AlertDialog
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup,
            savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(main_fragment.id, container, false)
//        val aboutButton: View = rootView.findViewById(R.layout.about_button)
        about_button.setOnClickListener(View.OnClickListener {
            fun onClick(view: View): Unit {
                var builder: AlertDialog.Builder = AlertDialog.Builder(getActivity())
                builder.setTitle(R.string.about_title)
                builder.setMessage(R.string.about_text)
                builder.setCancelable(false)
                builder.setPositiveButton(R.string.ok_label,
                        DialogInterface.OnClickListener {
                            fun onClick(di: DialogInterface, i: Int) = 1
                        })
                mDialog.show()
            }
        })
        return rootView
    }
    override fun onPause() {
        super.onPause()
        mDialog?.dismiss()
    }
}