package ru.mirea.kirichenkoal.lesson9.presentation;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;

public class AddNoteDialog {

    public interface OnNoteSaved {
        void onSave(String note);
    }

    public static void show(Context context, String plantName, OnNoteSaved callback) {
        final EditText input = new EditText(context);
        input.setHint("Введите заметку для " + plantName);

        new AlertDialog.Builder(context)
                .setTitle("Добавить заметку")
                .setView(input)
                .setPositiveButton("Сохранить", (dialog, which) ->
                        callback.onSave(input.getText().toString()))
                .setNegativeButton("Отмена", null)
                .show();
    }
}
