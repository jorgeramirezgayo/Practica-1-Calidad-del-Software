package com.example.easynotes.Model

import android.content.Context
import android.graphics.Color
import java.io.File
import java.lang.RuntimeException
import java.nio.file.Files

class NoteLoader {
    companion object {
        private var c = 1;
        private var context: Context? = null //This could be done by saving a fragment and asking it for its context
        private val notesFolder = "notes"
        private val noteParamsFolder = "noteParams"
        private val extension = ".md"
        private var notes = mutableMapOf<Int, NoteModel>();
        private var folders = mutableMapOf<String, FolderModel>();


        fun nameRepeat(t: String):Boolean {
            return folders.containsKey(t)
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

        fun setContext(context: Context?) {
            this.context = context;
            //resetToDefaultData()
            initialiseData();
        }
        fun setFolder(context: Context?) {
            this.context = context

            initialiseFolder()
        }


        fun addNote(n: NoteModel) {
            //n.title = "Nueva nota $c"
            //this.c += 1
            //pretty sure this is now useless, but not sure enough to remove it
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

        fun getNote(id: Int): NoteModel {
            return this.notes[id]
                ?: throw RuntimeException("Tried to get a non-existing Note. The id provided is not a key.")
        }

        fun getFolder(title: String?): FolderModel? {
            return this.folders[title]
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

        fun moveNoteToFolder(context: Context?, note: NoteModel?, folder: FolderModel?){
            if (note == null || folder == null) return;
            val noteParamsFile = File(context?.filesDir?.path + "/" + noteParamsFolder + "/" + note.relativePath + note.id.toString() + ".json") //en filesDir se guardan los ficheros persistentes
            val noteFile       = File(context?.filesDir?.path + "/" + notesFolder + "/" + note.relativePath + note.id.toString() + extension)
            if (!noteParamsFile.exists() || !noteFile.exists()){
                throw RuntimeException("Can't move nonexistent file")
            }
            note.relativePath = folder.relativePath + folder.title + "/"
            saveNote(context, note)

            noteParamsFile.delete()
            noteFile.delete()
        }

        fun changeNoteTitleColor(id: Int, newTitle: String, newColor: Int) {
            notes[id]!!.title = newTitle
            notes[id]!!.color = newColor
            saveNote(context,notes[id])
        }

        private fun initialiseData(){
            if (!File(context?.filesDir?.path + "/settings.json").exists()){
                resetToDefaultData()
            }
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
        private fun initialiseFolder() {
            TODO("Not yet implemented")
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

        /**
         * Careful with this one. It deletes everything inside filesDir,
         * and creates the default data again
         */
        private fun resetToDefaultData() {
            val p = context?.filesDir
            p?.listFiles()?.forEach { file ->
                file.deleteRecursively()
            }

            val noteParamsFolderPath = File(context?.filesDir?.path + "/" + noteParamsFolder)
            val notesFolderPath = File(context?.filesDir?.path + "/" + notesFolder)

            //create both folders, and throw an exception if there's an error
            if (!noteParamsFolderPath.mkdir() || !notesFolderPath.mkdir()){
                throw RuntimeException("Error creating the folder")
            }
            val settingsFile = File(context?.filesDir?.path + "/settings.json")
            settingsFile.writeText("{\"isFirstAccess\":\"false\"}")

            //create default notes
            var title = "Lista de la compra"
            var desc = "-Patatas\n-Leche\n-Pan bimbo\n-Toallitas\n-Pimienta Negra\n"
            var c = Color.WHITE
            var id: Int = (title + "\n" + desc).hashCode()
            saveNote(context, NoteModel(title, c, desc, id,"Folder1/"))

            title = "Pelis pa ver"
            desc = "-Vengadores\n-Seven Kings must die\n-Pesadilla antes de Navidad\n"
            id = (title + "\n" + desc).hashCode()
            saveNote(context, NoteModel(title, c, desc, id))

            title = "Series pa ver"
            desc = "-TWD Daryl Dixon\n-Vikingos (La p* mejor serie)\n-Hora de aventuras\n"
            id = (title + "\n" + desc).hashCode()
            saveNote(context, NoteModel(title, c, desc, id))

            title = "Poema"
            desc = "Que bonitos ojos tienes\nTo be continued"
            id = (title + "\n" + desc).hashCode()
            saveNote(context, NoteModel(title, c, desc, id))

            title = "Contraseñas"
            desc =
                "-Twitter: ConstraseñaNoSegura1\n-Gmail: Constraseñ@Principal\n-Insta:ContraseñaSegur@1\n"
            id = (title + "\n" + desc).hashCode()
            saveNote(context, NoteModel(title, c, desc, id))

            title = "Pollito al pesto"
            desc =
                "1. Hacer macarrones en caldo de pollo\n2. Echar cebolla y ajo a la sarten con aceite caliente\n3. Al poco echar pollo troceado y especiado(cebolla en polvo, pimienta negra y hiervas provenzales)\n4. Cuando este todo hecho juntarlo en un plato\n5. Echar queso rallado, bolitas de mozzarella, pesto y tomate\n6. Calentar en el micro 2 min\n7. Remover y disfrutar"
            id = (title + "\n" + desc).hashCode()
            saveNote(context, NoteModel(title, c, desc, id))

            title = "La Resis"
            desc =
                "-Ángelito 44\n-Marcos 24\n-Inés 14\n-Ana 10\n-Carlos 08\n-Abril 03\n-Monchi 21\n-Adri 17\n-Ferreira 1\n-Odín 18\n-Emilio 33\n-Ander 16\n-Gormiti 06\n-Pablo 18\n"
            id = (title + "\n" + desc).hashCode()
            saveNote(context, NoteModel(title, c, desc, id))

            title = "Telefonos"
            desc = "-Casa: 123 45 67 89\n-Mama: 987 65 43 21 \n-FBI: 112 11 22 11\n"
            id = (title + "\n" + desc).hashCode()
            saveNote(context, NoteModel(title, c, desc, id))

            title = "Dirección casa"
            desc = "Calle Tulipan s/n\n"
            id = (title + "\n" + desc).hashCode()
            saveNote(context, NoteModel(title, c, desc, id))

            title = "About us"
            desc =
                "We are TechnexInnovations and EasyNotes was developed by:\n-Noelia Berzosa Parra\n-Sergio Hernández Sandoval\n-Ángel Marqués García\n-Ikár Vladislav Martínez de Lizarduy Kostornichenko\n-Miguel Ángel Sánchez Miranda\n-Álvaro Serrano Rodrigo\n-Pablo Requejo Postlbauer\n-Guillermo Romero Almazán\n-Miguel Quero Prieto\n"
            id = (title + "\n" + desc).hashCode()
            saveNote(context, NoteModel(title, c, desc, id))

            //previously was
            mutableMapOf(
                0 to NoteModel(
                    "Lista de la compra",
                    Color.WHITE,
                    "-Patatas\n-Leche\n-Pan bimbo\n-Toallitas\n-Pimienta Negra\n",
                    "Lista de la compra".hashCode()
                ),
                1 to NoteModel(
                    "Pelis pa ver",
                    Color.WHITE,
                    "-Vengadores\n-Seven Kings must die\n-Pesadilla antes de Navidad\n",
                    "Pelis pa ver".hashCode()
                ),
                2 to NoteModel(
                    "Series pa ver",
                    Color.WHITE,
                    "-TWD Daryl Dixon\n-Vikingos (La p* mejor serie)\n-Hora de aventuras\n",
                    "Series pa ver".hashCode()
                ),
                3 to NoteModel(
                    "Poema",
                    Color.WHITE,
                    "Que bonitos ojos tienes\nTo be continued",
                    "Poema".hashCode()
                ),
                4 to NoteModel(
                    "Contraseñas",
                    Color.WHITE,
                    "-Twitter: ConstraseñaNoSegura1\n-Gmail: Constraseñ@Principal\n-Insta:ContraseñaSegur@1\n",
                    "Contraseñas".hashCode()
                ),
                5 to NoteModel(
                    "Pollito al pesto",
                    Color.WHITE,
                    "1. Hacer macarrones en caldo de pollo\n2. Echar cebolla y ajo a la sarten con aceite caliente\n3. Al poco echar pollo troceado y especiado(cebolla en polvo, pimienta negra y hiervas provenzales)\n4. Cuando este todo hecho juntarlo en un plato\n5. Echar queso rallado, bolitas de mozzarella, pesto y tomate\n6. Calentar en el micro 2 min\n7. Remover y disfrutar",
                    "Pollito al pesto".hashCode()
                ),
                6 to NoteModel(
                    "La Resis",
                    Color.WHITE,
                    "-Ángelito 44\n-Marcos 24\n-Inés 14\n-Ana 10\n-Carlos 08\n-Abril 03\n-Monchi 21\n-Adri 17\n-Ferreira 1\n-Odín 18\n-Emilio 33\n-Ander 16\n-Gormiti 06\n-Pablo 18\n",
                    "La Resis".hashCode()
                ),
                7 to NoteModel(
                    "Telefonos",
                    Color.WHITE,
                    "-Casa: 123 45 67 89\n-Mama: 987 65 43 21 \n-FBI: 112 11 22 11\n",
                    "Telefonos".hashCode()
                ),
                8 to NoteModel(
                    "Dirección casa",
                    Color.WHITE,
                    "Calle Tulipan s/n\n",
                    "Dirección casa".hashCode()
                ),
                9 to NoteModel(
                    "About us",
                    Color.WHITE,
                    "We are TechnexInnovations and EasyNotes was developed by:\n-Noelia Berzosa Parra\n-Sergio Hernández Sandoval\n-Ángel Marqués García\n-Ikár Vladislav Martínez de Lizarduy Kostornichenko\n-Miguel Ángel Sánchez Miranda\n-Álvaro Serrano Rodrigo\n-Pablo Requejo Postlbauer\n-Guillermo Romero Almazán\n-Miguel Quero Prieto\n",
                    "About us".hashCode()
                ),
            )
        }


    }
}

