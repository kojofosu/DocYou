package com.edue.docyou;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static com.edue.docyou.Camera.cameraData;

/**
 * Created by Fosu on 9/16/2017.
 */

public class AddNew extends AppCompatActivity{
    private EditText txtSpeechInput;
    private ImageButton btnSpeak;
    BottomAppBar addNewBottomAppBar;
    FloatingActionButton addNewFAB;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private static final int SELECTED_PICTURE=1;
    private EditText mEtTitle;
    private EditText mEtContent;

    private String mNoteFileName;
    private Note mLoadedNote;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnew);

        mEtTitle = (EditText) findViewById(R.id.etTitle);
        mEtContent = (EditText) findViewById(R.id.etContent);
        addNewBottomAppBar = findViewById(R.id.add_new_bottom_app_bar);
        addNewFAB = findViewById(R.id.add_new_material_fab);



        mNoteFileName = getIntent().getStringExtra("NOTE_FILE");
        if(mNoteFileName != null && !mNoteFileName.isEmpty()){
            mLoadedNote = Utilities.getNoteByName(this, mNoteFileName);

            if (mLoadedNote != null){
                mEtTitle.setText(mLoadedNote.getTitle());
                mEtContent.setText(mLoadedNote.getContent());
            }
        }
        txtSpeechInput = (EditText) findViewById(R.id.etContent);
        //btnSpeak = (ImageButton) findViewById(R.id.ibMicrophone);

        //hide the action bar
        // getActionBar().hide();
        getActionBar();

//        btnSpeak.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                promptSpeechInput();
//            }
//        });

        bottomAppBar();
    }

    private void bottomAppBar() {
        addNewFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save
                Note note;

                if(mLoadedNote == null){
                    note = new Note(System.currentTimeMillis(), mEtTitle.getText().toString()
                            , mEtContent.getText().toString());
                }
                else {

                    //The "System.currentTimeMillis()" will set the current time and date after saving but will create a new docyou
                    // The "mLoadedNote.getDateTime()" will use the old date and time even after saving
                    note = new Note(mLoadedNote.getDateTime(), mEtTitle.getText().toString()
                            , mEtContent.getText().toString());
                }

                if(Utilities.saveNote(getApplicationContext(), note)) {
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        addNewBottomAppBar.inflateMenu(R.menu.bottom_appbar_menu);
        addNewBottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.action_open_camera){
                    //Open camera
                    //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); or...
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, cameraData);
                }
                if (id == R.id.action_open_gallery){
                    //Open gallery
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, SELECTED_PICTURE);
                }
                return true;
            }
        });

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.append(result.get(0)+" ");


                }

                break;

            }

        }
    }

    // This opens the camera when the "Camera" button is clicked
    public void openCamera(View view) {
        //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); or...
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, cameraData);
    }
    // This opens the gallery when the "gallery" button is clicked
    public void openGallery (View view){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECTED_PICTURE);
    }

    public void onClickCancel (View view){
        // This inflates a dialog when the "Cancel" button in the addnew page is clicked
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Discard Memo");
        builder.setMessage("All changes will be discarded.");
        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "Discarded", Toast.LENGTH_SHORT).show();
                // This finish i've typed here will close the add new page and go back to the main...
                // activity page when the "Discard" option is selected in the dialog
                finish();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // this return will still be on the addnew page
                        //return ;
                    }
                });
        builder.create().show();

    }

    public void onClickSave (View view){
        Note note;

        if(mLoadedNote == null){
            note = new Note(System.currentTimeMillis(), mEtTitle.getText().toString()
                    , mEtContent.getText().toString());
        } else {

            //The "System.currentTimeMillis()" will set the current time and date after saving but will create a new docyou
            // The "mLoadedNote.getDateTime()" will use the old date and time even after saving
            note = new Note(mLoadedNote.getDateTime(), mEtTitle.getText().toString()
                    , mEtContent.getText().toString());
        }



        if(Utilities.saveNote(this, note)) {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void onClickDelete (View view){
        if (mLoadedNote == null){
            finish();
        }else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("delete?")
                    // ".setMessage("You are about to delete " + mEtTitle.getText().toString() + ", are you sure?")" will tell you you are about to delete and display the title of the note you want to delete
                    .setMessage("You are about to delete this note")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Utilities.deleteNote(getApplicationContext(), mLoadedNote.getDateTime() + Utilities.FILE_EXTENSION);

                           Toast.makeText( getApplicationContext(), /*mEtTitle.getText().toString()+ */ "Note deleted", Toast.LENGTH_SHORT).show();

                            finish();

                        }
                    })
                    .setNegativeButton("No", null)
                    //.setCancelable(false); will prevent the dialog from closing when you click outside the dialog
                    .setCancelable(true);
            dialog.show();


            // Utilities.deleteNote(getApplicationContext(), mLoadedNote.getDateTime() + Utilities.FILE_EXTENSION);

        }

    }


}
