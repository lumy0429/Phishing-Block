package com.example.phishingblock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    public static ArrayList<MainRecordItem> main_record_list = new ArrayList<>();
    public interface OnItemClickListener {
        void onItemClick(MainRecordItem item);

    }
    private final OnItemClickListener listener;
    public MainAdapter(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_main, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        holder.onBind(main_record_list.get(position));
    }

    public void setList(ArrayList<MainRecordItem> list){
        this.main_record_list = list;
        notifyDataSetChanged();
    }
    public void updateList(MainRecordItem item){
        if(this.main_record_list == null){
            this.main_record_list = new ArrayList<MainRecordItem>();
        }
        this.main_record_list.add(item);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return main_record_list.size();
    }
    public void setListener(OnItemClickListener listener){

    }
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView numview;
        TextView timeview;
        TextView wordview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            numview = itemView.findViewById(R.id.textView9);
            timeview = itemView.findViewById(R.id.textView10);
            wordview = itemView.findViewById(R.id.textView13);
        }

        void onBind(MainRecordItem item){

            numview.setText(item.num);
            timeview.setText(item.time);
            wordview.setText(item.word);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onItemClick(item);
                }
            });

        }
    }

}
