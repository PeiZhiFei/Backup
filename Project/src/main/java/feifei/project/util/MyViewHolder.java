package feifei.project.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyViewHolder {
	public SparseArray<View> viewSparseArray;
	int position;

	public View getmContentView() {
		return mContentView;
	}

	public int getPosition() {
		return position;
	}

	public Context context;
	public View mContentView;
//单例模式需要私有化构造否则getViewHolder没有任何意义
 private	MyViewHolder(Context context, int layoutId, View convertView,
			ViewGroup parent, int position) {
		this.context = context;
		this.position = position;
		viewSparseArray = new SparseArray<View>();
		mContentView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mContentView.setTag(this);

	}

	public static MyViewHolder getViewHolder(Context context, int layoutId,
			View convertView, ViewGroup parent, int position) {
		if (convertView == null) {
			return new MyViewHolder(context, layoutId, convertView, parent,
					position);
		} else {
			return (MyViewHolder) convertView.getTag();
		}
	}

	public <T extends View> T getView(int viewId) {
		View view = viewSparseArray.get(viewId);
		if (view == null) {
			view = mContentView.findViewById(viewId);
			viewSparseArray.put(viewId, view);
		}
		return (T) view;

	}

	public void setText(int viewID, String text) {
		TextView textView = getView(viewID);
		textView.setText(text);
	}

}
