package com.example.easynotes.Model

data class NoteModel(var title: String, var color: Int, var text: String, var id:Int, var relativePath:String = "", var tags: MutableList<String> = ArrayList()) {

    override fun toString(): String {
        return "$title\n$text"//\n$relativePath"
    }

    fun toStringParams(path: String): String {
        val tagsString = tags.toString().replace(", ","% ")
        return "{\"color\":\"$color\",\"id\":\"$id\",\"notePath\":\"$path\",tags:\"$tagsString\"}"
    }

    fun addTag(tag: String){
        tags.add(tag)
    }
    fun removeTag(tag: String){
        tags.remove(tag)
    }

}