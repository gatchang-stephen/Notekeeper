package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var listItem: RecyclerView
    private lateinit var drawerLayout: DrawerLayout

    private val linearLayoutManager by lazy { LinearLayoutManager(applicationContext) }
    private val noteRecyclerViewAdapter by lazy { NoteRecyclerViewAdapter(DataManager.notes) }
    private val gridLayoutManager by lazy { GridLayoutManager(applicationContext, 2) }
    private val courseRecyclerViewAdapter by lazy { CourseRecyclerViewAdapter(DataManager.courses.values.toList()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        listItem = findViewById(R.id.listItems)
        displayNotes()
        displayCourses()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, NoteActivity::class.java))
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle =
            ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                0, 0
            )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    private fun displayCourses() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        listItem.layoutManager = gridLayoutManager
        listItem.adapter = courseRecyclerViewAdapter
        navView.menu.findItem(R.id.nav_courses).isChecked = true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()

        }
    }

    private fun displayNotes() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        listItem.layoutManager = linearLayoutManager
        listItem.adapter = noteRecyclerViewAdapter
        navView.menu.findItem(R.id.nav_notes).isChecked = true

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        listItem.adapter?.notifyDataSetChanged()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_notes -> {
                displayNotes()
            }
            R.id.nav_courses -> {
                displayCourses()
            }
            R.id.nav_share -> {
                handleSelection("Don't you think you have shared enough")

            }
            R.id.nav_send -> {
                handleSelection("Send")

            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleSelection(message: String) {
        Snackbar.make(listItem, message, Snackbar.LENGTH_SHORT).show()
    }
}