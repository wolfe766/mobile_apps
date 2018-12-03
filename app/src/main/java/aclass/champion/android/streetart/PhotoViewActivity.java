package aclass.champion.android.streetart;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PhotoViewActivity extends Activity {

    private ImageView mPhotoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        mPhotoView = findViewById(R.id.photoViews);
        Bundle b = getIntent().getExtras();
        String photoUrl = b.getString("path");
        Uri myUri = Uri.parse(photoUrl);
        Glide.with(this).load(myUri).into(mPhotoView);

    }

}