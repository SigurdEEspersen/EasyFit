package eugen.enterprise.easyfit.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import eugen.enterprise.easyfit.acquaintance.helpers.SetResult;
import eugen.enterprise.easyfit.acquaintance.interfaces.IExercise;

public class SetResultDataAdapter extends ArrayAdapter<SetResult> {
    private List<SetResult> setResults = new ArrayList<>();

    public SetResultDataAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    static class ViewHolder {
        TextView txt_set_result_exercise;
        TextView txt_set_result_reps;
        TextView txt_set_result_weight;
    }

    @Override
    public void add(SetResult exercise) {
        setResults.add(exercise);
        super.add(exercise);
        notifyDataSetChanged();
    }

    @Override
    public SetResult getItem(int index) {
        return this.setResults.get(index);
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.set_result_data_card, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_set_result_exercise = row.findViewById(R.id.txt_set_result_exercise);
            viewHolder.txt_set_result_reps = row.findViewById(R.id.txt_set_result_reps);
            viewHolder.txt_set_result_weight = row.findViewById(R.id.txt_set_result_weight);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        final SetResult setResult = getItem(position);
        viewHolder.txt_set_result_exercise.setText(setResult.getExerciseName());
        viewHolder.txt_set_result_reps.setText(String.valueOf(setResult.getReps()));

        String weight = String.valueOf(setResult.getWeight());
        if (weight.endsWith(".0")) {
            weight = String.valueOf(Math.round(setResult.getWeight()));
        }
        viewHolder.txt_set_result_weight.setText(weight);

        return row;
    }
}
