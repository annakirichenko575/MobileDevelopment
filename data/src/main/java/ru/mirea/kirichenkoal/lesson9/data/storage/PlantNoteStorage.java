package ru.mirea.kirichenkoal.lesson9.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class PlantNoteStorage {

    private final Context context;

    public PlantNoteStorage(Context context) {
        this.context = context.getApplicationContext();
    }

    public void saveNote(String plantName, String newNote) {
        SharedPreferences prefs = context.getSharedPreferences("plant_notes", Context.MODE_PRIVATE);
        String existingNotes = prefs.getString(plantName, "");

        // добавляем дату и перевод строки
        StringBuilder updated = new StringBuilder();
        if (!existingNotes.isEmpty()) {
            updated.append(existingNotes).append("\n\n");
        }

        updated.append("📝 ").append(newNote);

        prefs.edit().putString(plantName, updated.toString()).apply();
    }

    public String getNote(String plantName) {
        SharedPreferences prefs = context.getSharedPreferences("plant_notes", Context.MODE_PRIVATE);
        return prefs.getString(plantName, "");
    }
}
