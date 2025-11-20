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
import androidx.core.content.ContextCompat;
import android.widget.ImageButton;
import com.example.todotask.data.repository.UserRepository;
import com.example.todotask.data.model.User;


public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
/*    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;*/
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

            ImageButton btnFingerprint = findViewById(R.id.btnFingerprint);

           /* // Kiểm tra thiết bị có hỗ trợ vân tay
            BiometricManager biometricManager = BiometricManager.from(this);
            if (biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS) {
                btnFingerprint.setEnabled(false);
            }
            // Khi ấn nút vân tay
            btnFingerprint.setOnClickListener(view -> showFingerprintDialog());*/



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

                // 👉 Lưu user vào SharedPreferences
                getSharedPreferences("user_prefs", MODE_PRIVATE)
                        .edit()
                        .putString("user_name", currentUser.getName())
                        .putString("user_email", currentUser.getGmail())
                        .apply();
                //finger pass
                getSharedPreferences("user_prefs", MODE_PRIVATE)
                        .edit()
                        .putString("user_name", currentUser.getName())
                        .putString("user_email", currentUser.getGmail())
                        .putString("user_password", password)   // <-- thêm dòng này
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
    /*private void showFingerprintDialog() {
        BiometricPrompt biometricPrompt = new BiometricPrompt(
                LoginActivity.this,
                getMainExecutor(),
                new BiometricPrompt.AuthenticationCallback() {

                    @Override
                    public void onAuthenticationSucceeded(
                            BiometricPrompt.AuthenticationResult result) {

                        super.onAuthenticationSucceeded(result);

                        // Lấy email đã lưu từ SharedPreferences
                        String email = getSharedPreferences("user_prefs", MODE_PRIVATE)
                                .getString("user_email", null);

                        if (email == null) {
                            Toast.makeText(LoginActivity.this,
                                    "Chưa từng đăng nhập – không thể dùng vân tay!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Lấy mật khẩu đã lưu
                        String password = getSharedPreferences("user_prefs", MODE_PRIVATE)
                                .getString("user_password", null);

                        if (password == null) {
                            Toast.makeText(LoginActivity.this,
                                    "Không tìm thấy mật khẩu. Hãy đăng nhập lại 1 lần.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Gọi login tự động
                        boolean success = loginViewModel.login(email, password);

                        if (success) {
                            UserRepository repo = new UserRepository(LoginActivity.this);
                            com.example.todotask.data.model.User user =
                                    repo.login(email, password);

                            // Lưu lại user
                            getSharedPreferences("user_prefs", MODE_PRIVATE)
                                    .edit()
                                    .putString("user_name", user.getName())
                                    .apply();

                            Intent intent = new Intent(LoginActivity.this,
                                    com.example.todotask.ui.main.MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Xác thực OK nhưng đăng nhập thất bại!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Đăng nhập bằng vân tay")
                .setDescription("Chạm cảm biến để đăng nhập")
                .setNegativeButtonText("Hủy")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }*/

}
