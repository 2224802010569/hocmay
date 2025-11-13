package com.example.todotask.ui.task;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todotask.R;
import com.example.todotask.data.model.Task;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.VH> {

    private final List<Task> list;
    private final OnItemClick listener;

    public interface OnItemClick {
        void onEdit(Task task);
        void onDelete(Task task);
        void onToggleComplete(Task task);
    }

    public TaskAdapter(List<Task> list, OnItemClick listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Task t = list.get(position);
        holder.tvTitle.setText(t.getTitle());
        holder.tvDescription.setText(t.getDescription());
        String time = (t.getStartTime() != null ? t.getStartTime() : "") +
                (t.getEndTime() != null ? " → " + t.getEndTime() : "");
        holder.tvTime.setText("Hạn: " + time);

        // Trạng thái (dấu tròn xanh hoặc đỏ)
        holder.tvStatus.setText(t.isCompleted() ? "●" : "○");
        holder.tvStatus.setTextColor(t.isCompleted() ? Color.GREEN : Color.RED);

       /* // Hiển thị màu nhóm (tạm thời đặt cố định, có thể đổi bằng categoryId sau)
        holder.colorIndicator.setBackgroundColor(Color.parseColor("#FFD700"));*/

        if (t.getCategoryColor() != null && !t.getCategoryColor().isEmpty()) {
            try {
                holder.colorIndicator.setBackgroundColor(Color.parseColor(t.getCategoryColor()));
            } catch (IllegalArgumentException e) {
                holder.colorIndicator.setBackgroundColor(Color.LTGRAY); // màu mặc định nếu sai
            }
        } else {
            holder.colorIndicator.setBackgroundColor(Color.LTGRAY);
        }


        // Khi click vào item → hiển thị menu Sửa / Xóa
        holder.itemView.setOnClickListener(v -> {
            CharSequence[] options = {"Sửa", "Xóa"};
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Chọn thao tác cho task");
            builder.setItems(options, (dialog, which) -> {
                if (which == 0) {
                    listener.onEdit(t);
                } else if (which == 1) {
                    listener.onDelete(t);
                }
            });
            builder.show();
        });

        // Khi nhấn vào vòng tròn trạng thái thì toggle complete
        holder.tvStatus.setOnClickListener(v -> listener.onToggleComplete(t));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        View colorIndicator;
        TextView tvTitle, tvDescription, tvTime, tvStatus;

        VH(@NonNull View itemView) {
            super(itemView);
            colorIndicator = itemView.findViewById(R.id.colorIndicator);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
