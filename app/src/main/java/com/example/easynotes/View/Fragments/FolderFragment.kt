package com.example.easynotes.View.Fragments

import android.graphics.Color
import com.example.easynotes.View.NoteAdapter
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easynotes.Model.NoteLoader
import com.example.easynotes.Model.NoteModel
import com.example.easynotes.ViewModel.NoteViewModel
import com.example.easynotes.databinding.FragmentWelcomeBinding
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AlertDialog
import com.example.easynotes.R
import android.widget.EditText
import android.widget.Button
import android.widget.RadioGroup
import androidx.navigation.fragment.findNavController
import com.example.easynotes.Model.FolderModel
import com.example.easynotes.databinding.FragmentFolderBinding
import com.google.android.material.appbar.MaterialToolbar


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FolderFragment : Fragment() {

    private var _binding: FragmentFolderBinding? = null
    private val noteViewModel: NoteViewModel by viewModels()
    private val binding get() = _binding!!

    private var selectedColor: Int = 0
    private var currentPath = ""

    private var folder = FolderModel("p", 0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val id = arguments?.getString("id")
        this.folder = NoteLoader.getFolder(id)!!
        println("Folder: "+this.folder)
        this.currentPath = this.folder.title+"/"
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = this.folder.title
        _binding = FragmentFolderBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener {
            showNoteDialog()
        }
        binding.delete.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(this.context)
            builder.setMessage("Si confirmas, la carpeta se borrará para siempre :(")
                .setCancelable(false)
                .setPositiveButton("Borrar Carpeta") { _, _ ->
                    // Delete selected note from database
                    NoteLoader.deleteFolder(this.folder) //might be a mistake to access this directly
                    findNavController().navigate(R.id.action_FolderFragment_to_FirstFragment)
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
        binding.recyclerView.clearAnimation()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.loadNotes(currentPath)
    }

    private fun loadNotes(path: String) {
        val notes = noteViewModel.getNotes(path)
        val folders = noteViewModel.getFolders(path)
        val adapter = NoteAdapter(notes, folders, (resources.displayMetrics.widthPixels - 160) / 2)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter
    }


    private fun showNoteDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Agregar Nota")

        val view = layoutInflater.inflate(R.layout.dialog_add_note, null)

        val titleEditText = view.findViewById<EditText>(R.id.editTextTitle)
        val descriptionEditText = view.findViewById<EditText>(R.id.editTextDescription)
        val colorRadioGroup = view.findViewById<RadioGroup>(R.id.colorRadioGroup)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnAccept = view.findViewById<Button>(R.id.btnAccept)
        builder.setView(view)
        val dialog = builder.create()

        colorRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            // Handle color selection based on the checkedId
            selectedColor = when (checkedId) {
                R.id.radioRed -> Color.parseColor("#f5f5dc")
                R.id.radioGreen -> Color.parseColor("#C1FFC1")
                R.id.radioBlue -> Color.parseColor("#ADD8E6")
                else -> 0
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnAccept.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val selectedRadioButtonId = colorRadioGroup.checkedRadioButtonId

            if (title.isNotEmpty() && description.isNotEmpty() && selectedRadioButtonId != -1) {
                val newNote = NoteModel(
                    title,
                    selectedColor,
                    description,
                    (title + "\n" + description + "\n" + selectedColor).hashCode(), this.currentPath
                )
                newNote.relativePath = currentPath;
                noteViewModel.addNote(newNote)
                NoteLoader.saveNote(
                    context,
                    newNote
                ) //idk if we should access NoteLoader from here. should just create a method on noteViewModel as a fix
                setupRecyclerView()
                dialog.dismiss()
            } else {
                Snackbar.make(
                    view,
                    "El título, la descripción y el color son obligatorios",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        dialog.show()
    }
    private fun showDeleteFolderDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Deseas eliminar la carpeta?")

        val view = layoutInflater.inflate(R.layout.dialog_confirm, null)

        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnAccept = view.findViewById<Button>(R.id.btnAccept)

        builder.setView(view)
        val dialog = builder.create()


        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnAccept.setOnClickListener {


                dialog.dismiss()


        }
        dialog.show()
    }

    private fun setupRecyclerView() {
        loadNotes(currentPath)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}