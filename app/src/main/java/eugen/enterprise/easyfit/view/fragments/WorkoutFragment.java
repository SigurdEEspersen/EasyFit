package eugen.enterprise.easyfit.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.helpers.Common;
import eugen.enterprise.easyfit.acquaintance.helpers.Workout;
import eugen.enterprise.easyfit.acquaintance.interfaces.IMuscleGroup;
import eugen.enterprise.easyfit.view.activities.MainActivity;
import eugen.enterprise.easyfit.view.adapters.WorkoutAdapter;
import eugen.enterprise.easyfit.viewmodel.WorkoutViewModel;

public class WorkoutFragment extends Fragment {

    private Button btn_planWorkout;
    private ListView workoutList;
    private WorkoutViewModel workoutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_workout, container, false);

        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

        btn_planWorkout = root.findViewById(R.id.btn_planWorkout);
        workoutList = root.findViewById(R.id.workout_listview);

        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_planWorkout.setOnClickListener(v -> {
            ((MainActivity) getActivity()).swapTab(getView(), R.id.navigation_plan, null);
        });

        Bundle b = this.getArguments();
        if (b != null) {
            btn_planWorkout.setVisibility(View.GONE);
            Workout workout = (Workout) b.getSerializable("workout");
            WorkoutAdapter adapter = new WorkoutAdapter(requireContext(), R.layout.workout_card);
            adapter.injectData(requireContext(), getActivity(), workout, this);
            for (IMuscleGroup muscleGroup : workout.getMuscleGroups()) {
                adapter.add(muscleGroup);
            }
            workoutList.setAdapter(adapter);
        }

        workoutList.setOnTouchListener((v, event) -> {
            Common.hideKeyboard(getContext(), getView(), getActivity());
            return false;
        });

        workoutViewModel.getAddedSetResult().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success) {
                    Toast.makeText(getContext(), "Results saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Problem saving results. Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}