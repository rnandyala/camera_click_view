package com.example.camera;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageButton;

public class FormAction extends AppCompatActivity {

    ImageButton mTakePicture;
    ImageButton mTakeVideo;

    private static final int RequestThumbnailPicture  = 005;
    private static final int RequestThumbnailVideo = 006;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == RequestThumbnailPicture){
            // I should captured image here in the imageview ..
             data.getExtras().getString("fileName");


        }

        if(requestCode == RequestThumbnailVideo){
            data.getExtras().getString("fileName");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        mTakePicture = findViewById(R.id.take_picture);

        mTakeVideo = findViewById(R.id.take_video);




        mTakePicture.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(FormAction.this,TakePictureOrVideo.class);

                        i.putExtra("camera",1);

                        startActivityForResult(i, RequestThumbnailPicture);
                    }
                }


        );

        mTakeVideo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(FormAction.this,TakePictureOrVideo.class);

                        i.putExtra("video",1);

                        startActivityForResult(i, RequestThumbnailVideo);
                    }
                }


        );



    }

}
