package eugen.enterprise.easyfit.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.helpers.Common;
import eugen.enterprise.easyfit.acquaintance.interfaces.IExercise;

public class SetsAdapter extends ArrayAdapter<IExercise> {
    private List<IExercise> exercises = new ArrayList<>();
    Activity activity;
    ListView exerciseList;
    ListView setList;

    public SetsAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void injectData(Activity activity, ListView exerciseList, ListView setList) {
        this.activity = activity;
        this.exerciseList = exerciseList;
        this.setList = setList;
    }

    static class ViewHolder {
        TextView txt_workout_exercise_set;
        Button btn_startPause;
        RelativeLayout btn_set_data;
        RelativeLayout layout_set_result_data;
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
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        final IExercise exercise = getItem(position);
        viewHolder.txt_workout_exercise_set.setText("#" + (position + 1) + " set");
        viewHolder.btn_startPause.setText("Pause " + exercise.getPauseDurationSeconds() + "s");

        viewHolder.btn_startPause.setOnClickListener(v -> {
            viewHolder.btn_startPause.setTextSize(24);
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
            thread.start();
        });

        viewHolder.btn_set_data.setOnClickListener(v -> {
            if (viewHolder.layout_set_result_data.getVisibility() == View.GONE) {
                viewHolder.layout_set_result_data.setVisibility(View.VISIBLE);
                Common.updateParentListView(exerciseList, setList, viewHolder.layout_set_result_data, true);
            } else {
                viewHolder.layout_set_result_data.setVisibility(View.GONE);
                Common.updateParentListView(setList, viewHolder.layout_set_result_data, false);
                Common.updateParentListView(exerciseList, setList, false);
            }
        });

        return row;
    }
}
