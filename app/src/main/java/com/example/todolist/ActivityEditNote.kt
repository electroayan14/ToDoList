package com.example.todolist

import android.app.Activity
import android.content.Intent
import android.icu.text.Normalizer2
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_activiy_add_edit_note.*

const val EXTRA_ID = "com.example.todolist.EXTRA_ID"
 const val EXTRA_TITLE = "com.example.todolist.EXTRA_TITLE"
 const val EXTRA_DESCRIPTION = "com.example.todolist.EXTRA_DESCRIPTION"
 const val EXTRA_PRIORITY = "com.example.todolist.EXTRA_PRIORITY"

class activiy_add_edit_note : AppCompatActivity() {
    private lateinit var mode: Mode

    private var noteId: Int =-1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activiy_add_edit_note)

        numberpicker_button.minValue= 1
        numberpicker_button.maxValue= 10

        supportActionBar?.setHomeAsUpIndicator(R.drawable.cancel)

        noteId= intent.getIntExtra(EXTRA_ID,-1)
        mode= if (noteId== -1) Mode.AddNote
            else Mode.EditNote

        when(mode){
            Mode.AddNote -> title ="Add Note"
            Mode.EditNote -> {
                title="Edit Note"
                txt_1.setText(intent.getStringExtra(EXTRA_TITLE))
                txt_2.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
                numberpicker_button.value=intent.getIntExtra(EXTRA_PRIORITY,-1)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater= menuInflater
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_note ->{
                saveNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote(){
        val title=txt_1.text.toString()
        val desc=txt_2.text.toString()
        val priority=numberpicker_button.value

        if(title.isEmpty()|| desc.isEmpty()){
            Toast.makeText(this,"please insert title and description",Toast.LENGTH_LONG).show()
            return
        }

        val data= Intent()
        if (noteId!= -1)
            data.putExtra(EXTRA_ID,noteId)
        data.putExtra(EXTRA_TITLE,title)
        data.putExtra(EXTRA_DESCRIPTION,desc)
        data.putExtra(EXTRA_PRIORITY,priority)

        setResult(Activity.RESULT_OK,data)
        finish()
    }
    private sealed class Mode{
        object AddNote: Mode()
        object EditNote: Mode()
    }
}