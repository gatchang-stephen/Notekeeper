package com.example.notekeeper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class CourseRecyclerViewAdapter(private val courses: List<CourseInfo>) :
    RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_course, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = courses[position]
        holder.apply {
            textCourse?.text = course.title
            notePosition = position
        }
    }

    override fun getItemCount() = courses.size

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val textCourse = itemView?.findViewById<TextView>(R.id.textCourse)
        var notePosition = 0

        init {
            itemView?.setOnClickListener {
                Toast.makeText(itemView.context, " Item clicked", Toast.LENGTH_SHORT).show()
//                val intent = Intent(itemView.context, NoteActivity::class.java)
//                intent.putExtra(NOTE_POSITION, notePosition)
//                itemView.context.startActivity(intent)
//                itemView.context.startActivity(
//                    Intent(itemView.context, NoteActivity::class.java).putExtra(
//                        NOTE_POSITION, notePosition
//                    )
//                )
            }
        }


    }
}