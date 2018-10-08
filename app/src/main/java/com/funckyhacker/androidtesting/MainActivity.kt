package com.funckyhacker.androidtesting

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.funckyhacker.androidtesting.broadCastReceiver.BroadcastReceiverActivity
import com.funckyhacker.androidtesting.contentProvider.ContentProviderActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val broadcast1 = findViewById<Button>(R.id.broadcast1_button)
        broadcast1.setOnClickListener {
            startActivity(BroadcastReceiverActivity.createIntent(this))
        }

        val contentReceiver = findViewById<Button>(R.id.content_receiver)
        contentReceiver.setOnClickListener {
            startActivity(ContentProviderActivity.createIntent(this))
        }
    }

}
