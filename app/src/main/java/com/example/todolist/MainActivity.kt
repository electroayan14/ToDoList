package com.example.todolist

import android.app.Activity
import android.content.Intent
import android.icu.lang.UCharacter.IndicPositionalCategory.LEFT
import android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.EnumSet.of

const val ADD_NOTE_REQUEST = 1
const val EDIT_NOTE_REQUEST= 1

class MainActivity : AppCompatActivity() {
    private lateinit var vm: NoteViewModel
    private lateinit var adapter:NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerview()

        setUpListeners()

        vm=ViewModelProvider(this).get(NoteViewModel::class.java)

        vm.getAllNotes().observe(this, Observer {
            Log.i("Notes Observed","$it")

            adapter.submitList(it)
        })
    }
    private fun setUpListeners(){
        floatingActionButton.setOnClickListener {
            val intent = Intent(this,ActivityEditNote::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,LEFT or RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note= adapter.getNoteAt(viewHolder.adapterPosition)
                vm.delete(note)
            }
        }).attachToRecyclerView(recyler_view)
    }
    private fun setUpRecyclerview(){
        recyler_view.layoutManager=LinearLayoutManager(this)
        recyler_view.setHasFixedSize(true)

        adapter= NoteAdapter { clickedNote->
            val intent = Intent(this,ActivityEdit)
            intent.putExtra(EXTRA_ID,clickedNote.id)
            intent.putExtra(EXTRA_TITLE,clickedNote.title)
            intent.putExtra(EXTRA_DESCRIPTION,clickedNote.description)
            intent.putExtra(EXTRA_PRIORITY,clickedNote.priority)
            startActivityForResult(intent, EDIT_NOTE_REQUEST)
        }
        recyler_view.adapter= adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data!=null && requestCode== ADD_NOTE_REQUEST && resultCode==Activity.RESULT_OK){
            val title: String? = data.getStringExtra(EXTRA_TITLE)
            val description: String? = data.getStringExtra(EXTRA_DESCRIPTION)
            val priority:Int =data.getIntExtra(EXTRA_PRIORITY,-1)
            vm.insert(Note(title.toString(), description.toString(),priority))
            Toast.makeText(this,"Note Inserted!",Toast.LENGTH_SHORT).show()
        }
        else if (data!=null && requestCode== EDIT_NOTE_REQUEST && resultCode==Activity.RESULT_OK){
            val id = data.getIntExtra(EXTRA_ID,-1)
            if (id==-1){
                Toast.makeText(this,"Note couldn't be updated",Toast.LENGTH_SHORT).show()
                return
            }
            val title: String? = data.getStringExtra(EXTRA_TITLE)
            val description: String? = data.getStringExtra(EXTRA_DESCRIPTION)
            val priority: Int = data.getIntExtra(EXTRA_PRIORITY,-1)
            vm.insert(Note(title.toString(), description.toString(),priority))
            Toast.makeText(this,"Note Updated",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Not not saved",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.main_menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.close_button ->{
                vm.deleteAllNotes()
                Toast.makeText(this,"All notes deleted",Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}


