package eugen.enterprise.easyfit.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.enums.EMuscleGroup;
import eugen.enterprise.easyfit.acquaintance.interfaces.IExercise;
import eugen.enterprise.easyfit.viewmodel.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton btn_return = findViewById(R.id.btn_return);
        btn_return.setOnClickListener(v -> finish());

        SettingsViewModel viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        viewModel.LoadExercises(getApplicationContext());

        viewModel.getExercises().observe(this, new Observer<IExercise[]>() {
            @Override
            public void onChanged(IExercise[] exercises) {
                ArrayList<String> chestExercises = new ArrayList<>();
                ArrayList<String> shoulderExercises = new ArrayList<>();
                ArrayList<String> backExercises = new ArrayList<>();
                ArrayList<String> legExercises = new ArrayList<>();
                ArrayList<String> tricepExercises = new ArrayList<>();
                ArrayList<String> bicepExercises = new ArrayList<>();

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

                Spinner spinner_chest = findViewById(R.id.spinner_chest);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(), R.layout.custom_spinner, chestExercises);
                adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_border);
                spinner_chest.setAdapter(adapter);

                spinner_chest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //TODO
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                Spinner spinner_shoulders = findViewById(R.id.spinner_shoulders);
                adapter = new ArrayAdapter<>(getApplication(), R.layout.custom_spinner, shoulderExercises);
                adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_border);
                spinner_shoulders.setAdapter(adapter);

                spinner_shoulders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //TODO
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                Spinner spinner_back = findViewById(R.id.spinner_back);
                adapter = new ArrayAdapter<>(getApplication(), R.layout.custom_spinner, backExercises);
                adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_border);
                spinner_back.setAdapter(adapter);

                spinner_back.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //TODO
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                Spinner spinner_legs = findViewById(R.id.spinner_legs);
                adapter = new ArrayAdapter<>(getApplication(), R.layout.custom_spinner, legExercises);
                adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_border);
                spinner_legs.setAdapter(adapter);

                spinner_legs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //TODO
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                Spinner spinner_triceps = findViewById(R.id.spinner_triceps);
                adapter = new ArrayAdapter<>(getApplication(), R.layout.custom_spinner, tricepExercises);
                adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_border);
                spinner_triceps.setAdapter(adapter);

                spinner_triceps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //TODO
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                Spinner spinner_biceps = findViewById(R.id.spinner_biceps);
                adapter = new ArrayAdapter<>(getApplication(), R.layout.custom_spinner, bicepExercises);
                adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_border);
                spinner_biceps.setAdapter(adapter);

                spinner_biceps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //TODO
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });
    }
}
