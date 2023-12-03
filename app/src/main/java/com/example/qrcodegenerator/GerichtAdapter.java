package com.example.qrcodegenerator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

        double preis = gericht.getPreis();
        String preisString = String.format("%.2f", preis);
        String formattedPreis = preisString.replace('.', ',') + "€";
        holder.textViewGerichtPreis.setText(formattedPreis);


        /*StringBuilder allergienText = new StringBuilder();
        for (String allergie : gericht.getAllergien()) {
            allergienText.append(allergie).append(", ");
        }
        holder.textViewInfo.setText(allergienText.toString()); */

        StringBuilder zutatenText = new StringBuilder();
        for (String zutat : gericht.getZutaten()) {
            zutatenText.append(zutat.substring(0, 1).toUpperCase() + zutat.substring(1)).append(", ");
        }
        zutatenText.deleteCharAt(zutatenText.length()-2);
        holder.textViewInfo.setText(zutatenText.toString());

        holder.gerichtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle für die Auswahl
                v.setSelected(!v.isSelected());
            }
        });


    }

    @Override
    public int getItemCount() {
        return gerichtList.size();
    }

    static class GerichtViewHolder extends RecyclerView.ViewHolder {
        TextView textViewGerichtName;
        TextView textViewGerichtPreis;
        TextView textViewInfo;
        LinearLayout gerichtLayout;

        GerichtViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGerichtName = itemView.findViewById(R.id.textViewGerichtName);
            textViewGerichtPreis = itemView.findViewById(R.id.textViewGerichtPreis);
            textViewInfo = itemView.findViewById(R.id.textViewAdditionalInfo);
            gerichtLayout = itemView.findViewById(R.id.gerichtLayout);
        }
    }
}
