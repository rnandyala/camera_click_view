package com.example.camera;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import android.view.View;
import android.widget.Button;

public class CameraSettings extends AppCompatActivity {

    Button mClosePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_settings);

        mClosePreference = findViewById(R.id.mClosePreference);


       try {
           getSupportFragmentManager()
                   .beginTransaction()
                   .replace(android.R.id.content, new CameraPreference())
                   .commit();

       }
       catch (Exception ex){
           AlertDialog.Builder mBuilder = new AlertDialog.Builder(CameraSettings.this);
           mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {

                       }
                   }
           );
           AlertDialog alert = mBuilder.create();
           alert.show();
       }










    }


    public static class CameraPreference extends PreferenceFragmentCompat {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.camera_settings);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            //super.onCreate(savedInstanceState);

        }
    }

}
