package com.zinzin.lolcounter.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zinzin.lolcounter.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Activity activity;
    private List<String> infoList = new ArrayList<>();
    private int color;

    public ItemAdapter(Activity activity, List<String> infoList, int color) {
        this.activity = activity;
        this.infoList.addAll(infoList);
        this.color = color;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_items, viewGroup, false);
        return new ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(infoList.get(position), position);
        String item = infoList.get(position);
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transform(new CenterCrop())
                .transform(new RoundedCorners(7));
        Glide.with(activity).load(item).apply(options).into(viewHolder.ivItem);
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ivItem = itemView.findViewById(R.id.iv_item);
        }

        void bind(final String item, final int position){}
    }
}
