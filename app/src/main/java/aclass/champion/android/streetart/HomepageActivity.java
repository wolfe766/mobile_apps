package aclass.champion.android.streetart;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class HomepageActivity extends AppCompatActivity {
    public static final String TAG = "Homepage Activity";
    private String[] imageUrls = new String[]{
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/biesnsthye?alt=media&token=04498522-dac9-4f71-b3bf-0ff0aef15244.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/psnzbmvmud?alt=media&token=7b6650a6-c287-4ac1-b7b7-c01bfc4ae82e.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/hlnrduoqbg?alt=media&token=f8b4ff33-2b0a-4625-b5f7-58f01d6fa300.png"
    };
    private final FirebaseFirestore FIRESTOREDB = FirebaseFirestore.getInstance();
    final User userTestObject = new User("gzdzpb2smITIcqUAmRuy");
    DatabaseController dbController;
    StorageController stController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //userTestObject.setImageUrls();
        //String[] x = userTestObject.getImageIds();
        //Log.d(TAG, userTestObject.getImageIds().);

        //imageUrls = dbController.getUserImageURLS(FIRESTOREDB, userTestObject.getImageIds());
        
        ViewPager viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageUrls);
        viewPager.setAdapter(adapter);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked " + imageUrls[0]);
            }
        });

        Button mPicButton, mSearchButton, mMapButton, mSettingsButton;
        dbController = new DatabaseController();
        stController = new StorageController();

        dbController.getUserData(FIRESTOREDB, userTestObject);


        mPicButton = findViewById(R.id.take_pic_button);
        mSearchButton = findViewById(R.id.search_button);
        mMapButton = findViewById(R.id.map_button);
        mSettingsButton = findViewById(R.id.settings_button);


        mPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, CapturePhotoActivity.class);
                startActivity(intent);
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, SettingsActivity.class);
                startActivity(intent);
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
}
