package com.example.easynotes

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.easynotes.Model.NoteLoader
import com.example.easynotes.Model.NoteModel
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
        val note = NoteModel("Título", 0, "Contenido", 1, "path", mutableListOf())

        // Act
        NoteLoader.addNote(note)

        // Assert
        assertEquals(note, NoteLoader.getNote(1))
    }

    @Test
    fun getAllNotesTest() {
        // Arrange
        val note1 = NoteModel("Título 1", 1, "Contenido 1", 1, "path", mutableListOf())
        val note2 = NoteModel("Título 2", 2, "Contenido 2", 2, "path", mutableListOf())
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
        val originalNote = NoteModel("Título", 1, "Contenido", 1, "path", mutableListOf())
        NoteLoader.addNote(originalNote)
        val editedNote = NoteModel("Título Editado", 2, "Contenido Editado", 1, "path", mutableListOf())

        // Act
        NoteLoader.saveNote(context, editedNote)

        // Assert
        assertEquals(editedNote, NoteLoader.getNote(1))
    }

    @Test
    fun deleteNoteTest() {
        // Arrange
        val note = NoteModel("Título", 1, "Contenido", 1, "path", mutableListOf())
        NoteLoader.addNote(note)

        // Act
        NoteLoader.deleteNote(note)

        // Assert
        assertNull(NoteLoader.getNote(1))
    }
}
