package com.edue.docyou;

import android.content.Context;
//import android.support.annotation.LayoutRes;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Fosu on 9/16/2017.
 */

public class NoteAdapter extends ArrayAdapter<Note> {
    public NoteAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Note> notes) {
        super(context, resource, notes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item, null);
        }
        Note note = getItem(position);

        if (note != null){
            TextView title =(TextView) convertView.findViewById(R.id.list_note_title);
            TextView date = (TextView) convertView.findViewById(R.id.list_note_date);
            TextView content = (TextView) convertView.findViewById(R.id.list_note_content);

            title.setText(note.getTitle());
            date.setText(note.getDateTimeFormatted(getContext()));
// prints the content unto the listview
            content.setText(note.getContent());

        }
        return convertView;
    }
    // to make the latest saved note appear at the top, code begins from here
    @Override
    public int getCount() {
        return super.getCount();

    }

    @Nullable
    @Override
    public Note getItem(int position) {
        // this brings the latest saved note first(at the top)
        return super.getItem(getCount()- position-1);

    }

}
