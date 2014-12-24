/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.expandingcells;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;

/**
 * This is a custom array adapter used to populate the listview whose items will
 * expand to display extra content in addition to the default display.
 */
public class CustomArrayAdapter extends ArrayAdapter<ExpandableListItem> {

	private List<ExpandableListItem> mData;
	private int mLayoutViewResourceId;
	private ExpandingListView expandingListView;

	public CustomArrayAdapter(Context context, int layoutViewResourceId, List<ExpandableListItem> data,
		ExpandingListView expandingListView) {
		super(context, layoutViewResourceId, data);
		mData = data;
		mLayoutViewResourceId = layoutViewResourceId;
		this.expandingListView = expandingListView;
	}

	/**
	 * Populates the item in the listview cell with the appropriate data. This method
	 * sets the thumbnail image, the title and the extra text. This method also updates
	 * the layout parameters of the item's view so that the image and title are centered
	 * in the bounds of the collapsed view, and such that the extra text is not displayed
	 * in the collapsed state of the cell.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ExpandableListItem object = mData.get(position);

		if (convertView == null) {
			LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
			convertView = inflater.inflate(mLayoutViewResourceId, parent, false);
			convertView.setBackgroundColor(Color.BLACK);
		}

		/*
		LinearLayout linearLayout = (LinearLayout) (convertView.findViewById(R.id.item_linear_layout));
		LinearLayout.LayoutParams linearLayoutParams =
			new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, object.getCollapsedHeight());
		linearLayout.setLayoutParams(linearLayoutParams);
*/

		ImageView imgView = (ImageView) convertView.findViewById(R.id.image_view);
		TextView titleView = (TextView) convertView.findViewById(R.id.title_view);
		TextView textView = (TextView) convertView.findViewById(R.id.text_view);

		titleView.setText(object.getTitle());
		imgView.setImageBitmap(
			getCroppedBitmap(BitmapFactory.decodeResource(getContext().getResources(), object.getImgResource(), null)));
		textView.setText(object.getText());

		convertView.setLayoutParams(
			new ListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));

		Button button = (Button) convertView.findViewById(R.id.pressBtn);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PopupMenu popupMenu = new PopupMenu(getContext(), v);
				popupMenu.inflate(R.menu.popup_menu);
				popupMenu.show();
			}
		});
		ToggleButton viewToggleDetails = (ToggleButton) convertView.findViewById(R.id.toggle_details);
		viewToggleDetails.setFocusable(false);
		viewToggleDetails.setOnClickListener(
			new ToggleExpandingListViewOnClickListener(expandingListView, convertView, R.id.toggle_details));

		ExpandingLayout expandingLayout = (ExpandingLayout) convertView.findViewById(R.id.expanding_layout);
		expandingLayout.setExpandedHeight(object.getExpandedHeight());
		expandingLayout.setSizeChangedListener(object);

		if (!object.isExpanded()) {
			expandingLayout.setVisibility(View.GONE);
		}
		else {
			expandingLayout.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	/**
	 * Crops a circle out of the thumbnail photo.
	 */
	public Bitmap getCroppedBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		paint.setAntiAlias(true);

		int halfWidth = bitmap.getWidth() / 2;
		int halfHeight = bitmap.getHeight() / 2;

		canvas.drawCircle(halfWidth, halfHeight, Math.max(halfWidth, halfHeight), paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}


}