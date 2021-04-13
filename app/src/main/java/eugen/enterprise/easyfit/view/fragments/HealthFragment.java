package eugen.enterprise.easyfit.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.enums.ECalorieTarget;
import eugen.enterprise.easyfit.acquaintance.helpers.Common;
import eugen.enterprise.easyfit.acquaintance.helpers.MacroResult;
import eugen.enterprise.easyfit.viewmodel.HealthViewModel;

public class HealthFragment extends Fragment {

    private HealthViewModel healthViewModel;
    private ImageButton water_bottle_1, water_bottle_2, water_bottle_3, water_bottle_4;
    private TextView txt_remaining_water;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_health, container, false);

        healthViewModel = new ViewModelProvider(requireActivity()).get(HealthViewModel.class);

        water_bottle_1 = root.findViewById(R.id.water_bottle_1);
        water_bottle_2 = root.findViewById(R.id.water_bottle_2);
        water_bottle_3 = root.findViewById(R.id.water_bottle_3);
        water_bottle_4 = root.findViewById(R.id.water_bottle_4);
        txt_remaining_water = root.findViewById(R.id.txt_remaining_water);

        Integer existingSelection = healthViewModel.getSelectedWaterBottle().getValue();
        if (existingSelection != null) {
            healthViewModel.setSelectedWaterBottle(existingSelection);
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        water_bottle_1.setOnClickListener(v -> {
            Integer existingSelection = healthViewModel.getSelectedWaterBottle().getValue();
            if (existingSelection != null) {
                if (existingSelection < 1) {
                    healthViewModel.setSelectedWaterBottle(1);
                }
                if (existingSelection == 1) {
                    healthViewModel.setSelectedWaterBottle(0);
                }
            } else {
                healthViewModel.setSelectedWaterBottle(1);
            }
        });

        water_bottle_2.setOnClickListener(v -> {
            Integer existingSelection = healthViewModel.getSelectedWaterBottle().getValue();
            if (existingSelection != null) {
                if (existingSelection == 1) {
                    healthViewModel.setSelectedWaterBottle(2);
                } else if (existingSelection == 2) {
                    healthViewModel.setSelectedWaterBottle(1);
                }
            }
        });

        water_bottle_3.setOnClickListener(v -> {
            Integer existingSelection = healthViewModel.getSelectedWaterBottle().getValue();
            if (existingSelection != null) {
                if (existingSelection == 2) {
                    healthViewModel.setSelectedWaterBottle(3);
                } else if (existingSelection == 3) {
                    healthViewModel.setSelectedWaterBottle(2);
                }
            }
        });

        water_bottle_4.setOnClickListener(v -> {
            Integer existingSelection = healthViewModel.getSelectedWaterBottle().getValue();
            if (existingSelection != null) {
                if (existingSelection == 3) {
                    healthViewModel.setSelectedWaterBottle(4);
                } else if (existingSelection == 4) {
                    healthViewModel.setSelectedWaterBottle(3);
                }
            }
        });

        healthViewModel.getSelectedWaterBottle().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 1) {
                    water_bottle_1.setImageResource(R.drawable.icon_bottle_full);
                    water_bottle_2.setImageResource(R.drawable.icon_bottle_add);
                    water_bottle_3.setImageResource(R.drawable.icon_bottle_empty);
                    water_bottle_4.setImageResource(R.drawable.icon_bottle_empty);
                    txt_remaining_water.setText("Remaining Water - 1.5l");
                } else if (integer == 2) {
                    water_bottle_1.setImageResource(R.drawable.icon_bottle_full);
                    water_bottle_2.setImageResource(R.drawable.icon_bottle_full);
                    water_bottle_3.setImageResource(R.drawable.icon_bottle_add);
                    water_bottle_4.setImageResource(R.drawable.icon_bottle_empty);
                    txt_remaining_water.setText("Remaining Water - 1l");
                } else if (integer == 3) {
                    water_bottle_1.setImageResource(R.drawable.icon_bottle_full);
                    water_bottle_2.setImageResource(R.drawable.icon_bottle_full);
                    water_bottle_3.setImageResource(R.drawable.icon_bottle_full);
                    water_bottle_4.setImageResource(R.drawable.icon_bottle_add);
                    txt_remaining_water.setText("Remaining Water - 0.5l");
                } else if (integer == 4) {
                    water_bottle_1.setImageResource(R.drawable.icon_bottle_full);
                    water_bottle_2.setImageResource(R.drawable.icon_bottle_full);
                    water_bottle_3.setImageResource(R.drawable.icon_bottle_full);
                    water_bottle_4.setImageResource(R.drawable.icon_bottle_full);
                    txt_remaining_water.setText("Remaining Water - 0l");
                } else {
                    water_bottle_1.setImageResource(R.drawable.icon_bottle_add);
                    water_bottle_2.setImageResource(R.drawable.icon_bottle_empty);
                    water_bottle_3.setImageResource(R.drawable.icon_bottle_empty);
                    water_bottle_4.setImageResource(R.drawable.icon_bottle_empty);
                    txt_remaining_water.setText("Remaining Water - 2l");
                }
            }
        });
    }
}