package com.example.writease

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private val list: ArrayList<Note>, private val dbHelper: DBHelper, private val tvNoNotes: TextView): RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }
    class ViewHolder(view: View, listener: ItemClickListener): RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvContent: TextView = view.findViewById(R.id.tvContent)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)

        init {
            itemView.setOnClickListener {
                listener.onClickListener(adapterPosition)
            }
        }

    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_note_item, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = list[position]
        if (note.title.length <= 22) {
            holder.tvTitle.text = note.title
        } else {
            holder.tvTitle.text = "${note.title.substring(0, 22)}..."
        }
        if (note.content.length <= 34) {
            holder.tvContent.text = note.content
        } else {
            holder.tvContent.text = "${note.content.substring(0, 34)}..."
        }


        holder.ivDelete.setOnClickListener {
            dbHelper.deleteNote(note.id)
            list.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            if (list.size == 0) {
                tvNoNotes.visibility = View.VISIBLE
            }
        }

    }

}
