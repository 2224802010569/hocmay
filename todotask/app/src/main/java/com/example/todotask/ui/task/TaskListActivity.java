package com.example.todotask.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todotask.R;
import com.example.todotask.data.model.Task;
import com.example.todotask.data.repository.TaskRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity implements TaskAdapter.OnItemClick {

    private RecyclerView rv;
    private TaskAdapter adapter;
    private TaskRepository repo;
    private List<Task> tasks = new ArrayList<>();
    private FloatingActionButton fab;
    private ImageButton btnMenu, btnFilter;

    private static final int REQ_ADD = 1001;

    // 🔹 Biến lưu nhóm hiện đang được lọc
    private String currentFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        rv = findViewById(R.id.recyclerTasks);
        fab = findViewById(R.id.fabAdd);
        btnFilter = findViewById(R.id.btnFilter);

        repo = new TaskRepository(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(tasks, this);
        rv.setAdapter(adapter);

        loadTasks();

        fab.setOnClickListener(v -> {
            Intent i = new Intent(this, AddTaskActivity.class);
            startActivityForResult(i, REQ_ADD);
        });

        btnFilter.setOnClickListener(v -> showFilterMenu(v));
        btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(TaskListActivity.this, btnMenu);
            popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_account) {
                    Toast.makeText(TaskListActivity.this, "Account", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.menu_into) {
                    // Mở trang Nhà phát hành
                    Intent intent = new Intent(TaskListActivity.this, PublisherActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.menu_group) {
                    Toast.makeText(TaskListActivity.this, "Nhóm", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });

            popup.show();
        });

    }

    // 🔹 Hiển thị popup menu lọc
    private void showFilterMenu(android.view.View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_filter, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> handleFilter(item));
        popup.show();
    }

    // 🔹 Xử lý khi chọn item trong menu lọc
    private boolean handleFilter(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_tdm:
                currentFilter = "TDMU";
                filterTasks();
                Toast.makeText(this, "Đang lọc: TDMU", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.filter_dilam:
                currentFilter = "Đi làm";
                filterTasks();
                Toast.makeText(this, "Đang lọc: Đi làm", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.filter_clear:
                currentFilter = null;
                loadTasks();
                Toast.makeText(this, "Đã bỏ lọc", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    private void loadTasks() {
        tasks.clear();
        tasks.addAll(repo.getAll());
        adapter.notifyDataSetChanged();
    }

    // 🔹 Hàm lọc
    private void filterTasks() {
        tasks.clear();
        if (currentFilter == null) {
            tasks.addAll(repo.getAll());
        } else {
            tasks.addAll(repo.getTasksByGroup(currentFilter));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEdit(Task task) {
        Toast.makeText(this, "Edit: " + task.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDelete(Task task) {
        repo.delete(task.getTaskId());
        loadTasks();
        Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToggleComplete(Task task) {
        task.setCompleted(!task.isCompleted());
        repo.update(task);
        loadTasks();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADD) {
            loadTasks();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentFilter != null) filterTasks();
        else loadTasks();
    }
}
