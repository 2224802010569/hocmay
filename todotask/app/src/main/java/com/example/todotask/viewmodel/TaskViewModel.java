package com.example.todotask.viewmodel;

import android.content.Context;
import com.example.todotask.data.model.Task;
import com.example.todotask.data.repository.TaskRepository;

import java.util.List;

public class TaskViewModel {
    private final TaskRepository repository;

    public TaskViewModel(Context context) {
        repository = new TaskRepository(context);
    }

    public List<Task> getAllTasks() {
        return repository.getAll();
    }

    public boolean insert(Task task) {
        return repository.add(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(int id) {
        repository.delete(id);
    }
}
