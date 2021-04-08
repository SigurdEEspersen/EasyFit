package eugen.enterprise.easyfit.acquaintance.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class Common {

    public static void hideKeyboard(Context context, View view, Activity activity) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void updateListViewHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void updateParentListView(ListView parent, ListView child, Boolean add) {
        ListAdapter adapter = child.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, child);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = parent.getLayoutParams();
        if (add) {
            params.height += totalHeight + 35;
        } else {
            params.height -= totalHeight + 35;
        }
        parent.setLayoutParams(params);
    }

    public static void updateParentListView(ListView grandparent, ListView parent, RelativeLayout child, Boolean add) {
        child.measure(0,0);
        int height = child.getMeasuredHeight();

        ViewGroup.LayoutParams params_grandparent = grandparent.getLayoutParams();
        ViewGroup.LayoutParams params_parent = parent.getLayoutParams();
        if (add) {
            params_grandparent.height += height;
            params_parent.height += height;
        } else {
            params_grandparent.height -= height;
            params_parent.height -= height;
        }
        grandparent.setLayoutParams(params_grandparent);
        parent.setLayoutParams(params_parent);
    }
}
