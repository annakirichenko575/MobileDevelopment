package ru.mirea.kirichenkoal.lesson9.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthActivity;
import ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthManager;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageProfile = view.findViewById(R.id.imageProfile);
        TextView tvTitle = view.findViewById(R.id.tvProfileTitle);
        TextView tvSubtitle = view.findViewById(R.id.tvSubtitle);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        if (!AuthManager.isLoggedIn()) {
            tvTitle.setText("Гость");
            tvSubtitle.setText("Войдите, чтобы получить доступ ко всем функциям 🌿");
            imageProfile.setImageResource(R.drawable.ic_profile_guest);
            btnLogout.setText("Войти / Зарегистрироваться");
            btnLogout.setOnClickListener(v ->
                    startActivity(new Intent(requireContext(), AuthActivity.class)));
        } else {
            String email = AuthManager.currentUser() != null ? AuthManager.currentUser().getEmail() : null;
            tvTitle.setText("Профиль пользователя");
            tvSubtitle.setText((email != null && !email.isEmpty()) ? email : "Авторизованный пользователь");
            imageProfile.setImageResource(R.drawable.ic_profile);
            btnLogout.setText("Выйти");
            btnLogout.setOnClickListener(v -> {
                AuthManager.logout();
                startActivity(new Intent(requireContext(), AuthActivity.class));
                requireActivity().finish();
            });
        }
    }
}
