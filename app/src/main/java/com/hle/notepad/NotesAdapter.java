package com.hle.notepad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<MyViewHolder> { //Adapter has the target is MyViewHolder

    public static List<Notes> noteList; //Create the list that adapter is going to work with
    private MainActivity mainAct; //We need to call function on main activity
    public static OnNoteClickListener listener;

    NotesAdapter(List<Notes> litotes, MainActivity mainactivity) {
        this.noteList = litotes;
        this.mainAct = mainactivity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    //inflate the layout of sample and hook it to the entry
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_entry, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Notes note = noteList.get(position);
        // for each note in noteList, set its value to the each textView of MyViewHolder

        if (note.getTitle().length()>80){
            holder.title.setText(note.getTitle().substring(0, 80) + "...");
        } else {holder.title.setText(note.getTitle());}

        if (note.getContent().length()>80){
            holder.content.setText(note.getContent().substring(0, 80) + "...");
        } else {holder.content.setText(note.getContent());}



        holder.dateTime.setText(note.getLastDate().toString());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public interface OnNoteClickListener {
        void onNoteClick(Notes note);
    }

    public void setOnNoteClickListener(OnNoteClickListener listener) {
        this.listener = listener;
    }
}
