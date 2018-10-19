package com.funckyhacker.androidtesting.contentProvider

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.database.DataSetObservable
import android.database.DataSetObserver
import android.os.Handler
import android.support.v7.widget.RecyclerView

abstract class CursorRecyclerViewAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T> {

    private val mDataSetObservable = DataSetObservable()
    protected var mAutoRequery: Boolean = false
    protected lateinit var cursor: Cursor
    protected var mDataValid: Boolean = false
    protected lateinit var mContext: Context
    protected var mRowIDColumn: Int = 0
    private var mChangeObserver: ChangeObserver? = null
    protected var mDataSetObserver: DataSetObserver? = MyDataSetObserver()

    constructor(context: Context, cursor: Cursor) : super() {
        init(context, cursor, true)
    }

    constructor(context: Context, cursor: Cursor, autoRequery: Boolean) : super() {
        init(context, cursor, autoRequery)
    }

    protected fun init(context: Context, c: Cursor, autoRequery: Boolean) {
        val cursorPresent = c != null
        mAutoRequery = autoRequery
        cursor = c
        mDataValid = cursorPresent
        mContext = context
        mRowIDColumn = if (cursorPresent) c!!.getColumnIndexOrThrow("_id") else -1
        mChangeObserver = ChangeObserver()
        if (cursorPresent) {
            c!!.registerContentObserver(mChangeObserver)
            c.registerDataSetObserver(mDataSetObserver)
        }
    }

    override fun onBindViewHolder(viewHolder: T, position: Int) {
        if (!mDataValid) {
            throw IllegalStateException("this should only be called when the cursor is valid")
        }
        if (!cursor!!.moveToPosition(position)) {
            throw IllegalStateException("couldn't move cursor to position $position")
        }
        bindView(viewHolder, mContext, cursor)
    }

    override fun getItemCount(): Int {
        return if (mDataValid && cursor != null) {
            cursor!!.count
        } else {
            0
        }
    }

    fun changeCursor(cursor: Cursor) {
        val old = swapCursor(cursor)
        old?.close()
    }

    fun swapCursor(newCursor: Cursor?): Cursor? {
        if (newCursor === cursor) {
            return null
        }
        val oldCursor = cursor
        if (oldCursor != null) {
            if (mChangeObserver != null) oldCursor.unregisterContentObserver(mChangeObserver)
            if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver)
        }
        cursor = newCursor
        if (newCursor != null) {
            if (mChangeObserver != null) newCursor.registerContentObserver(mChangeObserver)
            if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver)
            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id")
            mDataValid = true
            // notify the observers about the new cursor
            notifyDataSetChanged()
        } else {
            mRowIDColumn = -1
            mDataValid = false
            // notify the observers about the lack of a data set
            notifyDataSetInvalidated()
        }
        return oldCursor
    }

    fun registerDataSetObserver(observer: DataSetObserver) {
        mDataSetObservable.registerObserver(observer)
    }

    fun unregisterDataSetObserver(observer: DataSetObserver) {
        mDataSetObservable.unregisterObserver(observer)
    }

    fun notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated()
    }

    protected fun onContentChanged() {
        if (mAutoRequery && cursor != null && !cursor!!.isClosed) {
            mDataValid = cursor!!.requery()
        }
    }

    abstract fun bindView(viewHolder: T, context: Context, cursor: Cursor)

    private inner class ChangeObserver : ContentObserver(Handler()) {

        override fun deliverSelfNotifications(): Boolean {
            return true
        }

        override fun onChange(selfChange: Boolean) {
            onContentChanged()
        }
    }

    private inner class MyDataSetObserver : DataSetObserver() {
        override fun onChanged() {
            mDataValid = true
            notifyDataSetChanged()
        }

        override fun onInvalidated() {
            mDataValid = false
            notifyDataSetInvalidated()
        }
    }
}
