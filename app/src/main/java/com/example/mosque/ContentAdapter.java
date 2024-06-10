package com.example.mosque;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {

    private Context context;
    private List<Content> contentList;
    private FirebaseFirestore firestore;

    public ContentAdapter(Context context, List<Content> contentList) {
        this.context = context;
        this.contentList = contentList;
        this.firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_content, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        Content content = contentList.get(position);
        holder.bind(content);
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvType;
        ImageView imageView;
        View deleteView;

        ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvType = itemView.findViewById(R.id.tvType);
            imageView = itemView.findViewById(R.id.imageView);
            deleteView = itemView.findViewById(R.id.deleteView);
        }

        void bind(Content content) {
            tvTitle.setText(content.getTitle());
            tvDescription.setText(content.getDescription());
            tvType.setText(content.getType());
            if (content.getImageUrl() != null && !content.getImageUrl().isEmpty()) {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(Uri.parse(content.getImageUrl())).into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
            }

            deleteView.setOnClickListener(v -> deleteContent(content));
        }
    }

    private void deleteContent(Content content) {
        if (content.getId() == null) {
            Toast.makeText(context, "Error: el ID del contenido es nulo", Toast.LENGTH_SHORT).show();
            return;
        }
        firestore.collection("contents").document(content.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    contentList.removeIf(c -> content.getId().equals(c.getId()));
                    notifyDataSetChanged();
                    Toast.makeText(context, "Contenido eliminado correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Error al eliminar el contenido", Toast.LENGTH_SHORT).show());
    }
}
