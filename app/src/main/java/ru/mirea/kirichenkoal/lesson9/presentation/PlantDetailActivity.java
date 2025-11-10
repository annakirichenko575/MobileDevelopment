package ru.mirea.kirichenkoal.lesson9.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.appbar.MaterialToolbar;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.data.network.PlantInfoRepository;
import ru.mirea.kirichenkoal.lesson9.data.network.dto.PlantInfoApiModel;
import ru.mirea.kirichenkoal.lesson9.data.repository.MockPlantRepository;
import ru.mirea.kirichenkoal.lesson9.data.storage.PlantNoteStorage;
import ru.mirea.kirichenkoal.lesson9.domain.models.PlantItem;

public class PlantDetailActivity extends AppCompatActivity {

    private PlantItem plant;
    private PlantNoteStorage noteStorage;
    private TextView textNote;
    private TextView textWatering;

    // добавляем сетевой репозиторий и LiveData
    private final PlantInfoRepository plantInfoRepo = new PlantInfoRepository();
    private final MutableLiveData<PlantInfoApiModel> plantInfo = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);

        // Инициализация Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbarBack);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        noteStorage = new PlantNoteStorage(this);

        int position = getIntent().getIntExtra("plant_index", -1);
        if (position >= 0 && MockPlantRepository.getInstance().getPlants().getValue() != null) {
            plant = MockPlantRepository.getInstance().getPlants().getValue().get(position);
        }

        ImageView imagePlant = findViewById(R.id.imagePlant);
        TextView textName = findViewById(R.id.textPlantName);
        textNote = findViewById(R.id.textNote);
        textWatering = findViewById(R.id.textWatering); // для данных о поливе

        // Данные растения
        if (plant != null) {
            textName.setText(plant.getName());
            int resId = getResources().getIdentifier(plant.getImageName(), "drawable", getPackageName());
            imagePlant.setImageResource(resId);

            // Загружаем информацию о поливе из сети
            plantInfoRepo.loadPlantInfo(plant.getImageName(), plantInfo);
        }

        plantInfo.observe(this, info -> {
            if (info != null) {
                textWatering.setText("Полив: " + info.getWatering());
                ((TextView) findViewById(R.id.textTemperature))
                        .setText("Температура: " + info.getTemperature());
                ((TextView) findViewById(R.id.textHumidity))
                        .setText("Влажность: " + info.getHumidity());
            } else {
                textWatering.setText("Ошибка загрузки данных");
            }
        });

        // Загрузка заметки, если есть
        String savedNote = noteStorage.getNote(plant.getName());
        if (savedNote != null && !savedNote.isEmpty()) {
            textNote.setVisibility(View.VISIBLE);
            textNote.setText(savedNote);
        }

        // Кнопка добавления заметки
        MaterialButton btnAddNote = findViewById(R.id.buttonAddNote);
        btnAddNote.setOnClickListener(v -> {
            AddNoteDialog.show(this, plant.getName(), note -> {
                noteStorage.saveNote(plant.getName(), note);
                textNote.setVisibility(View.VISIBLE);
                String allNotes = noteStorage.getNote(plant.getName());
                textNote.setText(allNotes);
            });
        });
    }
}
