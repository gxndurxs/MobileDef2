package ru.mirea.ostrovskiy.resultapifragmentapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DataFragment extends Fragment {

    public DataFragment() {
        super(R.layout.fragment_data);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText editText = view.findViewById(R.id.edit_text_data);

        view.findViewById(R.id.button_open_bottom_sheet).setOnClickListener(v -> {
            String dataToSend = editText.getText().toString();

            Bundle result = new Bundle();
            result.putString("data_bundle_key", dataToSend);
            getParentFragmentManager().setFragmentResult("data_request_key", result);

            new BottomSheetResultFragment().show(getParentFragmentManager(), "BottomSheetDialog");
        });
    }
}