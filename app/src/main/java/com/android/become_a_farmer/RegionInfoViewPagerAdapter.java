package com.android.become_a_farmer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RegionInfoViewPagerAdapter extends RecyclerView.Adapter<RegionInfoViewPagerAdapter.RegionInfoViewHoler> {
    private RecyclerItem item;
    public RegionInfoViewPagerAdapter(RecyclerItem item) {
        super();
        this.item = item;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindViewHolder(@NonNull RegionInfoViewHoler holder, int position) {
        String title = "";
        String description = "";
        switch(position){
            case 0:
                title = "지역 소개";
                description = item.getIntroduction();
                break;
            case 1:
                title = "특산물";
                description = item.getCrop();
                break;
            case 2:
                title = item.getExperienceTitle();
                description = item.getExperienceContent();
                break;
        }
        holder.title.setText(title);
        holder.description.setText(description);
    }

    @NonNull
    @Override
    public RegionInfoViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_region_detail, parent, false);
        RegionInfoViewHoler vh = new RegionInfoViewHoler(view);
        return vh;
    }

    class RegionInfoViewHoler extends RecyclerView.ViewHolder{
        TextView title;
        TextView description;
        public RegionInfoViewHoler(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.fragment_region_detail_title);
            description = itemView.findViewById(R.id.fragment_region_detail_description);
        }
    }
}

