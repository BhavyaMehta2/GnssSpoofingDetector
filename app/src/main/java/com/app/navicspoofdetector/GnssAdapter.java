package com.app.navicspoofdetector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GnssAdapter extends RecyclerView.Adapter<GnssAdapter.ViewHolder> {
    private final List<GnssSat> list;

    public GnssAdapter(List<GnssSat> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.table_row, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GnssSat myListData = list.get(position);
        holder.cons.setText(myListData.getConstellation());
        holder.id.setText(String.valueOf(myListData.getId()));
        holder.mean.setText(String.valueOf(Math.round(myListData.getMean()*100.00)/100.00));
        holder.cn0.setText(myListData.getCn0() == 0 ? "-" : String.valueOf(myListData.getCn0()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<GnssSat> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cons, id, mean, cn0;

        public ViewHolder(View itemView) {
            super(itemView);
            cons = itemView.findViewById(R.id.gnssCon);
            id = itemView.findViewById(R.id.gnssID);
            mean = itemView.findViewById(R.id.gnssMean);
            cn0 = itemView.findViewById(R.id.gnssCN0);
        }
    }
}