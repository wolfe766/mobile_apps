//Firebase example app for next checkpoint

package aclass.champion.android.streetart;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Main Activity";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView mImageView;
    private Bitmap mImageThumbnail;
    private final long MAX_DOWNLOAD_SIZE_BYTES = 10000000; //10 megs
    private File mLocalFile;

    private Button mCreateButton;
    private Button mReadButton;
    private Button mUpdateButton;
    private Button mDeleteButton;

    private StorageReference mStorageRef;

    private boolean mImageTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate called");

        ImageView mImageView = (ImageView) findViewById(R.id.image_box);

        mCreateButton = (Button) findViewById(R.id.create_button);
        mReadButton = (Button) findViewById(R.id.read_button);
        mUpdateButton = (Button) findViewById(R.id.update_button);
        mDeleteButton = (Button) findViewById(R.id.delete_button);

        //prepare firebase
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mCreateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Take a picture
                mImageTaken = true;
                dispatchTakePictureIntent();
            }
        });

        mUpdateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Upload taken image to firebase
                if(mImageTaken == true){
                    uploadToFirebase();
                }else{
                    Toast.makeText(MainActivity.this,"You need to take an image to upload!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mReadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Download debug image from firebase
                mImageTaken = true;
                downloadFromFirebase();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Delete debug image on Firebase
                deleteFromFirebase();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume called");
    }
    @Override
    public void onPause(){
        super.onPause();

        Log.d(TAG, "onPause called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    private void dispatchTakePictureIntent() {
        //Initiate the camera process
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //When the "camera request" intent is completed, perform this action
        //TODO: NOTE THIS CURRENTLY ONLY RETRIEVES THE THUMBNAIL OF THE TAKEN IMAGE
        //TODO: NOT THE FULL SIZE IMAGE YET
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            boolean galaxyDevice = false;
            mImageThumbnail = null;

            ImageView mImageView = (ImageView) findViewById(R.id.image_box);

            //Try to set the thumbnail image
            try{
                Bundle extras = data.getExtras();
                mImageThumbnail = (Bitmap) extras.get("data");
                Glide.with(this).load(mImageThumbnail).into(mImageView);
            }catch(NullPointerException ex){
                //we failed to set the image, maybe we're on a Galaxy device
                //Hitting a null pointer exception here usually means galaxy device
                galaxyDevice = true;
            }

            if(galaxyDevice){
                //Compatibility for Galaxy devices where extras.get("data") will return null
                try{
                    mImageThumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                }catch(IOException ex){
                    //If we still failed to get the image, then it was a problem with the camera
                    //TODO: Do something when we get an error with the camera
                }
                //No error, update the image
                Glide.with(this).load(mImageThumbnail).into(mImageView);
            }
        }
    }

    private void uploadToFirebase(){
        //We saved our image from when we took it with the camera
        if(mImageThumbnail != null){

            //Create a new raw bytes output stream, convert the bitmap to raw bytes,
            // then convert to an array that can be written too firebase
            ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
            mImageThumbnail.compress(Bitmap.CompressFormat.PNG, 100, output_stream);
            byte[] image_as_byte_array = output_stream.toByteArray();

            //TODO: debug_image_one should be something unique for each image, and the
            //TODO: folder should correspond to a category or area
            StorageReference debug_reference = mStorageRef.child("debug/debug_image_one");

            //Start the upload to Firebase
            debug_reference.putBytes(image_as_byte_array).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get URL to the uploaded content
                    Uri downloadUri = taskSnapshot.getUploadSessionUri();
                    Toast.makeText(MainActivity.this,"Successful Upload",Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //TODO: Handle unsuccessful uploads
                    Toast.makeText(MainActivity.this,"FAILED Upload" + e,Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void downloadFromFirebase(){

        //Prepare the ImageView object
        mImageView = (ImageView) findViewById(R.id.image_box);

        //Create a temp file to load the image into
        try{
            mLocalFile = File.createTempFile("images","jpg");
        }catch(IOException e){
            //TODO: IOException
        }

        //Here is the image we're retrieving
        //TODO: Needs to retrieve a specific image, not this hard-coded one
        StorageReference debug_reference = mStorageRef.child("debug/debug_image_one");

        debug_reference.getFile(mLocalFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                //When the file is successfully downloaded, notify user and update the image box
                Toast.makeText(MainActivity.this,"Success Download",Toast.LENGTH_SHORT).show();
                Glide.with(MainActivity.this).load(mLocalFile).into(mImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               //Download fail, notify user with the error
                Toast.makeText(MainActivity.this,"FAILED Download" + e,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFromFirebase(){
        //TODO: Needs to delete specific files, not just the debug file
        StorageReference debug_reference = mStorageRef.child("debug/debug_image_one");
        debug_reference.delete();
        Toast.makeText(MainActivity.this,"Deleted debug file on firebase",Toast.LENGTH_SHORT).show();
    }



}
