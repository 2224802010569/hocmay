package com.example.todotask.ui.task;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todotask.R;
import com.example.todotask.data.model.Task;
import com.example.todotask.data.model.Category;
import com.example.todotask.data.repository.TaskRepository;
import com.example.todotask.data.repository.CategoryRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.view.View;
import android.widget.AdapterView;


public class AddTaskActivity extends AppCompatActivity {

    private EditText edtTitle, edtDescription, edtStartTime, edtEndTime;
    private Spinner spCategory;
    private Button btnSave;
    private TaskRepository taskRepo;
    private CategoryRepository categoryRepo;
    private int selectedCategoryId = -1;
    private Calendar startCal, endCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        edtTitle = findViewById(R.id.edtTaskTitle);
        edtDescription = findViewById(R.id.edtDescription);
        edtStartTime = findViewById(R.id.edtStartTime);
        edtEndTime = findViewById(R.id.edtEndTime);
        spCategory = findViewById(R.id.spCategory);
        btnSave = findViewById(R.id.btnSaveTask);

        taskRepo = new TaskRepository(getApplication());
        categoryRepo = new CategoryRepository(getApplication());

        startCal = Calendar.getInstance();
        endCal = Calendar.getInstance();

        setupCategorySpinner();
        setupDatePickers();

        btnSave.setOnClickListener(v -> saveTask());
    }
    private void setupCategorySpinner() {
        // Lấy danh sách từ repository (hàm của bạn là getAll())
        List<Category> categories = categoryRepo.getAll();

        // Nếu danh sách rỗng, thêm 1 mục "Không phân loại"
        if (categories == null) {
            categories = new java.util.ArrayList<>();
        }
        // Tạo 1 Category "Không phân loại" tạm (id = -1)
        Category none = new Category(-1, "Không phân loại", "#CCCCCC");
        categories.add(0, none);

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        // Khi chọn, lấy id thực của Category
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category sel = (Category) parent.getItemAtPosition(position);
                if (sel != null) selectedCategoryId = sel.getId();
                else selectedCategoryId = -1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryId = -1;
            }
        });
    }



    private void setupDatePickers() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

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
        String title = edtTitle.getText().toString().trim();
        String desc = edtDescription.getText().toString().trim();
        String start = edtStartTime.getText().toString().trim();
        String end = edtEndTime.getText().toString().trim();

        if (title.isEmpty() || start.isEmpty() || end.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(desc);
        // nếu selectedCategoryId == -1 => để null (không phân loại)
        if (selectedCategoryId >= 0) task.setCategoryId(selectedCategoryId);
        else task.setCategoryId(null);
        task.setStartTime(start);
        task.setEndTime(end);
        task.setCompleted(false);
        task.setNotified(false);
        task.setUserId(1); // TODO: thay bằng user hiện tại khi có login

        long inserted = taskRepo.insert(task);
        if (inserted != -1 && inserted > 0) {
            Toast.makeText(this, "Đã lưu task!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lưu thất bại!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }


}
