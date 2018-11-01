package aclass.champion.android.streetart;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Random;

public class StorageController {
    public static final String TAG = "Main Activity";

    public int uploadPictureToStorage(final FirebaseFirestore db, final DatabaseController dbController, StorageReference mStorageRef, Bitmap mImageThumbnail) {
        //We saved our image from when we took it with the camera
        int errorCode = 0;
        if (mImageThumbnail != null) {

            //Create a new raw bytes output stream, convert the bitmap to raw bytes,
            // then convert to an array that can be written too firebase
            ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
            mImageThumbnail.compress(Bitmap.CompressFormat.PNG, 100, output_stream);
            byte[] image_as_byte_array = output_stream.toByteArray();

            //TODO: debug_image_one should be something unique for each image, and the
            //TODO: folder should correspond to a category or area
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();
            StringBuilder buffer = new StringBuilder(targetStringLength);
            for (int i = 0; i < targetStringLength; i++) {
                int randomLimitedInt = leftLimit + (int)
                        (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            String generatedString = buffer.toString();
            final StorageReference debug_reference = mStorageRef.child(generatedString);


            //Start the upload to Firebase
            final UploadTask uploadTask = debug_reference.putBytes(image_as_byte_array);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get URL to the uploaded content
                    Uri downloadUri = taskSnapshot.getUploadSessionUri();

                    //Toast.makeText(MainActivity.this,"Successful Upload",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //TODO: Handle unsuccessful uploads
                    //Toast.makeText(MainActivity.this,"FAILED Upload" + e,Toast.LENGTH_SHORT).show();
                }
            });
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return debug_reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        dbController.uploadPictureToFirestore(db, downloadUri.toString());
                    }
                }
            });
        } else {
            errorCode = 1;
        }
        return errorCode;
    }
}
