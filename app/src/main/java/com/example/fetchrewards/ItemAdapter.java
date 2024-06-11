package com.example.fetchrewards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (itemList != null && position < itemList.size()) {
            Item item = itemList.get(position);
            if (item != null) {
                holder.nameTextView.setText(item.getName());
                holder.listIdTextView.setText(String.valueOf(item.getListId()));
            } else {
                holder.nameTextView.setText("Unknown");
                holder.listIdTextView.setText("Unknown");
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView listIdTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            listIdTextView = itemView.findViewById(R.id.listIdTextView);
        }
    }
}

