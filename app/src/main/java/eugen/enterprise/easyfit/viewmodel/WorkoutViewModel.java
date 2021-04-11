package eugen.enterprise.easyfit.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import eugen.enterprise.easyfit.acquaintance.helpers.SetResult;
import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.model.repository.WorkoutService;

public class WorkoutViewModel extends ViewModel {

    private MutableLiveData<Boolean> addedSetResult;
    private WorkoutService workoutService;

    public WorkoutViewModel() {
        addedSetResult = new MutableLiveData<>();
        workoutService = new WorkoutService();
    }

    public LiveData<Boolean> getAddedSetResult() {
        return addedSetResult;
    }

    public void addSetResult(SetResult result, Context c) {
        workoutService.saveSetResult(result, new Callback() {
            @Override
            public void onResponse(Object o) {
                addedSetResult.postValue((Boolean) o);
            }
        }, c);
    }
}