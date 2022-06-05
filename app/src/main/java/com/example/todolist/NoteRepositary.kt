package com.example.todolist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance
import java.util.Calendar.getInstance

class NoteRepositary(application: Application) {
    private lateinit var noteDao: NoteDao
    private lateinit var allNotes: LiveData<List<Note>>

    private val database = NoteDatabase.getInstance(application)

    init {
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
    }

    fun insert(note: Note) {
        subscribeOnBackground {
            noteDao.insert(note)
        }
    }

    fun update(note: Note) {
        subscribeOnBackground {
            noteDao.update(note)
        }
    }

    fun delete(note: Note) {
        subscribeOnBackground{
            noteDao.delete(note)
        }
    }

    fun deleteAllNotes() {
        subscribeOnBackground {
            noteDao.deleteAllNotes()
        }
    }
    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }
}