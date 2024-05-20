package com.example.mosque;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

 class Home extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private MapsFragment mapsFragment;
    private PayFragment payFragment;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frame);

        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        mapsFragment = new MapsFragment();
        payFragment = new PayFragment();

        setFragment(homeFragment); // Mostrar el fragmento de inicio por defecto

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        setFragment(homeFragment);
                        return true;
                    case R.id.search:
                        setFragment(searchFragment);
                        return true;
                    case R.id.maps:
                        setFragment(mapsFragment);
                        return true;
                    case R.id.pay:
                        setFragment(payFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }
}/*
webView = findViewById(R.id.webView);
WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Habilitar JavaScript si es necesario
        webView.loadUrl("https://www.ciacv.org/");


@Override
public void onBackPressed() {
    if (webView.canGoBack()) {
        webView.goBack();
    } else {
        super.onBackPressed();
    }
}*/

