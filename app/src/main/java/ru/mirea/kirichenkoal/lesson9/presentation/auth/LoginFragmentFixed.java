package ru.mirea.kirichenkoal.lesson9.presentation.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.presentation.MainActivity;
import ru.mirea.kirichenkoal.lesson9.data.repository.AuthRepositoryImpl;
import ru.mirea.kirichenkoal.lesson9.domain.repository.AuthRepository;
import ru.mirea.kirichenkoal.lesson9.domain.usecases.LoginUseCase;

public class LoginFragmentFixed extends Fragment {

    private TextInputEditText etEmail, etPassword;
    private TextInputLayout emailLayout, passwordLayout;
    private MaterialButton btnLogin, btnGuest;
    private View progressBar;

    private LoginUseCase loginUseCase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthRepository authRepository = new AuthRepositoryImpl();
        loginUseCase = new LoginUseCase(authRepository);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupClickListeners();
    }

    private void initViews(View view) {
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        emailLayout = view.findViewById(R.id.emailLayout);
        passwordLayout = view.findViewById(R.id.passwordLayout);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnGuest = view.findViewById(R.id.btnGuest);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());

        btnGuest.setOnClickListener(v -> {
            // ПРОСТОЙ INTENT БЕЗ NAVIGATION
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        getView().findViewById(R.id.tvForgotPassword).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Функция восстановления пароля в разработке", Toast.LENGTH_SHORT).show();
        });
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        clearErrors();

        if (!validateInputs(email, password)) {
            return;
        }

        setLoading(true);

        loginUseCase.execute(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onComplete(ru.mirea.kirichenkoal.lesson9.domain.models.AuthResult result) {
                // ВЕСЬ КОД ВНУТРИ runOnUiThread:
                requireActivity().runOnUiThread(() -> {
                    setLoading(false); // ТОЛЬКО ОДИН РАЗ И В UI ПОТОКЕ

                    if (result.isSuccess()) {
                        Toast.makeText(requireContext(), "Добро пожаловать!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(requireActivity(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    } else {
                        showError(result.getMessage());
                    }
                });
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        boolean isValid = true;

        if (email.isEmpty()) {
            emailLayout.setError("Введите email");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Неверный формат email");
            isValid = false;
        }

        if (password.isEmpty()) {
            passwordLayout.setError("Введите пароль");
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Пароль должен содержать минимум 6 символов");
            isValid = false;
        }

        return isValid;
    }

    private void clearErrors() {
        emailLayout.setError(null);
        passwordLayout.setError(null);
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    private void setLoading(boolean loading) {
        if (progressBar != null) {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        }
        btnLogin.setEnabled(!loading);
        btnGuest.setEnabled(!loading);

        if (loading) {
            btnLogin.setText("Вход...");
        } else {
            btnLogin.setText("Войти");
        }
    }
}