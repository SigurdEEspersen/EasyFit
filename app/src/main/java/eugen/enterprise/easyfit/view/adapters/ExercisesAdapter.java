package eugen.enterprise.easyfit.view.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import java.util.ArrayList;
import java.util.List;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.helpers.Common;
import eugen.enterprise.easyfit.acquaintance.helpers.Workout;
import eugen.enterprise.easyfit.acquaintance.interfaces.IExercise;
import eugen.enterprise.easyfit.view.fragments.WorkoutFragment;

public class ExercisesAdapter extends ArrayAdapter<IExercise> {
    private List<IExercise> exercises = new ArrayList<>();
    private Context context;
    private Workout workout;
    private Activity activity;
    private ListView parentList;
    private WorkoutFragment fragment;

    public ExercisesAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void injectData(Context context, Activity activity, Workout workout, ListView parentList, WorkoutFragment fragment) {
        this.context = context;
        this.activity = activity;
        this.workout = workout;
        this.parentList = parentList;
        this.fragment = fragment;
    }

    static class ViewHolder {
        TextView txt_workout_exercise;
        TextView txt_workout_exercise_info;
        ImageButton img_expander;
        RelativeLayout btn_expandSets;
        RelativeLayout layout_sets;
        CardView cardView;
        ListView workout_list_sets;
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

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.exercise_card, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_workout_exercise = row.findViewById(R.id.txt_workout_exercise);
            viewHolder.txt_workout_exercise_info = row.findViewById(R.id.txt_workout_exercise_info);
            viewHolder.cardView = row.findViewById(R.id.workout_baseCard_exercise);
            viewHolder.img_expander = row.findViewById(R.id.img_expander);
            viewHolder.layout_sets = row.findViewById(R.id.layout_sets);
            viewHolder.btn_expandSets = row.findViewById(R.id.btn_expandSets);
            viewHolder.workout_list_sets = row.findViewById(R.id.workout_list_sets);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        final IExercise exercise = getItem(position);
        viewHolder.txt_workout_exercise.setText(exercise.getName());

        String workoutLoad = "";
        switch (workout.getWorkoutLoad()) {
            case Regular:
                workoutLoad = "10-12 reps";
                break;
            case Medium:
                workoutLoad = "8 reps";
                break;
            case Heavy:
                workoutLoad = "4-6 reps";
                break;
        }
        viewHolder.txt_workout_exercise_info.setText(workout.getSetsPrExercise() + " sets of " + workoutLoad);

        viewHolder.btn_expandSets.setOnClickListener(v -> {
            if (viewHolder.layout_sets.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(viewHolder.cardView, new AutoTransition());
                viewHolder.layout_sets.setVisibility(View.VISIBLE);
                viewHolder.img_expander.setImageResource(R.drawable.ic_baseline_expand_less_24);
                Common.updateParentListView(parentList, viewHolder.workout_list_sets, true);
            } else if (viewHolder.layout_sets.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(viewHolder.cardView, new AutoTransition());
                viewHolder.layout_sets.setVisibility(View.GONE);
                viewHolder.img_expander.setImageResource(R.drawable.ic_baseline_expand_more_24);
                Common.updateParentListView(parentList, viewHolder.workout_list_sets, false);
            }
        });

        View finalRow = row;
        viewHolder.workout_list_sets.setOnTouchListener((v, event) -> {
            Common.hideKeyboard(context, finalRow, activity);
            return false;
        });

        SetsAdapter adapter = new SetsAdapter(context, R.layout.exercise_card);
        adapter.injectData(context, activity, parentList, viewHolder.workout_list_sets, fragment);
        for (int i = 0; i < workout.getSetsPrExercise(); i++) {
            adapter.add(exercise);
        }
        viewHolder.workout_list_sets.setAdapter(adapter);
        Common.updateListViewHeight(viewHolder.workout_list_sets);

        return row;
    }
}
