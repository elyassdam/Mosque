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
    private OnDeleteClickListener onDeleteClickListener;

    public StoreAdapter(List<Store> storeList) {
        this.storeList = storeList;
        this.onDeleteClickListener = onDeleteClickListener;
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
        View deleteButton;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewType = itemView.findViewById(R.id.textViewType);
            imageView = itemView.findViewById(R.id.imageView);
            deleteButton = itemView.findViewById(R.id.btnDelete);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onDeleteClickListener.onDeleteClick(position);
                    }
                }
            });
        }

        public void bind(Store store) {
            textViewName.setText(store.getName());
            textViewAddress.setText(store.getAddress());
            textViewType.setText(store.getType());
            if (store.getImageUrl() != null) {
                // Cargar imagen utilizando Glide
                Glide.with(imageView.getContext()).load(store.getImageUrl()).into(imageView);
            }
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
}
