package com.example.easynotes.View

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.easynotes.Model.FolderModel
import com.example.easynotes.Model.NoteModel
import com.example.easynotes.R

class NoteAdapter(private val notes: List<NoteModel>, private val folders: List<FolderModel>, private val noteSize: Int) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size + folders.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position<folders.size) { //it's a folder
            val folder = folders[position]
            holder.title.text = folder.title

            this.addBorder(holder.itemView, Color.GREEN/*folders[position].color*/)
            val params = TableRow.LayoutParams(
                noteSize,
                noteSize,
                1.0f
            )
            params.setMargins(32,16,32,32)
            holder.itemView.layoutParams = params
            holder.itemView.isClickable = true
            // Esto son mierdas de navegación, Pablo, te toca pegarte con esto

            val args = Bundle()
            args.putString("noteText", folder.title)
            args.putString("title", folder.title)
            args.putString("id", folder.title)
            holder.folderImage.setImageResource(R.drawable.folde)
            this.addBorder(holder.itemView, folder.color)
            holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_FirstFragment_to_FolderFragment, args))
        } else { //it's a note

            holder.folderImage.setImageResource(R.drawable.emptyi)
            holder.title.text = notes[position-folders.size].title
            holder.text.text = notes[position-folders.size].text
            this.addBorder(holder.itemView, notes[position-folders.size].color)
            val params = TableRow.LayoutParams(
                noteSize,
                noteSize,
                1.0f
            )
            params.setMargins(32,16,32,32)
            holder.itemView.layoutParams = params
            holder.itemView.isClickable = true
            val note = notes[position-folders.size]
            val args = Bundle()
            args.putString("noteText", note.text)
            args.putString("title", note.title)
            args.putString("id", note.id.toString())
            holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.SecondFragment, args))
        }
    }

    /**
     * Changes the screen to the info screen note
     * @param it is the note
     */


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val text: TextView = itemView.findViewById(R.id.text)
        val folderImage: ImageView = itemView.findViewById(R.id.folderImage)
    }

    private fun addBorder(view: View, bg: Int){
        val border = GradientDrawable()
        border.setColor(bg)
        border.setStroke(5, Color.BLACK)
        border.cornerRadius = 8f
        view.background = border
    }
}