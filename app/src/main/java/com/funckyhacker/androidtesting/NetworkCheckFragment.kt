package com.funckyhacker.androidtesting

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.widget.Toast
import com.funckyhacker.androidtesting.util.NetworkUtils
import timber.log.Timber

class NetworkCheckFragment : Fragment() {

    companion object {

        val TAG = NetworkCheckFragment::class.java.simpleName!!
        const val ACTION_CHECK_INTERNET = "action_check_internet"
        const val KEY_CHECK_INTERNET = "key_check_internet"

        fun createInstance(): NetworkCheckFragment {
            return NetworkCheckFragment()
        }
    }

    private val intentFilter: IntentFilter by lazy {
        IntentFilter(ACTION_CHECK_INTERNET)
    }

    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Timber.w("capo onReceive")
            val action = intent?.action ?: ""
            if(action != ACTION_CHECK_INTERNET) {
                return
            }
            context ?: return
            val isConnected = NetworkUtils.isInternetConnected(context)
            if (isConnected) {
                Toast.makeText(context, "Network is connected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Network is disconnected", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(context!!).registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(receiver)
    }


}
