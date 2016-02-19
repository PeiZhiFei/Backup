package com.runkun.lbsq.view.swipeex;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class SwipeMenu {

	private Context mContext;
	private List<SwipeMenuItem> mItems;
	private int mViewType;
	private int mGroupPos;
	private int mChildPos;

	public SwipeMenu(Context context, int groupPos, int childPos) {
		mContext = context;
		mItems = new ArrayList<SwipeMenuItem>();
		mGroupPos = groupPos;
		mChildPos = childPos;
	}

	public Context getContext() {
		return mContext;
	}

	public void addMenuItem(SwipeMenuItem item) {
		mItems.add(item);
	}

	public void removeMenuItem(SwipeMenuItem item) {
		mItems.remove(item);
	}

	public List<SwipeMenuItem> getMenuItems() {
		return mItems;
	}

	public SwipeMenuItem getMenuItem(int index) {
		return mItems.get(index);
	}

	public int getViewType() {
		return mViewType;
	}

	public void setViewType(int viewType) {
		this.mViewType = viewType;
	}

	public int getGroupPos() {
		return mGroupPos;
	}

	public void setGroupPos(int groupPos) {
		mGroupPos = groupPos;
	}

	public int getChildPos() {
		return mChildPos;
	}

	public void setChildPos(int childPos) {
		mChildPos = childPos;
	}

	public void setPosition(int groupPosition, int childPosition) {
		mGroupPos = groupPosition;
		mChildPos = childPosition;
	}

}
