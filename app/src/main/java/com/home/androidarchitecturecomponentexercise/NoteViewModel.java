package com.home.androidarchitecturecomponentexercise;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<NoteObject>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.getAllNotes();
    }

    public void insert(NoteObject note){
        noteRepository.insert(note);
    }

    public void update(NoteObject note){
        noteRepository.update(note);
    }
    public void delete(NoteObject note){
        noteRepository.delete(note);
    }

    public void deleteAllNotes(){
        noteRepository.deleteAllNotes();
    }

    public LiveData<List<NoteObject>> getAllNotes(){
        return allNotes;
    }
}
