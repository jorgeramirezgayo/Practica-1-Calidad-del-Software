package com.example.easynotes.ViewModel

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.toColorInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easynotes.Model.FolderModel
import com.example.easynotes.Model.NoteLoader
import com.example.easynotes.Model.NoteModel

class NoteViewModel : ViewModel() {
    val noteModel = MutableLiveData<List<NoteModel>>()
    val folderModel = MutableLiveData<List<FolderModel>>()

    fun getNotes(path: String): List<NoteModel> {
        val notes = NoteLoader.getNotes(path)
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        changeColors(notes.values, isDarkMode)
        noteModel.postValue(notes.values.toList())
        println("NoteViewModel.getNotes " + notes.values.toList())
        return notes.values.toList()
    }

    fun getFolders(path: String): List<FolderModel> {
        val folders = NoteLoader.getFolders(path)
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        changeColors(folders.values, isDarkMode)
        folderModel.postValue(folders.values.toList())
        return folders.values.toList()
    }

    fun addNote(n: NoteModel) {
        NoteLoader.addNote(n)
    }

    fun addFolder(f: FolderModel) {
        NoteLoader.addFolder(f)
    }

    private fun changeColors(items: Collection<*>, isDarkMode: Boolean) {
        for (item in items) {
            when (item) {
                is NoteModel -> {
                    item.color = when (item.color) {
                        "#f5f5dc".toColorInt() -> if (isDarkMode) "#959586".toColorInt() else "#f5f5dc".toColorInt()
                        "#C1FFC1".toColorInt() -> if (isDarkMode) "#177317".toColorInt() else "#C1FFC1".toColorInt()
                        "#ADD8E6".toColorInt() -> if (isDarkMode) "#226D85".toColorInt() else "#ADD8E6".toColorInt()
                        else -> item.color
                    }
                }
                is FolderModel -> {
                    item.color = when (item.color) {
                        "#f5f5dc".toColorInt() -> if (isDarkMode) "#959586".toColorInt() else "#f5f5dc".toColorInt()
                        "#C1FFC1".toColorInt() -> if (isDarkMode) "#177317".toColorInt() else "#C1FFC1".toColorInt()
                        "#ADD8E6".toColorInt() -> if (isDarkMode) "#226D85".toColorInt() else "#ADD8E6".toColorInt()
                        else -> item.color
                    }
                }
            }
        }
    }
}
