package com.example.notekeeper

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.snackbar.Snackbar

class NoteActivity : AppCompatActivity() {
    private var notePosition = POSITION_NOT_SET

    private lateinit var spinnerCourses: Spinner
    private lateinit var noteTitle: TextView
    private lateinit var noteText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        spinnerCourses = findViewById(R.id.spinnerCourses)
        noteTitle = findViewById(R.id.noteTitle)
        noteText = findViewById(R.id.noteText)

        val adapterCourses =
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_spinner_item,
                DataManager.courses.values.toList()
            )
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCourses.adapter = adapterCourses

        notePosition =
            savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?: intent.getIntExtra(
                NOTE_POSITION,
                POSITION_NOT_SET
            )

        if (notePosition != POSITION_NOT_SET)
            displayNote()
        else {
            DataManager.notes.add(NoteInfo())
            notePosition = DataManager.notes.lastIndex
        }
    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }

    private fun saveNote() {
        val note = DataManager.notes[notePosition]
        note.title = noteTitle.text.toString()
        note.text = noteText.text.toString()
        note.course = spinnerCourses.selectedItem as CourseInfo?
    }

    private fun displayNote() {
        if (notePosition > DataManager.notes.lastIndex) {
            val message = "Note not found"
            showMessage(message)
            return
        }
        val note = DataManager.notes[notePosition]
        noteTitle.text = note.title
        noteText.text = note.text

        val coursePosition = DataManager.courses.values.indexOf(note.course)
        spinnerCourses.setSelection(coursePosition)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                if (notePosition < DataManager.notes.lastIndex) {
                    moveNext()
                } else {
                    val message = "No more notes"
                    showMessage(message)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMessage(message: String) =
        Snackbar.make(noteTitle, message, Snackbar.LENGTH_LONG).show()

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (notePosition >= DataManager.notes.lastIndex) {
            val itemMenu = menu?.findItem(R.id.action_next)
            if (itemMenu != null) {
                itemMenu.icon =
                    AppCompatResources.getDrawable(applicationContext, R.drawable.ic_block_24)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun moveNext() {
        ++notePosition
        displayNote()
        invalidateOptionsMenu()
    }
}