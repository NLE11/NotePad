package com.hle.notepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OpenNewNote extends AppCompatActivity {

    EditText title;
    EditText content;
    String oldTitle = "";
    String oldContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_new_note);

        title = findViewById(R.id.edittitle);
        content = findViewById(R.id.editcontent);

        Intent intent = getIntent();
        oldTitle = intent.getStringExtra("EDIT_TITLE");
        oldContent = intent.getStringExtra("EDIT_CONTENT");
        if (intent.hasExtra("EDIT_TITLE")) {
            setTitle("EDIT NOTE");
            title.setText(oldTitle);
            content.setText(oldContent);
        } else {
            setTitle("NEW NOTE");
            title.setText("");
            content.setText("");
        }
        content.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                save();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save() {
        String titlesave = title.getText().toString();
        String contentsave = content.getText().toString();
        if (!oldTitle.equals(titlesave) || !oldContent.equals(contentsave)) { //compare new edit with old edit to see if it's different
            Intent data = new Intent();
            data.putExtra("USER_TEXT_TITLE", titlesave);
            data.putExtra("USER_TEXT_CONTENT", contentsave);
            setResult(RESULT_OK, data);
        } else return;
    }

    @Override
    public void onBackPressed() {
        // Pressing the back arrow closes the current activity, returning us to the original activity
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.twotone_save_black_36dp);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                save();
                OpenNewNote.super.onBackPressed();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                OpenNewNote.super.onBackPressed();
            }
        });
        builder.setMessage("Your note is not saved! \nDo you want to save '" + title.getText() + "'?");
        builder.setTitle("SAVE");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}