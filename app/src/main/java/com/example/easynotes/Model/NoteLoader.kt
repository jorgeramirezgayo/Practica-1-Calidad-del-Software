package com.example.easynotes.Model

import android.content.Context
import java.io.File
import java.lang.RuntimeException

class NoteLoader {
    companion object {
        private var context: Context? = null //This could be done by saving a fragment and asking it for its context
        private val notesFolder = "notes"
        private val noteParamsFolder = "noteParams"
        private val extension = ".md"
        private var notes = mutableMapOf<Int, NoteModel>();
        private var folders = mutableMapOf<String, FolderModel>();

        fun setContext(context: Context?) {
            this.context = context;
            initialiseData();
        }

        fun addNote(n: NoteModel) {
            this.notes[n.id] = n
        }

        fun addFolder(f: FolderModel){
            this.folders[f.title] = f
        }

        fun deleteNote(n: NoteModel) {
            if (n == null) return;
            val paramsFile = File(context?.filesDir?.path + "/" + noteParamsFolder + "/" + n.relativePath + n.id.toString() + ".json") //en filesDir se guardan los ficheros persistentes
            val noteFile = File(context?.filesDir?.path + "/" + notesFolder + "/" + n.relativePath + n.id.toString() + extension)

            paramsFile.delete()
            noteFile.delete()
            this.notes.remove(n.id);
        }

        fun deleteFolder(f: FolderModel) {
            if (f == null) return;
            val paramsFile = File(context?.filesDir?.path + "/" + noteParamsFolder + "/" + f.relativePath + f.title) //en filesDir se guardan los ficheros persistentes
            val noteFile = File(context?.filesDir?.path + "/" + notesFolder + "/" + f.relativePath + f.title)
            val keyss = this.notes.keys.toList()
            for (i in keyss) {
                if (this.notes[i]?.relativePath == (f.title + "/")) {
                    this.notes[i]?.let { note ->
                        this.deleteNote(note)
                    }
                }
            }
            paramsFile.delete()
            noteFile.delete()
            this.folders.remove(f.title);
        }

        fun saveNote(context: Context?, note: NoteModel?) {
            if (note == null) return;
            val paramsFile = File(context?.filesDir?.path + "/" + noteParamsFolder + "/" + note.relativePath + note.id.toString() + ".json") //en filesDir se guardan los ficheros persistentes
            val noteFile = File(context?.filesDir?.path + "/" + notesFolder + "/" + note.relativePath + note.id.toString() + extension)

            paramsFile.writeText(note.toStringParams(noteFile.path))
            noteFile.writeText(note.toString())
        }

        fun saveFolder(context: Context?, folder: FolderModel?){
            if (folder == null) return;
            val paramsFile = File(context?.filesDir?.path + "/" + noteParamsFolder + "/" + folder.relativePath + folder.title) //en filesDir se guardan los ficheros persistentes
            val noteFile = File(context?.filesDir?.path + "/" + notesFolder + "/" + folder.relativePath + folder.title)

            if (!paramsFile.mkdir() || !noteFile.mkdir()){
                throw RuntimeException("Error creating the folder")
            }
        }

        fun nameRepeat(t: String):Boolean {
            return folders.containsKey(t)
        }

        fun changeNoteTitleColor(id: Int, newTitle: String, newColor: Int) {
            notes[id]!!.title = newTitle
            notes[id]!!.color = newColor
            saveNote(context,notes[id])
        }

        private fun initialiseData(){
            val path = File(context?.filesDir?.path + "/" + noteParamsFolder)

            path?.walk()?.forEach { file ->
                if (file.isFile) {
                    val note = noteModelFromFile(file);
                    if (note != null) notes[note.id] = note
                } else if (file.isDirectory && file.name != noteParamsFolder) {
                    var f = FolderModel(file.name, 0);
                    f.relativePath = file.path.removePrefix(context?.filesDir?.path + "/noteParams/").removeSuffix(file.name) //"/data/user/0/com.example.easynotes/files/noteParams/"
                    if (f.relativePath != "") f.relativePath += "/"
                    folders[file.name] = f;
                }
            }
        }

        private fun noteModelFromFile(file: File): NoteModel? {
            if (file.isDirectory) return null;
            val parameters = mutableMapOf<String, String>()
            var jsonParams = file.readText().removePrefix("{").removeSuffix("}")
            for (p in jsonParams.split(",")) {
                var key = p.split(":")[0].removePrefix("\"").removeSuffix("\"")
                var value = p.split(":")[1].removePrefix("\"").removeSuffix("\"")
                parameters[key] = value
            }
            val color = parameters["color"]?.toInt()
                ?: throw RuntimeException("Didn't find color in JSON file")
            val noteFile = File(parameters["notePath"]
                    ?: throw RuntimeException("Didn't find associated note in JSON file"))
            var fileText = noteFile.readText()
            var index = fileText.indexOf("\n")
            var id = noteFile.name.removeSuffix(".md").toInt()
            var tags: MutableList<String> = parameters["tags"]?.removeSuffix("]")?.removePrefix("[")?.split("% ")?.toMutableList() ?: ArrayList()
            val note = NoteModel(fileText.substring(0, index), color, fileText.substring(index+2, fileText.length), id,"", tags);
            note.relativePath = file.path.removePrefix(context?.filesDir?.path + "/noteParams/").removeSuffix("$id.json"); //"/data/user/0/com.example.easynotes/files/noteParams/"
            return note
        }

        fun getNotes(path:String): Map<Int, NoteModel> {
            println("NoteLoader.getNotes "+path)
            for(i in notes.keys){
                println(notes[i].toString())
            }
            println( notes.filter { entry -> entry.value.relativePath == path })
            return notes.filter { entry -> entry.value.relativePath == path }
        }

        fun getFolders(path:String): Map<String, FolderModel> {
            return if (path=="")
                folders
            else
                folders.filter { entry -> entry.value.relativePath == path }
        }

        fun getNote(id: Int): NoteModel {
            return this.notes[id]
                ?: throw RuntimeException("Tried to get a non-existing Note. The id provided is not a key.")
        }

        fun getFolder(title: String?): FolderModel? {
            return this.folders[title]
        }

    }
}

