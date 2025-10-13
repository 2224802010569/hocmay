package com.example.todotask.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.todotask.data.database.DatabaseHelper;
import com.example.todotask.data.model.User;

public class UserDao {
    private final DatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public User getUserByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER,
                null,
                DatabaseHelper.COLUMN_GMAIL + "=? AND " + DatabaseHelper.COLUMN_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD))
            );
            cursor.close();
        }
        db.close();
        return user;
    }

    public long addUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, user.getName());
        values.put(DatabaseHelper.COLUMN_GMAIL, user.getGmail());
        values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());
        long id = db.insert(DatabaseHelper.TABLE_USER, null, values);
        db.close();
        return id;
    }
}
