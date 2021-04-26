package eugen.enterprise.easyfit.view.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
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
import eugen.enterprise.easyfit.view.activities.MainActivity;
import eugen.enterprise.easyfit.viewmodel.PlanViewModel;

public class PlanFragment extends Fragment {

    private PlanViewModel planViewModel;
    private Button btn_splitFullBody, btn_split2Split, btn_split3Split, btn_duration1, btn_duration2, btn_duration3,
            btn_muscleGroupChest, btn_muscleGroupLegs, btn_muscleGroupShoulders, btn_muscleGroupBiceps,
            btn_muscleGroupTriceps, btn_muscleGroupBack, btn_generateWorkout, btn_extrasAbs, btn_extrasRunning,
            btn_extrasBiking, btn_extrasStairs, btn_extrasRowing;
    private int muscleGroupAmountSelected, muscleGroupAmountMax;
    private HashMap<EMuscleGroup, Boolean> toggledMuscleGroups;
    private HashMap<EMuscleGroup, Button> muscleGroupButton;
    private RadioButton btn_preworkout, btn_postworkout;
    private SeekBar extras_duration_seekbar;
    private TextView extras_duration_txt;
    private boolean newlyCreatedWorkout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_plan, container, false);

        planViewModel = new ViewModelProvider(requireActivity()).get(PlanViewModel.class);

        btn_generateWorkout = root.findViewById(R.id.btn_generateWorkout);

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

        btn_preworkout = root.findViewById(R.id.extras_preworkout);
        btn_postworkout = root.findViewById(R.id.extras_postworkout);
        extras_duration_seekbar = root.findViewById(R.id.extras_duration_seekbar);
        extras_duration_txt = root.findViewById(R.id.extras_duration_txt);
        btn_extrasAbs = root.findViewById(R.id.btn_extrasAbs);
        btn_extrasRunning = root.findViewById(R.id.btn_extrasRunning);
        btn_extrasBiking = root.findViewById(R.id.btn_extrasBiking);
        btn_extrasStairs = root.findViewById(R.id.btn_extrasStairs);
        btn_extrasRowing = root.findViewById(R.id.btn_extrasRowing);

        List<EMuscleGroup> selectedMuscleGroups = planViewModel.getSelectedMuscleGroups().getValue();
        if (selectedMuscleGroups == null) {
            planViewModel.setSelectedMuscleGroups(new ArrayList<>());
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_splitFullBody.setOnClickListener(v -> {
            muscleGroupAmountMax = 6;
            toggleAllMuscleGroups(true);
            planViewModel.setSelectedSplit(EWorkoutSplit.FullBody);
        });
        btn_split2Split.setOnClickListener(v -> {
            muscleGroupAmountMax = 3;
            toggleAllMuscleGroups(false);
            planViewModel.setSelectedSplit(EWorkoutSplit.TwoSplit);
        });
        btn_split3Split.setOnClickListener(v -> {
            muscleGroupAmountMax = 2;
            toggleAllMuscleGroups(false);
            planViewModel.setSelectedSplit(EWorkoutSplit.ThreeSplit);
        });

        btn_duration1.setOnClickListener(v -> {
            planViewModel.setSelectedDuration(EWorkoutDuration.OneHour);
        });
        btn_duration2.setOnClickListener(v -> {
            planViewModel.setSelectedDuration(EWorkoutDuration.OneHalfHour);
        });
        btn_duration3.setOnClickListener(v -> {
            planViewModel.setSelectedDuration(EWorkoutDuration.TwoHours);
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
            planViewModel.setSelectedExtras(EWorkoutExtras.Core);
        });
        btn_extrasRunning.setOnClickListener(v -> {
            planViewModel.setSelectedExtras(EWorkoutExtras.Running);
        });
        btn_extrasBiking.setOnClickListener(v -> {
            planViewModel.setSelectedExtras(EWorkoutExtras.Biking);
        });
        btn_extrasStairs.setOnClickListener(v -> {
            planViewModel.setSelectedExtras(EWorkoutExtras.Stairs);
        });
        btn_extrasRowing.setOnClickListener(v -> {
            planViewModel.setSelectedExtras(EWorkoutExtras.Rowing);
        });

        btn_generateWorkout.setOnClickListener(v -> {
            if (planViewModel.getSelectedSplit().getValue() == null) {
                Toast.makeText(getContext(), "Select workout split", Toast.LENGTH_SHORT).show();
                return;
            }
            if (planViewModel.getSelectedDuration().getValue() == null) {
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
            newlyCreatedWorkout = true;
            planViewModel.createWorkout(getContext());
        });

        planViewModel.getWorkout().observe(requireActivity(), workout -> {
            if (newlyCreatedWorkout) {
                ((MainActivity) getActivity()).swapTab(getView(), R.id.navigation_workout, null);
                newlyCreatedWorkout = false;
            }
        });

        btn_preworkout.setOnClickListener(v -> {
            planViewModel.setPreWorkout(true);
        });

        btn_postworkout.setOnClickListener(v -> {
            planViewModel.setPreWorkout(false);
        });

        extras_duration_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0:
                        planViewModel.setExtrasDuration(10);
                        break;
                    case 1:
                        planViewModel.setExtrasDuration(15);
                        break;
                    case 2:
                        planViewModel.setExtrasDuration(20);
                        break;
                    case 3:
                        planViewModel.setExtrasDuration(25);
                        break;
                    case 4:
                        planViewModel.setExtrasDuration(30);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        planViewModel.getSelectedSplit().observe(requireActivity(), new Observer<EWorkoutSplit>() {
            @Override
            public void onChanged(EWorkoutSplit eWorkoutSplit) {
                switch (eWorkoutSplit) {
                    case FullBody:
                        muscleGroupAmountMax = 6;
                        selectButton(btn_splitFullBody, "split");
                        break;
                    case TwoSplit:
                        muscleGroupAmountMax = 3;
                        selectButton(btn_split2Split, "split");
                        break;
                    case ThreeSplit:
                        muscleGroupAmountMax = 2;
                        selectButton(btn_split3Split, "split");
                        break;
                }
            }
        });

        planViewModel.getSelectedDuration().observe(requireActivity(), new Observer<EWorkoutDuration>() {
            @Override
            public void onChanged(EWorkoutDuration eWorkoutDuration) {
                switch (eWorkoutDuration) {
                    case OneHour:
                        selectButton(btn_duration1, "duration");
                        break;
                    case OneHalfHour:
                        selectButton(btn_duration2, "duration");
                        break;
                    case TwoHours:
                        selectButton(btn_duration3, "duration");
                        break;
                }
            }
        });

        planViewModel.getSelectedMuscleGroups().observe(requireActivity(), new Observer<List<EMuscleGroup>>() {
            @Override
            public void onChanged(List<EMuscleGroup> eMuscleGroups) {
                muscleGroupAmountSelected = 0;
                for (EMuscleGroup muscleGroup : eMuscleGroups) {
                    Button button = muscleGroupButton.get(muscleGroup);
                    button.setBackgroundResource(R.drawable.btn_border_selected);
                    muscleGroupAmountSelected++;
                    toggledMuscleGroups.replace(muscleGroup, true);
                }
                updateRecommendedMuscleGroups();
            }
        });

        planViewModel.getSelectedExtras().observe(requireActivity(), new Observer<EWorkoutExtras>() {
            @Override
            public void onChanged(EWorkoutExtras eWorkoutExtras) {
                switch (eWorkoutExtras) {
                    case Core:
                        selectButton(btn_extrasAbs, "extras");
                        break;
                    case Running:
                        selectButton(btn_extrasRunning, "extras");
                        break;
                    case Biking:
                        selectButton(btn_extrasBiking, "extras");
                        break;
                    case Stairs:
                        selectButton(btn_extrasStairs, "extras");
                        break;
                    case Rowing:
                        selectButton(btn_extrasRowing, "extras");
                        break;
                }
            }
        });

        planViewModel.getPreWorkout().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean preworkout) {
                if (preworkout) {
                    btn_preworkout.setChecked(true);
                    btn_preworkout.setTextColor(Color.parseColor("#FF0D1117"));
                    btn_postworkout.setTextColor(Color.parseColor("#FFFFFFFF"));
                } else {
                    btn_postworkout.setChecked(true);
                    btn_postworkout.setTextColor(Color.parseColor("#FF0D1117"));
                    btn_preworkout.setTextColor(Color.parseColor("#FFFFFFFF"));
                }
            }
        });

        planViewModel.getExtrasDuration().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer durationMinutes) {
                switch (durationMinutes) {
                    case 10:
                        extras_duration_seekbar.setProgress(0);
                        extras_duration_txt.setText("10 min");
                        break;
                    case 15:
                        extras_duration_seekbar.setProgress(1);
                        extras_duration_txt.setText("15 min");
                        break;
                    case 20:
                        extras_duration_seekbar.setProgress(2);
                        extras_duration_txt.setText("20 min");
                        break;
                    case 25:
                        extras_duration_seekbar.setProgress(3);
                        extras_duration_txt.setText("25 min");
                        break;
                    case 30:
                        extras_duration_seekbar.setProgress(4);
                        extras_duration_txt.setText("30 min");
                        break;
                }
            }
        });
    }

    private void selectButton(Button button, String group) {
        if (group.equals("extras") && muscleGroupAmountMax == 0) {
            Toast.makeText(MainActivity.getAppContext(), "Select workout split", Toast.LENGTH_SHORT).show();
            planViewModel.setSelectedExtras(new MutableLiveData<>());
            return;
        }

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

    private void toggleMuscleGroup(EMuscleGroup muscleGroup, boolean alreadyToggled) {
        if (muscleGroupAmountMax == 0) {
            Toast.makeText(getContext(), "Select workout split", Toast.LENGTH_SHORT).show();
            return;
        }

        Button button = muscleGroupButton.get(muscleGroup);
        List<EMuscleGroup> selectedMuscleGroups = planViewModel.getSelectedMuscleGroups().getValue();
        if (alreadyToggled) {
            if (planViewModel.getSelectedSplit().getValue() == EWorkoutSplit.FullBody && muscleGroupAmountSelected < 6) {
                Toast.makeText(getContext(), "Minimum of 5 muscle groups when training full body", Toast.LENGTH_SHORT).show();
                return;
            }

            button.setBackgroundResource(R.drawable.btn_border);
            toggledMuscleGroups.replace(muscleGroup, false);
            selectedMuscleGroups.remove(muscleGroup);
            planViewModel.setSelectedMuscleGroups(selectedMuscleGroups);
        } else {
            if (muscleGroupAmountSelected < muscleGroupAmountMax) {
                selectedMuscleGroups.add(muscleGroup);
                planViewModel.setSelectedMuscleGroups(selectedMuscleGroups);
            }
        }

        if (muscleGroupAmountSelected == muscleGroupAmountMax) {
            updateMuscleGroupAlpha(false);
        } else {
            updateMuscleGroupAlpha(true);
        }
    }

    private void updateMuscleGroupAlpha(Boolean allVisible) {
        for (Map.Entry<EMuscleGroup, Boolean> pair : toggledMuscleGroups.entrySet()) {
            Button muscleGroup = muscleGroupButton.get(pair.getKey());
            if (allVisible) {
                muscleGroup.setAlpha(1);
            } else {
                if (pair.getValue() == false) {
                    muscleGroup.setAlpha(0.5f);
                } else {
                    muscleGroup.setAlpha(1);
                }
            }
        }
    }

    private void updateRecommendedMuscleGroups() {
        for (Map.Entry<EMuscleGroup, Boolean> pair : toggledMuscleGroups.entrySet()) {
            if (pair.getValue()) {
                switch (pair.getKey()) {
                    case Chest:
                    case Shoulders:
                        if (!toggledMuscleGroups.get(EMuscleGroup.Triceps)) {
                            btn_muscleGroupTriceps.setBackgroundResource(R.drawable.btn_border_recommended);
                        }
                        break;
                    case Back:
                        if (!toggledMuscleGroups.get(EMuscleGroup.Biceps)) {
                            btn_muscleGroupBiceps.setBackgroundResource(R.drawable.btn_border_recommended);
                        }
                        break;
                    case Legs:
                        break;
                    case Triceps:
                        if (!toggledMuscleGroups.get(EMuscleGroup.Chest)) {
                            btn_muscleGroupChest.setBackgroundResource(R.drawable.btn_border_recommended);
                        }
                        if (!toggledMuscleGroups.get(EMuscleGroup.Shoulders)) {
                            btn_muscleGroupShoulders.setBackgroundResource(R.drawable.btn_border_recommended);
                        }
                        break;
                    case Biceps:
                        if (!toggledMuscleGroups.get(EMuscleGroup.Back)) {
                            btn_muscleGroupBack.setBackgroundResource(R.drawable.btn_border_recommended);
                        }
                        break;
                }
            }
        }
    }
}