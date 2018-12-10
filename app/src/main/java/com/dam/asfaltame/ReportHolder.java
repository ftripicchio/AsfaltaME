package com.dam.asfaltame;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ReportHolder {

    public ImageView reportIcon;
    public TextView reportAddress;

    ReportHolder(View base){
        reportIcon = base.findViewById(R.id.list_item_icon);
        reportAddress = base.findViewById(R.id.list_item_text);
    }
}
