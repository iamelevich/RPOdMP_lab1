package by.iamelevich.notes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import by.iamelevich.notes.activity.MainActivity.Companion.NOTE_ACTIVITY_EDIT_REQUEST_CODE
import by.iamelevich.notes.activity.NoteActivity
import by.iamelevich.notes.activity.NoteActivity.Companion.NOTE_ID
import by.iamelevich.notes.activity.NoteActivity.Companion.NOTE_TITLE
import by.iamelevich.notes.activity.NoteActivity.Companion.NOTE_TEXT
import by.iamelevich.notes.db.entity.Note
import java.text.DateFormat

class NoteListAdapter internal constructor(
    private val context: Context,
    private val noteViewModel: NoteViewModel
) : RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Note>() // Cached copy of notes

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteItemView: TextView = itemView.findViewById(R.id.textView)
        val updatedAtItemView: TextView = itemView.findViewById(R.id.updatedAt)
        val noteDeleteBtn: ImageButton = itemView.findViewById(R.id.delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = notes[position]
        var title = current.title
        if (title === null) {
            title = if (current.text.length > 50) current.text.substring(0, 50) + "..." else current.text
        }
        holder.noteItemView.text = title.replace('\n', ' ')
        holder.updatedAtItemView.text = context.getString(
            R.string.updatedAt_string,
            DateFormat.getDateInstance().format(current.updatedAt!!),
            DateFormat.getTimeInstance().format(current.updatedAt!!)
        )
        holder.noteItemView.setOnClickListener {
            val intent = Intent(context, NoteActivity::class.java);
            intent.putExtra(NOTE_ID, current.id)
            intent.putExtra(NOTE_TITLE, current.title)
            intent.putExtra(NOTE_TEXT, current.text)
            startActivityForResult(context as Activity, intent, NOTE_ACTIVITY_EDIT_REQUEST_CODE, null)
        }
        holder.noteDeleteBtn.setOnClickListener {
            noteViewModel.delete(current)
            notifyDataSetChanged()
        }
    }

    internal fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun getItemCount() = notes.size
}