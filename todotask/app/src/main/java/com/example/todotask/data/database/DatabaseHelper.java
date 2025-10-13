package com.example.todotask.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todotask.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USER = "User";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GMAIL = "gmail";
    public static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_GMAIL + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        // Dữ liệu mẫu
        db.execSQL("INSERT INTO " + TABLE_USER + " (name, gmail, password) VALUES " +
                "('ppk', '2224802010569@student.tdmu.edu.vn', '12345')," +
                "('Trần Thị B', 'thib@example.com', 'abcdef');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
}
