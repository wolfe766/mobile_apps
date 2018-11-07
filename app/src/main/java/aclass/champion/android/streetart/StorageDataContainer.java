package aclass.champion.android.streetart;

import android.content.Context;

import com.google.firebase.storage.StorageReference;

import java.io.File;

public class StorageDataContainer {

    public StorageReference storage;
    public File FilePath;
    public Context contex;
    public Picture pic;

    public StorageDataContainer(StorageReference stor, File fil, Context con, Picture pic) {
        this.storage = stor;
        this.FilePath = fil;
        this.contex = con;
        this.pic = pic;
    }
}
