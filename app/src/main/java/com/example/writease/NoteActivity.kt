package com.example.writease


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class NoteActivity : AppCompatActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var ivEdit: ImageView
    private lateinit var tvTitleText: TextView
    private lateinit var tvContentText: TextView
    private lateinit var dialog: Dialog
    private lateinit var layoutParams: LayoutParams
    private lateinit var tvSaveNote: TextView
    private lateinit var etNoteTitle: EditText
    private lateinit var etNoteContent: EditText
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_note)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivBack = findViewById(R.id.ivBack)
        ivEdit = findViewById(R.id.ivEdit)
        tvTitleText = findViewById(R.id.tvTitleText)
        tvContentText = findViewById(R.id.tvContentText)


        ivBack.setOnClickListener {
            val i = Intent(applicationContext, HomeActivity::class.java)
            startActivity(i)
            finish()
        }


        val id = intent.getStringExtra("noteId")
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")



        tvTitleText.text = title
        tvContentText.text = content

        dbHelper = DBHelper(this)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.new_notes_view)

        layoutParams = LayoutParams()
        layoutParams.width = LayoutParams.MATCH_PARENT
        layoutParams.height = LayoutParams.MATCH_PARENT
        dialog.window?.attributes = layoutParams

        tvSaveNote = dialog.findViewById(R.id.tvSaveNote)
        etNoteTitle = dialog.findViewById(R.id.etNoteTitle)
        etNoteContent = dialog.findViewById(R.id.etNoteContent)

        tvSaveNote.setOnClickListener {
            val noteTitle = etNoteTitle.text.toString()
            val noteContent = etNoteContent.text.toString()

            if (noteTitle.isNotEmpty() && noteContent.isNotEmpty()) {
                if (id != null) {
                    dbHelper.updateNote(id, noteTitle, noteContent)
                }
            }
            dialog.dismiss()
            val i = Intent(applicationContext, HomeActivity::class.java)
            i.putExtra("id", id)
            i.putExtra("noteTitle", noteTitle)
            i.putExtra("noteContent", noteContent)
            startActivity(i)
            finish()
        }

        ivEdit.setOnClickListener {
            etNoteTitle.setText(title)
            etNoteContent.setText(content)
            dialog.show()
        }
    }
}