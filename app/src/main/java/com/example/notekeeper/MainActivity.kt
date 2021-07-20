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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    NoteRecyclerViewAdapter.OnNoteSelectedListener {
    private lateinit var listItem: RecyclerView
    private lateinit var drawerLayout: DrawerLayout

    private val viewModel by lazy { ViewModelProvider(this)[MainActivityViewModel::class.java] }

    private val noteLayoutManager by lazy { LinearLayoutManager(applicationContext) }
    private val noteRecyclerViewAdapter by lazy {
        val adapter = NoteRecyclerViewAdapter(DataManager.loadNotes())
        adapter.setOnSelectedListener(this)
        adapter
    }
    private val courseLayoutManager by lazy { GridLayoutManager(applicationContext, 2) }
    private val courseRecyclerViewAdapter by lazy { CourseRecyclerViewAdapter(DataManager.courses.values.toList()) }

    private val maxRecentlyNotes = 5
    private val recentlyNotes = ArrayList<NoteInfo>(maxRecentlyNotes)
    private val recentlyNoteRecyclerViewAdapter by lazy {
        val adapter = NoteRecyclerViewAdapter(recentlyNotes)
        adapter.setOnSelectedListener(this)
        adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        fab
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, NoteActivity::class.java))
        }
//        recyclerView
        listItem = findViewById(R.id.listItems)

        handleDisplaySelection(viewModel.navDrawerDisplaySelection)
//        Navigation Drawer
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
        listItem.layoutManager = courseLayoutManager
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
        listItem.layoutManager = noteLayoutManager
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
            R.id.nav_notes,
            R.id.nav_courses,
            R.id.nav_recently_notes -> {
                handleDisplaySelection(item.itemId)
                viewModel.navDrawerDisplaySelection = item.itemId
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

    private fun handleDisplaySelection(itemId: Int) {
        when (itemId) {
            R.id.nav_notes -> {
                displayNotes()
            }
            R.id.nav_courses -> {
                displayCourses()
            }
            R.id.nav_recently_notes -> {
                displayRecentNotes()
            }
        }
    }

    override fun onNoteSelected(note: NoteInfo) {
        addToRecentlyNotes(note)

    }

    private fun addToRecentlyNotes(note: NoteInfo) {
        val existingIndex = recentlyNotes.indexOf(note)
        if (existingIndex == -1) {
            recentlyNotes.add(0, note)
            for (index in recentlyNotes.lastIndex.downTo(maxRecentlyNotes))
                recentlyNotes.removeAt(index)
        } else {
            for (index in (existingIndex - 1) downTo 0)
                recentlyNotes[index + 1] = recentlyNotes[index]
        }
    }

    private fun displayRecentNotes() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        listItem.layoutManager = noteLayoutManager
        listItem.adapter = recentlyNoteRecyclerViewAdapter
        navView.menu.findItem(R.id.nav_notes).isChecked = true
    }

    private fun handleSelection(message: String) {
        Snackbar.make(listItem, message, Snackbar.LENGTH_SHORT).show()
    }
}