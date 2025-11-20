package com.example.todotask.ui.main;
import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;
import android.content.Intent;
import com.example.todotask.ui.category.CategoryActivity;
import com.example.todotask.ui.task.TaskListActivity;
import com.example.todotask.ui.user.UserInfoActivity;
import com.example.todotask.ui.task.PublisherActivity;
public class MenuHelper {

    public static void showMainMenu(Context context, View anchor) {
        PopupMenu popup = new PopupMenu(context, anchor);
        popup.getMenu().add("Account");
        popup.getMenu().add("Into");
        popup.getMenu().add("Nhóm");
        popup.getMenu().add("List");

        popup.setOnMenuItemClickListener(item -> {
            Intent intent = null;
            switch (item.getTitle().toString()) {
                case "Account":
                    intent = new Intent(context, UserInfoActivity.class);
                    break;
                case "Into":
                    intent = new Intent(context, PublisherActivity.class);
                    break;
                case "Nhóm":
                    intent = new Intent(context, CategoryActivity.class);
                    break;
                case "List":
                    intent = new Intent(context, TaskListActivity.class);
                    break;
            }
            if (intent != null) context.startActivity(intent);
            return true;
        });
        popup.show();
    }
}
