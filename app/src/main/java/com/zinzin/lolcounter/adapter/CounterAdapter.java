package com.zinzin.lolcounter.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zinzin.lolcounter.DetailActivity;
import com.zinzin.lolcounter.R;
import com.zinzin.lolcounter.model.ItemHero;

import java.util.ArrayList;
import java.util.List;

public class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.ViewHolder> {

    private Activity activity;
    private List<ItemHero> infoList = new ArrayList<>();

    public CounterAdapter(Activity activity, List<ItemHero> infoList) {
        this.activity = activity;
        this.infoList.addAll(infoList);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_counter, viewGroup, false);
        return new CounterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(infoList.get(position), position);
        ItemHero item = infoList.get(position);
        viewHolder.ivHero.setPadding(-20,-20,-20,-20);
        Glide.with(activity).load(item.getUrl()).into(viewHolder.ivHero);
        viewHolder.tvName.setText(item.getName());
        ItemAdapter itemAdapter = new ItemAdapter(activity, item.getItem(), 0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        viewHolder.rvItem.setLayoutManager(layoutManager);
        viewHolder.rvItem.setAdapter(itemAdapter);
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHero;
        TextView tvName;
        RecyclerView rvItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ivHero = itemView.findViewById(R.id.iv_hero);
            tvName = itemView.findViewById(R.id.tv_name);
            rvItem = itemView.findViewById(R.id.rcv_item);
        }

        void bind(final ItemHero item, final int position){}
    }
}
