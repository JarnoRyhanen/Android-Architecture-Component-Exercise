package com.home.androidarchitecturecomponentexercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;

    private FloatingActionButton addNote;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildRecyclerView();
        createViewModel();

        addNote = findViewById(R.id.main_activity_fab);
        addNote.setOnClickListener(onClickListener);

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        if (result.getData().getIntExtra(IntentExtras.EXTRA_ID, -1) == -1) {
                            String title = result.getData().getStringExtra(IntentExtras.EXTRA_TITLE);
                            String description = result.getData().getStringExtra(IntentExtras.EXTRA_DESCRIPTION);
                            int priority = result.getData().getIntExtra(IntentExtras.EXTRA_PRIORITY, 1);
                            NoteObject noteObject = new NoteObject(title, description, priority);
                            noteViewModel.insert(noteObject);

                            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
                        } else {
                            int id = result.getData().getIntExtra(IntentExtras.EXTRA_ID, -1);
                            if (id == -1) {
                                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String title = result.getData().getStringExtra(IntentExtras.EXTRA_TITLE);
                            String description = result.getData().getStringExtra(IntentExtras.EXTRA_DESCRIPTION);
                            int priority = result.getData().getIntExtra(IntentExtras.EXTRA_PRIORITY, 1);
                            NoteObject noteObject = new NoteObject(title, description, priority);
                            noteObject.setId(id);
                            noteViewModel.update(noteObject);

                            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
                    }
                });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAtPosition(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void createViewModel() {
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<NoteObject>>() {
            @Override
            public void onChanged(List<NoteObject> noteObjects) {
                adapter.setNotes(noteObjects);
            }
        });
    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(noteObject -> {
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            intent.putExtra(IntentExtras.EXTRA_ID, noteObject.getId());
            intent.putExtra(IntentExtras.EXTRA_TITLE, noteObject.getTitle());
            intent.putExtra(IntentExtras.EXTRA_DESCRIPTION, noteObject.getDescription());
            intent.putExtra(IntentExtras.EXTRA_PRIORITY, noteObject.getPriority());
            intentActivityResultLauncher.launch(intent);
        });
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == addNote) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intentActivityResultLauncher.launch(intent);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_notes) {
            noteViewModel.deleteAllNotes();
            Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}