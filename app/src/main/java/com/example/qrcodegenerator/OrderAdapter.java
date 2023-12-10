package com.example.qrcodegenerator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Gericht> selectedGerichte;

    public OrderAdapter(List<Gericht> selectedGerichte) {
        this.selectedGerichte = selectedGerichte;
    }

    private OnListEmptyListener onListEmptyListener;

    public interface OnListEmptyListener {
        void onListEmpty();
    }

    public void setOnListEmptyListener(OnListEmptyListener listener) {
        this.onListEmptyListener = listener;
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
        holder.textViewGerichtPreis.setText(String.format("%.2f€", gericht.getPreis()*gericht.getAmount()));
        holder.editTextAmount.setText(String.valueOf(gericht.getAmount()));

        holder.editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int amount = Integer.parseInt(editable.toString());
                    holder.textViewGerichtPreis.setText(String.format("%.2f€", gericht.getPreis()*amount));
                } catch (NumberFormatException e) {
                    // Fehler beim Parsen der Zahl
                }
            }

        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Gericht removeGericht = selectedGerichte.get(position);
                selectedGerichte.remove(removeGericht);
                notifyItemRemoved(position);

                if (selectedGerichte.isEmpty() && onListEmptyListener != null) {
                    onListEmptyListener.onListEmpty();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedGerichte.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewGerichtName;
        TextView textViewGerichtPreis;
        EditText editTextAmount;
        FloatingActionButton deleteBtn;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGerichtName = itemView.findViewById(R.id.textViewOrderGerichtName);
            textViewGerichtPreis = itemView.findViewById(R.id.textViewOrderGerichtPreis);
            editTextAmount = itemView.findViewById(R.id.editTextOrderAmount);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
