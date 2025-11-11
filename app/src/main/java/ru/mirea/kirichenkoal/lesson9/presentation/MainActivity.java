package ru.mirea.kirichenkoal.lesson9.presentation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthActivity;
import ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_MAIN = "TAG_MAIN";
    private static final String TAG_SEARCH = "TAG_SEARCH";
    private static final String TAG_FAVORITE = "TAG_FAVORITE";
    private static final String TAG_PROFILE = "TAG_PROFILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        BottomNavigationView nav = findViewById(R.id.bottomNavigation);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                switchTo(TAG_MAIN, new MainFragment(), /*addToBackStack*/ false);
                return true;
            } else if (id == R.id.navigation_search) {
                switchTo(TAG_SEARCH, new SearchFragment(), true);
                return true;
            } else if (id == R.id.navigation_analyze) {
                // пока «заглушка»
                android.widget.Toast.makeText(this, "Функция анализа листа пока не реализована", android.widget.Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.navigation_favorite) {
                if (AuthManager.isGuest()) {
                    startActivity(new android.content.Intent(this, AuthActivity.class));
                    return true;
                }
                switchTo(TAG_FAVORITE, new FavoriteFragment(), true);
                return true;
            } else if (id == R.id.navigation_profile) {
                switchTo(TAG_PROFILE, new ProfileFragment(), true);
                return true;
            }
            return false;
        });

        // Первый запуск — показываем главную
        if (savedInstanceState == null) {
            nav.setSelectedItemId(R.id.navigation_home);
        }
    }

    private void switchTo(@NonNull String tag, @NonNull Fragment newInstance, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();

        // Проверяем, есть ли уже добавленный фрагмент с таким тегом
        Fragment target = fm.findFragmentByTag(tag);
        if (target == null) {
            target = newInstance; // создаём новый, если не найден
        }

        // Запускаем транзакцию
        androidx.fragment.app.FragmentTransaction tx = fm.beginTransaction();
        tx.setReorderingAllowed(true);
        tx.replace(R.id.fragmentContainer, target, tag);
        if (addToBackStack) {
            tx.addToBackStack(tag);
        }
        tx.commit();
    }
}
