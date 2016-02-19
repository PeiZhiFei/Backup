package feifei.project.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import feifei.project.R;


public class BottomView {
	private final View convertView;
	private final Context context;
	private Dialog bv;
	private boolean isTop = false;

	public BottomView(Context c, View convertView) {
		this.context = c;
		this.convertView = convertView;
	}

	public BottomView(Context c, int resource) {
		this.context = c;
		this.convertView = View.inflate(c, resource, null);
	}

	@SuppressWarnings("deprecation")
	public void showBottomView() {
		this.bv = new Dialog(this.context, R.style.dialog_bottom_style);
		this.bv.setCanceledOnTouchOutside(true);
		this.bv.getWindow().requestFeature(1);
		this.bv.setContentView(this.convertView);
		Window wm = this.bv.getWindow();
		WindowManager m = wm.getWindowManager();
		Display d = m.getDefaultDisplay();
		WindowManager.LayoutParams p = wm.getAttributes();
		p.width = (d.getWidth() * 1);
		if (this.isTop) {
			p.gravity = 48;
		} else {
			p.gravity = 80;
		}
		wm.setAttributes(p);
		this.bv.show();
	}

	public void setTopIfNecessary() {
		this.isTop = true;
	}

	public View getView() {
		return this.convertView;
	}

	public void dismissBottomView() {
		if (this.bv != null) {
			this.bv.dismiss();
		}
	}
}
