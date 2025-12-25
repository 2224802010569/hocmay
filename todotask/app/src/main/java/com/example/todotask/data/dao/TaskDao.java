package com.example.todotask.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todotask.data.database.DatabaseHelper;
import com.example.todotask.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    private final DatabaseHelper dbHelper;

    public TaskDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    private Task cursorToTask(Cursor c) {
        Task t = new Task();
        t.setTaskId(c.getInt(c.getColumnIndexOrThrow("task_id")));
        t.setTitle(c.getString(c.getColumnIndexOrThrow("title")));
        t.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
        t.setStartTime(c.getString(c.getColumnIndexOrThrow("start_time")));
        t.setEndTime(c.getString(c.getColumnIndexOrThrow("end_time")));
        t.setCompleted(c.getInt(c.getColumnIndexOrThrow("is_completed")) == 1);
        t.setGroupName(c.getString(c.getColumnIndexOrThrow("group_name"))); // nhớ thêm cột này trong DB
        return t;
    }
    public long insertTask(Task t) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_TASK_USER_ID, t.getUserId());
        if (t.getCategoryId() != null) cv.put(DatabaseHelper.COLUMN_TASK_CATEGORY_ID, t.getCategoryId());
        cv.put(DatabaseHelper.COLUMN_TASK_TITLE, t.getTitle());
        cv.put(DatabaseHelper.COLUMN_TASK_DESCRIPTION, t.getDescription());
        cv.put(DatabaseHelper.COLUMN_TASK_START, t.getStartTime());
        cv.put(DatabaseHelper.COLUMN_TASK_END, t.getEndTime());
        cv.put(DatabaseHelper.COLUMN_TASK_COMPLETED, t.isCompleted() ? 1 : 0);
        cv.put(DatabaseHelper.COLUMN_TASK_NOTIFIED, t.isNotified() ? 1 : 0);
        long id = db.insert(DatabaseHelper.TABLE_TASK, null, cv);
        db.close();
        return id;
    }

    public boolean updateTask(Task t) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_TASK_TITLE, t.getTitle());
        cv.put(DatabaseHelper.COLUMN_TASK_DESCRIPTION, t.getDescription());
        cv.put(DatabaseHelper.COLUMN_TASK_START, t.getStartTime());
        cv.put(DatabaseHelper.COLUMN_TASK_END, t.getEndTime());
        cv.put(DatabaseHelper.COLUMN_TASK_COMPLETED, t.isCompleted() ? 1 : 0);
        cv.put(DatabaseHelper.COLUMN_TASK_NOTIFIED, t.isNotified() ? 1 : 0);
        if (t.getCategoryId() != null) cv.put(DatabaseHelper.COLUMN_TASK_CATEGORY_ID, t.getCategoryId());
        int rows = db.update(DatabaseHelper.TABLE_TASK, cv, DatabaseHelper.COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(t.getTaskId())});
        db.close();
        return rows > 0;
    }

    public boolean deleteTask(int taskId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DatabaseHelper.TABLE_TASK, DatabaseHelper.COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
        return rows > 0;
    }

    // 🟢 Thêm task mới
    public long addTask(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", task.getUserId());
        values.put("category_id", task.getCategoryId());
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("start_time", task.getStartTime());
        values.put("end_time", task.getEndTime());
        values.put("is_completed", task.isCompleted() ? 1 : 0);
        values.put("is_notified", task.isNotified() ? 1 : 0);

        long id = db.insert("Task", null, values);
        db.close();
        return id;
    }

   public List<Task> getAllTasks() {
       List<Task> list = new ArrayList<>();
       SQLiteDatabase db = dbHelper.getReadableDatabase();

       // Dùng đúng câu SQL có JOIN
       String sql = "SELECT t.*, c.name AS categoryName, c.color AS categoryColor " +
               "FROM Task t LEFT JOIN Category c ON t.category_id = c.category_id " +
               "ORDER BY t.task_id DESC";

       Cursor cursor = db.rawQuery(sql, null);

       if (cursor != null && cursor.moveToFirst()) {
           do {
               Task t = new Task();
               t.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow("task_id")));
               t.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));

               int catIdx = cursor.getColumnIndex("category_id");
               if (!cursor.isNull(catIdx))
                   t.setCategoryId(cursor.getInt(catIdx));

               t.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
               t.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
               t.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow("start_time")));
               t.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow("end_time")));
               t.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow("is_completed")) == 1);
               t.setNotified(cursor.getInt(cursor.getColumnIndexOrThrow("is_notified")) == 1);

               // 🟡 Lấy thêm dữ liệu nhóm
               int nameIdx = cursor.getColumnIndex("categoryName");
               if (nameIdx != -1)
                   t.setGroupName(cursor.getString(nameIdx));

               int colorIdx = cursor.getColumnIndex("categoryColor");
               if (colorIdx != -1)
                   t.setCategoryColor(cursor.getString(colorIdx));

               list.add(t);
           } while (cursor.moveToNext());
           cursor.close();
       }

       db.close();
       return list;
   }


    // Optional: get tasks by user
    public List<Task> getTasksByUser(int userId) {
        List<Task> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TASK, null, DatabaseHelper.COLUMN_TASK_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, DatabaseHelper.COLUMN_TASK_ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Task t = new Task();
                t.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_ID)));
                t.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_USER_ID)));
                int catIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_CATEGORY_ID);
                if (!cursor.isNull(catIdx)) t.setCategoryId(cursor.getInt(catIdx));
                t.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TITLE)));
                t.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DESCRIPTION)));
                t.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_START)));
                t.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_END)));
                t.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_COMPLETED)) == 1);
                t.setNotified(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_NOTIFIED)) == 1);

                // 🟡 Lấy thêm dữ liệu nhóm
                int nameIdx = cursor.getColumnIndex("categoryName");
                if (nameIdx != -1) t.setGroupName(cursor.getString(nameIdx));

                int colorIdx = cursor.getColumnIndex("categoryColor");
                if (colorIdx != -1) t.setCategoryColor(cursor.getString(colorIdx));
                list.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }
    // 🟢 Lấy một task theo ID
    public Task getTaskById(int taskId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_TASK,
                null,
                DatabaseHelper.COLUMN_TASK_ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null
        );

        Task t = null;
        if (cursor != null && cursor.moveToFirst()) {
            t = new Task();
            t.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_ID)));
            t.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_USER_ID)));
            int catIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_CATEGORY_ID);
            if (!cursor.isNull(catIdx)) t.setCategoryId(cursor.getInt(catIdx));
            t.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TITLE)));
            t.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DESCRIPTION)));
            t.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_START)));
            t.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_END)));
            t.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_COMPLETED)) == 1);
            t.setNotified(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_NOTIFIED)) == 1);
            cursor.close();
        }
        db.close();
        return t;
    }
    public List<Task> getByCategory(int categoryId) {
        List<Task> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Dùng JOIN để lấy luôn tên nhóm và màu
        String sql = "SELECT t.*, c.name AS categoryName, c.color AS categoryColor " +
                "FROM Task t LEFT JOIN Category c ON t.category_id = c.category_id " +
                "WHERE t.category_id = ? " +
                "ORDER BY t.task_id DESC";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(categoryId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Task t = new Task();
                t.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow("task_id")));
                t.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));

                int catIdx = cursor.getColumnIndex("category_id");
                if (!cursor.isNull(catIdx)) t.setCategoryId(cursor.getInt(catIdx));

                t.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                t.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                t.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow("start_time")));
                t.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow("end_time")));
                t.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow("is_completed")) == 1);
                t.setNotified(cursor.getInt(cursor.getColumnIndexOrThrow("is_notified")) == 1);

                // 🟡 Lấy thêm tên nhóm & màu
                int nameIdx = cursor.getColumnIndex("categoryName");
                if (nameIdx != -1)
                    t.setGroupName(cursor.getString(nameIdx));

                int colorIdx = cursor.getColumnIndex("categoryColor");
                if (colorIdx != -1)
                    t.setCategoryColor(cursor.getString(colorIdx));

                list.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return list;
    }

    public List<Task> getTasksByGroup(String categoryName) {
        List<Task> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Lấy cả màu và tên nhóm qua JOIN
        String sql = "SELECT t.task_id, t.user_id, t.category_id, t.title, t.description, " +
                "t.start_time, t.end_time, t.is_completed, t.is_notified, " +
                "c.name AS categoryName, c.color AS categoryColor " +
                "FROM Task t " +
                "JOIN Category c ON t.category_id = c.category_id " +
                "WHERE c.name = ? " +
                "ORDER BY t.task_id DESC";

        Cursor cursor = db.rawQuery(sql, new String[]{ categoryName });

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Task t = new Task();
                t.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow("task_id")));

                int userIdx = cursor.getColumnIndex("user_id");
                if (userIdx != -1 && !cursor.isNull(userIdx)) t.setUserId(cursor.getInt(userIdx));

                int catIdx = cursor.getColumnIndex("category_id");
                if (catIdx != -1 && !cursor.isNull(catIdx)) t.setCategoryId(cursor.getInt(catIdx));

                t.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                t.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));

                int startIdx = cursor.getColumnIndex("start_time");
                if (startIdx != -1) t.setStartTime(cursor.getString(startIdx));
                int endIdx = cursor.getColumnIndex("end_time");
                if (endIdx != -1) t.setEndTime(cursor.getString(endIdx));

                int compIdx = cursor.getColumnIndex("is_completed");
                if (compIdx != -1) t.setCompleted(cursor.getInt(compIdx) == 1);

                int notiIdx = cursor.getColumnIndex("is_notified");
                if (notiIdx != -1) t.setNotified(cursor.getInt(notiIdx) == 1);

                // Lấy tên nhóm & màu (nếu có)
                int nameIdx = cursor.getColumnIndex("categoryName");
                if (nameIdx != -1 && !cursor.isNull(nameIdx)) t.setGroupName(cursor.getString(nameIdx));

                int colorIdx = cursor.getColumnIndex("categoryColor");
                if (colorIdx != -1 && !cursor.isNull(colorIdx)) t.setCategoryColor(cursor.getString(colorIdx));

                list.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return list;
    }
    public boolean hasTasksInCategory(int categoryId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM Task WHERE category_id = ?",
                new String[]{String.valueOf(categoryId)}
        );

        boolean hasTask = false;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                hasTask = cursor.getInt(0) > 0;
            }
            cursor.close();
        }

        db.close();
        return hasTask;
    }



}