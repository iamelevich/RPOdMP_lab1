package by.iamelevich.notes.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.iamelevich.notes.R
import by.iamelevich.notes.NoteListAdapter
import by.iamelevich.notes.NoteViewModel
import by.iamelevich.notes.db.entity.Note
import org.jetbrains.anko.doAsync
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val NOTE_ACTIVITY_NEW_REQUEST_CODE = 1
        const val NOTE_ACTIVITY_EDIT_REQUEST_CODE = 2
    }

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NoteListAdapter(this, noteViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel.allNotes.observe(this, Observer { notes ->
            // Update the cached copy of the words in the adapter.
            notes?.let { adapter.setNotes(it) }
        })

        fab.setOnClickListener {
            startActivityForResult(Intent(this@MainActivity, NoteActivity::class.java), NOTE_ACTIVITY_NEW_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NOTE_ACTIVITY_NEW_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val title: String? = data?.getStringExtra(NoteActivity.NOTE_TITLE)
            var text: String? = data?.getStringExtra(NoteActivity.NOTE_TEXT)
            if (text === null) {
                text = ""
            }
            val note = Note(title, text)
            noteViewModel.insert(note)
        } else if (requestCode == NOTE_ACTIVITY_EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            doAsync {
                val id = data?.getIntExtra(NoteActivity.NOTE_ID, 0)
                val title = data?.getStringExtra(NoteActivity.NOTE_TITLE)
                var text = data?.getStringExtra(NoteActivity.NOTE_TEXT)
                if (text === null) {
                    text = ""
                }
                var note = id?.let { noteViewModel.getById(it) }
                if (note === null) {
                    note = Note(title, text!!)
                }
                note.title = title
                note.text = text!!
                noteViewModel.update(note)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchViewMenuItem = menu.findItem(R.id.app_bar_search);
        val searchView = searchViewMenuItem.actionView as SearchView;
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                noteViewModel.setSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                noteViewModel.setSearch(newText)
                return true
            }
        })
        searchView.setOnCloseListener { noteViewModel.setSearch(null); false }
        return true
    }
}
