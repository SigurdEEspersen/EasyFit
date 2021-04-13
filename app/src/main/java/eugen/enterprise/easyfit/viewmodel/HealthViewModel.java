package eugen.enterprise.easyfit.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HealthViewModel extends ViewModel {

    private MutableLiveData<Integer> selectedWaterBottle;

    public HealthViewModel() {
        selectedWaterBottle = new MutableLiveData<>();
    }

    public LiveData<Integer> getSelectedWaterBottle() {
        return selectedWaterBottle;
    }

    public void setSelectedWaterBottle(Integer button) {
        selectedWaterBottle.postValue(button);
    }
}