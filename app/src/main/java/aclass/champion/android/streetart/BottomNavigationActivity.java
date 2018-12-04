package aclass.champion.android.streetart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private List fragments = new ArrayList(4);
    private static final String TAG_FRAGMENT_HOME = "tag_frag_home";
    private static final String TAG_FRAGMENT_CAMERA = "tag_frag_camera";
    private static final String TAG_FRAGMENT_SEARCH = "tag_frag_search";
    private static final String TAG_FRAGMENT_MAP = "tag_frag_map";
    private FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            MapFragmentUno mapFrag = new MapFragmentUno();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_fragmentholder, new HomeFragment())
                            .commit();
//                    startActivity(new Intent(BottomNavigationActivity.this, HomepageActivity.class));
                    return true;
                case R.id.navigation_camera:
//                    switchFragment(1, TAG_FRAGMENT_CAMERA);
                    startActivity(new Intent(BottomNavigationActivity.this, CapturePhotoActivity.class));
                    return true;
                case R.id.navigation_search:
//                    switchFragment(2, TAG_FRAGMENT_SEARCH);
//                    startActivity(new Intent(BottomNavigationActivity.this, SearchActivity.class));
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_fragmentholder, new SearchFragment())
                            .commit();
                    return true;
                case R.id.navigation_map:
//                    switchFragment(3, TAG_FRAGMENT_MAP);
                    startActivity(new Intent(BottomNavigationActivity.this, MapActivity.class));

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_fragmentholder, new MapFragment())
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
//        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        mAuth = FirebaseAuth.getInstance();

        // Set the 0th Fragment to be displayed by default.
        //switchFragment(0, TAG_FRAGMENT_HOME);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragmentholder, new HomeFragment())
                .commit();
        //startActivity(new Intent(BottomNavigationActivity.this, HomepageActivity.class));

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void buildFragmentsList() {
//        Fragment homeFragment = new HomeFragment();
//        Fragment cameraFragment = new Fragment();
//        Fragment searchFragment = new Fragment();
//        Fragment mapFragment = new Fragment();
//
//        fragments.add(homeFragment);
//        fragments.add(cameraFragment);
//        fragments.add(searchFragment);
//        fragments.add(mapFragment);

    }

    private void switchFragment(int pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragmentholder, (android.support.v4.app.Fragment) fragments.get(pos), tag)
                .commit();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //TODO: updateUI(currentUser);
    }
    //Menu options within the app.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }
}
