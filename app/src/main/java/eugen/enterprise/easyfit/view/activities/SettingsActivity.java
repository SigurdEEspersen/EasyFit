package eugen.enterprise.easyfit.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.enums.EMuscleGroup;
import eugen.enterprise.easyfit.acquaintance.interfaces.IExercise;
import eugen.enterprise.easyfit.acquaintance.interfaces.IPreferredExercise;
import eugen.enterprise.easyfit.viewmodel.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity {

    private boolean init_chest = true;
    private boolean init_shoulders = true;
    private boolean init_back = true;
    private boolean init_legs = true;
    private boolean init_triceps = true;
    private boolean init_biceps = true;
    private Spinner spinner_chest;
    private Spinner spinner_shoulders;
    private Spinner spinner_back;
    private Spinner spinner_legs;
    private Spinner spinner_triceps;
    private Spinner spinner_biceps;
    private ArrayAdapter<String> adapter_chest;
    private ArrayAdapter<String> adapter_shoulders;
    private ArrayAdapter<String> adapter_back;
    private ArrayAdapter<String> adapter_legs;
    private ArrayAdapter<String> adapter_triceps;
    private ArrayAdapter<String> adapter_biceps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton btn_return = findViewById(R.id.btn_return);
        btn_return.setOnClickListener(v -> finish());

        SettingsViewModel viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        viewModel.loadExercises(getApplicationContext());

        viewModel.getExercises().observe(this, new Observer<IExercise[]>() {
            @Override
            public void onChanged(IExercise[] exercises) {
                ArrayList<String> chestExercises = new ArrayList<>();
                ArrayList<String> shoulderExercises = new ArrayList<>();
                ArrayList<String> backExercises = new ArrayList<>();
                ArrayList<String> legExercises = new ArrayList<>();
                ArrayList<String> tricepExercises = new ArrayList<>();
                ArrayList<String> bicepExercises = new ArrayList<>();

                chestExercises.add("None");
                shoulderExercises.add("None");
                backExercises.add("None");
                legExercises.add("None");
                tricepExercises.add("None");
                bicepExercises.add("None");

                for (IExercise exercise : exercises) {
                    if (exercise.getMuscleGroup().equals(EMuscleGroup.Chest.name())) {
                        chestExercises.add(exercise.getName());
                    }

                    if (exercise.getMuscleGroup().equals(EMuscleGroup.Shoulders.name())) {
                        shoulderExercises.add(exercise.getName());
                    }

                    if (exercise.getMuscleGroup().equals(EMuscleGroup.Back.name())) {
                        backExercises.add(exercise.getName());
                    }

                    if (exercise.getMuscleGroup().equals(EMuscleGroup.Legs.name())) {
                        legExercises.add(exercise.getName());
                    }

                    if (exercise.getMuscleGroup().equals(EMuscleGroup.Triceps.name())) {
                        tricepExercises.add(exercise.getName());
                    }

                    if (exercise.getMuscleGroup().equals(EMuscleGroup.Biceps.name())) {
                        bicepExercises.add(exercise.getName());
                    }
                }

                spinner_chest = findViewById(R.id.spinner_chest);
                adapter_chest = new ArrayAdapter<>(getApplication(), R.layout.custom_spinner, chestExercises);
                adapter_chest.setDropDownViewResource(R.layout.custom_spinner_dropdown_border);
                spinner_chest.setAdapter(adapter_chest);

                spinner_chest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (init_chest) {
                            init_chest = false;
                            return;
                        }

                        String exerciseName = (String) parent.getAdapter().getItem(position);
                        viewModel.setPreferredExercise(exerciseName, EMuscleGroup.Chest, getApplicationContext());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                spinner_shoulders = findViewById(R.id.spinner_shoulders);
                adapter_shoulders = new ArrayAdapter<>(getApplication(), R.layout.custom_spinner, shoulderExercises);
                adapter_shoulders.setDropDownViewResource(R.layout.custom_spinner_dropdown_border);
                spinner_shoulders.setAdapter(adapter_shoulders);

                spinner_shoulders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (init_shoulders) {
                            init_shoulders = false;
                            return;
                        }

                        String exerciseName = (String) parent.getAdapter().getItem(position);
                        viewModel.setPreferredExercise(exerciseName, EMuscleGroup.Shoulders, getApplicationContext());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                spinner_back = findViewById(R.id.spinner_back);
                adapter_back = new ArrayAdapter<>(getApplication(), R.layout.custom_spinner, backExercises);
                adapter_back.setDropDownViewResource(R.layout.custom_spinner_dropdown_border);
                spinner_back.setAdapter(adapter_back);

                spinner_back.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (init_back) {
                            init_back = false;
                            return;
                        }

                        String exerciseName = (String) parent.getAdapter().getItem(position);
                        viewModel.setPreferredExercise(exerciseName, EMuscleGroup.Back, getApplicationContext());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                spinner_legs = findViewById(R.id.spinner_legs);
                adapter_legs = new ArrayAdapter<>(getApplication(), R.layout.custom_spinner, legExercises);
                adapter_legs.setDropDownViewResource(R.layout.custom_spinner_dropdown_border);
                spinner_legs.setAdapter(adapter_legs);

                spinner_legs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (init_legs) {
                            init_legs = false;
                            return;
                        }

                        String exerciseName = (String) parent.getAdapter().getItem(position);
                        viewModel.setPreferredExercise(exerciseName, EMuscleGroup.Legs, getApplicationContext());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                spinner_triceps = findViewById(R.id.spinner_triceps);
                adapter_triceps = new ArrayAdapter<>(getApplication(), R.layout.custom_spinner, tricepExercises);
                adapter_triceps.setDropDownViewResource(R.layout.custom_spinner_dropdown_border);
                spinner_triceps.setAdapter(adapter_triceps);

                spinner_triceps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (init_triceps) {
                            init_triceps = false;
                            return;
                        }

                        String exerciseName = (String) parent.getAdapter().getItem(position);
                        viewModel.setPreferredExercise(exerciseName, EMuscleGroup.Triceps, getApplicationContext());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                spinner_biceps = findViewById(R.id.spinner_biceps);
                adapter_biceps = new ArrayAdapter<>(getApplication(), R.layout.custom_spinner, bicepExercises);
                adapter_biceps.setDropDownViewResource(R.layout.custom_spinner_dropdown_border);
                spinner_biceps.setAdapter(adapter_biceps);

                spinner_biceps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (init_biceps) {
                            init_biceps = false;
                            return;
                        }

                        String exerciseName = (String) parent.getAdapter().getItem(position);
                        viewModel.setPreferredExercise(exerciseName, EMuscleGroup.Biceps, getApplicationContext());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

        viewModel.getSetPreferredExerciseStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success) {
                    Toast.makeText(SettingsActivity.this, "Updated preferred exercise", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "Failed to update preffered exercise", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.loadSavedPrefferedExercises(getApplicationContext());

        viewModel.getSavedPrefferedExercises().observe(this, new Observer<IPreferredExercise[]>() {
            @Override
            public void onChanged(IPreferredExercise[] savedPreferredExercises) {
                for (IPreferredExercise preferredExercise : savedPreferredExercises) {
                    switch (preferredExercise.getMuscleGroup()) {
                        case "Chest":
                            spinner_chest.setSelection(adapter_chest.getPosition(preferredExercise.getName()));
                            break;
                        case "Shoulders":
                            spinner_shoulders.setSelection(adapter_shoulders.getPosition(preferredExercise.getName()));
                            break;
                        case "Back":
                            spinner_back.setSelection(adapter_back.getPosition(preferredExercise.getName()));
                            break;
                        case "Legs":
                            spinner_legs.setSelection(adapter_legs.getPosition(preferredExercise.getName()));
                            break;
                        case "Triceps":
                            spinner_triceps.setSelection(adapter_triceps.getPosition(preferredExercise.getName()));
                            break;
                        case "Biceps":
                            spinner_biceps.setSelection(adapter_biceps.getPosition(preferredExercise.getName()));
                            break;
                    }
                }
            }
        });
    }
}
