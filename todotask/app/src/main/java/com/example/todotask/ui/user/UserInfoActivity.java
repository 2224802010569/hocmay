package com.example.todotask.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todotask.R;
import com.example.todotask.ui.login.LoginActivity;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);

        TextView tvUsername = findViewById(R.id.tvUsername);
        TextView tvEmail = findViewById(R.id.tvEmail);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Lấy thông tin thật từ Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("user_name");
        String email = intent.getStringExtra("user_email");

        // Hiển thị thông tin thật
        if (name != null && email != null) {
            tvUsername.setText("Tên đăng nhập: " + name);
            tvEmail.setText("Email: " + email);
        } else {
            tvUsername.setText("Tên đăng nhập: (Không có dữ liệu)");
            tvEmail.setText("Email: (Không có dữ liệu)");
        }

        // Nút đăng xuất
        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });
    }
}
