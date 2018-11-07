package aclass.champion.android.streetart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class StorageController extends AsyncTask<StorageDataContainer, Void, String> {
    public static final String TAG = "Storage Controller Activity";
    private final FirebaseFirestore FIRESTOREDB = FirebaseFirestore.getInstance();

    Uri downloadUri;

    @Override
    protected String doInBackground(StorageDataContainer... params) {
        StorageReference mStorageRef = params[0].storage;
        File photoPath = params[0].FilePath;
        Context context = params[0].contex;
        Picture pic = params[0].pic;

        DatabaseController firestore = new DatabaseController();

        //Update the photo download path with the return of the upload function
        pic.setFilePath(uploadPictureToStorage(mStorageRef, photoPath, context));

        firestore.uploadPictureToFirestore(FIRESTOREDB, pic);

        return "Success";
    }

    //Returns the download URI of the uploaded image
    public String uploadPictureToStorage(StorageReference mStorageRef, File photoFile, final Context mContext){
        //We saved our image from when we took it with the camera
        int errorCode = 0;

        final CountDownLatch signal = new CountDownLatch(1);

        if(photoFile != null){
            //Create a new raw bytes output stream, convert the bitmap to raw bytes,
            // then convert to an array that can be written too firebase
            Bitmap mImage = BitmapFactory.decodeFile(photoFile.toString());
            ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
            mImage.compress(Bitmap.CompressFormat.JPEG, 60, output_stream);
            byte[] image_as_byte_array = output_stream.toByteArray();

            String photoId = generatePhotoString();

            final StorageReference firebase_reference = mStorageRef.child(photoId);

            //Start the upload to Firebase
            final UploadTask uploadTask = firebase_reference.putBytes(image_as_byte_array);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get URL to the uploaded content
                    //Uri downloadUri = taskSnapshot.getUploadSessionUri();

                    Toast.makeText(mContext,"Successful Upload",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //TODO: Handle unsuccessful uploads
                    Toast.makeText(mContext,"FAILED Upload" + e,Toast.LENGTH_SHORT).show();
                }
            });
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return firebase_reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();
                        signal.countDown();
                    }
                }
            });
        } else {
            errorCode = 1;
        }

        try {
            signal.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return downloadUri.toString();
    }

    public String generatePhotoString(){
        int leftLimit = 98; // letter 'a'
        int rightLimit = 121; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }
}
