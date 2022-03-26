package com.android.become_a_farmer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        holder.txt_title.setText(item.getTitle());
        holder.txt_sub.setText(item.getSub());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(String title, String sub, String introduction){
        RecyclerItem item = new RecyclerItem(title, sub, introduction);
        list.add(item);
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_title;
        public TextView txt_sub;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.rcy_item_title);
            txt_sub = itemView.findViewById(R.id.rcy_item_sub);

            // 아이템 클릭 이벤트(title 클릭)
//            txt_title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    if (pos != RecyclerView.NO_POSITION){
//                        RecyclerItem item = list.get(pos);
//                        // item 객체 보내기
//                        Intent intent = new Intent(v.getContext(), RegionInfo.class);
//                        intent.putExtra("item", item);
//                        mContext.startActivity(intent);
//                    }
//
//                }
//            });
        }
    }
}
