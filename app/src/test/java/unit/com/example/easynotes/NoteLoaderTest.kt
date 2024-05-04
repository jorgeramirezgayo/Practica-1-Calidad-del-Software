import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.easynotes.Model.NoteLoader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class NoteLoaderTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        NoteLoader.setContext(context)
    }

    @Test
    fun addNoteTest() {
        // Arrange
        val note = NoteModel(1, "Título", "Contenido", "path", emptyList())

        // Act
        NoteLoader.addNote(note)

        // Assert
        assertEquals(note, NoteLoader.getNote(1))
    }

    @Test
    fun getAllNotesTest() {
        // Arrange
        val note1 = NoteModel(1, "Título 1", "Contenido 1", "path", emptyList())
        val note2 = NoteModel(2, "Título 2", "Contenido 2", "path", emptyList())
        NoteLoader.addNote(note1)
        NoteLoader.addNote(note2)

        // Act
        val allNotes = NoteLoader.getNotes("path")

        // Assert
        assertEquals(2, allNotes.size)
        assertEquals(note1, allNotes[1])
        assertEquals(note2, allNotes[2])
    }

    @Test
    fun editNoteTest() {
        // Arrange
        val originalNote = NoteModel(1, "Título", "Contenido", "path", emptyList())
        NoteLoader.addNote(originalNote)
        val editedNote = NoteModel(1, "Título Editado", "Contenido Editado", "path", emptyList())

        // Act
        NoteLoader.saveNote(context, editedNote)

        // Assert
        assertEquals(editedNote, NoteLoader.getNote(1))
    }

    @Test
    fun deleteNoteTest() {
        // Arrange
        val note = NoteModel(1, "Título", "Contenido", "path", emptyList())
        NoteLoader.addNote(note)

        // Act
        NoteLoader.deleteNote(note)

        // Assert
        assertNull(NoteLoader.getNote(1))
    }
}
