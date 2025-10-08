package ru.mirea.kirichenkoal.lesson9.data.repository;

import android.util.Log;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ru.mirea.kirichenkoal.lesson9.domain.models.AuthResult;
import ru.mirea.kirichenkoal.lesson9.domain.models.User;
import ru.mirea.kirichenkoal.lesson9.domain.repository.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {

    private static final String TAG = "AuthRepositoryImpl";
    private final FirebaseAuth firebaseAuth;
    private final Executor executor;

    public AuthRepositoryImpl() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.executor = Executors.newSingleThreadExecutor();
    }

    // Конструктор для тестирования
    public AuthRepositoryImpl(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void loginWithEmail(String email, String password, AuthCallback callback) {
        executor.execute(() -> {
            try {
                var task = firebaseAuth.signInWithEmailAndPassword(email, password);
                var authResult = Tasks.await(task);

                if (authResult.getUser() != null) {
                    User user = mapFirebaseUserToDomain(authResult.getUser());
                    callback.onComplete(AuthResult.success(user));
                } else {
                    callback.onComplete(AuthResult.error("Login failed"));
                }
            } catch (Exception e) {
                Log.e(TAG, "Login error: " + e.getMessage());
                callback.onComplete(AuthResult.error(getErrorMessage(e)));
            }
        });
    }

    @Override
    public void registerWithEmail(String email, String password, String displayName, AuthCallback callback) {
        executor.execute(() -> {
            try {
                // Создаем пользователя
                var createUserTask = firebaseAuth.createUserWithEmailAndPassword(email, password);
                var authResult = Tasks.await(createUserTask);

                if (authResult.getUser() != null) {
                    // Обновляем display name
                    var profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .build();

                    var updateTask = authResult.getUser().updateProfile(profileUpdates);
                    Tasks.await(updateTask);

                    User user = mapFirebaseUserToDomain(authResult.getUser());
                    callback.onComplete(AuthResult.success(user));
                } else {
                    callback.onComplete(AuthResult.error("Registration failed"));
                }
            } catch (Exception e) {
                Log.e(TAG, "Registration error: " + e.getMessage());
                callback.onComplete(AuthResult.error(getErrorMessage(e)));
            }
        });
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
    }

    @Override
    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public User getCurrentUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            return mapFirebaseUserToDomain(firebaseUser);
        }
        return null;
    }

    private User mapFirebaseUserToDomain(FirebaseUser firebaseUser) {
        return new User(
                firebaseUser.getUid(),
                firebaseUser.getEmail(),
                firebaseUser.getDisplayName(),
                firebaseUser.isEmailVerified()
        );
    }

    private String getErrorMessage(Exception e) {
        String errorMessage = e.getMessage();
        if (errorMessage == null) {
            return "Unknown error occurred";
        }

        // Более понятные сообщения об ошибках
        if (errorMessage.contains("EMAIL_NOT_FOUND") || errorMessage.contains("INVALID_EMAIL")) {
            return "Invalid email address";
        } else if (errorMessage.contains("INVALID_PASSWORD")) {
            return "Invalid password";
        } else if (errorMessage.contains("EMAIL_EXISTS")) {
            return "Email already registered";
        } else if (errorMessage.contains("WEAK_PASSWORD")) {
            return "Password is too weak";
        } else if (errorMessage.contains("NETWORK_ERROR")) {
            return "Network error. Check your connection";
        } else {
            return errorMessage;
        }
    }
}