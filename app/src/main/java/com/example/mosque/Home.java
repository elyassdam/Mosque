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

 public class Home extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private MapsFragment mapsFragment;

     private PayFragment payFragment;
    private SalatFragment salatFragment;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottom);
        frameLayout = findViewById(R.id.framel);

        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        mapsFragment = new MapsFragment();
        payFragment = new PayFragment();
        salatFragment=new SalatFragment();


     //   setFragment(homeFragment); // Mostrar el fragmento de inicio por defecto

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
                    case R.id.salat:
                        setFragment(salatFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.framel, fragment).commit();
    }
    private void createNotifications(){

     }
}

