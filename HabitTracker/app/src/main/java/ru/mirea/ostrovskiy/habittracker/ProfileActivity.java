package ru.mirea.ostrovskiy.habittracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import ru.mirea.ostrovskiy.habittracker.data.repository.HabitRepositoryImpl;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;

public class ProfileActivity extends AppCompatActivity {

    private HabitRepository habitRepository;
    private TextInputEditText firstNameEditText;
    private TextInputEditText lastNameEditText;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        habitRepository = new HabitRepositoryImpl(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Профиль");
        }

        firstNameEditText = findViewById(R.id.edit_text_first_name);
        lastNameEditText = findViewById(R.id.edit_text_last_name);
        emailTextView = findViewById(R.id.text_view_email_profile);
        Button saveButton = findViewById(R.id.button_save_profile);

        loadUserProfile();

        saveButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            habitRepository.saveUserProfile(firstName, lastName);
            Toast.makeText(this, "Профиль сохранен", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadUserProfile() {
        String[] userProfile = habitRepository.getUserProfile();
        emailTextView.setText(userProfile[0]);
        firstNameEditText.setText(userProfile[1]);
        lastNameEditText.setText(userProfile[2]);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}