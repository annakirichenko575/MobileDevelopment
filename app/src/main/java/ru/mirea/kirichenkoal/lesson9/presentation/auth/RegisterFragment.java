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
import ru.mirea.kirichenkoal.lesson9.domain.usecases.RegisterUseCase;

public class RegisterFragment extends Fragment {

    private TextInputEditText etDisplayName, etEmail, etPassword;
    private TextInputLayout nameLayout, emailLayout, passwordLayout;
    private MaterialButton btnRegister;
    private View progressBar;

    private RegisterUseCase registerUseCase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthRepository authRepository = new AuthRepositoryImpl();
        registerUseCase = new RegisterUseCase(authRepository);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupClickListeners();
    }

    private void initViews(View view) {
        etDisplayName = view.findViewById(R.id.etDisplayName);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        nameLayout = view.findViewById(R.id.nameLayout);
        emailLayout = view.findViewById(R.id.emailLayout);
        passwordLayout = view.findViewById(R.id.passwordLayout);
        btnRegister = view.findViewById(R.id.btnRegister);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> attemptRegistration());
    }

    private void attemptRegistration() {
        String displayName = etDisplayName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Сбрасываем ошибки
        clearErrors();

        // Валидация
        if (!validateInputs(displayName, email, password)) {
            return;
        }

        // Показываем прогресс
        setLoading(true);

        // Выполняем регистрацию
        registerUseCase.execute(email, password, displayName, new AuthRepository.AuthCallback() {
            @Override
            public void onComplete(ru.mirea.kirichenkoal.lesson9.domain.models.AuthResult result) {
                requireActivity().runOnUiThread(() -> {
                    setLoading(false);

                    if (result.isSuccess()) {
                        Toast.makeText(requireContext(), "Регистрация успешна!", Toast.LENGTH_SHORT).show();
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

    private boolean validateInputs(String displayName, String email, String password) {
        boolean isValid = true;

        if (displayName.isEmpty()) {
            nameLayout.setError("Введите ваше имя");
            isValid = false;
        }

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
        nameLayout.setError(null);
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
        btnRegister.setEnabled(!loading);

        if (loading) {
            btnRegister.setText("Регистрация...");
        } else {
            btnRegister.setText("Создать аккаунт");
        }
    }
}