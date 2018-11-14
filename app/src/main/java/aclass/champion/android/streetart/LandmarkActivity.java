package aclass.champion.android.streetart;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class LandmarkActivity extends AppCompatActivity {

    public static final String TAG = "Landmark Activity";
    private String[] imageUrls = new String[]{
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/biesnsthye?alt=media&token=04498522-dac9-4f71-b3bf-0ff0aef15244.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/psnzbmvmud?alt=media&token=7b6650a6-c287-4ac1-b7b7-c01bfc4ae82e.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/hlnrduoqbg?alt=media&token=f8b4ff33-2b0a-4625-b5f7-58f01d6fa300.png"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark);

        String landmarkName;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                landmarkName= null;
            } else {
                landmarkName= extras.getString("landmarkName");
                setTitle("StreetArt - " + landmarkName);

            }
        } else {
            landmarkName= (String) savedInstanceState.getSerializable("landmarkName");
        }
        //Toast.makeText(this, "Opened landmark: " + landmarkName, Toast.LENGTH_SHORT).show();

        //TODO get imageUrls[] based on landmarkName
        ViewPager viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageUrls);
        viewPager.setAdapter(adapter);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked ");
            }
        });
    }
}
