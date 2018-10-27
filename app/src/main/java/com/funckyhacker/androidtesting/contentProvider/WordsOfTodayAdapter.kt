package com.funckyhacker.androidtesting.contentProvider

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.funckyhacker.androidtesting.R
import java.util.*

class WordsOfTodayAdapter : CursorRecyclerViewAdapter<WordsOfTodayAdapter.ViewHolder> {
    override fun bindView(viewHolder: ViewHolder, context: Context, cursor: Cursor?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var data: List<Word> = ArrayList()

    constructor(context: Context, cursor: Cursor?, data: List<Word>) : super(context, cursor) {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_word, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<Word>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Word) = with(itemView) {
            // TODO: Bind the data with View
            setOnClickListener {
                // TODO: Handle on click
            }
        }
    }
}
