package ru.mirea.kirichenkoal.lesson9.presentation.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class AuthManager {

    private AuthManager() {}

    public static FirebaseUser currentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static boolean isLoggedIn() {
        FirebaseUser user = currentUser();
        return user != null && !user.isAnonymous();
    }

    public static boolean isGuest() {
        FirebaseUser user = currentUser();
        return user == null || user.isAnonymous();
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}
