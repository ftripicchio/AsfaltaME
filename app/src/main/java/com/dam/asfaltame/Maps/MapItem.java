package com.dam.asfaltame.Maps;

import com.dam.asfaltame.Model.Report;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapItem implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private final int mIconId;
    private final Report report;

    public MapItem(Report r, int iconId) {
        mPosition = new LatLng(r.getLatitud(), r.getLongitud());
        mTitle = null;
        mSnippet = null;
        mIconId = iconId;
        report = r;
    }

    public MapItem(double lat, double lng, String title, String snippet, int iconId, Report r) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        mIconId = iconId;
        report = r;
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

    public Report getReport() {
        return report;
    }
}
