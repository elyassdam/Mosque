package com.example.mosque;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NoticiaViewHolder> {

    private List<News> listaNoticias;

    public NewsAdapter(List<News> listaNoticias) {
        this.listaNoticias = listaNoticias;
    }

    @NonNull
    @Override
    public NoticiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NoticiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiaViewHolder holder, int position) {
        News noticia = listaNoticias.get(position);
        holder.bind(noticia);
    }

    @Override
    public int getItemCount() {
        return listaNoticias.size();
    }

    public class NoticiaViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitulo;
        private TextView textViewContenido;

        public NoticiaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitle);
            textViewContenido = itemView.findViewById(R.id.textViewContent);
        }

        public void bind(News noticia) {
            textViewTitulo.setText(noticia.getTitulo());
            textViewContenido.setText(noticia.getContenido());
        }
    }
}
