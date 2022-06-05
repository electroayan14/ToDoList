package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private val onItemClickListener:(Note)->Unit): ListAdapter<Note,NoteAdapter.NoteHolder>(
    diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.note_item,
            parent,false)
        return NoteHolder(itemview)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        with(getItem(position)){
            holder.tvTitle.text= title
            holder.tvDescription.text= description
            holder.tvPriority.text= priority.toString()
        }
    }
    fun getNoteAt(position: Int)= getItem(position)

    inner class NoteHolder(iv:View): RecyclerView.ViewHolder(iv){
        val tvTitle: TextView = itemView.findViewById(R.id.text_title)
        val tvDescription:TextView=itemView.findViewById(R.id.text_desc)
        val tvPriority:TextView=itemView.findViewById(R.id.numberpicker_button)

        init {
            itemView.setOnClickListener {
                if (adapterPosition!= NO_POSITION)
                    onItemClickListener(getItem(adapterPosition))
            }
        }
    }
}
private val diffCallback = object : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Note, newItem: Note) =
        oldItem.title == newItem.title
                && oldItem.description == newItem.description
                && oldItem.priority == newItem.priority
}