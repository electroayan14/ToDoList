package com.example.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class NoteViewModel(app: Application) : AndroidViewModel(app) {

    private val repositary = NoteRepositary(app)
    private val allNotes = repositary.getAllNotes()

    fun insert(note:Note){
        repositary.insert(note)
    }
    fun update(note:Note){
        repositary.update(note)
    }
    fun delete(note:Note){
        repositary.delete(note)
    }
    fun deleteAllNotes(note:Note){
        repositary.deleteAllNotes()
    }
    fun getAllNotes(): LiveData<List<Note>>{
        return allNotes
    }

}