package aclass.champion.android.streetart;

import android.location.Location;

public class Landmark {
    private String mId;
    private String mName;
    private Location mLocation;
    private String mRecentImageId;
    private String mFeaturedImageId;
    private String[] mHistoricImageIds;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public String getRecentImageId() {
        return mRecentImageId;
    }

    public void setRecentImageId(String recentImageId) {
        mRecentImageId = recentImageId;
    }

    public String getFeaturedImageId() {
        return mFeaturedImageId;
    }

    public void setFeaturedImageId(String featuredImageId) {
        mFeaturedImageId = featuredImageId;
    }

    public String[] getHistoricImageIds() {
        return mHistoricImageIds;
    }

    public void setHistoricImageIds(String[] historicImageIds) {
        mHistoricImageIds = historicImageIds;
    }
}