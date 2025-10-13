package com.example.todotask.viewmodel;

import android.content.Context;
import com.example.todotask.data.model.User;
import com.example.todotask.data.repository.UserRepository;

public class LoginViewModel {
    private final UserRepository userRepository;

    public LoginViewModel(Context context) {
        userRepository = new UserRepository(context);
    }

    public boolean login(String email, String password) {
        User user = userRepository.login(email, password);
        return user != null;
    }
}
