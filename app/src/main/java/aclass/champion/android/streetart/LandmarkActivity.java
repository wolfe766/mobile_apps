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
    private String[] highBrickel = new String[]{
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/Columbus-Murals-010.jpg?alt=media&token=3ead111e-bbf7-4e5a-9771-34065daabfaf.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/Columbus-Murals-011.jpg?alt=media&token=acbc87f9-22c5-41a6-b265-bfa2e8f9cb32.png"
    };
    private String[] lincolnHigh = new String[]{
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/Columbus-Murals-005.jpg?alt=media&token=76fe6d16-c230-4535-9dad-9de6d51c63ef.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/Short_North_Columbus_Ohio_CAPAAB0216_2x3_72dpi.jpg?alt=media&token=dfc5bd16-141c-42e8-90f0-ac839dd90bb0.png"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark);

        String landmarkName;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                landmarkName = null;
            } else {
                landmarkName = extras.getString("landmarkName");
                setTitle("StreetArt - " + landmarkName);
                //TODO get imageUrls[] based on landmarkName
                ViewPager viewPager = findViewById(R.id.view_pager);
                ViewPagerAdapter adapter;
                if (landmarkName.equals("HighBrickel")) {
                    adapter = new ViewPagerAdapter(this, highBrickel);
                }else if(landmarkName.equals("LincolnHigh")){
                    adapter = new ViewPagerAdapter(this, lincolnHigh);
                }else{
                    adapter = new ViewPagerAdapter(this, imageUrls);
                }
                viewPager.setAdapter(adapter);
                viewPager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "Clicked ");
                    }
                });

            }
        } else {
            landmarkName= (String) savedInstanceState.getSerializable("landmarkName");
        }
        //Toast.makeText(this, "Opened landmark: " + landmarkName, Toast.LENGTH_SHORT).show();

        //TODO get imageUrls[] based on landmarkName
//        ViewPager viewPager = findViewById(R.id.view_pager);
//        ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageUrls);
//        viewPager.setAdapter(adapter);
//        viewPager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "Clicked ");
//            }
//        });
    }
}
