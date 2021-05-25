package eugen.enterprise.easyfit.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.helpers.Common;
import eugen.enterprise.easyfit.acquaintance.helpers.NotificationIndex;
import eugen.enterprise.easyfit.acquaintance.helpers.SetResult;
import eugen.enterprise.easyfit.acquaintance.interfaces.IExercise;
import eugen.enterprise.easyfit.view.fragments.WorkoutFragment;
import eugen.enterprise.easyfit.viewmodel.MacroViewModel;
import eugen.enterprise.easyfit.viewmodel.WorkoutViewModel;

public class SetsAdapter extends ArrayAdapter<IExercise> {
    private final List<IExercise> exercises = new ArrayList<>();
    private Context context;
    private Activity activity;
    private ListView setList;
    private WorkoutFragment fragment;
    private int muscleGroupIndex;
    private int exerciseIndex;

    public SetsAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void injectData(Context context, Activity activity, ListView setList, WorkoutFragment fragment, int muscleGroupIndex, int exerciseIndex) {
        this.context = context;
        this.activity = activity;
        this.setList = setList;
        this.fragment = fragment;
        this.muscleGroupIndex = muscleGroupIndex;
        this.exerciseIndex = exerciseIndex;
    }

    static class ViewHolder {
        TextView txt_workout_exercise_set;
        Button btn_startPause;
        RelativeLayout btn_set_data;
        RelativeLayout layout_set_result_data;
        EditText txt_set_result_reps;
        EditText txt_set_result_weight;
        Button btn_save_set_data;
    }

    @Override
    public void add(IExercise exercise) {
        exercises.add(exercise);
        super.add(exercise);
        notifyDataSetChanged();
    }

    @Override
    public IExercise getItem(int index) {
        return this.exercises.get(index);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.sets_card, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_workout_exercise_set = row.findViewById(R.id.txt_workout_exercise_set);
            viewHolder.btn_startPause = row.findViewById(R.id.btn_startPause);
            viewHolder.btn_set_data = row.findViewById(R.id.btn_set_data);
            viewHolder.layout_set_result_data = row.findViewById(R.id.layout_set_result_data);
            viewHolder.txt_set_result_reps = row.findViewById(R.id.txt_set_result_reps);
            viewHolder.txt_set_result_weight = row.findViewById(R.id.txt_set_result_weight);
            viewHolder.btn_save_set_data = row.findViewById(R.id.btn_save_set_data);

            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        final IExercise exercise = getItem(position);
        viewHolder.txt_workout_exercise_set.setText("#" + (position + 1) + " set");
        viewHolder.btn_startPause.setText("Pause " + exercise.getPauseDurationSeconds() + "s");
        WorkoutViewModel viewModel = new ViewModelProvider(fragment.requireActivity()).get(WorkoutViewModel.class);

        Thread thread = new Thread(() -> {
            int pause = exercise.getPauseDurationSeconds();

            try {
                while (pause > 0) {
                    viewHolder.btn_startPause.setText(String.valueOf(pause--));
                    Thread.sleep(1000);
                }

                activity.runOnUiThread(() -> {
                    viewHolder.btn_startPause.setText("0");
                    viewHolder.btn_startPause.setEnabled(false);
                    viewHolder.btn_startPause.setBackgroundResource(R.drawable.btn_primary_disabled);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        viewHolder.btn_startPause.setOnClickListener(v -> {
            Integer existingPause = viewModel.getPauseCountdown().getValue();
            if (existingPause != null && existingPause > 0) {
                Toast.makeText(context, "Ongoing pause", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.addNotificationListener(new NotificationIndex(muscleGroupIndex, exerciseIndex, position, exercise.getPauseDurationSeconds()));
            viewHolder.btn_startPause.setTextSize(24);
            if (!thread.isAlive()) {
                thread.start();
            }
        });

        Integer existingPause = viewModel.getPauseCountdown().getValue();
        if (existingPause != null && existingPause > 0) {
            NotificationIndex notificationIndex = viewModel.getNotificationListener().getValue();
            if (notificationIndex != null && notificationIndex.getMuscleGroupIndex() == muscleGroupIndex
                    && notificationIndex.getExerciseIndex() == exerciseIndex && notificationIndex.getSetIndex() == position) {
                Thread existingThread = new Thread(() -> {
                    int pause = existingPause;
                    activity.runOnUiThread(() -> viewHolder.btn_startPause.setTextSize(24));
                    try {
                        while (pause > 0) {
                            viewHolder.btn_startPause.setText(String.valueOf(pause--));
                            Thread.sleep(1000);
                        }

                        activity.runOnUiThread(() -> {
                            viewHolder.btn_startPause.setText("0");
                            viewHolder.btn_startPause.setEnabled(false);
                            viewHolder.btn_startPause.setBackgroundResource(R.drawable.btn_primary_disabled);
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                existingThread.start();
            }
        }

        viewHolder.btn_set_data.setOnClickListener(v -> {
            if (viewHolder.layout_set_result_data.getVisibility() == View.GONE) {
                viewHolder.layout_set_result_data.setVisibility(View.VISIBLE);
                Common.updateParentListView(setList, viewHolder.layout_set_result_data, true);
            } else {
                viewHolder.layout_set_result_data.setVisibility(View.GONE);
                Common.updateParentListView(setList, viewHolder.layout_set_result_data, false);
            }
        });

        View finalRow = row;
        viewHolder.btn_save_set_data.setOnClickListener(v -> {
            if (viewHolder.txt_set_result_reps.getText().toString().isEmpty()) {
                Toast.makeText(activity, "Specify number of reps you got", Toast.LENGTH_SHORT).show();
                return;
            }
            if (viewHolder.txt_set_result_weight.getText().toString().isEmpty()) {
                Toast.makeText(activity, "Specify amount of weight you used", Toast.LENGTH_SHORT).show();
                return;
            }

            Common.hideKeyboard(context, finalRow, activity);
            SetResult result = new SetResult();
            result.setExerciseId(exercise.getId());
            result.setReps(Integer.parseInt(viewHolder.txt_set_result_reps.getText().toString()));
            result.setWeight(Double.parseDouble(viewHolder.txt_set_result_weight.getText().toString()));
            viewModel.addSetResult(result, context);
        });

        List<NotificationIndex> usedPauses = viewModel.getUsedPauses().getValue();
        for (NotificationIndex usedPause : usedPauses) {
            if (usedPause.getMuscleGroupIndex() == muscleGroupIndex
                    && usedPause.getExerciseIndex() == exerciseIndex
                    && usedPause.getSetIndex() == position) {
                viewHolder.btn_startPause.setTextSize(24);
                viewHolder.btn_startPause.setText("0");
                viewHolder.btn_startPause.setEnabled(false);
                viewHolder.btn_startPause.setBackgroundResource(R.drawable.btn_primary_disabled);
            }
        }

        return row;
    }
}
