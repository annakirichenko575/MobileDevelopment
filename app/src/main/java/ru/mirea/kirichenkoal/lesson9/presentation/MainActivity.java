package ru.mirea.kirichenkoal.lesson9.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.data.repository.PlantRepositoryImpl;
import ru.mirea.kirichenkoal.lesson9.domain.models.Plant;
import ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthActivity;
import ru.mirea.kirichenkoal.lesson9.presentation.viewmodel.PlantViewModel;
import ru.mirea.kirichenkoal.lesson9.presentation.viewmodel.PlantViewModelFactory;

/**
 * Главный экран приложения — теперь также работает через ViewModel.
 */
public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private FirebaseAuth firebaseAuth;

    // Добавляем ViewModel
    private PlantViewModel plantViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        checkAuthentication();

        initViews();
        setupViewModel();
        setupClickListeners();
    }

    private void initViews() {
        editText = findViewById(R.id.editTextPlant);
        textView = findViewById(R.id.textViewPlant);
    }

    private void setupViewModel() {
        PlantRepositoryImpl repo = new PlantRepositoryImpl(this);
        PlantViewModelFactory factory = new PlantViewModelFactory(repo);
        plantViewModel = new ViewModelProvider(this, factory).get(PlantViewModel.class);

        // Подписываемся на LiveData
        plantViewModel.getPlants().observe(this, plants -> {
            if (plants != null && !plants.isEmpty()) {
                Plant lastPlant = plants.get(plants.size() - 1);
                textView.setText("Ваше растение: " + lastPlant.getName());
            }
        });
    }

    private void setupClickListeners() {
        Button btnSave = findViewById(R.id.buttonSavePlant);
        Button btnGet = findViewById(R.id.buttonGetPlant);

        btnSave.setOnClickListener(v -> savePlant());
        btnGet.setOnClickListener(v -> getFavoritePlant());

        findViewById(R.id.buttonSearch).setOnClickListener(v -> openSearch());
        findViewById(R.id.buttonFavorites).setOnClickListener(v -> openFavorites());
        findViewById(R.id.buttonLogout).setOnClickListener(v -> logout());
    }

    private void savePlant() {
        String name = editText.getText().toString().trim();
        if (name.isEmpty()) {
            textView.setText("Введите название растения");
            return;
        }

        // сохраняем через репозиторий напрямую (ViewModel может остаться без save, если нет use case)
        PlantRepositoryImpl repo = new PlantRepositoryImpl(this);
        boolean result = repo.savePlant(new Plant(1, name));

        if (result) {
            Toast.makeText(this, "Сохранено: " + name, Toast.LENGTH_SHORT).show();
            editText.setText("");
            // обновляем данные из базы, чтобы отразились в LiveData
            plantViewModel.loadFromDatabase();
        } else {
            Toast.makeText(this, "Ошибка при сохранении", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFavoritePlant() {
        // теперь просто загружаем из базы — результат придёт через LiveData
        plantViewModel.loadFromDatabase();
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
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        } else {
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
