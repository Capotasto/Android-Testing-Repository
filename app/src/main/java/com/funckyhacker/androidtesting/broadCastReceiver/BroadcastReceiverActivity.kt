package com.funckyhacker.androidtesting.broadCastReceiver

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.funckyhacker.androidtesting.R
import timber.log.Timber

class BroadcastReceiverActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, BroadcastReceiverActivity::class.java)
        }
    }

    private var fragment: Fragment? = null

    private val intentFilter: IntentFilter by lazy {
        IntentFilter(NetworkCheckFragment.ACTION_CHECK_INTERNET)
    }

    private val receiver = NetworkReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broadast_receiver)

        fragment = supportFragmentManager.findFragmentByTag(NetworkCheckFragment.TAG)
        if (fragment == null) {
            fragment = NetworkCheckFragment.createInstance()
            supportFragmentManager.beginTransaction()
                    .add(fragment, NetworkCheckFragment.TAG)
                    .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.d("capo onResume")
        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }
}
