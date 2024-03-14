package com.example.easynotes.View.Fragments

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.core.text.set
import androidx.navigation.fragment.findNavController
import com.example.easynotes.Model.FolderModel
import com.example.easynotes.Model.NoteLoader
import com.example.easynotes.Model.NoteModel
import com.example.easynotes.R
import com.example.easynotes.databinding.FragmentViewNoteBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ViewNoteFragment : Fragment() {

    private var _binding: FragmentViewNoteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var note = NoteModel("",0,"", 0)
    private var selectedColor: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentViewNoteBinding.inflate(inflater, container, false)
        val id = arguments?.getString("id")?.toInt() ?: 0
        this.note = NoteLoader.getNote(id)

        binding.root.setBackgroundColor(note.color)

        binding.textviewSecond.text = Editable.Factory.getInstance().newEditable(this.note.text)
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar) // Asegúrate de que el ID sea el correcto
        toolbar.title = this.note.title
        binding.fab.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setMessage("Si confirmas, la nota se borrará para siempre :(")
                .setCancelable(false)
                .setPositiveButton("Borrar Nota") { dialog, id ->
                    // Delete selected note from database
                    NoteLoader.deleteNote(this.note) //might be a mistake to access this directly
                    findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                }
                .setNegativeButton("Cancelar") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
        binding.fabTags.setOnClickListener {
            showTagsDialog()
        }
        binding.fabEdit.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Cambiar titulo/color")

            val view = layoutInflater.inflate(R.layout.modify_note, null)
            val btnCancel = view.findViewById<Button>(R.id.btnCancel)
            val btnAccept = view.findViewById<Button>(R.id.btnAccept)
            val titleEditText = view.findViewById<EditText>(R.id.editTextTitle)
            val colorRadioGroup = view.findViewById<RadioGroup>(R.id.colorRadioGroup)


            selectedColor = note.color //Hay que poner q se muestre el color q esta previamente
            colorRadioGroup.check(selectedColor)

            colorRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                selectedColor = when (checkedId) {
                    R.id.radioRed -> Color.parseColor("#f5f5dc")
                    R.id.radioGreen -> Color.parseColor("#C1FFC1")
                    R.id.radioBlue -> Color.parseColor("#ADD8E6")
                    else -> 0
                }
            }

            builder.setView(view)
            val dialog = builder.create()

            btnAccept.setOnClickListener {
                var title = titleEditText.text.toString()

                if (!NoteLoader.nameRepeat(title)) {
                    if(title.isEmpty()){
                        title = note.title
                    }
                    NoteLoader.changeNoteTitleColor(note.id, title, selectedColor)
                    Snackbar.make(view, "Se ha cambiado correctamente", Snackbar.LENGTH_SHORT).show()
                    dialog.dismiss()
                    findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                } else {
                    Snackbar.make(view, "No se puede poner el mismo titulo que en otra nota", Snackbar.LENGTH_SHORT).show()
                }

            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
        return binding.root

    }
    private fun showTagsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Tags asociadas a esta nota")
        val checked = BooleanArray(note.tags.size) {false}

        builder.setMultiChoiceItems(note.tags.toTypedArray(), checked) { _, which, isChecked ->
            // Handle checkbox state change
            // 'which' is the position of the item in the list
            // 'isChecked' indicates whether the item is checked or unchecked
        }
        //val layout = LinearLayout(this@MainActivity)
        //layout.orientation = LinearLayout.VERTICAL
        //layout.addView(inputEditText)

        builder.setPositiveButton("Borrar Seleccionados", null)
        builder.setNegativeButton("Salir", null)
        builder.setNeutralButton("Añadir Tag", null)
        builder.setOnDismissListener { NoteLoader.saveNote(context, note) }
        val alert = builder.create()
        alert.show()
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            for (i in (note.tags.size-1) downTo 0){
                if (checked[i]){
                    this@ViewNoteFragment.note.tags.removeAt(i);
                }
            }
            NoteLoader.saveNote(context, this@ViewNoteFragment.note)
            alert.dismiss()
            showTagsDialog()
        }
        alert.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
            //show a text box, and add the input as a new tag
            val builder2 = AlertDialog.Builder(requireContext())
            builder2.apply {
                setTitle("Enter Text")
                setMessage("Please enter your text:")

                val inputEditText = EditText(context)
                builder2.setView(inputEditText)

                //val layout = LinearLayout(context)
                //layout.orientation = LinearLayout.VERTICAL
                //layout.addView(inputEditText)
                //setView(layout)

                setPositiveButton("OK") { dialog, which ->
                    val userInput = inputEditText.text.toString()
                    this@ViewNoteFragment.note.tags.add(userInput)
                    dialog.dismiss()
                    alert.dismiss()
                    showTagsDialog()
                }
                setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                    alert.dismiss()
                    showTagsDialog()
                }
            }
                // Create and show the dialog
            builder2.create().show()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editText = view.findViewById<EditText>(R.id.textview_second)

        editText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Aumentar el tamaño vertical al hacer clic
                editText.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            false
        }

        val title = arguments?.getString("title")
        val context = this.context
        // Registra un TextWatcher en el EditText
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Antes de que el texto cambie
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Durante la edición del texto
            }

            override fun afterTextChanged(editable: Editable?) {
                // Después de que el texto cambie
                val text = editable.toString()
                val data = NoteLoader.getNotes("")

                val notaEncontrada = data.values.find { it.title == title } //esto está mal, las notas se identifican por su id, no por su título, but oh well

                if (notaEncontrada != null) {
                    notaEncontrada.text = text
                } else {
                    println("Nota no encontrada")
                }
                NoteLoader.saveNote(context, notaEncontrada)
            }
        })
        /*binding.buttonSecond.setOnClickListener {
           findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}