package com.hle.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";;
    private final ArrayList<Notes> noteList = new ArrayList<>();

    private RecyclerView recyclerView;
    private NotesAdapter nAdapter;

    private static final int NOTE_REQUEST_CODE = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        nAdapter = new NotesAdapter(noteList, this);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.smoothScrollToPosition(0);

        nAdapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Notes note) {
                Intent intent = new Intent(MainActivity.this, OpenNewNote.class);
                intent.putExtra("EDIT_TITLE", note.getTitle());
                intent.putExtra("EDIT_CONTENT", note.getContent());
                intent.putExtra("LAST_DATE", note.getLastDate());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
                nAdapter.notifyDataSetChanged();
            }
        });
        noteList.clear();
        readJSONData(); //load all notes from NotesFile.json to empty Array noteList
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeJSONData(); //write every note in Array noteList into NotesFile.json
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_pad_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, "About this app", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(MainActivity.this, AboutThisApp.class);
                startActivity(intent1);
                return true;
            case R.id.newnote:
                Toast.makeText(this, "Add a note", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MainActivity.this, OpenNewNote.class);
                //put something in content because if intent2 is null, it cant be compared in save() in OpenNewNote
                intent2.putExtra("EDIT_TITLE", "");
                intent2.putExtra("EDIT_CONTENT","");
                intent2.putExtra("LAST_DATE", "");
                startActivityForResult(intent2, NOTE_REQUEST_CODE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NOTE_REQUEST_CODE || requestCode == EDIT_NOTE_REQUEST) {
            if (resultCode == RESULT_OK) {
                String title = data.getStringExtra("USER_TEXT_TITLE");
                String content = data.getStringExtra("USER_TEXT_CONTENT");
                if (title == null) {
                    Toast.makeText(this, "Null title not saved", Toast.LENGTH_SHORT).show();
                    setTitle("NOTE PAD " + "(" + Integer.toString(noteList.size()) + ")");
                    return;
                }
                else if (title.isEmpty()) {
                    Toast.makeText(this, "Empty title not saved", Toast.LENGTH_SHORT).show();
                    setTitle("NOTE PAD " + "(" + Integer.toString(noteList.size()) + ")");
                    return;
                }
                else {
                    noteList.add(0, new Notes(title, content));
                    Toast.makeText(this, "Save note!", Toast.LENGTH_SHORT).show();
                    nAdapter.notifyDataSetChanged();
                    setTitle("NOTE PAD " + "(" + Integer.toString(noteList.size()) + ")");
                }
            }
            else {
                return;
            }
        }
        else {
            return;
        }
    }

    private void writeJSONData() {

        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.notes_file), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            writer.setIndent("  ");
            writer.beginArray();  //open  a bracket [
            for (Notes n : noteList) { //for each note in the array, do the following { ... }
                writer.beginObject();
                writer.name("title").value(n.getTitle());
                writer.name("text").value(n.getContent());
                writer.name("lastDate").value(n.getLastDate().getTime());
                writer.endObject();
            }
            writer.endArray(); //close with a bracket ]
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readJSONData() {

        try {
            FileInputStream fis = getApplicationContext().
                    openFileInput(getString(R.string.notes_file));

            // Read string content from file
            byte[] data = new byte[(int) fis.available()]; // this technique is good for small files. load a new byte, with the size of what input string has available byte
            int loaded = fis.read(data); //tell the input string to read everything in that array and return the number of byte
            Log.d(TAG, "readJSONData: Loaded " + loaded + " bytes");
            fis.close();
            String json = new String(data);

            // Create JSON Array from string file content
            JSONArray noteArr = new JSONArray(json); // create a jsonarray using json string
            for (int i = 0; i < noteArr.length(); i++) {
                JSONObject nObj = noteArr.getJSONObject(i); //get the object at the position i

                // Access note data fields
                String title = nObj.getString("title");
                String text = nObj.getString("text");
                long dateMS = nObj.getLong("lastDate");

                // Create Note and add to ArrayList
                Notes n = new Notes(title, text);
                n.setLastDate(dateMS);
                noteList.add(n);
            }
            Log.d(TAG, "readJSONData: " + noteList);
            setTitle("NOTE PAD " + "(" + Integer.toString(noteList.size()) + ")");

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "readJSONData: " + e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Notes m = noteList.get(pos);
        Toast.makeText(v.getContext(), "Edit note: " + m.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(final View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        final Notes m = noteList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.twotone_delete_forever_black_36dp);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                noteList.remove(m);
                nAdapter.notifyDataSetChanged();
                setTitle("NOTE PAD " + "(" + Integer.toString(noteList.size()) + ")");
                Toast.makeText(v.getContext(), "Delete note: " + m.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });

        builder.setMessage("Do you want to delete?");
        builder.setTitle("DELETE");

        AlertDialog dialog = builder.create();
        dialog.show();

        return false;
    }
}