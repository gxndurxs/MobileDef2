package ru.mirea.ostrovskiy.fragmentmanagerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class DetailsFragment extends Fragment {
    private SharedViewModel viewModel;
    public DetailsFragment() { super(R.layout.fragment_details); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView detailsText = view.findViewById(R.id.details_text);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getSelected().observe(getViewLifecycleOwner(), detailsText::setText);
    }
}