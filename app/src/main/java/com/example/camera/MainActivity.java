package com.example.camera;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button mButton;
    private final static int REQUEST_CODE_READ_PERMISSION = 01;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.mPermissions_model);
        mButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                   try {
                       askPermission();
                   }
                   catch(Exception ex){

                       AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
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
                }
        );

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                &&
                (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                &&
                (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        ) {
            Toast.makeText(this, "already_granted_the_permissison", Toast.LENGTH_SHORT).show();
// user granted permission

        }
        // for second time when user visits this gets executed
        else if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))

                &&
                (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    &&
                (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))

        ) {
            Toast.makeText(this, "required camera, write  & read permission or else", Toast.LENGTH_SHORT).show();

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, REQUEST_CODE_READ_PERMISSION);
        } else {

            // second and subsequent denials
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, REQUEST_CODE_READ_PERMISSION);



        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

try {
    switch (requestCode) {

        case REQUEST_CODE_READ_PERMISSION:
// gets called when we request the permission for the first time
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "yeah I am asking permission for the first time and you granted it", Toast.LENGTH_SHORT).show();
            }
            // on check & deny shouldshowrequestpermissionrationale will  be false
            // as well as when I allow  permission shouldshowrequestpermissionrationale will be false
            //
            // else it will be true
            else if (!(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
                    && !(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) &&
                    !(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))) {
                Toast.makeText(this, "go to setting and accept the permission", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, " oops I cannot open camera grant failed", Toast.LENGTH_LONG).show();
            }

            return;
    }
}
catch (Exception ex){
    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
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




}
