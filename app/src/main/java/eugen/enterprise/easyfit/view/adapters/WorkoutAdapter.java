package eugen.enterprise.easyfit.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import eugen.enterprise.easyfit.acquaintance.interfaces.IMuscleGroup;

public class WorkoutAdapter extends ArrayAdapter<IMuscleGroup> {
    private List<IMuscleGroup> muscleGroups = new ArrayList<>();
    private Context context;
    private Workout workout;
    private Activity activity;

    public WorkoutAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void injectData(Context context, Activity activity, Workout workout) {
        this.context = context;
        this.activity = activity;
        this.workout = workout;
    }

    static class ViewHolder {
        TextView txt_workout_muscleGroup;
        ImageButton img_expander;
        LinearLayout btn_expandExercises;
        RelativeLayout layout_exercises;
        CardView cardView;
        ListView exercisesList;
    }

    @Override
    public void add(IMuscleGroup muscleGroup) {
        muscleGroups.add(muscleGroup);
        super.add(muscleGroup);
        notifyDataSetChanged();
    }

    @Override
    public IMuscleGroup getItem(int index) {
        return this.muscleGroups.get(index);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.workout_card, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_workout_muscleGroup = row.findViewById(R.id.txt_workout_muscleGroup);
            viewHolder.cardView = row.findViewById(R.id.workout_baseCard);
            viewHolder.img_expander = row.findViewById(R.id.img_expander);
            viewHolder.layout_exercises = row.findViewById(R.id.layout_exercises);
            viewHolder.btn_expandExercises = row.findViewById(R.id.btn_expandExercises);
            viewHolder.exercisesList = row.findViewById(R.id.workout_list_exercises);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        final IMuscleGroup muscleGroup = getItem(position);
        viewHolder.txt_workout_muscleGroup.setText(muscleGroup.getName());

        viewHolder.btn_expandExercises.setOnClickListener(v -> {
            if (viewHolder.layout_exercises.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(viewHolder.cardView,
                        new AutoTransition());
                viewHolder.layout_exercises.setVisibility(View.VISIBLE);
                viewHolder.img_expander.setImageResource(R.drawable.ic_baseline_expand_less_24);
            } else if (viewHolder.layout_exercises.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(viewHolder.cardView,
                        new AutoTransition());
                viewHolder.layout_exercises.setVisibility(View.GONE);
                viewHolder.img_expander.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }
        });

        ExercisesAdapter adapter = new ExercisesAdapter(context, R.layout.exercise_card);
        adapter.injectData(context, activity, workout);
        for (IExercise exercise : muscleGroup.getExercises()) {
            adapter.add(exercise);
        }
        viewHolder.exercisesList.setAdapter(adapter);
        Common.updateListViewHeight(viewHolder.exercisesList);

        return row;
    }
}
