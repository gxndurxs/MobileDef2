package ru.mirea.ostrovskiy.fragmentmanagerapp;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class ListFragment extends Fragment {
    private SharedViewModel viewModel;
    public ListFragment() { super(R.layout.fragment_list); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        view.findViewById(R.id.button_russia).setOnClickListener(v -> viewModel.select("Столица России - Москва"));
        view.findViewById(R.id.button_usa).setOnClickListener(v -> viewModel.select("Столица США - Вашингтон"));
        view.findViewById(R.id.button_china).setOnClickListener(v -> viewModel.select("Столица Китая - Пекин"));
    }
}