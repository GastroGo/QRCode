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
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Gericht> selectedGerichte;

    public OrderAdapter(List<Gericht> selectedGerichte) {
        this.selectedGerichte = selectedGerichte;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item_gericht, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Gericht gericht = selectedGerichte.get(position);

        // Setzen Sie die Daten in die Ansichtselemente
        holder.textViewGerichtName.setText(gericht.getGerichtName());
        holder.textViewGerichtPreis.setText(String.format("%.2f€", gericht.getPreis()));
        holder.textViewAmount.setText(String.valueOf(gericht.getAmount()));
    }

    @Override
    public int getItemCount() {
        return selectedGerichte.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewGerichtName;
        TextView textViewGerichtPreis;
        TextView textViewAmount;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGerichtName = itemView.findViewById(R.id.textViewOrderGerichtName);
            textViewGerichtPreis = itemView.findViewById(R.id.textViewOrderGerichtPreis);
            textViewAmount = itemView.findViewById(R.id.textViewOrderAmount);
        }
    }
}
