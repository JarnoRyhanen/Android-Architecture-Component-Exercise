package com.home.androidarchitecturecomponentexercise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<NoteObject> noteObjectList = new ArrayList<>();

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        NoteObject noteObject = noteObjectList.get(position);
        holder.priority.setText(String.valueOf(noteObject.getPriority()));
        holder.title.setText(noteObject.getTitle());
        holder.description.setText(noteObject.getDescription());
    }

    @Override
    public int getItemCount() {
        return noteObjectList.size();
    }

    public void setNotes(List<NoteObject> noteObjects){
        this.noteObjectList = noteObjects;
        notifyDataSetChanged();
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView priority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            itemView.findViewById(R.id.note_item_text_view_title);
            itemView.findViewById(R.id.note_item_text_view_description);
            itemView.findViewById(R.id.note_item_text_view_priority);
        }
    }
}
