package aclass.champion.android.streetart;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;

public class CaptureFragment extends Fragment {

    private EditText mTitleField;
    private EditText mArtistField;
    private EditText mLandmarkField;
    private EditText mLocationField;
    private EditText mDateField;
    private EditText mDescriptionField;
    private ImageView mImageView;
    private Button mUploadButton;

    private final FirebaseFirestore FIRESTOREDB = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef;
    StorageController storage = new StorageController();
    DatabaseController firestore = new DatabaseController();


    private Picture mPic;
    private String mDownloadPath;
    private String mThumbnailString;
    private Location currentLocation;
    private File photoFile;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_capture, container, false);

        //Get all the relevant resources set up
        mTitleField = v.findViewById(R.id.photo_title);
        mArtistField = v.findViewById(R.id.photo_artist);
        mLandmarkField = v.findViewById(R.id.photo_landmark);
        mLocationField = v.findViewById(R.id.photo_location);
        mDateField = v.findViewById(R.id.photo_date);
        mDescriptionField = v.findViewById(R.id.photo_description);

        //Imageview for displaying a preview of what is about to be uploaded
        mImageView = v.findViewById(R.id.image_container);

        //Upload button
        mUploadButton = v.findViewById(R.id.button_upload);

        //Retrieve the data passed from the activity to this fragment
        Bundle dataFromActivity = this.getArguments();
        //The "path" key retrieves the file path, ,the "thumbnail" key retrieves the thumbnail string
        String filePath = dataFromActivity.getString("path");
        mThumbnailString = dataFromActivity.getString("thumbnail");

        //Use the passed string to create a new reference to the file on disk
        photoFile = new File(filePath);

        //Update the preview imageview
        Glide.with(this).load(photoFile).into(mImageView);


        //Update the Date text area with the current date
        mDateField.setText("Date: " + new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date()));

        //Update the location area with the current location
        Context mContext = getActivity();
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //No accuracy criteria on the location service
        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.NO_REQUIREMENT);
        //Retrieve the best location provider using that crtieria
        String bestProvider = lm.getBestProvider(locationCriteria, true);

        //Try to get the last location if location services are enabled
        try{
            currentLocation = lm.getLastKnownLocation(bestProvider);
        }catch(SecurityException e){
            Toast.makeText(mContext, "Location service rejected", Toast.LENGTH_SHORT).show();
        }

        //Update location field with relevant data
        if(currentLocation != null){
            String lat = String.valueOf(currentLocation.getLatitude());
            String longi = String.valueOf(currentLocation.getLongitude());
            mLocationField.setText("lat: + " + lat + ", long: " + longi);
        }else{
            mLocationField.setText("Unknown Location");
        }

        //Create realtime database reference
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create A StorageDataContainer to pass to the async task
                mPic = generatePictureFromData();
                StorageDataContainer cont = new StorageDataContainer(mStorageRef, photoFile, getActivity(), mPic);

                //Create the asynchronous storage uploader object
                new StorageController().execute(cont);
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    //Gets all the relevant data from fields
    public Picture generatePictureFromData(){
        Picture mPicTemp = new Picture();

        mPicTemp.setTitle(mTitleField.getText().toString());
        mPicTemp.setArtist(mArtistField.getText().toString());
        mPicTemp.setDate(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()));
        mPicTemp.setLocation(currentLocation);
        mPicTemp.setDescription(mDescriptionField.getText().toString());
        mPicTemp.setLandmark(mLandmarkField.getText().toString());
        mPicTemp.setThumbnail(mThumbnailString);

        return mPicTemp;
    }

}
