package com.hitoosoft.hrssapp.util;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

/**
 * @author   裴智飞
 * @date       2014-7-28
 * @date       下午8:17:38
 * @file         ToastCustom.java
 * @content  自己写的toast组件，默认显示在底部，白色，右进左出
 */
public class ToastCustom {

	public static int LENGTH = 4000;
	private final Context context;
	private View mView;
	private LayoutParams mLayoutParams;
	private boolean mFloating;

	public ToastCustom(Context context) {
		this.context = context;
	}

	public static ToastCustom toastShow(Context context, View layout) {
		ToastCustom result = new ToastCustom(context);
		result.mView = layout;
		result.mFloating = true;
		result.setLayoutGravity(Gravity.BOTTOM);
		return result;
	}

	public void show() {
		ToastManager manager = ToastManager.obtain((Activity) context);
		manager.add(this);
	}

	public boolean isShowing() {
		if (mFloating) {
			return mView != null && mView.getParent() != null;
		} else {
			return mView.getVisibility() == View.VISIBLE;
		}
	}

	public void cancel() {
		ToastManager.obtain((Activity) context).clearMsg(this);

	}

	public static void cancelAll() {
		ToastManager.clearAll();
	}

	public static void cancelAll(Context context) {
		ToastManager.release((Activity) context);
	}

	public Activity getActivity() {
		return (Activity) context;
	}

	public void setView(View view) {
		mView = view;
	}

	public View getView() {
		return mView;
	}

	public LayoutParams getLayoutParams() {
		if (mLayoutParams == null) {
			mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
		}
		return mLayoutParams;
	}

	public ToastCustom setLayoutGravity(int gravity) {
		mLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, gravity);
		return this;
	}

	public boolean isFloating() {
		return mFloating;
	}

	public void setFloating(boolean mFloating) {
		this.mFloating = mFloating;
	}

}
