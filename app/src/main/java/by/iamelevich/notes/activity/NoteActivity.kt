package by.iamelevich.notes.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import by.iamelevich.notes.R
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {

    private lateinit var editNoteView: EditText
    private var id: Int = 0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editNoteView = findViewById(R.id.editText)
        intent.getIntExtra(NOTE_ID, 0).let {
            id = it
        }
        intent.getStringExtra(NOTE_TEXT)?.let {
            editNoteView.setText(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.save_note -> {
                val replyIntent = Intent()
                if (TextUtils.isEmpty(editNoteView.text)) {
                    setResult(Activity.RESULT_CANCELED, replyIntent)
                } else {
                    val note = editNoteView.text.toString()
                    if (id != 0)
                        replyIntent.putExtra(NOTE_ID, id)
                    replyIntent.putExtra(NOTE_TEXT, note)
                    setResult(Activity.RESULT_OK, replyIntent)
                }
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val NOTE_TEXT = "by.iamelevich.notes.NOTE_TEXT"
        const val NOTE_ID = "by.iamelevich.notes.NOTE_ID"
    }
}
