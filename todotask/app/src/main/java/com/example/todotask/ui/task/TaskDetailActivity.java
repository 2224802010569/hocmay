package com.example.todotask.ui.task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todotask.R;
import com.example.todotask.data.model.Task;
import com.example.todotask.viewmodel.TaskViewModel;

public class TaskDetailActivity extends AppCompatActivity {

    private EditText edtTaskTitle, edtStartTime, edtEndTime, edtDescription;
    private ImageButton btnPickStartTime, btnPickEndTime;
    private Button btnSaveTask, btnBackTask;

    private TaskViewModel viewModel;
    private Task currentTask;

    private int taskId; // Nhận ID task từ Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        viewModel = new TaskViewModel(this);

        // --- Ánh xạ view ---
        edtTaskTitle = findViewById(R.id.edtTaskTitle);
        edtStartTime = findViewById(R.id.edtStartTime);
        edtEndTime = findViewById(R.id.edtEndTime);
        edtDescription = findViewById(R.id.edtDescription);

        btnPickStartTime = findViewById(R.id.btnPickStartTime);
        btnPickEndTime = findViewById(R.id.btnPickEndTime);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnBackTask = findViewById(R.id.btnBackTask);

        // --- Nhận taskId từ Intent ---
        taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId != -1) {
            currentTask = viewModel.getAllTasks()
                    .stream()
                    .filter(t -> t.getTaskId() == taskId)
                    .findFirst()
                    .orElse(null);

            if (currentTask != null) {
                showTaskDetail(currentTask);
            } else {
                Toast.makeText(this, "Không tìm thấy task", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        // --- Chọn thời gian ---
        btnPickStartTime.setOnClickListener(v -> showTimePicker(edtStartTime));
        btnPickEndTime.setOnClickListener(v -> showTimePicker(edtEndTime));

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
    }

    // Hiển thị hộp chọn thời gian (giả lập đơn giản, có thể dùng DatePickerDialog)
    private void showTimePicker(EditText target) {
        Toast.makeText(this, "Hiển thị DateTimePicker cho: " + target.getHint(), Toast.LENGTH_SHORT).show();
        // TODO: bạn có thể thay bằng DatePickerDialog + TimePickerDialog thực tế
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
            // Cập nhật task hiện có
            currentTask.setTitle(title);
            currentTask.setStartTime(start);
            currentTask.setEndTime(end);
            currentTask.setDescription(desc);
            viewModel.update(currentTask);
            Toast.makeText(this, "Đã cập nhật task", Toast.LENGTH_SHORT).show();
        } else {
            // Tạo mới task
            Task newTask = new Task();
            newTask.setTitle(title);
            newTask.setStartTime(start);
            newTask.setEndTime(end);
            newTask.setDescription(desc);
            newTask.setCompleted(false);
            newTask.setNotified(false);
            newTask.setUserId(1); // TODO: lấy userId thực tế từ session
            viewModel.insert(newTask);
            Toast.makeText(this, "Đã thêm task mới", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    // --- Xác nhận xóa task (tuỳ chọn) ---
    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa task")
                .setMessage("Bạn có chắc muốn xóa task này không?")
                .setPositiveButton("Xóa", (DialogInterface dialog, int which) -> {
                    if (currentTask != null) {
                        viewModel.delete(currentTask.getTaskId());
                        Toast.makeText(this, "Đã xóa task", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
