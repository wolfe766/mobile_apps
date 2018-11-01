package aclass.champion.android.streetart;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class CapturePhotoActivity extends AppCompatActivity {
    public static final String TAG = "Capture Photo Activity";

    private Button mCreateImageButton;
    private ImageView mImageView;
    private Context mContext;
    private File mPhotoFile;

    private static final int REQUEST_PHOTO= 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_photo);

        mCreateImageButton = (Button) findViewById(R.id.create_button);
        mImageView = (ImageView) findViewById(R.id.image_box);

        //First get the "context" of our application, aka the private data directory path
        mContext = this.getApplicationContext();

        //Create a new file corresponding to that location, then create a new file in that
        //directory with the generated filename
        //The camera intent will load the image into this location if we tell it to do so
        File filesDir = mContext.getFilesDir();
        mPhotoFile = new File(filesDir, getPhotoFilename());


        //We can now always retrieve the location of that file using the following code
        Log.d(TAG, mPhotoFile.getAbsolutePath());

        //Create a new intent with the special "ACTION_IMAGE_CAPTURE" code, meaning
        //it will start our default app designed to handle camera requests
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mCreateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a URI object (aka the path) to our file. The authority indicate the
                //directory that our app will use to store private content. It was specified by
                //me earlier in the AndroidManiest.xml file.
                Uri uri = FileProvider.getUriForFile(CapturePhotoActivity.this,
                        "com.test.file",
                        mPhotoFile);

                //Add the URI as an Extra to our CaptureImage intent. By providing this URI,
                //the camera app will automatically store the full sized photo in that location.
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                //Start the activity with the REQUEST_PHOTO code. That code is simply 2 (defined above)
                //It is how we know which intent we used below in the OnActivityResult overridden
                //callback function
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

    }

    //This function is overridden, it is automatically called whenever the startActivityForResult
    //intent is completed asynchronously
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the intent used code REQUEST_PHOTO (2) we know it was our camera request
        // that returned, so we handle it here
        if (requestCode == REQUEST_PHOTO) {
            // Check that the result was a success
            if (resultCode == RESULT_OK) {
                Glide.with(this).load(mPhotoFile).into(mImageView);
            }
        }
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
        Log.d(TAG, "onDestroy called")
        ;
    }

    public String getPhotoFilename() {
        String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        return "IMG_" + time + ".jpg";
    }

    public File getPhotoFile() {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, getPhotoFilename());
    }
}
