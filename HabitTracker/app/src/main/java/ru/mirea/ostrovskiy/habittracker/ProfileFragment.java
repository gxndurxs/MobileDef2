package ru.mirea.ostrovskiy.habittracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView userEmailTextView = view.findViewById(R.id.text_view_user_email);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("USER_NAME", "Не удалось загрузить email");

        userEmailTextView.setText("Email пользователя: " + userEmail);
    }
}