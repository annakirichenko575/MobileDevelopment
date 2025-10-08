package ru.mirea.kirichenkoal.lesson9.presentation.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import ru.mirea.kirichenkoal.lesson9.R;

public class AuthActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initViews();
        setupViewPager();
        setupTabLayout();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }

    private void setupViewPager() {
        AuthPagerAdapter adapter = new AuthPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Отключаем свайп для переключения вкладок (опционально)
        // viewPager.setUserInputEnabled(false);
    }

    private void setupTabLayout() {
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Вход");
                    } else {
                        tab.setText("Регистрация");
                    }
                }
        ).attach();
    }
}