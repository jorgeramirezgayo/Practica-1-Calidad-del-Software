package com.example.easynotes.Model

data class FolderModel(var title: String, var color: Int ) {
    var relativePath = "";

    override fun toString(): String {
        return "$title\n"
    }

}