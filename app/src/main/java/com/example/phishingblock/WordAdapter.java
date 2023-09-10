package com.example.phishingblock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;

public class WordAdapter extends RecyclerView.Adapter<com.example.phishingblock.WordAdapter.ViewHolder>{
        public static ArrayList<WordItem> word_list = new ArrayList<>();
        public interface OnItemClickListener {
            void onItemClick(WordItem item);

        }

        @NonNull
        @Override
        public com.example.phishingblock.WordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, parent, false);

            return new com.example.phishingblock.WordAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull com.example.phishingblock.WordAdapter.ViewHolder holder, int position) {
            holder.onBind(word_list.get(position));
        }

    //중복된 단어 제거
    public void setList(ArrayList<String> list){
        HashSet<String> set = new HashSet<>();
        set.clear();
        for(int i = 0 ; i < list.size() ; i++){
            set.add(list.get(i));
        }
        ArrayList<String> list2 = new ArrayList<>(set);


        this.word_list.clear();
        for(String c: list2){
            this.word_list.add(new WordItem(c));

        }
        notifyDataSetChanged();
    }
        public void updateList(WordItem item){
            if(this.word_list == null){
                this.word_list = new ArrayList<WordItem>();
            }
            this.word_list.add(item);
            notifyDataSetChanged();
        }
        @Override
        public int getItemCount() {
            return word_list.size();
        }
        public void setListener(com.example.phishingblock.WordAdapter.OnItemClickListener listener){

        }
        class ViewHolder extends RecyclerView.ViewHolder {

            TextView wordsview;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);



                wordsview = itemView.findViewById(R.id.textView3);
            }

            void onBind(WordItem item){
                wordsview.setText(item.words);
        }

    }

}
