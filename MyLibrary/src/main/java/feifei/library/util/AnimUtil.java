package feifei.library.util;

import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import feifei.library.R;


public class AnimUtil
{

    private static final int duration_short = 300;

    public static void animTo(Activity activity)
    {
        animActivity(activity, false);
    }

    public static void animToFinish(Activity activity)
    {
        activityFinish(activity);
        animActivity(activity, false);
    }

    public static void animBack(Activity activity)
    {
        animActivity(activity, true);
    }

    public static void animBackFinish(Activity activity)
    {
        activityFinish(activity);
        animActivity(activity, true);
    }


    public static void animShow(View view)
    {
        view.startAnimation(getaAlphaAnimation(0, 1, duration_short));
    }

    public static void animShowShort(View view)
    {
        view.startAnimation(getaAlphaAnimation(0, 1, duration_short));
    }

    public static void animGone(View view)
    {
        view.startAnimation(getaAlphaAnimation(1, 0, duration_short));
    }


    public static void animShakeText(View view)
    {
        Animation shakeAnim = new TranslateAnimation (0, 10, 0, 0);
        shakeAnim.setInterpolator(new CycleInterpolator (5));
        shakeAnim.setDuration(500);
        view.startAnimation(shakeAnim);
    }

    public static void animScan(View view)
    {
        TranslateAnimation translateAnimation = new TranslateAnimation (
                Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
                0.95f);
        translateAnimation.setDuration(1500);
        translateAnimation.setRepeatCount(-1);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setInterpolator(new LinearInterpolator ());
        view.setAnimation(translateAnimation);
    }

    public static AlphaAnimation getaAlphaAnimation(int from, int to,
                                                    int duration)
    {
        AlphaAnimation alphaAnimationShow = new AlphaAnimation (from, to);
        alphaAnimationShow.setDuration(duration);
        return alphaAnimationShow;
    }

    public static RotateAnimation getRotateAnimation(float from, float to,
                                                     int duration)
    {
        RotateAnimation rotateAnimation = new RotateAnimation (from, to,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator ());
        rotateAnimation.setDuration(duration);
        rotateAnimation.setFillAfter(true);
        return rotateAnimation;
    }

    public static RotateAnimation getDialogRotateAnimation()
    {
        RotateAnimation rotateAnimation = new RotateAnimation (0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(1500);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setStartOffset(-1);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setInterpolator(new LinearInterpolator ());
        return rotateAnimation;
    }

    public static RotateAnimation getDialogRotateAnimation2()
    {
        RotateAnimation mAnim = new RotateAnimation (0, 360, Animation.RESTART,
                0.5f, Animation.RESTART, 0.5f);
        mAnim.setDuration(1500);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setRepeatMode(Animation.INFINITE);
        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
        mAnim.setInterpolator(new LinearInterpolator ());
        return mAnim;
    }

    public static ScaleAnimation getScaleAnimation(float start, float end,
                                                   int duration)
    {
        ScaleAnimation scaleAnimation = new ScaleAnimation (start, end, start,
                end, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillAfter(true);
        return scaleAnimation;
    }

    /**
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @param duration：自定义时间
     * @return TranslateAnimation
     * @notice 获取位移动画，自定义时间
     */
    public static TranslateAnimation getTranslateAnimation(float x1, float x2, float y1, float y2, int duration)
    {
        TranslateAnimation translateAnimation = new TranslateAnimation (Animation.RELATIVE_TO_SELF, x1, Animation.RELATIVE_TO_SELF, x2, Animation.RELATIVE_TO_SELF, y1, Animation.RELATIVE_TO_SELF, y2);
        translateAnimation.setDuration(duration);
        translateAnimation.setFillAfter(true);
        return translateAnimation;

    }

    /**
     * @param view
     * @notice 控件下落振动反弹的动画
     */
    @TargetApi(Build.VERSION_CODES.DONUT)
    public static void animFallDown(View view)
    {
        AnimationSet fallDown = new AnimationSet (true);
        fallDown.addAnimation(getaAlphaAnimation(0, 1, 800));
        fallDown.addAnimation(getTranslateAnimation(0, 0, -1.0f, 0, 1000));
        fallDown.setInterpolator(new BounceInterpolator ());
        view.startAnimation(fallDown);
    }

    public static Animation getScaleGoneAnimation()
    {
        AnimationSet animationSet = new AnimationSet (true);
        animationSet.addAnimation(new ScaleAnimation (1.0f, 2.0f, 1.0f, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f));
        animationSet.addAnimation(new AlphaAnimation (1.0f, 0.0f));
        animationSet.setDuration(500);
        animationSet.setInterpolator(new DecelerateInterpolator ());
        // animationSet.setFillAfter(true);
        return animationSet;
    }

    public static void setGone(View view)
    {
        if (view.getVisibility() != View.GONE)
        {
            view.setVisibility(View.GONE);
        }
    }

    public static void setInvisible(View view)
    {
        if (view.getVisibility() != View.INVISIBLE)
        {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void setVisible(View view)
    {
        if (view.getVisibility() != View.VISIBLE)
        {
            view.setVisibility(View.VISIBLE);
        }
    }

    private static void animActivity(Activity activity,
                                     boolean back)
    {
//        int INRIGHT = R.anim.activity_in_from_right;
//        int OUTLEFT = R.anim.activity_out_to_left;
//        int INLEFT = R.anim.activity_in_from_left;
//        int OUTRIGHT = R.anim.activity_out_to_right;

//        int INRIGHT = R.anim.zoom_enter;
//        int OUTLEFT = R.anim.zoom_exit;
//        int INLEFT = R.anim.unzoom_in;
//        int OUTRIGHT = R.anim.unzoom_out;
        int INRIGHT = R.anim.toast_in;
        int OUTLEFT = R.anim.toast_out;
        int INLEFT = R.anim.toast_in2;
        int OUTRIGHT = R.anim.toast_out2;

//        int INFADE = R.anim.fade_in;
//        int HOLD = R.anim.fade_hold;
//        int OUTFADE = R.anim.fade_out;

        if (!back)
        {
            activity.overridePendingTransition(INRIGHT, OUTLEFT);
        } else
        {
            activity.overridePendingTransition(INLEFT, OUTRIGHT);
        }
    }

    //左右摇晃的动画
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static ObjectAnimator nope(View view)
    {
        // int delta = view.getResources().getDimensionPixelOffset(R.dimen.spacing_medium);
        int delta = 10;
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat (0f, 0),
                Keyframe.ofFloat (.10f, -delta),
                Keyframe.ofFloat (.26f, delta),
                Keyframe.ofFloat (.42f, -delta),
                Keyframe.ofFloat (.58f, delta),
                Keyframe.ofFloat (.74f, -delta),
                Keyframe.ofFloat (.90f, delta),
                Keyframe.ofFloat (1f, 0f)
        );

        return ObjectAnimator.ofPropertyValuesHolder (view, pvhTranslateX).
                setDuration(500);
    }

    public static void shake(View view)
    {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            ObjectAnimator object = nope(view);
//            object.setRepeatCount(3);
            object.start();
        }
    }


    private static void activityFinish(Activity activity)
    {
        activity.finish();
    }

    public static void animScaleAlpha(View view)
    {
        AnimationSet set = new AnimationSet (true);
        set.addAnimation(getaAlphaAnimation(0, 1, 800));
        set.addAnimation(getScaleAnimation(0.5f, 1, 800));
        view.startAnimation(set);
    }

    public static void animImageScreen(final ImageView imageView,
                                       final Drawable mPicture1, final Drawable mPicture2,
                                       final Drawable mPicture3)
    {
        final Animation mFadeIn = AnimUtil.getaAlphaAnimation(0, 1, 1000);
        final Animation mFadeOut = AnimUtil.getaAlphaAnimation(1, 0, 1000);
        final AnimationSet anim = new AnimationSet (true);
        anim.addAnimation(mFadeOut);
        final ScaleAnimation helperAnimation = new ScaleAnimation (1.1f, 1.0f,
                1.1f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        helperAnimation.setFillAfter(false);
        helperAnimation.setInterpolator(new DecelerateInterpolator ());
        helperAnimation.setDuration(1500);
        anim.addAnimation(helperAnimation);

        final ScaleAnimation mFadeInScale = new ScaleAnimation (1f, 1.1f, 1f,
                1.1f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mFadeInScale.setFillAfter(false);
        mFadeInScale.setInterpolator(new DecelerateInterpolator ());
        mFadeInScale.setDuration(6000);
        imageView.setImageDrawable(mPicture1);
        imageView.startAnimation(mFadeIn);
        mFadeIn.setAnimationListener(new AnimationListener ()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                imageView.startAnimation(mFadeInScale);
            }
        });
        mFadeInScale.setAnimationListener(new AnimationListener ()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                imageView.startAnimation(anim);
            }
        });
        anim.setAnimationListener(new AnimationListener ()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if (imageView.getDrawable().equals(mPicture1))
                {
                    imageView.setImageDrawable(mPicture2);
                } else if (imageView.getDrawable().equals(mPicture2))
                {
                    imageView.setImageDrawable(mPicture3);
                } else if (imageView.getDrawable().equals(mPicture3))
                {
                    imageView.setImageDrawable(mPicture1);
                }
                imageView.startAnimation(mFadeIn);
            }
        });

    }

    public static void animImageRotate(View view)
    {
        RotateAnimation rotateAnimation = new RotateAnimation (0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(10000);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new OvershootInterpolator ());
        view.startAnimation(rotateAnimation);
    }

    public static void animListShow(ViewGroup viewGroup)
    {
        AnimationSet set = new AnimationSet (true);
        Animation animation = new AlphaAnimation (0.0f, 1.0f);
        animation.setDuration(300);
        set.addAnimation(animation);
        animation = new TranslateAnimation (Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController (
                set, 0.5f);
        viewGroup.setLayoutAnimation(controller);
    }

    public static void animDoorTextHint(View view)
    {
        Animation anim = getaAlphaAnimation(0, 1, 1500);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);
    }

    public static void animLayoutAlpha(LinearLayout layout)
    {
        LayoutTransition transition = new LayoutTransition ();
        ObjectAnimator appearing = ObjectAnimator.ofFloat (layout, "alpha", 0f,
                1f);
        ObjectAnimator disappearing = ObjectAnimator.ofFloat (layout, "alpha",
                1f, 0f);
        appearing.setDuration(300);
        disappearing.setDuration(300);
        transition.setAnimator(LayoutTransition.APPEARING, appearing);
        transition.setAnimator(LayoutTransition.DISAPPEARING, disappearing);
    }


}
