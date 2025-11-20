package com.example.todotask.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupMenu;
import com.example.todotask.ui.main.MenuHelper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todotask.R;
import com.example.todotask.ui.category.CategoryActivity;
import com.example.todotask.ui.user.UserInfoActivity;
import android.widget.ImageButton;
public class PublisherActivity extends AppCompatActivity {

    private ImageButton btnFilter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher);
        btnFilter = findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(v -> MenuHelper.showMainMenu(this, v));
    }

}
