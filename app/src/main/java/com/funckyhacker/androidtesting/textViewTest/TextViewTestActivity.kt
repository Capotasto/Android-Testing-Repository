package com.funckyhacker.androidtesting.textViewTest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.funckyhacker.androidtesting.R

class TextViewTestActivity : AppCompatActivity() {

    companion object {
        fun createIntent(contest: Context) : Intent {
            return Intent(contest, TextViewTestActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_view_test)
    }
}
