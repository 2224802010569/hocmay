package com.example.todotask.data.repository;

import android.content.Context;
import com.example.todotask.data.dao.UserDao;
import com.example.todotask.data.model.User;

public class UserRepository {
    private final UserDao userDao;

    public UserRepository(Context context) {
        this.userDao = new UserDao(context);
    }

    public User login(String email, String password) {
        return userDao.getUserByEmailAndPassword(email, password);
    }

    public boolean register(User user) {
        long result = userDao.addUser(user);
        return result != -1;
    }
}
