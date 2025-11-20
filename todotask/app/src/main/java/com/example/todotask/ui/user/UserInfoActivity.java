package com.example.todotask.ui.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todotask.R;
import com.example.todotask.ui.category.CategoryActivity;
import com.example.todotask.ui.login.LoginActivity;
import com.example.todotask.ui.task.PublisherActivity;
import android.widget.ImageButton;
import com.example.todotask.ui.main.MenuHelper;

public class UserInfoActivity extends AppCompatActivity {

    private ImageButton btnFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);

        TextView tvUsername = findViewById(R.id.tvUsername);
        TextView tvEmail = findViewById(R.id.tvEmail);
        Button btnLogout = findViewById(R.id.btnLogout);
        btnFilter = findViewById(R.id.btnFilter);
        // 🔹 Lấy thông tin từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = prefs.getString("user_name", "(Không có dữ liệu)");
        String email = prefs.getString("user_email", "(Không có dữ liệu)");

        tvUsername.setText("Tên đăng nhập: " + name);
        tvEmail.setText("Email: " + email);

        // 🔹 Đăng xuất: xóa prefs và quay lại Login
        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });
        btnFilter.setOnClickListener(v -> MenuHelper.showMainMenu(this, v));

    }

}
