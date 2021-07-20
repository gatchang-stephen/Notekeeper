package com.example.notekeeper

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.*
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewNoteTest {

    @Rule
    @JvmField
    val listNote = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun newTest() {
        val course = DataManager.courses["android_async"]
        val noteTitle = "This is note test"
        val noteText = "This is note body test"

        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.noteTitle)).perform(typeText(noteTitle))
        onView(withId(R.id.noteText)).perform(typeText(noteText))
        onView(withId(R.id.spinnerCourses)).perform(click())
        onData(allOf(instanceOf(CourseInfo::class.java), equalTo(course))).perform(
            click(), closeSoftKeyboard()
        )
        pressBack()

        val newNote = DataManager.notes.last()
        assertEquals(course, newNote.course)
        assertEquals(noteTitle, newNote.title)
        assertEquals(noteText, newNote.text)
    }
}