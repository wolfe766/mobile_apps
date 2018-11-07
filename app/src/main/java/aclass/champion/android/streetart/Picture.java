package aclass.champion.android.streetart;

import android.location.Location;

import java.util.Date;

public class Picture {
    private String mId;
    private String mFilePath;
    private String mTitle;
    private String mArtist;
    private String mDate;
    private Location mLocation;
    private String mDescription;
    private String mLandmark;
    private String[] mTagList;

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    private String mThumbnail;

    public Picture(){}

    public Picture(String id) {
        this.mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getLandmark() {
        return mLandmark;
    }

    public void setLandmark(String landmark) {
        mLandmark = landmark;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String[] getTagList() {
        return mTagList;
    }

    public void setTagList(String[] tagList) {
        mTagList = tagList;
    }
}

