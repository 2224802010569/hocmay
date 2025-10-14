package com.example.todotask.ui.splash;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todotask.R;
import com.example.todotask.ui.login.LoginActivity;
import com.example.todotask.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String email = prefs.getString("user_email", null);

            Intent intent;
            if (email != null) {
                // Đã đăng nhập trước đó → vào Main
                intent = new Intent(getApplicationContext(), MainActivity.class);
            } else {
                // Chưa đăng nhập → vào Login
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);

    }
}
