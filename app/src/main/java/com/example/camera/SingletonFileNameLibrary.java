package com.example.camera;

import java.util.ArrayList;

public class SingletonFileNameLibrary {

    private ArrayList<String> mListOfFileNames = new ArrayList<>();
     private static SingletonFileNameLibrary mSingleTonFileNameLibrary = null;
    private SingletonFileNameLibrary(){

    }


    public static SingletonFileNameLibrary getInstance(){

       if(mSingleTonFileNameLibrary == null) {
           mSingleTonFileNameLibrary = new SingletonFileNameLibrary();
       return mSingleTonFileNameLibrary;
       }
        else
     return   mSingleTonFileNameLibrary;
    }

    public ArrayList<String> getListOfFileName(){

        return mListOfFileNames;

    }

    public void setmListOfFileNames(String mFileName){

        mListOfFileNames.add(mFileName);

    }

}
