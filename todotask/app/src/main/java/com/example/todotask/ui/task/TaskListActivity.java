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
import com.example.todotask.ui.category.CategoryActivity;
import com.example.todotask.ui.user.UserInfoActivity;
import com.example.todotask.ui.main.MenuHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.todotask.data.repository.CategoryRepository;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity implements TaskAdapter.OnItemClick {

    private RecyclerView rv;
    private TaskAdapter adapter;
    private TaskRepository repo;
    private List<Task> tasks = new ArrayList<>();
    private FloatingActionButton fab;
    private ImageButton btnFilter;

    private static final int REQ_ADD = 1001;

    // 🔹 Biến lưu nhóm hiện đang được lọc
    private String currentFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        //khai báo search
        EditText etSearch = findViewById(R.id.etSearch);



        rv = findViewById(R.id.recyclerTasks);
        fab = findViewById(R.id.fabAdd);
        btnFilter = findViewById(R.id.btnFilter);
        /*btnMenu = findViewById(R.id.btnMenu);*/
        ImageButton btnGroupDropdown = findViewById(R.id.btnGroupDropdown);

        btnGroupDropdown.setOnClickListener(v -> showGroupDropdown(v));


        repo = new TaskRepository(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(tasks, this);
        rv.setAdapter(adapter);

        // Hiển thị tên người dùng
        TextView tvUsername = findViewById(R.id.tvUsername);

        // Lấy dữ liệu từ SharedPreferences
        String username = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("user_name", "Người dùng");

        // Gán tên vào TextView
        tvUsername.setText(username);

        loadTasks();

        fab.setOnClickListener(v -> {
            Intent i = new Intent(this, AddTaskActivity.class);
            startActivityForResult(i, REQ_ADD);
        });

        //btnFilter.setOnClickListener(v -> showMainMenu(v));
        btnFilter.setOnClickListener(v -> MenuHelper.showMainMenu(this, v));

        //ánh xạ etsearhc
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBySearch(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }



    private void loadTasks() {
        tasks.clear();
        tasks.addAll(repo.getAll());
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onEdit(Task task) {
        Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
        intent.putExtra("task_id", task.getTaskId());
        startActivity(intent);
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
        if (currentFilter != null) {
            tasks.clear();
            tasks.addAll(repo.getTasksByGroup(currentFilter));
            adapter.notifyDataSetChanged();
        } else {
            loadTasks();
        }

    }
    private void showGroupDropdown(android.view.View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        CategoryRepository categoryRepository = new CategoryRepository(this);

        //  Lấy danh sách nhóm từ DB
        List<com.example.todotask.data.model.Category> categories = categoryRepository.getAll();

        //  Thêm nhóm từ DB vào menu
        for (com.example.todotask.data.model.Category category : categories) {
            popup.getMenu().add(category.getName());
        }

        //  Thêm lựa chọn hiển thị tất cả
        popup.getMenu().add("Tất cả");

        popup.setOnMenuItemClickListener(item -> {
            String selectedGroup = item.getTitle().toString();

            if (selectedGroup.equals("Tất cả")) {
                currentFilter = null;
                loadTasks();
            } else {
                currentFilter = selectedGroup;

                // 🟢 Lấy task theo nhóm từ DB
                tasks.clear();
                tasks.addAll(repo.getTasksByGroup(selectedGroup));
                adapter.notifyDataSetChanged();
            }

            Toast.makeText(this, "Lọc: " + selectedGroup, Toast.LENGTH_SHORT).show();
            return true;
        });

        popup.show();
    }

    private void filterBySearch(String keyword) {
        List<Task> allTasks;

        // Nếu đang lọc theo nhóm Category thì chỉ tìm trong nhóm đó
        if (currentFilter != null) {
            allTasks = repo.getTasksByGroup(currentFilter);
        } else {
            allTasks = repo.getAll();
        }

        tasks.clear();

        if (keyword.isEmpty()) {
            tasks.addAll(allTasks);
        } else {
            for (Task t : allTasks) {
                if (t.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                    tasks.add(t);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

}
