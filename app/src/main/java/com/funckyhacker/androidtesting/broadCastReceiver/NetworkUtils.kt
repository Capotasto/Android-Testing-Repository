package com.funckyhacker.androidtesting.broadCastReceiver

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtils {

    companion object {

        fun isInternetConnected(context: Context): Boolean {
            return isWifiConnected(context) || isMobileConnected(context)
        }

        fun isWifiConnected(context: Context): Boolean {
            return isConnected(context, ConnectivityManager.TYPE_WIFI)
        }

        fun isMobileConnected(context: Context): Boolean {
            return isConnected(context, ConnectivityManager.TYPE_MOBILE)
        }

        private fun isConnected(context: Context, type: Int): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
            val networks = connectivityManager.allNetworks
            networks.forEach {
                val networkInfo = connectivityManager.getNetworkInfo(it)
                if(networkInfo.type == type) {
                    return true
                }
            }
            return false
        }
    }
}
