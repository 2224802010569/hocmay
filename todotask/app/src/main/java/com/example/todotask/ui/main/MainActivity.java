package com.example.todotask.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todotask.R;
import com.example.todotask.ui.user.UserInfoActivity;
import com.example.todotask.ui.category.CategoryActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btnUserInfo = findViewById(R.id.btnUserInfo);
        Button btnCategory = findViewById(R.id.btnCategory);

        btnUserInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
            startActivity(intent);
        });

        btnCategory.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            startActivity(intent);
        });
    }
}
