package eugen.enterprise.easyfit.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.enums.EMuscleGroup;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutDuration;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutExtras;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutSplit;
import eugen.enterprise.easyfit.acquaintance.helpers.Workout;
import eugen.enterprise.easyfit.view.activities.MainActivity;
import eugen.enterprise.easyfit.viewmodel.PlanViewModel;

public class PlanFragment extends Fragment {

    private PlanViewModel planViewModel;
    private Button btn_splitFullBody, btn_split2Split, btn_split3Split, btn_duration1, btn_duration2, btn_duration3,
            btn_muscleGroupChest, btn_muscleGroupLegs, btn_muscleGroupShoulders, btn_muscleGroupBiceps,
            btn_muscleGroupTriceps, btn_muscleGroupBack, btn_generateWorkout, btn_extrasAbs, btn_extrasRunning,
            btn_extrasBiking, btn_extrasStairs, btn_extrasRowing;
    private EWorkoutSplit selectedSplit;
    private EWorkoutDuration selectedDuration;
    private EWorkoutExtras selectedExtras;
    private int muscleGroupAmountSelected, muscleGroupAmountMax;
    private List<EMuscleGroup> selectedMuscleGroups;
    private HashMap<EMuscleGroup, Boolean> toggledMuscleGroups;
    private HashMap<EMuscleGroup, Button> muscleGroupButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_plan, container, false);

        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);

        btn_generateWorkout = root.findViewById(R.id.btn_generateWorkout);

        selectedMuscleGroups = new ArrayList<>();

        btn_splitFullBody = root.findViewById(R.id.btn_splitFullBody);
        btn_split2Split = root.findViewById(R.id.btn_split2Split);
        btn_split3Split = root.findViewById(R.id.btn_split3Split);

        btn_duration1 = root.findViewById(R.id.btn_duration1);
        btn_duration2 = root.findViewById(R.id.btn_duration2);
        btn_duration3 = root.findViewById(R.id.btn_duration3);

        btn_muscleGroupChest = root.findViewById(R.id.btn_muscleGroupChest);
        btn_muscleGroupLegs = root.findViewById(R.id.btn_muscleGroupLegs);
        btn_muscleGroupShoulders = root.findViewById(R.id.btn_muscleGroupShoulders);
        btn_muscleGroupBiceps = root.findViewById(R.id.btn_muscleGroupBiceps);
        btn_muscleGroupTriceps = root.findViewById(R.id.btn_muscleGroupTriceps);
        btn_muscleGroupBack = root.findViewById(R.id.btn_muscleGroupBack);

        toggledMuscleGroups = new HashMap<>();
        toggledMuscleGroups.put(EMuscleGroup.Chest, false);
        toggledMuscleGroups.put(EMuscleGroup.Legs, false);
        toggledMuscleGroups.put(EMuscleGroup.Shoulders, false);
        toggledMuscleGroups.put(EMuscleGroup.Biceps, false);
        toggledMuscleGroups.put(EMuscleGroup.Triceps, false);
        toggledMuscleGroups.put(EMuscleGroup.Back, false);

        muscleGroupButton = new HashMap<>();
        muscleGroupButton.put(EMuscleGroup.Chest, btn_muscleGroupChest);
        muscleGroupButton.put(EMuscleGroup.Legs, btn_muscleGroupLegs);
        muscleGroupButton.put(EMuscleGroup.Shoulders, btn_muscleGroupShoulders);
        muscleGroupButton.put(EMuscleGroup.Biceps, btn_muscleGroupBiceps);
        muscleGroupButton.put(EMuscleGroup.Triceps, btn_muscleGroupTriceps);
        muscleGroupButton.put(EMuscleGroup.Back, btn_muscleGroupBack);

        btn_extrasAbs = root.findViewById(R.id.btn_extrasAbs);
        btn_extrasRunning = root.findViewById(R.id.btn_extrasRunning);
        btn_extrasBiking = root.findViewById(R.id.btn_extrasBiking);
        btn_extrasStairs = root.findViewById(R.id.btn_extrasStairs);
        btn_extrasRowing = root.findViewById(R.id.btn_extrasRowing);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_splitFullBody.setOnClickListener(v -> {
            muscleGroupAmountMax = 6;
            selectedSplit = EWorkoutSplit.FullBody;
            selectButton(btn_splitFullBody, "split");
            toggleAllMuscleGroups(true);
        });
        btn_split2Split.setOnClickListener(v -> {
            muscleGroupAmountMax = 3;
            selectedSplit = EWorkoutSplit.TwoSplit;
            selectButton(btn_split2Split, "split");
            toggleAllMuscleGroups(false);
        });
        btn_split3Split.setOnClickListener(v -> {
            muscleGroupAmountMax = 2;
            selectedSplit = EWorkoutSplit.ThreeSplit;
            selectButton(btn_split3Split, "split");
            toggleAllMuscleGroups(false);
        });

        btn_duration1.setOnClickListener(v -> {
            selectedDuration = EWorkoutDuration.OneHour;
            selectButton(btn_duration1, "duration");
        });
        btn_duration2.setOnClickListener(v -> {
            selectedDuration = EWorkoutDuration.OneHalfHour;
            selectButton(btn_duration2, "duration");
        });
        btn_duration3.setOnClickListener(v -> {
            selectedDuration = EWorkoutDuration.TwoHours;
            selectButton(btn_duration3, "duration");
        });

        btn_muscleGroupChest.setOnClickListener(v -> {
            toggleMuscleGroup(EMuscleGroup.Chest, toggledMuscleGroups.get(EMuscleGroup.Chest));
        });
        btn_muscleGroupLegs.setOnClickListener(v -> {
            toggleMuscleGroup(EMuscleGroup.Legs, toggledMuscleGroups.get(EMuscleGroup.Legs));
        });
        btn_muscleGroupShoulders.setOnClickListener(v -> {
            toggleMuscleGroup(EMuscleGroup.Shoulders, toggledMuscleGroups.get(EMuscleGroup.Shoulders));
        });
        btn_muscleGroupBiceps.setOnClickListener(v -> {
            toggleMuscleGroup(EMuscleGroup.Biceps, toggledMuscleGroups.get(EMuscleGroup.Biceps));
        });
        btn_muscleGroupTriceps.setOnClickListener(v -> {
            toggleMuscleGroup(EMuscleGroup.Triceps, toggledMuscleGroups.get(EMuscleGroup.Triceps));
        });
        btn_muscleGroupBack.setOnClickListener(v -> {
            toggleMuscleGroup(EMuscleGroup.Back, toggledMuscleGroups.get(EMuscleGroup.Back));
        });

        btn_extrasAbs.setOnClickListener(v -> {
            selectedExtras = EWorkoutExtras.Abs;
            selectButton(btn_extrasAbs, "extras");
        });
        btn_extrasRunning.setOnClickListener(v -> {
            selectedExtras = EWorkoutExtras.Running;
            selectButton(btn_extrasRunning, "extras");
        });
        btn_extrasBiking.setOnClickListener(v -> {
            selectedExtras = EWorkoutExtras.Biking;
            selectButton(btn_extrasBiking, "extras");
        });
        btn_extrasStairs.setOnClickListener(v -> {
            selectedExtras = EWorkoutExtras.Stairs;
            selectButton(btn_extrasStairs, "extras");
        });
        btn_extrasRowing.setOnClickListener(v -> {
            selectedExtras = EWorkoutExtras.Rowing;
            selectButton(btn_extrasRowing, "extras");
        });

        btn_generateWorkout.setOnClickListener(v -> {
            if (selectedSplit == null) {
                Toast.makeText(getContext(), "Select workout split", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedDuration == null) {
                Toast.makeText(getContext(), "Select workout duration", Toast.LENGTH_SHORT).show();
                return;
            }
            if (muscleGroupAmountMax == 6) {
                if (muscleGroupAmountSelected < 5) {
                    Toast.makeText(getContext(), "Select at least 5 muscle groups", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                if (muscleGroupAmountSelected != muscleGroupAmountMax) {
                    Toast.makeText(getContext(), "Select " + muscleGroupAmountMax + " muscle groups", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            planViewModel.createWorkout(selectedSplit, selectedDuration, selectedMuscleGroups, selectedExtras, getContext());
        });

        planViewModel.getWorkout().observe(requireActivity(), new Observer<Workout>() {
            @Override
            public void onChanged(Workout workout) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("workout", workout);
                ((MainActivity)getActivity()).swapTab(getView(), R.id.navigation_workout, bundle);
            }
        });
    }

    private void selectButton(Button button, String group) {
        switch (group) {
            case "split":
                btn_splitFullBody.setBackgroundResource(R.drawable.btn_border);
                btn_splitFullBody.setAlpha(0.5f);
                btn_split2Split.setBackgroundResource(R.drawable.btn_border);
                btn_split2Split.setAlpha(0.5f);
                btn_split3Split.setBackgroundResource(R.drawable.btn_border);
                btn_split3Split.setAlpha(0.5f);
                break;
            case "duration":
                btn_duration1.setBackgroundResource(R.drawable.btn_border);
                btn_duration1.setAlpha(0.5f);
                btn_duration2.setBackgroundResource(R.drawable.btn_border);
                btn_duration2.setAlpha(0.5f);
                btn_duration3.setBackgroundResource(R.drawable.btn_border);
                btn_duration3.setAlpha(0.5f);
                break;
            case "extras":
                btn_extrasAbs.setBackgroundResource(R.drawable.btn_border);
                btn_extrasAbs.setAlpha(0.5f);
                btn_extrasRunning.setBackgroundResource(R.drawable.btn_border);
                btn_extrasRunning.setAlpha(0.5f);
                btn_extrasBiking.setBackgroundResource(R.drawable.btn_border);
                btn_extrasBiking.setAlpha(0.5f);
                btn_extrasStairs.setBackgroundResource(R.drawable.btn_border);
                btn_extrasStairs.setAlpha(0.5f);
                btn_extrasRowing.setBackgroundResource(R.drawable.btn_border);
                btn_extrasRowing.setAlpha(0.5f);
                break;
        }
        button.setBackgroundResource(R.drawable.btn_border_selected);
        button.setAlpha(1);
    }

    private void toggleAllMuscleGroups(boolean selected) {
        updateMuscleGroupAlpha(true);
        if (selected) {
            muscleGroupAmountSelected = 0;
            toggleMuscleGroup(EMuscleGroup.Chest, false);
            toggleMuscleGroup(EMuscleGroup.Legs, false);
            toggleMuscleGroup(EMuscleGroup.Shoulders, false);
            toggleMuscleGroup(EMuscleGroup.Biceps, false);
            toggleMuscleGroup(EMuscleGroup.Triceps, false);
            toggleMuscleGroup(EMuscleGroup.Back, false);
        } else {
            toggleMuscleGroup(EMuscleGroup.Chest, true);
            toggleMuscleGroup(EMuscleGroup.Legs, true);
            toggleMuscleGroup(EMuscleGroup.Shoulders, true);
            toggleMuscleGroup(EMuscleGroup.Biceps, true);
            toggleMuscleGroup(EMuscleGroup.Triceps, true);
            toggleMuscleGroup(EMuscleGroup.Back, true);
            muscleGroupAmountSelected = 0;
        }
    }

    private void toggleMuscleGroup(EMuscleGroup muscleGroup, boolean toggled) {
        Button button = muscleGroupButton.get(muscleGroup);
        if (toggled) {
            button.setBackgroundResource(R.drawable.btn_border);
            selectedMuscleGroups.remove(muscleGroup);
            muscleGroupAmountSelected--;
            toggledMuscleGroups.replace(muscleGroup, false);
        } else {
            if (muscleGroupAmountSelected < muscleGroupAmountMax) {
                button.setBackgroundResource(R.drawable.btn_border_selected);
                selectedMuscleGroups.add(muscleGroup);
                muscleGroupAmountSelected++;
                toggledMuscleGroups.replace(muscleGroup, true);
            }
        }

        if (muscleGroupAmountSelected == muscleGroupAmountMax) {
            updateMuscleGroupAlpha(false);
        } else {
            updateMuscleGroupAlpha(true);
        }
    }

    private void updateMuscleGroupAlpha(Boolean allVisible) {
        for (Map.Entry pair : toggledMuscleGroups.entrySet()) {
            Button muscleGroup = muscleGroupButton.get(pair.getKey());
            if (allVisible) {
                muscleGroup.setAlpha(1);
            } else {
                if ((Boolean) pair.getValue() == false) {
                    muscleGroup.setAlpha(0.5f);
                } else {
                    muscleGroup.setAlpha(1);
                }
            }
        }
    }
}