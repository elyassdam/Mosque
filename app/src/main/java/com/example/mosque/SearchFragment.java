package com.example.mosque;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño de este fragmento en concreto
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        // Configurar WebView para cargar la página web
        WebView webView = rootView.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Habilitar JavaScript si es necesario

        // Cargar la página web
        webView.loadUrl("https://www.ciacv.org/");

        return rootView;
    }
}
