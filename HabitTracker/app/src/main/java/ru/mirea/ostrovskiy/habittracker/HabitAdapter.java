package ru.mirea.ostrovskiy.habittracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar; // <-- НОВЫЙ ИМПОРТ
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private List<Habit> habits = new ArrayList<>();

    // --- НАЧАЛО: Добавляем обработчик кликов ---
    // Это стандартный способ сделать элементы списка кликабельными.
    public interface OnHabitClickListener {
        void onHabitClick(Habit habit);
    }

    private OnHabitClickListener onHabitClickListener;

    public void setOnHabitClickListener(OnHabitClickListener listener) {
        this.onHabitClickListener = listener;
    }
    // --- КОНЕЦ: Добавляем обработчик кликов ---


    public void setHabits(List<Habit> habits) {
        this.habits = habits;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_item, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.textViewHabitName.setText(habit.getName());
        holder.textViewDeadline.setText(habit.getDeadline()); // <-- Устанавливаем дедлайн
        holder.progressBarHabit.setProgress(habit.getProgress()); // <-- Устанавливаем прогресс

        // --- Устанавливаем слушатель клика на весь элемент списка ---
        holder.itemView.setOnClickListener(v -> {
            if (onHabitClickListener != null) {
                onHabitClickListener.onHabitClick(habit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewHabitName;
        private final TextView textViewDeadline; // <-- НОВОЕ ПОЛЕ
        private final ProgressBar progressBarHabit; // <-- НОВОЕ ПОЛЕ

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHabitName = itemView.findViewById(R.id.textViewHabitName);
            textViewDeadline = itemView.findViewById(R.id.textViewDeadline); // <-- Находим по ID
            progressBarHabit = itemView.findViewById(R.id.progressBarHabit); // <-- Находим по ID
        }
    }
}