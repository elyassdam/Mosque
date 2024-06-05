package com.example.mosque;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private List<Store> storeList;

    public StoreAdapter(List<Store> storeList) {
        this.storeList = storeList;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = storeList.get(position);
        holder.bind(store);
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    class StoreViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewAddress, textViewType;
        ImageView imageView;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewType = itemView.findViewById(R.id.textViewType);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bind(Store store) {
            textViewName.setText(store.getName());
            textViewAddress.setText(store.getAddress());
            textViewType.setText(store.getType());
            if (store.getImageUrl() != null) {
                // Cargar imagen usando Glide
                Glide.with(imageView.getContext()).load(store.getImageUrl()).into(imageView);
            }
        }
    }
}
