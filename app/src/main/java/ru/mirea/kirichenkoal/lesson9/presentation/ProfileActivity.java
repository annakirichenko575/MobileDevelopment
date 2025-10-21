package ru.mirea.kirichenkoal.lesson9.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.mirea.kirichenkoal.lesson9.R;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Button btnLogout = findViewById(R.id.btnLogout);
        // btnLogout.setOnClickListener(v -> finish());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.navigation_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            } else if (id == R.id.navigation_analyze) {
                Toast.makeText(this, "Функция анализа листа пока не реализована", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.navigation_favorite) {
                startActivity(new Intent(this, FavoriteActivity.class));
                return true;
            } else if (id == R.id.navigation_profile) {
                return true;
            }
            return false;
        });
    }
}
