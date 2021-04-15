package eugen.enterprise.easyfit.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import java.util.ArrayList;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.enums.ECalorieTarget;
import eugen.enterprise.easyfit.acquaintance.helpers.Common;
import eugen.enterprise.easyfit.acquaintance.helpers.MacroResult;
import eugen.enterprise.easyfit.viewmodel.HealthViewModel;

public class HealthFragment extends Fragment {

    private HealthViewModel healthViewModel;
    private ImageButton water_bottle_1, water_bottle_2, water_bottle_3, water_bottle_4;
    private ImageButton expander_healthy_fats, expander_healthy_carbs, expander_healthy_protein,
            expander_unhealthy_fats, expander_unhealthy_carbs, expander_unhealthy_protein;
    private LinearLayout layout_healthy_fats, layout_healthy_carbs, layout_healthy_protein,
            layout_unhealthy_fats, layout_unhealthy_carbs, layout_unhealthy_protein;
    private ListView list_healthy_fats, list_healthy_carbs, list_healthy_protein,
            list_unhealthy_fats, list_unhealthy_carbs, list_unhealthy_protein;
    private TextView txt_remaining_water;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_health, container, false);

        healthViewModel = new ViewModelProvider(requireActivity()).get(HealthViewModel.class);

        water_bottle_1 = root.findViewById(R.id.water_bottle_1);
        water_bottle_2 = root.findViewById(R.id.water_bottle_2);
        water_bottle_3 = root.findViewById(R.id.water_bottle_3);
        water_bottle_4 = root.findViewById(R.id.water_bottle_4);
        txt_remaining_water = root.findViewById(R.id.txt_remaining_water);

        expander_healthy_fats = root.findViewById(R.id.expander_healthy_fats);
        expander_healthy_carbs = root.findViewById(R.id.expander_healthy_carbs);
        expander_healthy_protein = root.findViewById(R.id.expander_healthy_protein);
        expander_unhealthy_fats = root.findViewById(R.id.expander_unhealthy_fats);
        expander_unhealthy_carbs = root.findViewById(R.id.expander_unhealthy_carbs);
        expander_unhealthy_protein = root.findViewById(R.id.expander_unhealthy_protein);

        layout_healthy_fats = root.findViewById(R.id.layout_healthy_fats);
        layout_healthy_carbs = root.findViewById(R.id.layout_healthy_carbs);
        layout_healthy_protein = root.findViewById(R.id.layout_healthy_protein);
        layout_unhealthy_fats = root.findViewById(R.id.layout_unhealthy_fats);
        layout_unhealthy_carbs = root.findViewById(R.id.layout_unhealthy_carbs);
        layout_unhealthy_protein = root.findViewById(R.id.layout_unhealthy_protein);

        list_healthy_fats = root.findViewById(R.id.list_healthy_fats);
        list_healthy_carbs = root.findViewById(R.id.list_healthy_carbs);
        list_healthy_protein = root.findViewById(R.id.list_healthy_protein);
        list_unhealthy_fats = root.findViewById(R.id.list_unhealthy_fats);
        list_unhealthy_carbs = root.findViewById(R.id.list_unhealthy_carbs);
        list_unhealthy_protein = root.findViewById(R.id.list_unhealthy_protein);

        Integer existingSelection = healthViewModel.getSelectedWaterBottle().getValue();
        if (existingSelection != null) {
            healthViewModel.setSelectedWaterBottle(existingSelection);
        }

        String[] healthyFats = {"Avocados", "Nuts & Seeds", "Olive, canola, peanut, and sesame oils", "Olives", "Peanut Butter", "Fatty fish & fish oil", "Eggs", ""};
        ArrayAdapter<String> healthyFatsAdapter = new ArrayAdapter<>(requireContext(), R.layout.array_adapter_custom_simple_item, healthyFats);
        list_healthy_fats.setAdapter(healthyFatsAdapter);
        list_healthy_fats.setDivider(null);
        Common.updateListViewHeight(list_healthy_fats);

        String[] healthyCarbs = {"Oats", "Sweet Potatos", "Fruit (Bananas, Apples, Oranges)", "Berries", "Whole Grains Food", "Nuts & Seeds", "Beans", "Vegetables", ""};
        ArrayAdapter<String> healthyCarbsAdapter = new ArrayAdapter<>(requireContext(), R.layout.array_adapter_custom_simple_item, healthyCarbs);
        list_healthy_carbs.setAdapter(healthyCarbsAdapter);
        list_healthy_carbs.setDivider(null);
        Common.updateListViewHeight(list_healthy_carbs);

        String[] healthyProtein = {"High protein foods examples:", "Meat & Fish", "Cheese", "Eggs", "Beans", "Hummus", "Nuts & Seeds"};
        ArrayAdapter<String> healthyProteinAdapter = new ArrayAdapter<>(requireContext(), R.layout.array_adapter_custom_simple_item, healthyProtein);
        list_healthy_protein.setAdapter(healthyProteinAdapter);
        list_healthy_protein.setDivider(null);
        Common.updateListViewHeight(list_healthy_protein);

        String[] unhealthyFats = {"Packaged snack foods", "Stick margarine", "Fried foods", "Pre-baked pastries & doughs", "Chicken skin", "Whole-fat dairy products (milk, cream, cheese)", "Butter", "Ice cream", ""};
        ArrayAdapter<String> unhealthyFatsAdapter = new ArrayAdapter<>(requireContext(), R.layout.array_adapter_custom_simple_item, unhealthyFats);
        list_unhealthy_fats.setAdapter(unhealthyFatsAdapter);
        list_unhealthy_fats.setDivider(null);
        Common.updateListViewHeight(list_unhealthy_fats);

        String[] unhealthyCarbs = {"Soda", "White pasta", "White rice", "Sugar", "Sugary drinks/snacks"};
        ArrayAdapter<String> unhealthyCarbsAdapter = new ArrayAdapter<>(requireContext(), R.layout.array_adapter_custom_simple_item, unhealthyCarbs);
        list_unhealthy_carbs.setAdapter(unhealthyCarbsAdapter);
        list_unhealthy_carbs.setDivider(null);
        Common.updateListViewHeight(list_unhealthy_carbs);

        String[] unhealthyProtein = {"No protein is bad protein!"};
        ArrayAdapter<String> unhealthyProteinAdapter = new ArrayAdapter<>(requireContext(), R.layout.array_adapter_custom_simple_item, unhealthyProtein);
        list_unhealthy_protein.setAdapter(unhealthyProteinAdapter);
        list_unhealthy_protein.setDivider(null);
        Common.updateListViewHeight(list_unhealthy_protein);

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

        layout_healthy_fats.setOnClickListener(v -> {
            if (list_healthy_fats.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(layout_healthy_fats, new AutoTransition());
                list_healthy_fats.setVisibility(View.VISIBLE);
                list_unhealthy_fats.setVisibility(View.VISIBLE);
                expander_healthy_fats.setImageResource(R.drawable.ic_baseline_expand_less_24);
                expander_unhealthy_fats.setImageResource(R.drawable.ic_baseline_expand_less_24);
            } else if (list_healthy_fats.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(layout_healthy_fats, new AutoTransition());
                list_unhealthy_fats.setVisibility(View.GONE);
                list_healthy_fats.setVisibility(View.GONE);
                expander_unhealthy_fats.setImageResource(R.drawable.ic_baseline_expand_more_24);
                expander_healthy_fats.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }
        });

        layout_healthy_carbs.setOnClickListener(v -> {
            if (list_healthy_carbs.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(layout_healthy_carbs, new AutoTransition());
                list_healthy_carbs.setVisibility(View.VISIBLE);
                expander_healthy_carbs.setImageResource(R.drawable.ic_baseline_expand_less_24);
                list_unhealthy_carbs.setVisibility(View.VISIBLE);
                expander_unhealthy_carbs.setImageResource(R.drawable.ic_baseline_expand_less_24);
            } else if (list_healthy_carbs.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(layout_healthy_carbs, new AutoTransition());
                list_healthy_carbs.setVisibility(View.GONE);
                expander_healthy_carbs.setImageResource(R.drawable.ic_baseline_expand_more_24);
                list_unhealthy_carbs.setVisibility(View.GONE);
                expander_unhealthy_carbs.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }
        });

        layout_healthy_protein.setOnClickListener(v -> {
            if (list_healthy_protein.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(layout_healthy_protein, new AutoTransition());
                list_healthy_protein.setVisibility(View.VISIBLE);
                expander_healthy_protein.setImageResource(R.drawable.ic_baseline_expand_less_24);
                list_unhealthy_protein.setVisibility(View.VISIBLE);
                expander_unhealthy_protein.setImageResource(R.drawable.ic_baseline_expand_less_24);
            } else if (list_healthy_protein.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(layout_healthy_protein, new AutoTransition());
                list_healthy_protein.setVisibility(View.GONE);
                expander_healthy_protein.setImageResource(R.drawable.ic_baseline_expand_more_24);
                list_unhealthy_protein.setVisibility(View.GONE);
                expander_unhealthy_protein.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }
        });

        layout_unhealthy_fats.setOnClickListener(v -> {
            if (list_unhealthy_fats.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(layout_unhealthy_fats, new AutoTransition());
                list_healthy_fats.setVisibility(View.VISIBLE);
                list_unhealthy_fats.setVisibility(View.VISIBLE);
                expander_healthy_fats.setImageResource(R.drawable.ic_baseline_expand_less_24);
                expander_unhealthy_fats.setImageResource(R.drawable.ic_baseline_expand_less_24);
            } else if (list_unhealthy_fats.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(layout_unhealthy_fats, new AutoTransition());
                list_unhealthy_fats.setVisibility(View.GONE);
                list_healthy_fats.setVisibility(View.GONE);
                expander_unhealthy_fats.setImageResource(R.drawable.ic_baseline_expand_more_24);
                expander_healthy_fats.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }
        });

        layout_unhealthy_carbs.setOnClickListener(v -> {
            if (list_unhealthy_carbs.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(layout_unhealthy_carbs, new AutoTransition());
                list_unhealthy_carbs.setVisibility(View.VISIBLE);
                expander_unhealthy_carbs.setImageResource(R.drawable.ic_baseline_expand_less_24);
                list_healthy_carbs.setVisibility(View.VISIBLE);
                expander_healthy_carbs.setImageResource(R.drawable.ic_baseline_expand_less_24);
            } else if (list_unhealthy_carbs.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(layout_unhealthy_carbs, new AutoTransition());
                list_unhealthy_carbs.setVisibility(View.GONE);
                expander_unhealthy_carbs.setImageResource(R.drawable.ic_baseline_expand_more_24);
                list_healthy_carbs.setVisibility(View.GONE);
                expander_healthy_carbs.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }
        });

        layout_unhealthy_protein.setOnClickListener(v -> {
            if (list_unhealthy_protein.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(layout_unhealthy_protein, new AutoTransition());
                list_unhealthy_protein.setVisibility(View.VISIBLE);
                expander_unhealthy_protein.setImageResource(R.drawable.ic_baseline_expand_less_24);
                list_healthy_protein.setVisibility(View.VISIBLE);
                expander_healthy_protein.setImageResource(R.drawable.ic_baseline_expand_less_24);
            } else if (list_unhealthy_protein.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(layout_unhealthy_protein, new AutoTransition());
                list_unhealthy_protein.setVisibility(View.GONE);
                expander_unhealthy_protein.setImageResource(R.drawable.ic_baseline_expand_more_24);
                list_healthy_protein.setVisibility(View.GONE);
                expander_healthy_protein.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }
        });
    }
}