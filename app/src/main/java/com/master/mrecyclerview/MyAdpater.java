package com.master.mrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pankaj Sharma on 17/7/17.
 */

public class MyAdpater extends RecyclerView.Adapter<MyAdpater.ViewHolder> {

    ArrayList<String> list;

    public MyAdpater(int size) {
        list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.list.add("" + i);
        }
    }

    public void addItem(int size) {
        for (int i = 0; i < size; i++) {
            list.add(i + "");
        }
        notifyDataSetChanged();
    }

    public void addItemOnTop(int size) {
        for (int i = 0; i < size; i++) {
            list.add(0, i + "");
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(list.get(position) + "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
