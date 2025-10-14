package com.example.todotask.data.repository;

import android.content.Context;
import com.example.todotask.data.dao.TaskDao;
import com.example.todotask.data.model.Task;
import com.example.todotask.data.database.DatabaseHelper;
import java.util.List;

public class TaskRepository {
    private final TaskDao dao;

    public TaskRepository(Context context) {
        dao = new TaskDao(context);
    }

    public List<Task> getAll() {
        return dao.getAllTasks();
    }

    public boolean add(Task task) {
        return dao.addTask(task) != -1;
    }

    public void delete(int id) {
        dao.deleteTask(id);
    }

    public void update(Task task) {
        dao.updateTask(task);
    }
    public Task getTaskById(int id) {
        return dao.getTaskById(id);
    }
    public List<Task> getTasksByGroup(String groupName) {
        return dao.getTasksByGroup(groupName);
    }


}
