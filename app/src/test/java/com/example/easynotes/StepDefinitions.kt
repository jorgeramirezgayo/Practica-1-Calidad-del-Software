package com.example.easynotes

//import android.content.Context
//import androidx.test.core.app.ApplicationProvider
import com.example.easynotes.Model.NoteLoader
import com.example.easynotes.Model.NoteModel
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

class StepDefinitions {
    private var title: String = "Título"
    private var color: Int = 0
    private var text: String = "Contenido"
    private var id: Int = 1
    private var relativePath: String = "path"
    private var tags: MutableList<String> = mutableListOf()
    private lateinit var note: NoteModel
    private lateinit var note2: NoteModel
    private lateinit var allNotes: Map<Int,NoteModel>
    private lateinit var editedNote: NoteModel
//    private lateinit var context: Context

    @Before
    fun setUp() {
        note = NoteModel(title, color, text, id, relativePath, tags)
        note2 = NoteModel("", 0, "", 0, "", tags)
        editedNote = NoteModel("", 0, "", 0, "", tags)

    }

    @Given("a note with title {string}, color {int}, text {string}, id {int}, a relative path {string} and a list of tags")
    fun aNoteWDetails(title: String, color: Int,  text: String, id:Int, relativePath:String) {
        this.note = NoteModel(title, color, text, id, relativePath, this.tags)
    }


    @When("the note is added")
    fun addNote() {
        NoteLoader.addNote(this.note)
    }

    @Then("the note with id {int} should be retrieved successfully")
    fun noteRetrieved(id: Int) {
        val addedNote = NoteLoader.getNote(this.id)
        assertEquals(this.note, addedNote)
    }

    @Given("an existing note with title {string}, color {int}, text {string}, id {int}, a relative path {string} and an empty list of tags")
    fun certainAlreadyAddedNote(title: String, color: Int,  text: String, id:Int, relativePath:String) {
        aNoteWDetails(title, color, text, id, relativePath)
        addNote()
    }

    @When("the note is deleted")
    fun deleteNote() {
        NoteLoader.deleteNote(this.note)
    }

    @Then("the note with id {int} should not exist")
    fun assertDeletedNote(id: Int){
        try {
            val deletedNote = NoteLoader.getNote(id)
            assertNull(deletedNote)
        } catch (e: RuntimeException) {
            assertEquals("${e.message}","Tried to get a non-existing Note. The id provided is not a key.")
        }
    }


    @Given("two notes, one with {string}, color {int}, text {string}, id {int}, a relative path {string} and a list of tags and the other one with {string}, color {int}, text {string}, id {int}, a relative path {string} and a list of tags")
    fun twoNotesWDetails(title1: String, color1: Int,  text1: String, id1:Int, relativePath1:String,title2: String, color2: Int,  text2: String, id2:Int, relativePath2:String) {
        this.note = NoteModel(title1, color1, text1, id1, relativePath1, this.tags)
        this.note2 = NoteModel(title2, color2, text2, id2, relativePath2, this.tags)
        NoteLoader.addNote(this.note)
        NoteLoader.addNote(this.note2)
    }


    @When("all notes are retrieved with path {string} provided")
    fun getAllNotes(relativePath:String) {
        val allNotes = NoteLoader.getNotes("path")
        this.allNotes = allNotes
    }

    @Then("the note with id {int} and the note with the id {int} should be 2 in size as well as portrayed")
    fun notesRetrieved(id1: Int, id2: Int) {
        assertEquals(2, this.allNotes.size)
        assertEquals(this.note, this.allNotes[1])
        assertEquals(this.note2, this.allNotes[2])
    }


    @Given("an edited note with title {string}, color {int}, text {string}, id {int}, a relative path {string} and an empty list of tags")
    fun certainEditedNote(title: String, color: Int,  text: String, id:Int, relativePath:String) {
        aNoteWDetails(title, color, text, id, relativePath)
        addNote()
        val editedNote = NoteModel("Título Editado", 2, "Contenido Editado", 1, "path", mutableListOf())
        this.editedNote = editedNote
    }

    @When("the note is saved")
    fun savedNote() {
        deleteNote()
        NoteLoader.addNote(this.editedNote)
//        NoteLoader.setContext(ApplicationProvider.getApplicationContext())
//        NoteLoader.saveNote(this.context, this.editedNote)



    }

    @Then("the retrieved note with id {int} should have changed")
    fun assertEditedNote(id: Int){
        assertEquals(this.editedNote, NoteLoader.getNote(this.id))
    }




}