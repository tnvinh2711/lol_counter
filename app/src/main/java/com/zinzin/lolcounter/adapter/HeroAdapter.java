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

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.ViewHolder> {

    private Activity activity;
    private List<ItemHero> unitsList = new ArrayList<>();
    private OnItemClickListener listener;

    public HeroAdapter(Activity activity, List<ItemHero> unitsList) {
        this.activity = activity;
        this.unitsList = unitsList;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hero_item, viewGroup, false);
        return new HeroAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(unitsList.get(position), position, listener);
        ItemHero units = unitsList.get(position);
        viewHolder.ivIconUnit.setPadding(-20,-20,-20,-20);
        viewHolder.tvNameUnit.setText(units.getName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(activity).load(units.getUrl_image()).apply(requestOptions).into(viewHolder.ivIconUnit);
    }

    @Override
    public int getItemCount() {
        return unitsList.size();
    }

    public void setList(ArrayList<ItemHero> result) {
        unitsList.clear();
        unitsList.addAll(result);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIconUnit;
        TextView tvNameUnit;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIconUnit = itemView.findViewById(R.id.iv_icon_unit);
            tvNameUnit = itemView.findViewById(R.id.tv_name);
        }

        void bind(final ItemHero item, final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.OnItemClick(item, position);
                }
            });
        }
    }


    public interface OnItemClickListener {
        void OnItemClick(ItemHero item, int position);
    }
}
