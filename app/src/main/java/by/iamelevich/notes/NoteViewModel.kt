package by.iamelevich.notes

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import by.iamelevich.notes.db.NoteRoomDatabase
import by.iamelevich.notes.db.entity.Note
import by.iamelevich.notes.db.repository.NoteRepository
import kotlinx.coroutines.launch

// Class extends AndroidViewModel and requires application as a parameter.
class NoteViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: NoteRepository
    // LiveData gives us updated words when they change.
    val allNotes: LiveData<List<Note>>
    private var search = MutableLiveData<String?>(null)

    fun setSearch(value: String?) {
        search.value = value
    }

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val noteDao = NoteRoomDatabase.getDatabase(application, viewModelScope).noteDao()
        repository =
            NoteRepository(
                noteDao
            )
        allNotes = Transformations.switchMap (search) { search ->
            if (search == null || search == "") {
                repository.allNotes
            } else {
                repository.searchNotes(search)
            }
        }
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on
     * the main thread, blocking the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called
     * viewModelScope which we can use here.
     */
    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun getById(id: Int): Note? = repository.getById(id)
}