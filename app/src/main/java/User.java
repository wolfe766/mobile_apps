import android.graphics.Bitmap;
import android.location.Location;

public class User {
    private String mId;
    private Location mUserLocation;
    private String mDisplayName;
    private String[] mImageIds;
    private Bitmap mAvatar;

    public String getId() {
        return mId;
    }

    public void setId(String Id) {
        mId = Id;
    }

    public Location getUserLocation() {
        return mUserLocation;
    }

    public void setUserLocation(Location userLocation) {
        mUserLocation = userLocation;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    public String[] getImageIds() {
        return mImageIds;
    }

    public void setImageIds(String[] imageIDs) {
        mImageIds = imageIDs;
    }

    public Bitmap getAvatar() {
        return mAvatar;
    }

    public void setAvatar(Bitmap avatar) {
        mAvatar = avatar;
    }
}
