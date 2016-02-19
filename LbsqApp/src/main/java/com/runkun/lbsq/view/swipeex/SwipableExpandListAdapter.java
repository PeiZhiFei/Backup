package com.runkun.lbsq.view.swipeex;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;

import com.runkun.lbsq.view.swipeex.SwipableExpandListView.OnMenuItemClickListener;
import com.runkun.lbsq.view.swipeex.SwipeMenuView.OnSwipeItemClickListener;

public abstract class SwipableExpandListAdapter extends
		BaseExpandableListAdapter implements OnSwipeItemClickListener {

	private ExpandableListAdapter mAdapter;
	private Context mContext;

	private OnMenuItemClickListener onMenuItemClickListener;

	public SwipableExpandListAdapter(Context context,
			ExpandableListAdapter adapter) {
		mAdapter = adapter;
		mContext = context;
	}

	@Override
	public int getGroupCount() {
		return mAdapter.getGroupCount();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mAdapter.getChildrenCount(groupPosition);
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mAdapter.getGroup(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mAdapter.getChild(groupPosition, childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return mAdapter.getGroupId(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return mAdapter.getChildId(groupPosition, childPosition);
	}

	@Override
	public boolean hasStableIds() {
		return mAdapter.hasStableIds();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		return mAdapter.getGroupView(groupPosition, isExpanded, convertView,
				parent);
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		SwipeMenuLayout layout = null;
		if (convertView == null) {
			View contentView = mAdapter.getChildView(groupPosition,
					childPosition, isLastChild, convertView, parent);
			SwipeMenu menu = new SwipeMenu(mContext, groupPosition,
					childPosition);
			// menu.setViewType(mAdapter.getItemViewType(position));
			createMenu(menu);
			SwipeMenuView menuView = new SwipeMenuView(menu,
					(SwipableExpandListView) parent);
			menuView.setOnSwipeItemClickListener(this);
			SwipableExpandListView listView = (SwipableExpandListView) parent;
			layout = new SwipeMenuLayout(contentView, menuView,
					listView.getCloseInterpolator(),
					listView.getOpenInterpolator());
			// layout.setPosition(position);
		} else {
			layout = (SwipeMenuLayout) convertView;

			View contentView = layout.getContentView();
			mAdapter.getChildView(groupPosition, childPosition, isLastChild,
					contentView, parent);

			SwipeMenuView menuView = layout.getMenuView();
			SwipeMenu menu = menuView.getMenu();
			menu.setGroupPos(groupPosition);
			menu.setChildPos(childPosition);

			layout.closeMenu();
			// layout.setPosition(position);
			// View view = mAdapter.getView(position, layout.getContentView(),
			// parent);
		}
		return layout;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return mAdapter.isChildSelectable(groupPosition, childPosition);
	}

	public void createMenu(SwipeMenu menu) {
	}

	@Override
	public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
		if (onMenuItemClickListener != null) {
			onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu,
					index);
		}
	}

	public void setOnMenuItemClickListener(
			OnMenuItemClickListener onMenuItemClickListener) {
		this.onMenuItemClickListener = onMenuItemClickListener;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		mAdapter.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		mAdapter.unregisterDataSetObserver(observer);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return mAdapter.areAllItemsEnabled();
	}

	@Override
	public boolean isEmpty() {
		return mAdapter.isEmpty();
	}
}
