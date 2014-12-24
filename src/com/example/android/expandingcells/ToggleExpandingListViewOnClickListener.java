package com.example.android.expandingcells;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class ToggleExpandingListViewOnClickListener implements View.OnClickListener {

	private static final String LOG_TAG = ToggleExpandingListViewOnClickListener.class.getName();

	private View expandableView;
	private ExpandingListView expandingListView;
	private int togglerButtonResId;
	private int collapsedSize;

	public ToggleExpandingListViewOnClickListener(ExpandingListView expandingListView, View expandableView,
		int togglerButtonResId) {
		this.expandableView = expandableView;
		this.expandingListView = expandingListView;
		this.togglerButtonResId = togglerButtonResId;
	}

	@Override
	public void onClick(View v) {
		Log.wtf(LOG_TAG, "OnClickListener()" + v);
		CompoundButton toggler = (CompoundButton) v.findViewById(togglerButtonResId);
		if (toggler.isChecked()) {
			collapsedSize = expandableView.getHeight();
			expandingListView.expandView(expandableView);
		}
		else {
			expandingListView.collapseView(expandableView, collapsedSize);
		}
	}

}
