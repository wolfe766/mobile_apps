//Firebase example app for next checkpoint

package aclass.champion.android.streetart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Main Activity";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView mImageView;
    private Bitmap mImageThumbnail;
    private File mLocalFile;

    private Button mCreateButton;
    private Button mReadButton;
    private Button mUpdateButton;
    private Button mDeleteButton;
    private Button mUpdatePictureName;
    private Button mHomepageButton;

    private StorageReference mStorageRef;

    private boolean mImageTaken = false;
    private final FirebaseFirestore FIRESTOREDB = FirebaseFirestore.getInstance();
    private String DOCREF = "empty";

    DatabaseController dbController;
    StorageController stController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate called");

        dbController = new DatabaseController();
        stController = new StorageController();

        final Picture teste = new Picture("mmxaOaynj6EnhtQZ1e6J");
        final User userTestObject = new User("gzdzpb2smITIcqUAmRuy");


        ImageView mImageView = findViewById(R.id.image_box);

        mCreateButton = (Button) findViewById(R.id.create_button);
        mReadButton = (Button) findViewById(R.id.read_button);
        mUpdateButton = (Button) findViewById(R.id.update_button);
        mDeleteButton = (Button) findViewById(R.id.delete_button);
        mUpdatePictureName = (Button) findViewById(R.id.change_name_button);
        mHomepageButton = (Button) findViewById(R.id.homepage_button);

        //prepare firebase storage
        mStorageRef = FirebaseStorage.getInstance().getReference();


        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Take a picture
                mImageTaken = true;
                dispatchTakePictureIntent();
            }
        });

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Upload taken image to firebase
                if(mImageTaken == true){
                    //Removed this functionality because parameters have changed and arent easy to get
                }else{
                    Toast.makeText(MainActivity.this,"You need to take an image to upload!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mReadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Download debug image from firebase
                mImageTaken = true;
                downloadFromFirebase();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete debug image on Firebase
//                Object[] x = dbController.getListOfPictureIDs(FIRESTOREDB, "landmark", "test").toArray();
//                Toast.makeText(MainActivity.this,"ID: "+ x.length,Toast.LENGTH_SHORT).show();

                dbController.getPictureData(FIRESTOREDB, teste);
                dbController.getUserData(FIRESTOREDB, userTestObject);
                Toast.makeText(MainActivity.this, "Artist: " + teste.getArtist(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Artist: " + teste.getArtist());
                Log.d(TAG, "IDs: " + userTestObject.getImageIds());


                //deleteFromFirebase();
            }
        });
        mUpdatePictureName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Change name on a picture
                updateFireStorePicture(FIRESTOREDB, DOCREF);
            }
        });
        mHomepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchHomeScreenIntent();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onPause called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    public void onDestroy() {
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

    private void dispatchHomeScreenIntent() {
        Intent homeScreenIntent = new Intent(this, HomepageActivity.class);
        startActivity(homeScreenIntent);
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
            try {
                Bundle extras = data.getExtras();
                mImageThumbnail = (Bitmap) extras.get("data");
                Glide.with(this).load(mImageThumbnail).into(mImageView);
            } catch (NullPointerException ex) {
                //we failed to set the image, maybe we're on a Galaxy device
                //Hitting a null pointer exception here usually means galaxy device
                galaxyDevice = true;
            }

            if (galaxyDevice) {
                //Compatibility for Galaxy devices where extras.get("data") will return null
                try {
                    mImageThumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                } catch (IOException ex) {
                    //If we still failed to get the image, then it was a problem with the camera
                    //TODO: Do something when we get an error with the camera
                }
                //No error, update the image
                Glide.with(this).load(mImageThumbnail).into(mImageView);
            }
        }
    }

    private void downloadFromFirebase() {

        //Prepare the ImageView object
        mImageView = (ImageView) findViewById(R.id.image_box);

        //Create a temp file to load the image into
        try {
            mLocalFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            //TODO: IOException
        }

        //Here is the image we're retrieving
        //TODO: Needs to retrieve a specific image, not this hard-coded one
        StorageReference debug_reference = mStorageRef.child("debug/debug_image_one");

        debug_reference.getFile(mLocalFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                //When the file is successfully downloaded, notify user and update the image box
                Toast.makeText(MainActivity.this, "Success Download", Toast.LENGTH_SHORT).show();
                getFireStorePicture(FIRESTOREDB, DOCREF);
                Glide.with(MainActivity.this).load(mLocalFile).into(mImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Download fail, notify user with the error
                Toast.makeText(MainActivity.this, "FAILED Download" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteFromFirebase() {
        //TODO: Needs to delete specific files, not just the debug file
        StorageReference debug_reference = mStorageRef.child("debug/debug_image_one");
        debug_reference.delete();
        Toast.makeText(MainActivity.this, "Deleted debug file on firebase", Toast.LENGTH_SHORT).show();
        deleteFireStorePicture(FIRESTOREDB, DOCREF);

    }

    private void getFireStorePicture(FirebaseFirestore db, final String ref) {
//        DocumentReference docRef = db.collection("pictures").document();
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        Toast.makeText(MainActivity.this,document.getId() + " => " + document.getData(),Toast.LENGTH_LONG).show();
//                    } else {
//                        Log.d(TAG, "No such document " + ref);
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });

        CollectionReference colRef = db.collection("pictures");
        Query query = colRef.whereEqualTo("date", "20181024_032724");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    if (query.size() > 0) {
                        DocumentSnapshot document = query.getDocuments().remove(0);
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            Toast.makeText(MainActivity.this, document.getId() + " => " + document.getData(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.d(TAG, "No such document " + ref);
                        }
                    } else {
                        Log.d(TAG, "No such document " + ref);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    private void deleteFireStorePicture(final FirebaseFirestore db, final String ref) {
        CollectionReference colRef = db.collection("pictures");
        Query query = colRef.whereEqualTo("date", "20181024_032724");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    if (query.size() > 0) {
                        DocumentSnapshot document = query.getDocuments().remove(0);
                        if (document.exists()) {
                            db.collection("pictures").document(document.getId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });
                        } else {
                            Log.d(TAG, "No such document " + ref);
                        }
                    } else {
                        Log.d(TAG, "No such document " + ref);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    private void updateFireStorePicture(final FirebaseFirestore db, final String ref) {
        CollectionReference colRef = db.collection("pictures");
        Query query = colRef.whereEqualTo("date", "20181030_104947");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    if (query.size() > 0) {
                        DocumentSnapshot document = query.getDocuments().remove(0);
                        if (document.exists()) {
                            db.collection("pictures").document(document.getId()).update("artist", "Changed Artist")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot name changed!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });
                        } else {
                            Log.d(TAG, "No such document " + ref);
                        }
                    } else {
                        Log.d(TAG, "No such document " + ref);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}