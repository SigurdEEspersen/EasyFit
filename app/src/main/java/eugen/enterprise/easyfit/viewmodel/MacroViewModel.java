package eugen.enterprise.easyfit.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import eugen.enterprise.easyfit.acquaintance.helpers.MacroResult;
import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.model.repository.MacroService;

public class MacroViewModel extends ViewModel {

    MutableLiveData<String> saveStatus;
    MutableLiveData<MacroResult> savedMacros;

    MacroService macroService;

    public MacroViewModel() {
        macroService = new MacroService();
        saveStatus = new MutableLiveData<>();
        savedMacros = new MutableLiveData<>();
    }

    public MutableLiveData<String> getSaveStatus() {
        return saveStatus;
    }

    public MutableLiveData<MacroResult> getSavedMacros() {
        return savedMacros;
    }

    public void saveMacros(MacroResult result, Context c) {
        macroService.saveMacros(result, new Callback() {
            @Override
            public void onResponse(Object o) {
                saveStatus.postValue((String) o);
            }
        }, c);
    }

    public void loadMacros(Context c) {
        macroService.loadMacros(new Callback() {
            @Override
            public void onResponse(Object o) {
                savedMacros.postValue((MacroResult) o);
            }
        }, c);
    }
}