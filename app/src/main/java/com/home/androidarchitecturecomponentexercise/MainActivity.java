package com.home.androidarchitecturecomponentexercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;

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
                        String title = result.getData().getStringExtra(IntentExtras.EXTRA_TITLE);
                        String description = result.getData().getStringExtra(IntentExtras.EXTRA_DESCRIPTION);
                        int priority = result.getData().getIntExtra(IntentExtras.EXTRA_PRIORITY, 1);

                        NoteObject noteObject = new NoteObject(title, description, priority);
                        noteViewModel.insert(noteObject);

                        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
                    }
                });
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
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == addNote) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intentActivityResultLauncher.launch(intent);
            }
        }
    };
}