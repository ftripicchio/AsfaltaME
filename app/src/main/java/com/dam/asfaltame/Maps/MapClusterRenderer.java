package com.dam.asfaltame.Maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;

import com.dam.asfaltame.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class MapClusterRenderer extends DefaultClusterRenderer<MapItem>{

    private final Context mContext;

    public MapClusterRenderer(Context context, GoogleMap map, ClusterManager<MapItem> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    @Override protected void onBeforeClusterItemRendered(MapItem item, MarkerOptions markerOptions) {
        Log.d("iconId", ((Integer)item.getmIconId()).toString());
        markerOptions.icon(vectorToBitmap(item.getmIconId()));
    }

    private BitmapDescriptor vectorToBitmap( int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(mContext.getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
