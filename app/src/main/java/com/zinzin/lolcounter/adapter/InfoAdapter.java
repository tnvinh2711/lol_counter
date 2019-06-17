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
import com.bumptech.glide.request.RequestOptions;
import com.zinzin.lolcounter.R;
import com.zinzin.lolcounter.model.ItemHero;

import java.util.ArrayList;
import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private Activity activity;
    private List<String> infoList = new ArrayList<>();
    private int color;

    public InfoAdapter(Activity activity, List<String> infoList, int color) {
        this.activity = activity;
        this.infoList = infoList;
        this.color = color;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_class, viewGroup, false);
        return new InfoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(infoList.get(position), position);
        String info = infoList.get(position);
        viewHolder.tvClass.setText(info);
        viewHolder.tvClass.setBackgroundColor(activity.getResources().getColor(color));
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvClass;

        public ViewHolder(View itemView) {
            super(itemView);
            tvClass = itemView.findViewById(R.id.tv_class);
        }

        void bind(final String item, final int position){}
    }
}
