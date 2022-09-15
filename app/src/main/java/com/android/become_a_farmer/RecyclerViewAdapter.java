package com.android.become_a_farmer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    ArrayList<RecyclerItem> list;
    private Context mContext;

    public RecyclerViewAdapter() {
        list = new ArrayList<>();
    }

    public RecyclerViewAdapter(Context mContext) {
        list = new ArrayList<>();
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_recycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        RecyclerItem item = list.get(position);
        holder.txt_title_main.setText(item.getTitle().split(" ")[0]);
        holder.txt_title_sub.setText(item.getTitle().split(" ")[1]);
        holder.txt_sub.setText(item.getSub());

        //지역별로 라벨 색을 변경하는 부분
        String regionName = item.getTitle().split(" ")[0];
        int color = R.color.lightGreen;
        switch(regionName){
            case "경기도":
                color = R.color.lightGreen;
                break;
            case "강원도":
                color = R.color.lightGreen;
                break;
            case "충청북도":
                color = R.color.lightYellow;
                break;
            case "충청남도":
                color = R.color.lightYellow;
                break;
            case "전라북도":
                color = R.color.red;
                break;
            case "전라남도":
                color = R.color.red;
                break;
            case "경상북도":
                color = R.color.blue;
                break;
            case "경상남도":
                color = R.color.blue;
                break;
            case "제주도":
                color = R.color.selectedMapColor;
                break;
        }
        //layer-list에서 id로 label을 찾고 색깔을 변경한다.
        LayerDrawable layerDrawable = (LayerDrawable) holder.itemView.getBackground(); //view.getBackground()
        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable
                .findDrawableByLayerId(R.id.item_label);
        gradientDrawable.setColor(ContextCompat.getColor(mContext, color));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(String title, String sub, String introduction, String crop,
                        String experienceTitle, String experienceContent){
        RecyclerItem item = new RecyclerItem(title, sub, introduction, crop, experienceTitle,
                experienceContent);
        list.add(item);
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_title_main;
        public TextView txt_title_sub;
        public TextView txt_sub;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_title_main = itemView.findViewById(R.id.rcy_item_title_main);
            txt_title_sub = itemView.findViewById(R.id.rcy_item_title_sub);
            txt_sub = itemView.findViewById(R.id.rcy_item_sub);

            // 아이템 클릭 이벤트(title 클릭)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos == RecyclerView.NO_POSITION){
                        return;
                    }
                    RecyclerItem item = list.get(pos);
                    // item 객체 보내기
                    Intent intent = new Intent(v.getContext(), RegionInfo.class);
                    intent.putExtra("item", item);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
