package eugen.enterprise.easyfit.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import eugen.enterprise.easyfit.R;
import eugen.enterprise.easyfit.acquaintance.interfaces.IMuscleGroup;

public class WorkoutAdapter extends ArrayAdapter<IMuscleGroup> {
    private List<IMuscleGroup> muscleGroups = new ArrayList<>();

    public WorkoutAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    static class ViewHolder {
        TextView txt_workout_muscleGroup;
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
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        final IMuscleGroup muscleGroup = getItem(position);
        viewHolder.txt_workout_muscleGroup.setText(muscleGroup.getName());

        return row;
    }
}
