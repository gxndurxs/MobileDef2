package ru.mirea.ostrovskiy.scrollviewapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearContainer = findViewById(R.id.linear_container);

        LayoutInflater inflater = getLayoutInflater();

        for (int i = 0; i < 100; i++) {

            long value = (long) Math.pow(2, i);

            TextView textView = new TextView(this);
            textView.setText("Элемент " + (i + 1) + ": " + value);
            textView.setPadding(0, 8, 0, 8);
            textView.setTextSize(18);

            linearContainer.addView(textView);
        }
    }
}