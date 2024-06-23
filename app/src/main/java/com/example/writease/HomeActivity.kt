package com.example.writease

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import org.w3c.dom.Text
import java.io.Serializable

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var ivAddNotes: ImageView
    private lateinit var tvNoNotes: TextView
    private lateinit var dialog: Dialog
    private lateinit var layoutParams: LayoutParams
    private lateinit var tvSaveNote: TextView
    private lateinit var etNoteTitle: EditText
    private lateinit var etNoteContent: EditText
    private lateinit var layoutManager: LayoutManager
    private lateinit var adapter: NotesAdapter
    private lateinit var dbHelper: DBHelper

    private val noteList = ArrayList<Note>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        ivAddNotes = findViewById(R.id.ivAddNotes)
        tvNoNotes = findViewById(R.id.tvNoNotes)


        dbHelper = DBHelper(this)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        dialog = Dialog(this)
        dialog.setContentView(R.layout.new_notes_view)

        layoutParams = LayoutParams()
        layoutParams.width = LayoutParams.MATCH_PARENT
        layoutParams.height = LayoutParams.MATCH_PARENT
        dialog.window?.attributes = layoutParams

        tvSaveNote = dialog.findViewById(R.id.tvSaveNote)
        etNoteTitle = dialog.findViewById(R.id.etNoteTitle)
        etNoteContent = dialog.findViewById(R.id.etNoteContent)


        adapter = NotesAdapter(noteList, dbHelper, tvNoNotes)
        recyclerView.adapter = adapter
        adapter.setItemClickListener(object : ItemClickListener{
            override fun onClickListener(position: Int) {
                val i = Intent(applicationContext, NoteActivity::class.java)
                i.putExtra("noteId", noteList[position].id)
                i.putExtra("title", noteList[position].title)
                i.putExtra("content", noteList[position].content)
                startActivity(i)
            }

        })

        val id = intent.getStringExtra("id")
        val noteTitle = intent.getStringExtra("noteTitle")
        val noteContent = intent.getStringExtra("noteContent")
        
        for ((index, item) in noteList.withIndex()) {
            if (id == item.id) {
                if (noteTitle != null && noteContent != null) {
                    noteList[index] = Note(id, noteTitle, noteContent)
                    adapter.notifyItemChanged(index)
                }
            }
        }

        val list = dbHelper.getAllNotes()
        if (list.isNotEmpty()) {
            tvNoNotes.visibility = View.GONE
            for (item in list) {
                noteList.add(Note(item.id, item.title, item.content))
                adapter.notifyItemInserted(noteList.size - 1)
            }
        }

        tvSaveNote.setOnClickListener{
            val title = etNoteTitle.text.toString()
            val content = etNoteContent.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val id = System.currentTimeMillis().toString()
                dbHelper.insertNote(Note(id, title, content))
                noteList.add(Note(id, title, content))
                adapter.notifyItemInserted(noteList.size - 1)
                tvNoNotes.visibility = View.GONE
            } else {
                Toast.makeText(applicationContext, "Fill the title and content field", Toast.LENGTH_SHORT).show()
            }
            etNoteTitle.text.clear()
            etNoteContent.text.clear()
            dialog.dismiss()
        }

        ivAddNotes.setOnClickListener {
            dialog.show()
        }


    }
}