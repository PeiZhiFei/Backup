package com.hitoosoft.hrssapp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hitoosoft.hrssapp.R;

public class ToastUtil {
	private final static int IMAGE_NOIMAGE = 0;
	private final static int IMAGE_NORMAL = 1;
	private final static int IMAGE_SUCCESS = 2;
	private final static int IMAGE_ERROE = 3;

	/**
	 * @param context
	 * @param string
	 * @notice 显示纯文字短消息
	 */
	public static void toast(Context context, String string) {
		toastShow(context, string, IMAGE_NOIMAGE, false);
	}

	/**
	 * @param context
	 * @param string
	 * @param imageType
	 *            1：正常，2：成功；3：警告
	 * @notice 显示带图标的短消息，图标定义
	 */
	public static void toast(Context context, String string, final int imageType) {
		toastShow(context, string, imageType, false);
	}

	/**
	 * @param context
	 * @param string
	 * @param imageType
	 *            1：正常，2：成功；3：警告
	 * @param time
	 *            ：true为长消息，false为短消息
	 * @notice 显示自定义图标的消息，图标说明，0：无；1：正常；2：成功；3：警告；
	 */
	public static void toast(Context context, String string, int imageType,
			boolean time) {
		toastShow(context, string, imageType, true);
	}

	private static void toastShow(final Context context, final String string,
			final int imageType, final boolean time) {
		try {
			// 保证在非UI线程里面调用也能显示
			((Activity) context).runOnUiThread(new Runnable() {
				public void run() {
					// xml中的dp要转为px
					int dp10 = dp2px(context, 10);
					int dp5 = dp2px(context, 5);
					RelativeLayout layout = new RelativeLayout(context);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.setMargins(dp10, dp10, dp10, dp10);
					layout.setPadding(dp10, dp10, dp10, dp10);
					layout.setBackgroundResource(R.drawable.white_blue_line);
					layout.setLayoutParams(params);
					ImageView imageView = new ImageView(context);
					RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					params2.setMargins(dp5, 0, 0, 0);
					params2.addRule(RelativeLayout.CENTER_VERTICAL);
					imageView.setLayoutParams(params2);
					imageView.setId(1);
					// 相对布局的上下左右关系还是需要设置id的
					TextView textView = new TextView(context);
					RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					// 设置marginleft
					params3.setMargins(dp10, 0, 0, 0);
					params3.addRule(RelativeLayout.CENTER_VERTICAL);
					params3.addRule(RelativeLayout.RIGHT_OF, 1);
					textView.setTextColor(Color.parseColor("#ffffff"));
					textView.setTextSize(18);
					textView.setPadding(0, 0, dp10, 0);
					textView.setMaxLines(5);
					textView.setLayoutParams(params3);
					layout.addView(imageView);
					layout.addView(textView);
					Toast toast = new Toast(context);
					textView.setText("" + string);
					imageView.setVisibility(View.VISIBLE);
					switch (imageType) {
					case IMAGE_NORMAL:
						imageView.setImageResource(R.drawable.icon_notice);
						break;
					case IMAGE_SUCCESS:
						imageView.setImageResource(R.drawable.icon_success);
						break;
					case IMAGE_ERROE:
						imageView.setImageResource(R.drawable.icon_error);
						break;
					case IMAGE_NOIMAGE:
						imageView.setVisibility(View.GONE);
						break;
					default:
						// 数字超出范围则不显示，容错性
						imageView.setVisibility(View.GONE);
						break;
					}
					toast.setView(layout);
					toast.setGravity(Gravity.BOTTOM, 0, 200);
					if (time) {
						toast.setDuration(Toast.LENGTH_LONG);
					} else {
						toast.setDuration(Toast.LENGTH_SHORT);
					}
					toast.show();
				}
			});
		} catch (Exception e) {
		}
	}

	/**
	 * @param context
	 * @param dp
	 * @return int
	 * @notice DP转换为PX
	 */
	public static int dp2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

}