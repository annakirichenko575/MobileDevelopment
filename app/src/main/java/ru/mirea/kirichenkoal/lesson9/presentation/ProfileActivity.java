package ru.mirea.kirichenkoal.lesson9.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthActivity;
import ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthManager;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView tvTitle = findViewById(R.id.tvProfileTitle);
        Button btnLogout = findViewById(R.id.btnLogout);

        if (!AuthManager.isLoggedIn()) {
            tvTitle.setText("Гость");
            btnLogout.setText("Войти / Зарегистрироваться");
            btnLogout.setOnClickListener(v ->
                    startActivity(new Intent(this, AuthActivity.class)));
        } else {
            String email = AuthManager.currentUser() != null ? AuthManager.currentUser().getEmail() : null;
            if (email != null && !email.isEmpty()) {
                tvTitle.setText(email);
            } else {
                tvTitle.setText("Мой профиль");
            }

            btnLogout.setText("Выйти");
            btnLogout.setOnClickListener(v -> {
                AuthManager.logout();
                startActivity(new Intent(this, AuthActivity.class));
                finish();
            });
        }

        // Нижнее меню
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
                if (ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthManager.isGuest()) {
                    startActivity(new Intent(this, ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthActivity.class));
                    return true;
                }
                startActivity(new Intent(this, FavoriteActivity.class));
                return true;

            } else if (id == R.id.navigation_profile) {
                return true;
            }
            return false;
        });
    }
}
