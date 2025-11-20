package com.example.todotask.ui.category;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todotask.R;
import com.example.todotask.data.repository.TaskRepository;
import com.example.todotask.data.model.Category;
import com.example.todotask.data.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import com.example.todotask.ui.main.MenuHelper;
import android.widget.ImageButton;
public class CategoryActivity extends AppCompatActivity {

    private CategoryRepository repo;
    private List<Category> list;
    private CategoryAdapter adapter;
    private ImageButton btnFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        repo = new CategoryRepository(this);
        list = new ArrayList<>(repo.getAll());

        RecyclerView rv = findViewById(R.id.recyclerCategory);
        Button btnAdd = findViewById(R.id.btnAddCategory);
        btnFilter = findViewById(R.id.btnFilter);

        adapter = new CategoryAdapter(list, this::onCategoryLongClick);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> showAddDialog());
        btnFilter.setOnClickListener(v -> MenuHelper.showMainMenu(this, v));
    }

    // ------------------ MENU KHI NHẤN GIỮ ------------------
    private void onCategoryLongClick(Category category) {
        String[] options = {"✏️ Sửa nhóm", "❌ Xóa nhóm"};

        new AlertDialog.Builder(this)
                .setTitle("Tùy chọn cho '" + category.getName() + "'")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showEditDialog(category);
                    } else if (which == 1) {
                        confirmDelete(category);
                    }
                })
                .show();
    }

    // ------------------ POPUP THÊM ------------------
    private void showAddDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final android.view.View view = inflater.inflate(R.layout.dialog_add_category, null);

        EditText etName = view.findViewById(R.id.etCategoryName);
        EditText etColor = view.findViewById(R.id.etCategoryColor);
        Button btnPick = view.findViewById(R.id.btnPickColor);

        btnPick.setOnClickListener(v -> showColorPicker(etColor));

        new AlertDialog.Builder(this)
                .setTitle("Thêm nhóm công việc")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String color = etColor.getText().toString().trim();
                    if (!name.isEmpty() && color.startsWith("#")) {
                        Category c = new Category(0, name, color);
                        if (repo.add(c)) refreshList();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ------------------ POPUP SỬA ------------------
    private void showEditDialog(Category oldCategory) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final android.view.View view = inflater.inflate(R.layout.dialog_add_category, null);

        EditText etName = view.findViewById(R.id.etCategoryName);
        EditText etColor = view.findViewById(R.id.etCategoryColor);
        Button btnPick = view.findViewById(R.id.btnPickColor);

        etName.setText(oldCategory.getName());
        etColor.setText(oldCategory.getColor());
        btnPick.setOnClickListener(v -> showColorPicker(etColor));

        new AlertDialog.Builder(this)
                .setTitle("Chỉnh sửa nhóm")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newName = etName.getText().toString().trim();
                    String newColor = etColor.getText().toString().trim();
                    if (!newName.isEmpty() && newColor.startsWith("#")) {
                        oldCategory.setName(newName);
                        oldCategory.setColor(newColor);
                        repo.update(oldCategory);
                        refreshList();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ------------------ POPUP XÁC NHẬN XÓA ------------------
    private void confirmDelete(Category c) {
        // Kiểm tra nhóm còn task hay không
        TaskRepository taskRepo = new TaskRepository(this);
        if (taskRepo.getByCategory(c.getId()).size() > 0) {
            Toast.makeText(this, "Nhóm này còn task, không thể xóa!", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Xóa nhóm?")
                .setMessage("Bạn có chắc muốn xóa '" + c.getName() + "' không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    repo.delete(c.getId());
                    refreshList();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ------------------ BẢNG MÀU ------------------
    private void showColorPicker(EditText etColor) {
        // Danh sách màu mặc định
        String[] colors = {"#FF5733", "#33C1FF", "#9D33FF", "#33FF99", "#FFD700", "#FF66B2"};

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 24, 24, 24);

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(3);
        grid.setRowCount(2);
        grid.setPadding(0, 0, 0, 16);

        for (String color : colors) {
            View colorView = new View(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 150;
            params.height = 150;
            params.setMargins(16, 16, 16, 16);
            colorView.setLayoutParams(params);
            colorView.setBackgroundColor(android.graphics.Color.parseColor(color));
            colorView.setClickable(true);
            colorView.setOnClickListener(v -> {
                etColor.setText(color);
                Toast.makeText(this, "Đã chọn màu " + color, Toast.LENGTH_SHORT).show();
            });
            grid.addView(colorView);
        }

        Button btnAddColor = new Button(this);
        btnAddColor.setText("+ Thêm màu mới");
        btnAddColor.setAllCaps(false);
        btnAddColor.setBackgroundTintList(getColorStateList(R.color.teal_200));
        btnAddColor.setOnClickListener(v -> showColorChooser(etColor));

        layout.addView(grid);
        layout.addView(btnAddColor);

        new AlertDialog.Builder(this)
                .setTitle("Chọn màu nhóm")
                .setView(layout)
                .setNegativeButton("Đóng", null)
                .show();
    }

    private void showColorChooser(EditText etColor) {
        // Layout tổng
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 24, 24, 24);

        // Preview màu
        final View colorPreview = new View(this);
        LinearLayout.LayoutParams previewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 200);
        previewParams.setMargins(0, 0, 0, 24);
        colorPreview.setLayoutParams(previewParams);
        colorPreview.setBackgroundColor(android.graphics.Color.RED);
        layout.addView(colorPreview);

        // SeekBars cho Hue, Saturation, Value
        final SeekBar hueBar = new SeekBar(this);
        final SeekBar satBar = new SeekBar(this);
        final SeekBar valBar = new SeekBar(this);
        hueBar.setMax(360);
        satBar.setMax(100);
        valBar.setMax(100);

        layout.addView(createLabeledSeekBar("Hue", hueBar));
        layout.addView(createLabeledSeekBar("Saturation", satBar));
        layout.addView(createLabeledSeekBar("Value", valBar));

        // Theo dõi thay đổi
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float h = hueBar.getProgress();
                float s = satBar.getProgress() / 100f;
                float v = valBar.getProgress() / 100f;
                int color = android.graphics.Color.HSVToColor(new float[]{h, s, v});
                colorPreview.setBackgroundColor(color);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        hueBar.setOnSeekBarChangeListener(listener);
        satBar.setOnSeekBarChangeListener(listener);
        valBar.setOnSeekBarChangeListener(listener);

        // Hiển thị dialog
        new AlertDialog.Builder(this)
                .setTitle("Chọn màu tùy chỉnh")
                .setView(layout)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    float[] hsv = {
                            hueBar.getProgress(),
                            satBar.getProgress() / 100f,
                            valBar.getProgress() / 100f
                    };
                    int color = android.graphics.Color.HSVToColor(hsv);
                    String hex = String.format("#%06X", (0xFFFFFF & color));
                    etColor.setText(hex);
                    Toast.makeText(this, "Đã thêm màu: " + hex, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private LinearLayout createLabeledSeekBar(String label, SeekBar seekBar) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(this);
        tv.setText(label);
        tv.setPadding(0, 0, 0, 8);
        container.addView(tv);
        container.addView(seekBar);
        container.setPadding(0, 8, 0, 8);
        return container;
    }

    private void showAddColorDialog(EditText etColor) {
        final EditText input = new EditText(this);
        input.setHint("#RRGGBB");
        input.setText("#");

        new AlertDialog.Builder(this)
                .setTitle("Nhập mã màu mới")
                .setView(input)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newColor = input.getText().toString().trim();
                    try {
                        // Kiểm tra hợp lệ và gán
                        android.graphics.Color.parseColor(newColor);
                        etColor.setText(newColor);
                        Toast.makeText(this, "Đã thêm màu " + newColor, Toast.LENGTH_SHORT).show();
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(this, "Mã màu không hợp lệ!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void refreshList() {
        list.clear();
        list.addAll(repo.getAll());
        adapter.notifyDataSetChanged();
    }

}
