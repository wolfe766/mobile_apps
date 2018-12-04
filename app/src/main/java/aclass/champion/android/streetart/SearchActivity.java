package aclass.champion.android.streetart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button mSearchButton;
    private LinearLayout mSearchBox;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mDocRef = db.getInstance().collection("pictures");
    public static final String TAG = "Search Activity";
    private List<DocumentSnapshot> docs;
    private List<Picture> recyclerInput = new ArrayList<>();
    private String defaultBitmapString = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDABALDA4MChAODQ4SERATGCgaGBYWGDEjJR0oOjM9PDkzODdASFxOQERXRTc4UG1RV19iZ2hnPk1xeXBkeFxlZ2P/2wBDARESEhgVGC8aGi9jQjhCY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2P/wAARCAAcADMDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDBW3KE7kOCPTtVhLSSKUmJm3ocgDIYYyT9MYrTVknGJPMW5+YMqgYbIOWx/Md+MUsiKMIxJOAF6BQN3qe3+0Pf1pJ9AaKsbbzvf5484LMvc4zkD2z+WeoqGRPJYocPDn5dxztBxzxzjn2/A1evYzDPlBtfADYXaG+oB4OfT2IpsKGZWIYEscbTglj6HHPPA9yfWquSU2jjki2kBy3ViPU/T26f/XFZMqiIso+ZSOM9R+Fa0z/ZSWiKsh/5Zschec/iOP8AJArJl+ZiSSSaGUiwv3B9KKWMfu178UV573PWi9EdQ9p56gx/Jcxcg9NwH9f8/WNNk6bSfImj5dcAqwA6gHvwOO/Wr8SCTT2lb70RAX6GoNSAe0iu8BZgSu5eM4wcn35rtceqPIjK+hTgVsspdCiAbgzHafQ8c9SPoTTr8MsrtGDHKNyyktw3J4btnjoOD2pJLp8JMoVZGHLLkZI7/U96ga5YwOuxNuSQMdMgj9M8Z6fnQndA1ZmVNIWYh8dMcDFVZFwevFWJmLMSTkk5JqND97gcDIq0GwqgIoUk5HpRUeaKh04s2VWSW5//2Q==";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchButton = findViewById(R.id.search_button);
        mSearchBox = findViewById(R.id.search_box);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchBox.setVisibility(View.GONE);
                retrievePicturesFromFirebase();
            }
        });


    }

    private void retrievePicturesFromFirebase(){
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //Get all the pictures
                //TODO: Don't get all the pictures
                docs = queryDocumentSnapshots.getDocuments();
                //Call function to turn this document into picture object list
                documentToPictures();
                createRecyclerView();
            }
        });
    }

    private void documentToPictures(){
        DocumentSnapshot curDoc;
        for(int i = 0; i < docs.size(); i++){
            Picture curPic = new Picture();

            curDoc = docs.get(i);
            curPic.setTitle(curDoc.getString("title"));
            curPic.setLandmark(curDoc.getString("landmark"));
            curPic.setArtist(curDoc.getString("artist"));
            curPic.setFilePath(curDoc.getString("path"));
            //Location causing erros
            //TODO: Fix this
            //curPic.setLocationString(curDoc.getString("location").toString());
            curPic.setDate(curDoc.getString("date"));
            curPic.setDescription("description");

            String thumb;
            if(curDoc.contains("thumbnail")){
                thumb = curDoc.getString("thumbnail");
            }else{
                thumb = defaultBitmapString;
            }

            //TODO: Dont encode images here
            curPic.setThumbnail(thumb);
            curPic.setThumbnailBitmap(encodeFromString(thumb));

            curPic.setId(curDoc.getId());

            recyclerInput.add(curPic);

            Log.d(TAG, curDoc.getString("title"));
            Log.d(TAG, curDoc.getString("artist"));
            Log.d(TAG, curDoc.getString("date"));
        }
    }

    private void createRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(SearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ListAdapter(recyclerInput, SearchActivity.this);
        recyclerView.setAdapter(mAdapter);
    }

    private Bitmap encodeFromString(String input){
        byte[] decodedString = Base64.decode(input, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
