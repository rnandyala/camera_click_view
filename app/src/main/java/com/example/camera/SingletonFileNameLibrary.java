package com.example.camera;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.ArrayList;

public class SingletonFileNameLibrary {

    private ArrayList<String> mListOfFileNames = new ArrayList<>();
     private static SingletonFileNameLibrary mSingleTonFileNameLibrary = null;
    private SingletonFileNameLibrary(){

    }


    public static SingletonFileNameLibrary getInstance(){
try {
    if (mSingleTonFileNameLibrary == null) {
        mSingleTonFileNameLibrary = new SingletonFileNameLibrary();
        return mSingleTonFileNameLibrary;
    } else
        return mSingleTonFileNameLibrary;
}
catch(Exception ex){
ex.getMessage();
}
return  null;
    }

    public ArrayList<String> getListOfFileName(){

        return mListOfFileNames;

    }

    public void setmListOfFileNames(String mFileName){
try {
    mListOfFileNames.add(mFileName);
}
catch(Exception ex){
    ex.getMessage();
}
    }

}
