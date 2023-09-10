package com.example.phishingblock;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.ViewHolder> {
    private ArrayList<FamilyItem> item_list_name;

    @NonNull
    @Override
    public FamilyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyAdapter.ViewHolder holder, int position) {
        holder.onBind(item_list_name.get(position));
    }

    public void setList(ArrayList<FamilyItem> list){
        this.item_list_name = list;
        notifyDataSetChanged();
    }
    public void updateList(FamilyItem item){
        if(this.item_list_name == null){
            this.item_list_name = new ArrayList<FamilyItem>();
        }
        this.item_list_name.add(item);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return item_list_name.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameview;
        TextView numberview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            nameview = itemView.findViewById(R.id.textView39);
            numberview = itemView.findViewById(R.id.textView41);
        }

        void onBind(FamilyItem item){

            nameview.setText(item.name);
            numberview.setText(item.number);

        }
    }
}

