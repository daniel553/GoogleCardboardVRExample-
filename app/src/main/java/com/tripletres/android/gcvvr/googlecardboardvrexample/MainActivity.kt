package com.tripletres.android.gcvvr.googlecardboardvrexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Create view
        setContentView(R.layout.activity_main)

        //Replace fragment
        val fragment = PanoramaFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit()
    }

}
