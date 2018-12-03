package aclass.champion.android.streetart;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        LatLng SmithLab = new LatLng(40.0027619, -83.013183);
        LatLng ConvCenter = new LatLng(39.9712352, -82.9981496);
        LatLng HighBrickel = new LatLng(39.976210, -83.003185);
        LatLng LincolnHigh = new LatLng(39.976932, -83.003242);


        mMap.addMarker(new MarkerOptions().position(SmithLab).title("Marker at Smith Lab")).setTag("SmithLab");
        mMap.addMarker(new MarkerOptions().position(ConvCenter).title("Marker at Convention Center")).setTag("ConventionCenter");
        mMap.addMarker(new MarkerOptions().position(HighBrickel).title("Marker at High & Brickel")).setTag("HighBrickel");
        mMap.addMarker(new MarkerOptions().position(LincolnHigh).title("Marker at Lincoln & High")).setTag("LincolnHigh");

        mMap.moveCamera(CameraUpdateFactory.newLatLng(SmithLab));
        mMap.setMinZoomPreference(13.0f);
        CameraUpdateFactory.zoomTo(1.0f);

        mMap.setOnMarkerClickListener(this);

    }
    @Override
    public boolean onMarkerClick(Marker marker) {

        // Retrieve the data from the marker.
        String landmarkName = marker.getTag().toString();
        Toast.makeText(this, "Opening landmark: " + landmarkName, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MapActivity.this, LandmarkActivity.class).putExtra("landmarkName", landmarkName);
        startActivity(intent);

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

}
