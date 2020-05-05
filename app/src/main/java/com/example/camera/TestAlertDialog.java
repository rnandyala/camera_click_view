package com.example.camera;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;



interface IAlertListener{

    void getStatus(boolean isComplete);
}
public class TestAlertDialog {

    boolean isComplete;
    AlertDialog.Builder mBuilder;
    IAlertListener mIAlertListener;

    public TestAlertDialog(Context mContext, String message){

        mBuilder = new AlertDialog.Builder(mContext);

        mBuilder.setMessage(message).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        isComplete = true;


                        dialog.dismiss();
                    }
                }


        );

AlertDialog alert = mBuilder.create();

alert.show();
    }



}
