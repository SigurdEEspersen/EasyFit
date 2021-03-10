package eugen.enterprise.easyfit.acquaintance.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Common {

    public static void hideKeyboard(Context context, View view, Activity activity) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
