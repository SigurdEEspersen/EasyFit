package eugen.enterprise.easyfit.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.enums.ECalorieTarget;
import eugen.enterprise.easyfit.acquaintance.helpers.Common;
import eugen.enterprise.easyfit.acquaintance.helpers.MacroResult;
import eugen.enterprise.easyfit.viewmodel.MacroViewModel;

public class MacroFragment extends Fragment {

    private MacroViewModel macroViewModel;
    private Button btn_calculate, btn_save, btn_load;
    private RadioButton rbtn_male, rbtn_female, rbtn_max_deficit, rbtn_deficit, rbtn_maintain,
            rbtn_surplus, rbtn_max_surplus;
    private Spinner dropdown_activity;
    private EditText txt_weight, txt_height, txt_age;
    private TextView txt_calories, txt_protein, txt_carbs, txt_fat;
    private RelativeLayout layout_parent, layout_macros;
    private GridLayout layout_results;
    private LinearLayout layout_max_deficit, layout_deficit, layout_maintain, layout_surplus,
            layout_max_surplus, layout_male, layout_female;
    private Boolean male;
    private ECalorieTarget selectedCalorieTarget;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_macro, container, false);
        macroViewModel = new ViewModelProvider(this).get(MacroViewModel.class);

        layout_parent = root.findViewById(R.id.layout_parent);
        layout_macros = root.findViewById(R.id.layout_macros);
        layout_results = root.findViewById(R.id.layout_results);

        layout_male = root.findViewById(R.id.layout_male);
        layout_female = root.findViewById(R.id.layout_female);

        layout_max_deficit = root.findViewById(R.id.layout_max_deficit);
        layout_deficit = root.findViewById(R.id.layout_deficit);
        layout_maintain = root.findViewById(R.id.layout_maintain);
        layout_surplus = root.findViewById(R.id.layout_surplus);
        layout_max_surplus = root.findViewById(R.id.layout_max_surplus);

        txt_weight = root.findViewById(R.id.txt_weight);
        txt_height = root.findViewById(R.id.txt_height);
        txt_age = root.findViewById(R.id.txt_age);

        txt_calories = root.findViewById(R.id.txt_calories);
        txt_protein = root.findViewById(R.id.txt_protein);
        txt_carbs = root.findViewById(R.id.txt_carbs);
        txt_fat = root.findViewById(R.id.txt_fat);

        rbtn_male = root.findViewById(R.id.rbtn_male);
        rbtn_female = root.findViewById(R.id.rbtn_female);
        rbtn_max_deficit = root.findViewById(R.id.rbtn_max_deficit);
        rbtn_deficit = root.findViewById(R.id.rbtn_deficit);
        rbtn_maintain = root.findViewById(R.id.rbtn_maintain);
        rbtn_surplus = root.findViewById(R.id.rbtn_surplus);
        rbtn_max_surplus = root.findViewById(R.id.rbtn_max_surplus);

        btn_calculate = root.findViewById(R.id.btn_calculate);

        btn_save = root.findViewById(R.id.btn_save);
        btn_save.setEnabled(false);
        btn_save.setAlpha(0.5f);
        btn_load = root.findViewById(R.id.btn_load);

        dropdown_activity = root.findViewById(R.id.spinner_activity);
        String[] items = new String[]
                {
                        "Idle (No activity at all)",
                        "Minimal (Little or no exercise)",
                        "Light (Exercise 1-3 times/week)",
                        "Moderate (Exercise 4-5 times/week)",
                        "Active (Active job & exercise 3-4 times/week)",
                        "Very Active (Active job & exercise 5-7 times/week)",
                        "Super Active (Intense active job & exercise daily)"
                };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.custom_spinner, items);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        dropdown_activity.setAdapter(adapter);

        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout_parent.setOnTouchListener((v, event) -> {
            Common.hideKeyboard(getContext(), v, getActivity());
            txt_weight.clearFocus();
            txt_height.clearFocus();
            txt_age.clearFocus();
            return false;
        });
        layout_macros.setOnTouchListener((v, event) -> {
            Common.hideKeyboard(getContext(), v, getActivity());
            txt_weight.clearFocus();
            txt_height.clearFocus();
            txt_age.clearFocus();
            return false;
        });
        layout_results.setOnTouchListener((v, event) -> {
            Common.hideKeyboard(getContext(), v, getActivity());
            txt_weight.clearFocus();
            txt_height.clearFocus();
            txt_age.clearFocus();
            return false;
        });

        rbtn_male.setOnCheckedChangeListener((buttonView, isMale) -> {
            if (isMale) {
                male = true;
                rbtn_female.setChecked(false);
            }
        });
        rbtn_female.setOnCheckedChangeListener((buttonView, isFemale) -> {
            if (isFemale) {
                male = false;
                rbtn_male.setChecked(false);
            }
        });
        layout_male.setOnTouchListener((v, event) -> {
            rbtn_male.setChecked(true);
            return false;
        });
        layout_female.setOnTouchListener((v, event) -> {
            rbtn_female.setChecked(true);
            return false;
        });

        rbtn_max_deficit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCalorieTarget(rbtn_max_deficit);
                selectedCalorieTarget = ECalorieTarget.MaxCalorieDeficit;
            }
        });
        rbtn_deficit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCalorieTarget(rbtn_deficit);
                selectedCalorieTarget = ECalorieTarget.CalorieDeficit;
            }
        });
        rbtn_maintain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCalorieTarget(rbtn_maintain);
                selectedCalorieTarget = ECalorieTarget.Maintain;
            }
        });
        rbtn_surplus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCalorieTarget(rbtn_surplus);
                selectedCalorieTarget = ECalorieTarget.CalorieSurplus;
            }
        });
        rbtn_max_surplus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCalorieTarget(rbtn_max_surplus);
                selectedCalorieTarget = ECalorieTarget.MaxCalorieSurplus;
            }
        });
        layout_max_deficit.setOnTouchListener((v, event) -> {
            rbtn_max_deficit.setChecked(true);
            return false;
        });
        layout_deficit.setOnTouchListener((v, event) -> {
            rbtn_deficit.setChecked(true);
            return false;
        });
        layout_maintain.setOnTouchListener((v, event) -> {
            rbtn_maintain.setChecked(true);
            return false;
        });
        layout_surplus.setOnTouchListener((v, event) -> {
            rbtn_surplus.setChecked(true);
            return false;
        });
        layout_max_surplus.setOnTouchListener((v, event) -> {
            rbtn_max_surplus.setChecked(true);
            return false;
        });

        btn_calculate.setOnClickListener(v -> {
            if (calculateMacros()) {
                btn_save.setEnabled(true);
                btn_save.setAlpha(1);
            }
        });

        btn_save.setOnClickListener(v -> {
            macroViewModel.saveMacros(createMacroResult(), getContext());
        });
        btn_load.setOnClickListener(v -> {
            macroViewModel.loadMacros(getContext());
        });

        macroViewModel.getSaveStatus().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        macroViewModel.getSavedMacros().observe(requireActivity(), new Observer<MacroResult>() {
            @Override
            public void onChanged(MacroResult result) {
                male = result.isMale();
                if (male) {
                    rbtn_male.setChecked(true);
                } else {
                    rbtn_female.setChecked(true);
                }

                selectedCalorieTarget = result.getCalorieTarget();
                switch (selectedCalorieTarget) {
                    case MaxCalorieDeficit:
                        rbtn_max_deficit.setChecked(true);
                        break;
                    case CalorieDeficit:
                        rbtn_deficit.setChecked(true);
                        break;
                    case Maintain:
                        rbtn_maintain.setChecked(true);
                        break;
                    case CalorieSurplus:
                        rbtn_surplus.setChecked(true);
                        break;
                    case MaxCalorieSurplus:
                        rbtn_max_surplus.setChecked(true);
                        break;
                }

                txt_weight.setText(String.valueOf(Math.round(result.getWeight())));
                txt_height.setText(String.valueOf(Math.round(result.getHeight())));
                txt_age.setText(String.valueOf(result.getAge()));

                for (int i = 0; i < dropdown_activity.getAdapter().getCount(); i++) {
                    if (dropdown_activity.getAdapter().getItem(i).equals(result.getActivity())) {
                        dropdown_activity.setSelection(i);
                    }
                }

                txt_calories.setText(String.valueOf(result.getCalories()));
                txt_protein.setText(String.valueOf(result.getProteins()));
                txt_carbs.setText(String.valueOf(result.getCarbs()));
                txt_fat.setText(String.valueOf(result.getFat()));
            }
        });
    }

    private boolean calculateMacros() {
        if (!checkData()) {
            return false;
        }

        double weight = Double.parseDouble(txt_weight.getText().toString());
        double height = Double.parseDouble(txt_height.getText().toString());
        int age = Integer.parseInt(txt_age.getText().toString());

        double dailyCalories = (10 * weight) + (6.25 * height) - (5 * age);
        dailyCalories = male ? dailyCalories + 5 : dailyCalories - 161;

        String activityLevel = dropdown_activity.getSelectedItem().toString();
        if (activityLevel.contains("Minimal")) {
            dailyCalories = dailyCalories * 1.2;
        } else if (activityLevel.contains("Light")) {
            dailyCalories = dailyCalories * 1.35;
        } else if (activityLevel.contains("Moderate")) {
            dailyCalories = dailyCalories * 1.5;
        } else if (activityLevel.contains("3-4")) {
            dailyCalories = dailyCalories * 1.65;
        } else if (activityLevel.contains("Very Active")) {
            dailyCalories = dailyCalories * 1.80;
        } else if (activityLevel.contains("Super Active")) {
            dailyCalories = dailyCalories * 1.95;
        }

        switch (selectedCalorieTarget) {
            case MaxCalorieDeficit:
                dailyCalories = dailyCalories * 0.8;
                break;
            case CalorieDeficit:
                dailyCalories = dailyCalories * 0.9;
                break;
            case CalorieSurplus:
                dailyCalories = dailyCalories * 1.1;
                break;
            case MaxCalorieSurplus:
                dailyCalories = dailyCalories * 1.2;
                break;
            default:
                break;
        }

        double proteinGram = male ? 2.2 * weight : 1.8 * weight;
        double proteinCalories = proteinGram * 4;

        double fatCalories = male ? dailyCalories * 0.2 : dailyCalories * 0.3;
        double fatGram = fatCalories / 9;

        double carbsCalories = dailyCalories - proteinCalories - fatCalories;
        double carbsGram = carbsCalories / 4;

        txt_calories.setText(String.valueOf(Math.round(dailyCalories)));
        txt_protein.setText(String.valueOf(Math.round(proteinGram)));
        txt_carbs.setText(String.valueOf(Math.round(carbsGram)));
        txt_fat.setText(String.valueOf(Math.round(fatGram)));

        return true;
    }

    private boolean checkData() {
        //TODO marker felter med rød i stedet for toast
        if (male == null) {
            Toast.makeText(getContext(), "Select gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedCalorieTarget == null) {
            Toast.makeText(getContext(), "Select calorie target", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txt_weight.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Weight missing", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txt_height.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Height missing", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txt_age.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Age missing", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private MacroResult createMacroResult() {
        MacroResult result = new MacroResult();
        result.setMale(male);
        result.setCalorieTarget(selectedCalorieTarget);
        result.setWeight(Double.parseDouble(txt_weight.getText().toString()));
        result.setHeight(Double.parseDouble(txt_height.getText().toString()));
        result.setAge(Integer.parseInt(txt_age.getText().toString()));
        result.setActivity(dropdown_activity.getSelectedItem().toString());
        result.setCalories(Integer.parseInt(txt_calories.getText().toString()));
        result.setProteins(Integer.parseInt(txt_protein.getText().toString()));
        result.setCarbs(Integer.parseInt(txt_carbs.getText().toString()));
        result.setFat(Integer.parseInt(txt_fat.getText().toString()));
        return result;
    }

    private void setCalorieTarget(RadioButton selection) {
        rbtn_max_deficit.setChecked(false);
        rbtn_deficit.setChecked(false);
        rbtn_maintain.setChecked(false);
        rbtn_surplus.setChecked(false);
        rbtn_max_surplus.setChecked(false);
        selection.setChecked(true);
    }
}