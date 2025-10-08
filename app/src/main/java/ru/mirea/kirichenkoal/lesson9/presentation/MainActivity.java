package ru.mirea.kirichenkoal.lesson9.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.data.repository.PlantRepositoryImpl;
import ru.mirea.kirichenkoal.lesson9.domain.models.Plant;
import ru.mirea.kirichenkoal.lesson9.domain.repository.PlantRepository;
import ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthActivity;

public class MainActivity extends AppCompatActivity {

    private PlantRepository plantRepository;
    private EditText editText;
    private TextView textView;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализируем Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        checkAuthentication();

        plantRepository = new PlantRepositoryImpl(this);
        initViews();
        setupClickListeners();
    }

    private void initViews() {
        editText = findViewById(R.id.editTextPlant);
        textView = findViewById(R.id.textViewPlant);
    }

    private void setupClickListeners() {
        Button btnSave = findViewById(R.id.buttonSavePlant);
        Button btnGet = findViewById(R.id.buttonGetPlant);

        btnSave.setOnClickListener(v -> savePlant());
        btnGet.setOnClickListener(v -> getFavoritePlant());

        findViewById(R.id.buttonSearch).setOnClickListener(v -> openSearch());
        findViewById(R.id.buttonFavorites).setOnClickListener(v -> {
            Intent intent = new Intent(this, FavoriteActivity.class);
            startActivity(intent);
        });

        // Кнопка выхода
        findViewById(R.id.buttonLogout).setOnClickListener(v -> logout());
    }

    private void savePlant() {
        String name = editText.getText().toString().trim();
        if (name.isEmpty()) {
            textView.setText("Введите название растения");
            return;
        }
        boolean result = plantRepository.savePlant(new Plant(1, name));
        textView.setText("Сохранено: " + name);
        editText.setText("");
    }

    private void getFavoritePlant() {
        Plant plant = plantRepository.getFavoritePlant();
        textView.setText("Ваше растение: " + plant.getName());
    }

    private void openSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void openFavorites() {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    private void logout() {
        firebaseAuth.signOut();
        checkAuthentication();
    }

    private void checkAuthentication() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            // Пользователь не авторизован - переходим на AuthActivity
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Пользователь авторизован
            String userName = currentUser.getDisplayName();
            String userEmail = currentUser.getEmail();
            if (userName != null && !userName.isEmpty()) {
                Toast.makeText(this, "Добро пожаловать, " + userName + "!", Toast.LENGTH_SHORT).show();
            } else if (userEmail != null) {
                Toast.makeText(this, "Добро пожаловать!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}