package com.example.todotask.ui.task;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todotask.R;
import com.example.todotask.data.model.Task;
import com.example.todotask.data.model.Category;
import com.example.todotask.viewmodel.TaskViewModel;
import com.example.todotask.data.repository.CategoryRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskDetailActivity extends AppCompatActivity {

    private EditText edtTaskTitle, edtStartTime, edtEndTime, edtDescription;
    private ImageButton btnPickStartTime, btnPickEndTime, btnGroupDropdown;
    private Button btnSaveTask, btnBackTask;
    private TextView tvGroupName;

    private TaskViewModel viewModel;
    private CategoryRepository categoryRepo;
    private Task currentTask;

    private int taskId;
    private int selectedCategoryId = -1;
    private List<Category> categoryList;
    private Calendar startCal, endCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        viewModel = new TaskViewModel(this);
        categoryRepo = new CategoryRepository(getApplication());

        // --- Ánh xạ view ---
        edtTaskTitle = findViewById(R.id.edtTaskTitle);
        edtStartTime = findViewById(R.id.edtStartTime);
        edtEndTime = findViewById(R.id.edtEndTime);
        edtDescription = findViewById(R.id.edtDescription);
        btnPickStartTime = findViewById(R.id.btnPickStartTime);
        btnPickEndTime = findViewById(R.id.btnPickEndTime);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnBackTask = findViewById(R.id.btnBackTask);
        btnGroupDropdown = findViewById(R.id.btnGroupDropdown);
        tvGroupName = findViewById(R.id.tvGroupName);

        startCal = Calendar.getInstance();
        endCal = Calendar.getInstance();

        categoryList = categoryRepo.getAll();
        if (categoryList == null) categoryList = new ArrayList<>();

        // --- Nhận taskId từ Intent ---
        taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId != -1) {
            currentTask = viewModel.getTaskById(taskId);
            if (currentTask != null) {
                showTaskDetail(currentTask);
            } else {
                Toast.makeText(this, "Không tìm thấy task", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        // --- Nút chọn nhóm ---
        btnGroupDropdown.setOnClickListener(v -> showCategoryDialog());

        // --- Chọn thời gian ---
        setupDatePickers();

        // --- Nút Lưu ---
        btnSaveTask.setOnClickListener(v -> saveTask());

        // --- Nút Quay lại ---
        btnBackTask.setOnClickListener(v -> finish());
    }

    private void showTaskDetail(Task task) {
        edtTaskTitle.setText(task.getTitle());
        edtStartTime.setText(task.getStartTime());
        edtEndTime.setText(task.getEndTime());
        edtDescription.setText(task.getDescription());

        // Hiển thị tên nhóm nếu có
        if (task.getCategoryId() != null) {
            selectedCategoryId = task.getCategoryId();
            Category cat = categoryRepo.getById(selectedCategoryId);
            if (cat != null) tvGroupName.setText(cat.getName());
        }
    }

    private void showCategoryDialog() {
        if (categoryList.isEmpty()) {
            Toast.makeText(this, "Chưa có nhóm nào!", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] names = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++) {
            names[i] = categoryList.get(i).getName();
        }

        new AlertDialog.Builder(this)
                .setTitle("Chọn nhóm công việc")
                .setItems(names, (dialog, which) -> {
                    Category selected = categoryList.get(which);
                    selectedCategoryId = selected.getId();
                    tvGroupName.setText(selected.getName());
                })
                .show();
    }

    private void setupDatePickers() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        edtStartTime.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, day) -> {
                startCal.set(year, month, day);
                edtStartTime.setText(sdf.format(startCal.getTime()));
            }, startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH), startCal.get(Calendar.DAY_OF_MONTH)).show();
        });

        edtEndTime.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, day) -> {
                endCal.set(year, month, day);
                edtEndTime.setText(sdf.format(endCal.getTime()));
            }, endCal.get(Calendar.YEAR), endCal.get(Calendar.MONTH), endCal.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void saveTask() {
        String title = edtTaskTitle.getText().toString().trim();
        String start = edtStartTime.getText().toString().trim();
        String end = edtEndTime.getText().toString().trim();
        String desc = edtDescription.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên task", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentTask != null) {
            currentTask.setTitle(title);
            currentTask.setStartTime(start);
            currentTask.setEndTime(end);
            currentTask.setDescription(desc);

            if (selectedCategoryId >= 0)
                currentTask.setCategoryId(selectedCategoryId);
            else
                currentTask.setCategoryId(null);

            viewModel.update(currentTask);
            Toast.makeText(this, "Đã cập nhật task", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
