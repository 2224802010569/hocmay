package com.example.todotask.ui.category;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todotask.R;
import com.example.todotask.data.model.Category;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<Category> list;
    private final OnCategoryLongClick listener;

    public interface OnCategoryLongClick {
        void onLongClick(Category category);
    }

    public CategoryAdapter(List<Category> list, OnCategoryLongClick listener) {
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategoryItem);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category c = list.get(position);
        holder.tvName.setText(c.getName());
        holder.tvName.setBackgroundColor(Color.parseColor(c.getColor()));

        holder.tvName.setOnLongClickListener(v -> {
            listener.onLongClick(c);
            return true;
        });
    }

    @Override
    public int getItemCount() { return list.size(); }
}
