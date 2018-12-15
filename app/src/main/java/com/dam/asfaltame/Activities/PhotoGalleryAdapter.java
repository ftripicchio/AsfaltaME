package com.dam.asfaltame.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dam.asfaltame.R;

import java.util.List;

public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<String> pathList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView riv;

        public MyViewHolder(View view) {
            super(view);

            riv = view.findViewById(R.id.gallery_item_image);

        }
    }


    public PhotoGalleryAdapter(Context context, List<String> pathList) {
        this.context = context;
        this.pathList = pathList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);

        if (itemView.getLayoutParams ().width == RecyclerView.LayoutParams.MATCH_PARENT)
            itemView.getLayoutParams ().width = RecyclerView.LayoutParams.WRAP_CONTENT;

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String path  = pathList.get(position);
        Glide
                .with(context)
                .load(path)
                .into(holder.riv);

        holder.riv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImageViewActivity.class);
                i.putExtra("path", path);
                context.startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return pathList.size();
    }
}
