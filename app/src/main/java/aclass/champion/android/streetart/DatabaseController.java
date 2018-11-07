package aclass.champion.android.streetart;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseController {
    public static final String TAG = "Main Activity";

    public void uploadPictureToFirestore(FirebaseFirestore db, Picture mPic) {
        //Create a new picture hash to upload to firestore
        Map<String, Object> picture = new HashMap<>();

        //Add all relevant data to the picture hash
        String loc = mPic.getLocation().getLatitude() + "," + mPic.getLocation().getLongitude();

        picture.put("title", mPic.getTitle());
        picture.put("location", loc); //defined above
        picture.put("landmark", mPic.getLandmark());
        picture.put("artist", mPic.getArtist());
        picture.put("description", mPic.getDescription());
        picture.put("path", mPic.getFilePath());
        picture.put("date", mPic.getDate());
        picture.put("thumbnail", mPic.getThumbnail());

        // Add a new document with a generated ID
        db.collection("pictures")
                .add(picture)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    //filter for landmarks or user
    public List getListOfPictureIDs(FirebaseFirestore db, String filter, String target){
        final List listOfRefs = new ArrayList();
        List listOfIDs= new ArrayList();

        //listOfIDs.add("test");
        db.collection("pictures")
                .whereEqualTo(filter, target)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listOfRefs.add(document.getId());
                                Log.d(TAG, "SUCCESS " +document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        for (int i = 0; i < listOfRefs.size(); i++) {
        }
        return listOfRefs;
    }


    //gets metadata for an image
    public void getPictureData(FirebaseFirestore db, final Picture pictureObject){
          db.collection("pictures").document(pictureObject.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if (task.isSuccessful()){
                     pictureObject.setArtist(task.getResult().get("artist").toString());
                     pictureObject.setDate(task.getResult().get("date").toString());
                     pictureObject.setDescription(task.getResult().get("description").toString());
                     pictureObject.setFilePath(task.getResult().get("path").toString());
                     pictureObject.setTitle(task.getResult().get("title").toString());
                     pictureObject.setLandmark(task.getResult().get("landmark").toString());
                 }
                 Log.d(TAG, "getting picture object fields complete");
             }
         });
    }

    //gets data tied to a user
    public void getUserData(final FirebaseFirestore db, final User userObject){
        db.collection("users").document(userObject.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    userObject.setDisplayName(task.getResult().get("userName").toString());
                    String[] ImageIDs = (String[]) task.getResult().get("imageIds");
                    userObject.setImageIds(ImageIDs);
                    //Log.d(TAG, "FUCK: "+ ImageIDs.toString());
                    //String[] ImageUrls = getUserImageURLS(db, ImageIDs);

                    //userObject.setImageUrls(ImageUrls);

                    Log.d(TAG, "getting user's picture ID list successful");
                }
            }
        });
    }


    public void getLandmarkData(){

    }

    public void updatePictureInFirestore(){

    }
    //get unique ref (ID, or path only) getDocRefByField("pictures", "path", "testPath")
    private DocumentReference getPictureDocRefByField(FirebaseFirestore db, String collectionName, String fieldName, String fieldValue){
        CollectionReference colRef = db.collection(collectionName);
        Query query = colRef.whereEqualTo(fieldName, fieldValue);
        DocumentReference docRef = query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        }).getResult().getDocuments().remove(0).getReference();

        return docRef;
    }
    public String[] getUserImageURLS(FirebaseFirestore db, String[] IdArray){
        String[] imageUrls = new String[IdArray.length];

        CollectionReference colRef = db.collection("pictures");

        for (int i = 0; i < imageUrls.length; i++){
            imageUrls[i] = getImageURLFromID(colRef, IdArray[i]);
        }

        return imageUrls;
    }
    private String getImageURLFromID(CollectionReference colRef, String id){
        String url = "";
        colRef.document(id).getPath();

        return url;
    }
}