package com.runkun.lbsq.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.runkun.lbsq.R;
import com.runkun.lbsq.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class RatingBarView extends LinearLayout {
	private List<ImageView> mStars = new ArrayList<ImageView>();

	private boolean mClickable = true;
	private OnRatingListener onRatingListener;
	private Object bindObject;
	private float starImageSize;
	private int starCount;
	private Drawable starEmptyDrawable;
	private Drawable starFillDrawable;

	public void setStarFillDrawable(Drawable starFillDrawable) {
		this.starFillDrawable = starFillDrawable;
	}

	public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
		this.starEmptyDrawable = starEmptyDrawable;
	}

	public void setStarCount(int startCount) {
		this.starCount = starCount;
	}

	public void setStarImageSize(float starImageSize) {
		this.starImageSize = starImageSize;
	}

	private int startCount;

	public void setBindObject(Object bindObject) {
		this.bindObject = bindObject;
	}

	public void setOnRatingListener(OnRatingListener onRatingListener) {
		this.onRatingListener = onRatingListener;
	}

	public void setmClickable(boolean clickable) {
		this.mClickable = clickable;
	}

	public RatingBarView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.HORIZONTAL);
		starImageSize = 10;
		starCount = 5;
		starEmptyDrawable = getResources()
				.getDrawable(R.drawable.evaluate_gray);
		starFillDrawable = getResources().getDrawable(R.drawable.evaluate_gold);

		for (int i = 0; i < starCount; ++i) {
			ImageView imageView = getStarImageView(context, attrs);
			imageView.setScaleType(ScaleType.CENTER_INSIDE);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mClickable) {
						setStar(indexOfChild(v) + 1);
						if (onRatingListener != null) {
							onRatingListener.onRating(bindObject,
									indexOfChild(v) + 1);
						}
					}

				}
			});
			// LinearLayout.LayoutParams layoutParams=new
			// LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			int x = Tools.getWidth(context) / starCount - 30;
			LinearLayout.LayoutParams layoutParams = new LayoutParams(x, x);
			layoutParams.setMargins(20, 20, 0, 0);
			addView(imageView, layoutParams);

		}
		setPadding(10, 0, 10, 0);
		setGravity(Gravity.CENTER);

	}

	private ImageView getStarImageView(Context context, AttributeSet attrs) {

		ImageView imageView = new ImageView(context);
		LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(
				Math.round(starImageSize), Math.round(starImageSize),
				Gravity.CENTER);
		// para.bottomMargin = 10;
		// para.leftMargin = 10;
		// para.rightMargin = 10;
		// para.topMargin = 10;
		imageView.setLayoutParams(para);
		// imageView.setPadding(5, 0, 20, 0);
		int px = Tools.dp2px(context, 13);
		imageView.setPadding(px, px, px, px);
		imageView.setImageDrawable(starEmptyDrawable);
		imageView.setMaxWidth(10);
		imageView.setMaxHeight(10);
		return imageView;

	}

	public void setStar(int starCount) {
		setStar(starCount, true);
	}

	public void setStar(int starCount, boolean animation) {
		starCount = starCount > this.starCount ? this.starCount : starCount;
		starCount = starCount < 0 ? 0 : starCount;

		for (int i = 0; i < starCount; ++i) {
			((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
			if (animation) {
//				new AnimatorSet().playTogether(
//						ObjectAnimator.ofFloat(getChildAt(i), "alpha", 0, 1, 1, 1),
//						ObjectAnimator.ofFloat(getChildAt(i), "scaleX", 0.3f, 1.05f, 0.9f, 1),
//						ObjectAnimator.ofFloat(getChildAt(i), "scaleY", 0.3f, 1.05f, 0.9f, 1));
				getChildAt(i).startAnimation(getScaleAnimation(0.5f, 1.0f, 500));
//				YoYo.with(Techniques.BounceIn).duration(400)
//						.playOn(getChildAt(i));
			}
		}

		for (int i = this.starCount - 1; i >= starCount; --i) {
			((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
		}

	}

	public interface OnRatingListener {

		void onRating(Object bindObject, int RatingScore);

	}

	public int getStartCount() {
		int count = 0;
		for (int i = 0; i < starCount; ++i) {
			if (((ImageView) getChildAt(i)).getDrawable() == starFillDrawable) {
				count++;
			}
		}
		return count;
	}


	public static ScaleAnimation getScaleAnimation(float start, float end,
												   int duration) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(start, end, start,
				end, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(duration);
//		scaleAnimation.setFillAfter(true);
		scaleAnimation.setInterpolator(new BounceInterpolator());
		return scaleAnimation;
	}

}
