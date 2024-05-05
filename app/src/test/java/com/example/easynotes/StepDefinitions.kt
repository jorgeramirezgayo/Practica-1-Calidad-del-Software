package com.example.easynotes

import com.example.easynotes.Model.NoteLoader
import com.example.easynotes.Model.NoteModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import io.cucumber.java.es.E
import org.junit.Assert.assertEquals
import java.lang.reflect.Array

class StepDefinitions {
    private var title: String = "Título"
    private var color: Int = 0
    private var text: String = "Contenido"
    private var id: Int = 1
    private var relativePath: String = "path"
    private var tags: MutableList<String> =mutableListOf()
    private lateinit var note: NoteModel;


    @Given("a note with title {string}, color {int}, text {string}, id {int}, a relative path {string} and a list of tags")
    fun aNewNoteWDetails(title: String, color: Int,  text: String, id:Int, relativePath:String) {
        this.title = title
        this.color = color
        this.text = text
        this.id = id
        this.relativePath = relativePath
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
}
