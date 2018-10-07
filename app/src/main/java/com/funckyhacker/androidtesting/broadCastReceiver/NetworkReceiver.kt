package com.funckyhacker.androidtesting.broadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import timber.log.Timber

class NetworkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.w("capo onReceive")
        context ?: return
        val i = Intent(NetworkCheckFragment.ACTION_CHECK_INTERNET)
        i.putExtra(NetworkCheckFragment.KEY_CHECK_INTERNET,
                NetworkUtils.isInternetConnected(context))
        LocalBroadcastManager.getInstance(context).sendBroadcast(i)
    }
}
