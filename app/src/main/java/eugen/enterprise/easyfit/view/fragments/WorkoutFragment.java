package eugen.enterprise.easyfit.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.viewmodel.WorkoutViewModel;

public class WorkoutFragment extends Fragment {

    private WorkoutViewModel workoutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        workoutViewModel =
                new ViewModelProvider(this).get(WorkoutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_workout, container, false);
        final TextView textView = root.findViewById(R.id.text_workout);
        workoutViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}