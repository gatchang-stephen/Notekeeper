package com.example.notekeeper

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class NoteRecyclerViewAdapter(private val notes: List<NoteInfo>) :
    RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder>() {
    private var onNoteSelectedListener: OnNoteSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_notes, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.apply {
            textCourse?.text = note.course?.title
            textTitle?.text = note.title
            notePosition = position
        }
    }

    override fun getItemCount() = notes.size

    fun setOnSelectedListener(listener: OnNoteSelectedListener) {
        onNoteSelectedListener = listener
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val textCourse = itemView?.findViewById<TextView>(R.id.textCourse)
        val textTitle = itemView?.findViewById<TextView>(R.id.textTitle)
        var notePosition = 0

        init {
            itemView?.setOnClickListener {
                onNoteSelectedListener?.onNoteSelected(notes[notePosition])
//                Toast.makeText(itemView.context," Item clicked", Toast.LENGTH_SHORT).show()
//                val intent = Intent(itemView.context, NoteActivity::class.java)
//                intent.putExtra(NOTE_POSITION, notePosition)
//                itemView.context.startActivity(intent)
                itemView.context.startActivity(
                    Intent(itemView.context, NoteActivity::class.java).putExtra(
                        NOTE_POSITION, notePosition
                    )
                )
            }
        }


    }

    interface OnNoteSelectedListener {
        fun onNoteSelected(note: NoteInfo)
    }
}