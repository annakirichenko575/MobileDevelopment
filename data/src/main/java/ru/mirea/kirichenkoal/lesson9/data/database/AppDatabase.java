package ru.mirea.kirichenkoal.lesson9.data.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import ru.mirea.kirichenkoal.lesson9.data.database.dao.PlantDao;
import ru.mirea.kirichenkoal.lesson9.data.database.entity.PlantEntity;

@Database(entities = {PlantEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PlantDao plantDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "plant_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}