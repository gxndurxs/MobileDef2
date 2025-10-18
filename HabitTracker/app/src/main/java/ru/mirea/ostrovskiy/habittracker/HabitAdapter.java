package ru.mirea.ostrovskiy.habittracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;

// Адаптер должен наследоваться от RecyclerView.Adapter
public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    // Список данных, которые мы будем отображать
    private List<Habit> habits = new ArrayList<>();

    // Метод для обновления данных в адаптере
    public void setHabits(List<Habit> habits) {
        this.habits = habits;
        notifyDataSetChanged(); // Сообщаем RecyclerView, что данные изменились и нужно перерисовать список
    }

    // Этот метод создает новый объект ViewHolder (новую строку списка), когда RecyclerView в этом нуждается.
    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создаем View из нашего XML-файла habit_item.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_item, parent, false);
        return new HabitViewHolder(view);
    }

    // Этот метод берет данные из объекта Habit по определенной позиции и кладет их в ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.textViewHabitName.setText(habit.getName());
    }

    // Этот метод просто возвращает общее количество элементов в списке.
    @Override
    public int getItemCount() {
        return habits.size();
    }

    // ViewHolder — это класс-обертка для одного элемента списка (для одной строки).
    // Он хранит в себе ссылки на View (например, на наш TextView) внутри этой строки.
    static class HabitViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewHabitName;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            // Находим наш TextView по ID внутри habit_item.xml
            textViewHabitName = itemView.findViewById(R.id.textViewHabitName);
        }
    }
}