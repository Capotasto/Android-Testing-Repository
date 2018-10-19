package com.funckyhacker.androidtesting.contentProvider

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.funckyhacker.androidtesting.R
import java.util.*


class ContentProviderActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ContentProviderActivity::class.java)
        }

        private const val REQUEST_PERMISSION = 1000
    }

    private val dateTextView by lazy {
        findViewById<TextView>(R.id.content_date_text)
    }
    private val contentImage by lazy {
        findViewById<ImageView>(R.id.contet_image)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_provider)
        checkPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doTask()
            } else {
                // それでも拒否された時の対応
                val toast = Toast.makeText(this, "何もできません", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    private fun doTask() {
        try {
            val cursor = getImage()

            cursor ?: return
            if (cursor.moveToFirst()) {
                val idColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
                val titleColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.TITLE)
                val dateTakenColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)

                val id = cursor.getLong(idColNum)
                val title = cursor.getString(titleColNum)
                val dateTaken = cursor.getLong(dateTakenColNum)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = dateTaken;
                val text = DateFormat.format("yyyy/MM/dd(E) kk:mm:ss", calendar).toString()
                dateTextView.text = text
                contentImage.setImageURI(imageUri)

            }
             cursor.close()
        } catch (e: SecurityException) {
            Toast.makeText(this, "Nedd Storage Permission!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImage(): Cursor? {
        var queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.DATE_TAKEN
        )
        val sortOrder = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
        queryUri = queryUri.buildUpon().appendQueryParameter("limit", "1").build()
        return contentResolver.query(queryUri, projection, null, null, sortOrder)
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            doTask()
        } else {
            // 拒否していた場合
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION)
        } else {
            Toast.makeText(this, "アプリ実行に許可が必要です", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION)
        }
    }
}
