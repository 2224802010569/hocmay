package com.example.todotask.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todotask.R;
import com.example.todotask.viewmodel.LoginViewModel;
/*//import finger
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricManager;*/
//import androidx.core.content.ContextCompat;
//import android.widget.ImageButton;
//import com.example.todotask.data.repository.UserRepository;
//import com.example.todotask.data.model.User;


public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        loginViewModel = new LoginViewModel(this);

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        TextView tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> {




            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = loginViewModel.login(email, password);
            if (success) {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                // Lấy user hiện tại từ DB
                com.example.todotask.data.model.User currentUser =
                        new com.example.todotask.data.repository.UserRepository(this)
                                .login(email, password);

                //  Lưu user vào SharedPreferences
                getSharedPreferences("user_prefs", MODE_PRIVATE)
                        .edit()
                        .putString("user_name", currentUser.getName())
                        .putString("user_email", currentUser.getGmail())
                        .apply();

                // Chuyển sang MainActivity
                Intent intent = new Intent(getApplicationContext(), com.example.todotask.ui.main.MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
            startActivity(intent);
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });
    }


}
