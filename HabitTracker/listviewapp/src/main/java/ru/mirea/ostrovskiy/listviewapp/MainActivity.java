package ru.mirea.ostrovskiy.listviewapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list_view);

        List<String> bookList = generateBookList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                bookList
        );

        listView.setAdapter(adapter);
    }

    private List<String> generateBookList() {
        List<String> list = new ArrayList<>();
        list.add("Айзек Азимов - Академия");
        list.add("Фрэнк Герберт - Дюна");
        list.add("Джордж Оруэлл - 1984");
        list.add("Рэй Брэдбери - 451 градус по Фаренгейту");
        list.add("Артур Кларк - Космическая одиссея 2001");
        list.add("Уильям Гибсон - Нейромант");
        list.add("Филип Дик - Мечтают ли андроиды об электроовцах?");
        list.add("Станислав Лем - Солярис");
        list.add("братья Стругацкие - Пикник на обочине");
        list.add("Дэн Симмонс - Гиперион");
        list.add("Роберт Хайнлайн - Звёздный десант");
        list.add("Гарри Гаррисон - Неукротимая планета");
        list.add("Клиффорд Саймак - Город");
        list.add("Андрэ Нортон - Саргассы в космосе");
        list.add("Сергей Лукьяненко - Ночной Дозор");
        list.add("Джон Уиндэм - День триффидов");
        list.add("Ричард Матесон - Я — легенда");
        list.add("Олдос Хаксли - О дивный новый мир");
        list.add("Евгений Замятин - Мы");
        list.add("Нил Стивенсон - Лавина");
        list.add("Питер Уоттс - Ложная слепота");
        list.add("Чайна Мьевиль - Вокзал потерянных снов");
        list.add("Иэн Бэнкс - Вспомни о Флебе");
        list.add("Альфред Бестер - Тигр! Тигр!");
        list.add("Джон Скальци - Люди в красном");
        list.add("Энди Вейер - Марсианин");
        list.add("Лю Цысинь - Задача трёх тел");
        list.add("Джеймс Кори - Пространство");
        list.add("Эрнест Клайн - Первому игроку приготовиться");
        list.add("Кадзуо Исигуро - Не отпускай меня");
        list.add("Дуглас Адамс - Автостопом по галактике");
        list.add("Урсула Ле Гуин - Левая рука Тьмы");
        list.add("Роджер Желязны - Хроники Амбера");
        return list;
    }
}