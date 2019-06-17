package com.edue.docyou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Fosu on 9/16/2017.
 */

public class Camera extends AppCompatActivity implements View.OnClickListener{
    ImageButton ib;
    Bitmap bmp;
    final static int cameraData = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnew);
        initialize();
    }

    private void initialize() {
        //ib = (ImageButton) findViewById(R.id.ibCamera);
    }

    public void openCamera (View view){
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, cameraData);
    }

    @Override
    public void onClick(View view){
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, cameraData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            bmp = (Bitmap) extras.get("data");
        }

    }
}
