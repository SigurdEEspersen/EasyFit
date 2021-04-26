package eugen.enterprise.easyfit.view.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.enums.ECalorieTarget;
import eugen.enterprise.easyfit.acquaintance.helpers.Common;
import eugen.enterprise.easyfit.acquaintance.helpers.MacroResult;
import eugen.enterprise.easyfit.view.activities.MainActivity;
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
            layout_max_surplus;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_macro, container, false);
        macroViewModel = new ViewModelProvider(requireActivity()).get(MacroViewModel.class);

        layout_parent = root.findViewById(R.id.layout_parent);
        layout_macros = root.findViewById(R.id.layout_macros);
        layout_results = root.findViewById(R.id.layout_results);

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View row = inflater.inflate(R.layout.custom_spinner, null);
                final TextView txt = row.findViewById(R.id.txt_spinnerText);
                switch (position) {
                    case 0:
                        txt.setText("Idle");
                        break;
                    case 1:
                        txt.setText("Minimal");
                        break;
                    case 2:
                        txt.setText("Light");
                        break;
                    case 3:
                        txt.setText("Moderate");
                        break;
                    case 4:
                        txt.setText("Active");
                        break;
                    case 5:
                        txt.setText("Very Active");
                        break;
                    case 6:
                        txt.setText("Super Active");
                        break;
                }
                return row;
            }
        };
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        dropdown_activity.setAdapter(adapter);

        dropdown_activity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                macroViewModel.setActivityLevel(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        macroViewModel.setIsMale(true);

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
                macroViewModel.setIsMale(true);
            }
        });
        rbtn_female.setOnCheckedChangeListener((buttonView, isFemale) -> {
            if (isFemale) {
                macroViewModel.setIsMale(false);
            }
        });

        macroViewModel.getIsMale().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isMale) {
                if (isMale) {
                    rbtn_male.setChecked(true);
                    rbtn_male.setTextColor(Color.parseColor("#FF0D1117"));
                    rbtn_female.setTextColor(Color.parseColor("#FFFFFFFF"));
                } else {
                    rbtn_female.setChecked(true);
                    rbtn_female.setTextColor(Color.parseColor("#FF0D1117"));
                    rbtn_male.setTextColor(Color.parseColor("#FFFFFFFF"));
                }
                rbtn_male.setChecked(isMale);
            }
        });

        txt_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    macroViewModel.setWeight(Double.parseDouble(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txt_height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    macroViewModel.setHeight(Double.parseDouble(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txt_age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    macroViewModel.setAge(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        macroViewModel.getWeight().observe(requireActivity(), new Observer<Double>() {
            @Override
            public void onChanged(Double weight) {
                String value = String.valueOf(weight);
                if (value.endsWith(".0")) {
                    value = value.substring(0, value.length() - 2);
                }

                if (!txt_weight.getText().toString().equals(value)) {
                    txt_weight.setText(value);
                }
            }
        });

        macroViewModel.getHeight().observe(requireActivity(), new Observer<Double>() {
            @Override
            public void onChanged(Double height) {
                String value = String.valueOf(height);
                if (value.endsWith(".0")) {
                    value = value.substring(0, value.length() - 2);
                }

                if (!txt_height.getText().toString().equals(value)) {
                    txt_height.setText(value);
                }
            }
        });

        macroViewModel.getAge().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer age) {
                String value = String.valueOf(age);
                if (!txt_age.getText().toString().equals(value)) {
                    txt_age.setText(value);
                }
            }
        });

        macroViewModel.getActivityLevel().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String activityLevel) {
                if (activityLevel.contains("Minimal")) {
                    dropdown_activity.setSelection(1);
                } else if (activityLevel.contains("Light")) {
                    dropdown_activity.setSelection(2);
                } else if (activityLevel.contains("Moderate")) {
                    dropdown_activity.setSelection(3);
                } else if (activityLevel.contains("3-4")) {
                    dropdown_activity.setSelection(4);
                } else if (activityLevel.contains("Very Active")) {
                    dropdown_activity.setSelection(5);
                } else if (activityLevel.contains("Super Active")) {
                    dropdown_activity.setSelection(6);
                } else {
                    dropdown_activity.setSelection(0);
                }
            }
        });

        rbtn_max_deficit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCalorieTarget(rbtn_max_deficit);
                macroViewModel.setCalorieTarget(ECalorieTarget.MaxCalorieDeficit);
            }
        });
        rbtn_deficit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCalorieTarget(rbtn_deficit);
                macroViewModel.setCalorieTarget(ECalorieTarget.CalorieDeficit);
            }
        });
        rbtn_maintain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCalorieTarget(rbtn_maintain);
                macroViewModel.setCalorieTarget(ECalorieTarget.Maintain);
            }
        });
        rbtn_surplus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCalorieTarget(rbtn_surplus);
                macroViewModel.setCalorieTarget(ECalorieTarget.CalorieSurplus);
            }
        });
        rbtn_max_surplus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCalorieTarget(rbtn_max_surplus);
                macroViewModel.setCalorieTarget(ECalorieTarget.MaxCalorieSurplus);
            }
        });

        macroViewModel.getCalorieTarget().observe(requireActivity(), new Observer<ECalorieTarget>() {
            @Override
            public void onChanged(ECalorieTarget selectedCalorieTarget) {
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
                Toast.makeText(MainActivity.getAppContext(), message, Toast.LENGTH_SHORT).show();
                macroViewModel.setSaveStatus(new MutableLiveData<>());
            }
        });

        macroViewModel.getSavedMacros().observe(requireActivity(), new Observer<MacroResult>() {
            @Override
            public void onChanged(MacroResult result) {
                if (result == null) {
                    Toast.makeText(getContext(), "No macros saved yet", Toast.LENGTH_SHORT).show();
                    return;
                }

                macroViewModel.setIsMale(result.isMale());
                macroViewModel.setCalorieTarget(result.getCalorieTarget());

                txt_weight.setText(String.valueOf(Math.round(result.getWeight())));
                txt_height.setText(String.valueOf(Math.round(result.getHeight())));
                txt_age.setText(String.valueOf(result.getAge()));

                for (int i = 0; i < dropdown_activity.getAdapter().getCount(); i++) {
                    if (dropdown_activity.getAdapter().getItem(i).equals(result.getActivity())) {
                        dropdown_activity.setSelection(i);
                    }
                }

                macroViewModel.setCalculatedCalories(result.getCalories());
                macroViewModel.setCalculatedProtein(result.getProteins());
                macroViewModel.setCalculatedCarbs(result.getCarbs());
                macroViewModel.setCalculatedFat(result.getFat());

                macroViewModel.setSavedMacros(new MutableLiveData<>());
            }
        });

        macroViewModel.getCalculatedCalories().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer dailyCalories) {
                txt_calories.setText(String.valueOf(Math.round(dailyCalories)));
            }
        });

        macroViewModel.getCalculatedProtein().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer proteinGram) {
                txt_protein.setText(String.valueOf(Math.round(proteinGram)));
            }
        });

        macroViewModel.getCalculatedCarbs().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer carbsGram) {
                txt_carbs.setText(String.valueOf(Math.round(carbsGram)));
            }
        });

        macroViewModel.getCalculatedFat().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer fatGram) {
                txt_fat.setText(String.valueOf(Math.round(fatGram)));
            }
        });
    }

    private boolean calculateMacros() {
        if (!checkData()) {
            return false;
        }

        double weight = macroViewModel.getWeight().getValue();
        double height = macroViewModel.getHeight().getValue();
        int age = macroViewModel.getAge().getValue();
        boolean male = macroViewModel.getIsMale().getValue();

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

        switch (macroViewModel.getCalorieTarget().getValue()) {
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

        macroViewModel.setCalculatedCalories((int) Math.round(dailyCalories));
        macroViewModel.setCalculatedProtein((int) Math.round(proteinGram));
        macroViewModel.setCalculatedCarbs((int) Math.round(carbsGram));
        macroViewModel.setCalculatedFat((int) Math.round(fatGram));

        return true;
    }

    private boolean checkData() {
        //TODO marker felter med rød i stedet for toast
        if (macroViewModel.getIsMale().getValue() == null) {
            Toast.makeText(getContext(), "Select gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (macroViewModel.getCalorieTarget().getValue() == null) {
            Toast.makeText(getContext(), "Select calorie target", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (macroViewModel.getWeight().getValue() == null) {
            Toast.makeText(getContext(), "Weight missing", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (macroViewModel.getHeight().getValue() == null) {
            Toast.makeText(getContext(), "Height missing", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (macroViewModel.getAge().getValue() == null) {
            Toast.makeText(getContext(), "Age missing", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private MacroResult createMacroResult() {
        MacroResult result = new MacroResult();
        result.setMale(macroViewModel.getIsMale().getValue());
        result.setCalorieTarget(macroViewModel.getCalorieTarget().getValue());
        result.setWeight(macroViewModel.getWeight().getValue());
        result.setHeight(macroViewModel.getHeight().getValue());
        result.setAge(macroViewModel.getAge().getValue());
        result.setActivity(macroViewModel.getActivityLevel().getValue());
        result.setCalories(macroViewModel.getCalculatedCalories().getValue());
        result.setProteins(macroViewModel.getCalculatedProtein().getValue());
        result.setCarbs(macroViewModel.getCalculatedCarbs().getValue());
        result.setFat(macroViewModel.getCalculatedFat().getValue());
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