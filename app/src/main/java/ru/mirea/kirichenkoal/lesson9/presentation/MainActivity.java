package ru.mirea.kirichenkoal.lesson9.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.data.repository.PlantRepositoryImpl;
import ru.mirea.kirichenkoal.lesson9.domain.models.Plant;
import ru.mirea.kirichenkoal.lesson9.domain.repository.PlantRepository;
import ru.mirea.kirichenkoal.lesson9.domain.usecases.GetFavorityPlantByPage;
import ru.mirea.kirichenkoal.lesson9.domain.usecases.RemovePlantsFromFavorityByID;

public class MainActivity extends AppCompatActivity {

    private PlantRepository plantRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plantRepository = new PlantRepositoryImpl(this);

        EditText editText = findViewById(R.id.editTextPlant);
        TextView textView = findViewById(R.id.textViewPlant);

        Button btnSave = findViewById(R.id.buttonSavePlant);
        Button btnGet = findViewById(R.id.buttonGetPlant);
        Button btnOpenFavorite = findViewById(R.id.buttonOpenFavorite);

        // сохранить растение в избранное
        btnSave.setOnClickListener(v -> {
            String name = editText.getText().toString().trim();
            if (name.isEmpty()) {
                textView.setText("Введите название растения (не пустое)");
                return;
            }
            boolean result = plantRepository.savePlant(new Plant(1, name));
            textView.setText("Save result: " + result);
        });


        // получить одно "любимое" растение
        btnGet.setOnClickListener(v -> {
            Plant plant = plantRepository.getFavoritePlant();
            textView.setText("Favorite plant: " + plant.getName());
        });

        // открыть экран "Избранное"
        btnOpenFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
        });
    }
}
