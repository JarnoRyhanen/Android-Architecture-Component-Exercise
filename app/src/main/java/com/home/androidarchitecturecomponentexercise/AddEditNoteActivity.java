package com.home.androidarchitecturecomponentexercise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditNoteActivity extends AppCompatActivity {

    private static final String TAG = "AddEditNoteActivity";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        instantiateViews();

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        Intent intent = getIntent();
        if (intent.hasExtra(IntentExtras.EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(IntentExtras.EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(IntentExtras.EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(IntentExtras.EXTRA_PRIORITY, 1));
        } else {
            setTitle("Add Note");
        }


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close);
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        int priority = numberPickerPriority.getValue();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please insert a title and a description", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(IntentExtras.EXTRA_TITLE, title);
        data.putExtra(IntentExtras.EXTRA_DESCRIPTION, description);
        data.putExtra(IntentExtras.EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(IntentExtras.EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(IntentExtras.EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    private void instantiateViews() {
        editTextTitle = findViewById(R.id.activity_add_note_edit_text_title);
        editTextDescription = findViewById(R.id.activity_add_note_edit_text_description);
        numberPickerPriority = findViewById(R.id.activity_add_note_edit_text_number_picker);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            saveNote();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}