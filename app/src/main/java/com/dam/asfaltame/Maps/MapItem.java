package com.dam.asfaltame.Maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapItem implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private final int mIconId;

    public MapItem(double lat, double lng, int iconId) {
        mPosition = new LatLng(lat, lng);
        mTitle = null;
        mSnippet = null;
        mIconId = iconId;
    }

    public MapItem(double lat, double lng, String title, String snippet, int iconId) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        mIconId = iconId;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public int getmIconId() {
        return mIconId;
    }
}
