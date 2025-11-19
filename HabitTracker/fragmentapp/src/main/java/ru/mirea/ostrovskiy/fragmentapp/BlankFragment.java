package ru.mirea.ostrovskiy.fragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BlankFragment extends Fragment {

    public BlankFragment() {
        super(R.layout.fragment_blank);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = view.findViewById(R.id.text_view_student_number);

        if (getArguments() != null) {
            int studentNumber = getArguments().getInt("student_number_key");
            textView.setText("Мой номер по списку: " + studentNumber);
        }
    }
}