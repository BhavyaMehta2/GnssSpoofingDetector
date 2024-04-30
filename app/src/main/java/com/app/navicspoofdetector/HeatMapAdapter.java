package com.app.navicspoofdetector;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class HeatMapAdapter extends RecyclerView.Adapter<HeatMapAdapter.RecyclerViewHolder> {

    private final double[][] coeffs;
    private final Context mContext;

    public HeatMapAdapter(double[][] coeffs, Context mContext) {
        this.coeffs = coeffs;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.heatmap_element, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        double val;

        val = coeffs[position / coeffs.length][position % coeffs.length];

        if (val == -1)
            holder.heatmapElement.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        else if (val == 1)
            holder.heatmapElement.setBackgroundColor(mContext.getResources().getColor(R.color.black));
        else if (val >= 0.90)
            holder.heatmapElement.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        else if (val >= 0.60)
            holder.heatmapElement.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));
        else
            holder.heatmapElement.setBackgroundColor(mContext.getResources().getColor(R.color.green));

        holder.heatmapElement.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", Arrays.deepToString(coeffs));
            clipboard.setPrimaryClip(clip);
        });
    }

    @Override
    public int getItemCount() {
        return coeffs.length * coeffs[0].length;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final ImageView heatmapElement;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            heatmapElement = itemView.findViewById(R.id.icon);
        }
    }
}
