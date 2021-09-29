package com.home.androidarchitecturecomponentexercise;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<NoteObject>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(NoteObject note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(NoteObject note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(NoteObject note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

    public LiveData<List<NoteObject>> getAllNotes() {
        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<NoteObject, Void, Void> {

        private final NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteObject... noteObjects) {
            noteDao.insert(noteObjects[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<NoteObject, Void, Void> {

        private final NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteObject... noteObjects) {
            noteDao.update(noteObjects[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<NoteObject, Void, Void> {

        private final NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteObject... noteObjects) {
            noteDao.delete(noteObjects[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<NoteObject, Void, Void> {

        private final NoteDao noteDao;

        private DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteObject... noteObjects) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

}
