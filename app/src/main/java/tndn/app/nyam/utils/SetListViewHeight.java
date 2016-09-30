package tndn.app.nyam.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;


public class SetListViewHeight {
    public void init(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        ListAdapter adapter = listView.getAdapter();

        int listviewHeight = 0;

        listView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        listviewHeight = listView.getMeasuredHeight() * adapter.getCount() + (adapter.getCount() * listView.getDividerHeight());


        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = listviewHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();


    }
}
