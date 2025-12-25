package com.example.todotask.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todotask.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_USER = "User";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GMAIL = "gmail";
    public static final String COLUMN_PASSWORD = "password";
   // public static final String TABLE_CATEGORY = "categories";
    public static final String TABLE_CATEGORY = "Category";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY_NAME = "name";
    public static final String COLUMN_CATEGORY_COLOR = "color";

    public static final String TABLE_TASK = "Task";
    public static final String COLUMN_TASK_ID = "task_id";
    public static final String COLUMN_TASK_USER_ID = "user_id";
    public static final String COLUMN_TASK_CATEGORY_ID = "category_id";
    public static final String COLUMN_TASK_TITLE = "title";
    public static final String COLUMN_TASK_DESCRIPTION = "description";
    public static final String COLUMN_TASK_START = "start_time";
    public static final String COLUMN_TASK_END = "end_time";
    public static final String COLUMN_TASK_COMPLETED = "is_completed";
    public static final String COLUMN_TASK_NOTIFIED = "is_notified";
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


        // CREATE CATEGORY: dùng COLUMN_CATEGORY_ID = "category_id"
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + " ("
                + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CATEGORY_NAME + " TEXT NOT NULL, "
                + COLUMN_CATEGORY_COLOR + " TEXT NOT NULL)";
        db.execSQL(CREATE_CATEGORY_TABLE);

        // Task
        String CREATE_TASK_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TASK + " ("
                + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TASK_USER_ID + " INTEGER, "
                + COLUMN_TASK_CATEGORY_ID + " INTEGER, "
                + COLUMN_TASK_TITLE + " TEXT, "
                + COLUMN_TASK_DESCRIPTION + " TEXT, "
                + COLUMN_TASK_START + " TEXT, "
                + COLUMN_TASK_END + " TEXT, "
                + COLUMN_TASK_COMPLETED + " INTEGER DEFAULT 0, "
                + COLUMN_TASK_NOTIFIED + " INTEGER DEFAULT 0, "
                + "FOREIGN KEY(" + COLUMN_TASK_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "), "
                + "FOREIGN KEY(" + COLUMN_TASK_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + ")"
                + ")";
        db.execSQL(CREATE_TASK_TABLE);

        // Dữ liệu mẫu

        db.execSQL("INSERT INTO " + TABLE_USER + " ("
                + COLUMN_NAME + ", " + COLUMN_GMAIL + ", " + COLUMN_PASSWORD + ") VALUES "
                + "('ppk', '2224802010569@student.tdmu.edu.vn', '12345'),"
                + "('Trần Thị B', 'thib@example.com', 'abcdef');");

        db.execSQL("INSERT OR IGNORE INTO " + TABLE_CATEGORY + " ("
                + COLUMN_CATEGORY_NAME + "," + COLUMN_CATEGORY_COLOR + ") VALUES "
                + "('Công việc','#FF5733'), ('Cá nhân','#33C1FF')");

        db.execSQL("INSERT INTO " + TABLE_TASK + " ("
                + COLUMN_TASK_USER_ID + "," + COLUMN_TASK_CATEGORY_ID + "," + COLUMN_TASK_TITLE + ","
                + COLUMN_TASK_DESCRIPTION + "," + COLUMN_TASK_START + "," + COLUMN_TASK_END + ","
                + COLUMN_TASK_COMPLETED + "," + COLUMN_TASK_NOTIFIED + ") VALUES "
                + "(1,1,'Họp nhóm dự án','Chuẩn bị nội dung báo cáo tuần','2025-10-13 09:00:00','2025-10-13 10:30:00',0,0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }
}
