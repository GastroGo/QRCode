package com.example.qrcodegenerator;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

                // Aktuelle Position des Gerichts
                int position = holder.getAdapterPosition();

                // Prüfe, ob die Position gültig ist
                if (position != RecyclerView.NO_POSITION) {
                    Gericht selectedGericht = gerichtList.get(position);

                    if (v.isSelected()) {
                        selectedGericht.setSelected(true);
                        holder.amount.setText("1");
                        gericht.setAmount(1);
                    } else if (!v.isSelected()) {
                        selectedGericht.setSelected(false);
                        holder.amount.setText("0");
                    }
                }

            }
        });

        holder.amount.setText(String.valueOf(gericht.getAmount()));

        holder.amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int quantity = Integer.parseInt(editable.toString());
                    gericht.setAmount(quantity);
                } catch (NumberFormatException e) {
                    // Fehler beim Parsen der Zahl
                }
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
        EditText amount;

        GerichtViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGerichtName = itemView.findViewById(R.id.textViewGerichtName);
            textViewGerichtPreis = itemView.findViewById(R.id.textViewGerichtPreis);
            textViewInfo = itemView.findViewById(R.id.textViewAdditionalInfo);
            gerichtLayout = itemView.findViewById(R.id.gerichtLayout);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
