package ru.mirea.kirichenkoal.lesson9.presentation.auth;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AuthPagerAdapter extends FragmentStateAdapter {

    public AuthPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new LoginFragmentFixed();
        } else {
            return new RegisterFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Две вкладки: Вход и Регистрация
    }
}