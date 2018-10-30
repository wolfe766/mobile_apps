package aclass.champion.android.streetart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomepageActivity extends AppCompatActivity {
    public static final String TAG = "Homepage Activity";

    private Button mPicButton;
    private Button mSearchButton;
    private Button mMapButton;
    private Button mSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

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
}
