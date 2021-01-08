package com.edue.docyou;

import android.content.Intent;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // private static final String TAG = "MainActivity";
    FloatingActionButton fabu;
    private ListView listviewnotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();
        toolbar.setTitle("DocYou");
        toolbar.setNavigationIcon(R.mipmap.ic_launcher_round);

        fabu = (FloatingActionButton) findViewById(R.id.fab);
        listviewnotes = (ListView) findViewById(R.id.main_listview);
    }


    public void addNew (View view){
        Intent i = new Intent(this, AddNew.class);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_delete){
            deleteNote();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteNote() {
        if (listviewnotes == null){
            finish();
        } else {
            Utilities.deleteNote(getApplicationContext(), listviewnotes + Utilities.FILE_EXTENSION);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        listviewnotes.setAdapter(null);
        //   listviewnotes.setMenuCreator(creator);

        final ArrayList<Note> notes = Utilities.getAllSavedNotes(this);

        if(notes == null || notes.size() == 0){
            getApplicationContext();
            Toast.makeText(this,  "No saved notes", Toast.LENGTH_SHORT).show();

        }else{
            NoteAdapter na = new NoteAdapter(this,R.layout.item, notes);
            listviewnotes.setAdapter(na);

            listviewnotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String fileName = ((Note) listviewnotes.getItemAtPosition(i)).getDateTime()
                            + Utilities.FILE_EXTENSION;

                    Intent viewNoteIntent = new Intent(getApplicationContext(), AddNew.class);
                  /* This "viewNoteIntent.putExtra("NOTE_FILE", fileName);" will copy the texts...
                  in the text view page into the edittext in the addnew page.
                  if you ommmit this command, and you click on the item in the listview and it goes...
                  into the addnew page, the edittexts will be empty

                   */
                    viewNoteIntent.putExtra("NOTE_FILE", fileName);
                    startActivity(viewNoteIntent);
                }
            });

        }
    }


}
