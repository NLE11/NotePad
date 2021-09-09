package com.hle.notepad;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView content;
    TextView dateTime;

    MyViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.titlesample);
        content = view.findViewById(R.id.contentsample);
        dateTime = view.findViewById(R.id.datesample);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                if (NotesAdapter.listener != null && pos != RecyclerView.NO_POSITION) {
                    NotesAdapter.listener.onNoteClick(NotesAdapter.noteList.get(pos));
                }

            }
        });


    }
}
