package ru.mirea.kirichenkoal.lesson9.presentation;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.kirichenkoal.lesson9.R;

public class AnalyzeFragment extends Fragment {

    private ImageView imageView;
    private TextView textResult;
    private Interpreter tflite;
    private List<String> labels;

    // 🔹 Новый способ выбрать изображение
    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    handleImage(uri);
                } else {
                    Toast.makeText(requireContext(), "Изображение не выбрано", Toast.LENGTH_SHORT).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analyze, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.imageView);
        textResult = view.findViewById(R.id.textResult);

        try {
            tflite = new Interpreter(loadModelFile());
            labels = loadLabels();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Ошибка загрузки модели", Toast.LENGTH_LONG).show();
        }

        // 👇 обработчик кнопки
        view.findViewById(R.id.buttonPickImage).setOnClickListener(v -> pickImage());
    }

    private void pickImage() {
        pickImageLauncher.launch("image/*"); // ✅ Современный вызов
    }

    private void handleImage(Uri uri) {
        try {
            Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(
                    requireActivity().getContentResolver(), uri);
            imageView.setImageBitmap(bitmap);
            classifyImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Ошибка обработки изображения", Toast.LENGTH_SHORT).show();
        }
    }

    private void classifyImage(Bitmap bitmap) {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
        float[][][][] input = new float[1][224][224][3];

        for (int x = 0; x < 224; x++) {
            for (int y = 0; y < 224; y++) {
                int pixel = resized.getPixel(x, y);
                input[0][y][x][0] = ((pixel >> 16) & 0xFF) / 255.0f;
                input[0][y][x][1] = ((pixel >> 8) & 0xFF) / 255.0f;
                input[0][y][x][2] = (pixel & 0xFF) / 255.0f;
            }
        }

        float[][] output = new float[1][labels.size()];
        tflite.run(input, output);

        int maxIndex = 0;
        float maxProb = 0;
        for (int i = 0; i < labels.size(); i++) {
            if (output[0][i] > maxProb) {
                maxProb = output[0][i];
                maxIndex = i;
            }
        }

        textResult.setText(labels.get(maxIndex) +
                "\nУверенность: " + String.format("%.2f", maxProb * 100) + "%");
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        try (FileInputStream fis = new FileInputStream(requireContext().getAssets().openFd("plant_disease_model.tflite").getFileDescriptor());
             FileChannel channel = fis.getChannel()) {
            long startOffset = requireContext().getAssets().openFd("plant_disease_model.tflite").getStartOffset();
            long declaredLength = requireContext().getAssets().openFd("plant_disease_model.tflite").getDeclaredLength();
            return channel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        }
    }

    private List<String> loadLabels() throws IOException {
        List<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(requireContext().getAssets().open("labels.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) result.add(line);
        }
        return result;
    }
}
