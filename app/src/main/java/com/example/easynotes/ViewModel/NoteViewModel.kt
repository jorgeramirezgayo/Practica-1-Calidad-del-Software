package com.example.easynotes.ViewModel

import android.graphics.Color
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.toColorInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easynotes.Model.FolderModel
import com.example.easynotes.Model.NoteLoader
import com.example.easynotes.Model.NoteModel
import com.example.easynotes.R


class NoteViewModel : ViewModel() {
    val noteModel = MutableLiveData<List<NoteModel>>()
    val folderModel = MutableLiveData<List<FolderModel>>()
    fun getNotes(path:String): List<NoteModel> {
        val notes = NoteLoader.getNotes(path)
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        if (isDarkMode){
            for (note in notes.values) {
                if (note.color=="#f5f5dc".toColorInt()){
                    note.color="#959586".toColorInt()
                }else if(note.color=="#C1FFC1".toColorInt()){
                    note.color="#177317".toColorInt()
                }else if (note.color=="#ADD8E6".toColorInt()) {
                    note.color = "#226D85".toColorInt()
                }
            }
        }else{
            for (note in notes.values) {
                if (note.color=="#959586".toColorInt()){
                    note.color="#f5f5dc".toColorInt()
                }else if(note.color=="#177317".toColorInt()){
                    note.color="#C1FFC1".toColorInt()
                }else if(note.color=="#226D85".toColorInt()){
                    note.color = "#ADD8E6".toColorInt()
                }
            }
        }
        noteModel.postValue(notes.values.toList())
        println("NoteViewModel.getNotes "+notes.values.toList())
        return notes.values.toList()
    }

    fun getFolders(path:String): List<FolderModel> {
        val folders = NoteLoader.getFolders(path)
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        if (isDarkMode){
            for (folder in folders.values) {
                if (folder.color=="#f5f5dc".toColorInt()){
                    folder.color="#959586".toColorInt()
                }else if(folder.color=="#C1FFC1".toColorInt()){
                    folder.color="#177317".toColorInt()
                }else if (folder.color=="#ADD8E6".toColorInt()) {
                    folder.color = "#226D85".toColorInt()
                }
            }
        }else{
            for (folder in folders.values) {
                if (folder.color=="#959586".toColorInt()){
                    folder.color="#f5f5dc".toColorInt()
                }else if(folder.color=="#177317".toColorInt()){
                    folder.color="#C1FFC1".toColorInt()
                }else if(folder.color=="#226D85".toColorInt()){
                    folder.color = "#ADD8E6".toColorInt()
                }
            }
        }
        folderModel.postValue(folders.values.toList())
        return folders.values.toList()
    }
    fun addNote(n: NoteModel){
        NoteLoader.addNote(n)
    }

    fun addFolder(f: FolderModel){
        NoteLoader.addFolder(f)
    }

    fun deleteNote(n: NoteModel){
        NoteLoader.deleteNote(n)
    }
}