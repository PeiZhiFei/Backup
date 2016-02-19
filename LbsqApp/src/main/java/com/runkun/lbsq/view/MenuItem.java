package com.runkun.lbsq.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.runkun.lbsq.R;

public class MenuItem extends LinearLayout {
	private ImageButton ib;
	private TextView tv;

	public MenuItem(Context context) {
		super(context);
	}

	public MenuItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.include_menu_item, this, true);
		ib = (ImageButton) findViewById(R.id.menu_top_img);
		tv = (TextView) findViewById(R.id.menu_bottom_text);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public MenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setTextViewForText(String parm) {
		tv.setText(parm);
	}

	public void setImageButton(int imgResource) {
		ib.setScaleType(ScaleType.FIT_XY);
		ib.setBackgroundResource(imgResource);

	}

}
