package com.example.qrcodegenerator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GerichtAdapter extends RecyclerView.Adapter<GerichtAdapter.GerichtViewHolder> {

    private List<Gericht> gerichtList;

    public GerichtAdapter(List<Gericht> gerichtList) {
        this.gerichtList = gerichtList;
    }

    @NonNull
    @Override
    public GerichtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gericht, parent, false);
        return new GerichtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GerichtViewHolder holder, int position) {
        Gericht gericht = gerichtList.get(position);
        holder.textViewGerichtName.setText(gericht.getGerichtName());
        holder.textViewGerichtPreis.setText(String.valueOf(gericht.getPreis()));


        /*StringBuilder allergienText = new StringBuilder();
        for (String allergie : gericht.getAllergien()) {
            allergienText.append(allergie).append(", ");
        }
        holder.textViewInfo.setText(allergienText.toString()); */

        StringBuilder zutatenText = new StringBuilder();
        for (String zutat : gericht.getZutaten()) {
            zutatenText.append(zutat).append(", ");
        }
        holder.textViewInfo.setText(zutatenText.toString());
    }

    @Override
    public int getItemCount() {
        return gerichtList.size();
    }

    static class GerichtViewHolder extends RecyclerView.ViewHolder {
        TextView textViewGerichtName;
        TextView textViewGerichtPreis;
        TextView textViewInfo;

        GerichtViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGerichtName = itemView.findViewById(R.id.textViewGerichtName);
            textViewGerichtPreis = itemView.findViewById(R.id.textViewGerichtPreis);
            textViewInfo = itemView.findViewById(R.id.textViewAdditionalInfo);
        }
    }
}
