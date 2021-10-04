package com.home.androidarchitecturecomponentexercise;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private final NoteDao noteDao;
    private final LiveData<List<NoteObject>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(NoteObject note) {
        NoteOperationRunnable noteOperationRunnable = new NoteOperationRunnable(noteDao, note, "insert");
        new Thread(noteOperationRunnable).start();
    }

    public void update(NoteObject note) {
        NoteOperationRunnable noteOperationRunnable = new NoteOperationRunnable(noteDao, note, "update");
        new Thread(noteOperationRunnable).start();
    }

    public void delete(NoteObject note) {
        NoteOperationRunnable noteOperationRunnable = new NoteOperationRunnable(noteDao, note, "delete");
        new Thread(noteOperationRunnable).start();
    }

    public void deleteAllNotes() {
        NoteOperationRunnable noteOperationRunnable = new NoteOperationRunnable(noteDao, "deleteAll");
        new Thread(noteOperationRunnable).start();
    }

    public LiveData<List<NoteObject>> getAllNotes() {
        return allNotes;
    }


    private static class NoteOperationRunnable implements Runnable {

        private final NoteDao noteDao;
        private final NoteObject noteObject;
        private final String operation;

        NoteOperationRunnable(NoteDao noteDao, NoteObject noteObject, String operation) {
            this.noteDao = noteDao;
            this.noteObject = noteObject;
            this.operation = operation;
        }

        NoteOperationRunnable(NoteDao noteDao, String operation) {
            this.noteDao = noteDao;
            this.operation = operation;
            noteObject = null;
        }

        @Override
        public void run() {
            switch (operation) {
                case "insert":
                    noteDao.insert(noteObject);
                    break;
                case "update":
                    noteDao.update(noteObject);
                    break;
                case "delete":
                    noteDao.delete(noteObject);
                    break;
                case "deleteAll":
                    noteDao.deleteAllNotes();
                    break;
            }
        }
    }
}
