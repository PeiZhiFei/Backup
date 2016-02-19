package feifei.library.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TabWidget;
import android.widget.TextView;

import feifei.library.util.Tools;


/*
   数字角标组件
   BottomView bottomView = new BottomView(this, R.layout.test);
   bottomView.showBottomView();
   BadgeView badgeView=new BadgeView(this,bottomView.getView().findViewById(R.id.test));
   badgeView.setText("3");
   badgeView.setBadgeMargin(10);
   badgeView.show();

   代码中使用的一个范例，为0就不显示

       public static void badgeView (final Context context, final View view, final int number) {
           ((Activity) context).runOnUiThread (new Runnable () {
               public void run () {
                   BadgeView badgeView = new BadgeView (context, view);
                   if (number != 0) {
                       badgeView.setText (String.valueOf (number));
                       badgeView.show (true);
                   }
                   if (number == 0) {
                       badgeView.hide (true);
                   }
               }
           });
       }
   */
public class BadgeView extends TextView
{

    public static final int POSITION_TOP_LEFT = 1;
    public static final int POSITION_TOP_RIGHT = 2;
    public static final int POSITION_BOTTOM_LEFT = 3;
    public static final int POSITION_BOTTOM_RIGHT = 4;
    public static final int POSITION_CENTER = 5;

    private static final int DEFAULT_MARGIN_DIP = 5;
    private static final int DEFAULT_LR_PADDING_DIP = 5;
    private static final int DEFAULT_CORNER_RADIUS_DIP = 8;
    private static final int DEFAULT_POSITION = POSITION_TOP_RIGHT;
    private static final int DEFAULT_BADGE_COLOR = Color.parseColor ("#CCFF0000");
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    private static Animation fadeIn;
    private static Animation fadeOut;

    private Context context;
    private View target;

    private int badgePosition;
    private int badgeMarginH;
    private int badgeMarginV;
    private int badgeColor;

    private boolean isShown;

    private ShapeDrawable badgeBg;

    private int targetTabIndex;

    public BadgeView (Context context)
    {
        this(context, (AttributeSet) null, 0);
    }

    public BadgeView (Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param target  目标组件
     */
    public BadgeView (Context context, View target)
    {
        this(context, null, 0, target, 0);
    }

    /**
     * @param context
     * @param target  tab标签
     * @param index   tab的索引
     */
    public BadgeView (Context context, TabWidget target, int index)
    {
        this(context, null, 0, target, index);
    }

    public BadgeView (Context context, AttributeSet attrs, int defStyle)
    {
        this(context, attrs, defStyle, null, 0);
    }

    public BadgeView (Context context, AttributeSet attrs, int defStyle, View target, int tabIndex)
    {
        super(context, attrs, defStyle);
        init(context, target, tabIndex);
    }

    private void init(Context context, View target, int tabIndex)
    {
        this.context = context;
        this.target = target;
        this.targetTabIndex = tabIndex;
        badgePosition = DEFAULT_POSITION;
        badgeMarginH = Tools.dp2px (context, DEFAULT_MARGIN_DIP);
        badgeMarginV = badgeMarginH;
        badgeColor = DEFAULT_BADGE_COLOR;
        setTypeface(Typeface.DEFAULT_BOLD);
        int paddingPixels = Tools.dp2px (context, DEFAULT_LR_PADDING_DIP);
        setPadding(paddingPixels, 0, paddingPixels, 0);
        setTextColor(DEFAULT_TEXT_COLOR);
        fadeIn = new AlphaAnimation (0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator ());
        fadeIn.setDuration(200);
        fadeOut = new AlphaAnimation (1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator ());
        fadeOut.setDuration(200);
        isShown = false;
        if (this.target != null)
        {
            applyTo(this.target);
        } else
        {
            show();
        }
    }

    private void applyTo(View target)
    {

        LayoutParams lp = target.getLayoutParams();
        ViewParent parent = target.getParent();
        FrameLayout container = new FrameLayout (context);

        if (target instanceof TabWidget )
        {
            target = ((TabWidget) target).getChildTabViewAt(targetTabIndex);
            this.target = target;
            ((ViewGroup) target).addView(container, new LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            this.setVisibility(View.GONE);
            container.addView(this);
        } else
        {
            ViewGroup group = (ViewGroup) parent;
            int index = group.indexOfChild(target);
            group.removeView(target);
            group.addView(container, index, lp);
            container.addView(target);
            this.setVisibility(View.GONE);
            container.addView(this);
            group.invalidate();
        }
    }

    /**
     * @notice 设置组件可见
     */
    public void show()
    {
        show(false, null);
    }

    /**
     * @param animate：true则为默认的渐变动画
     * @notice 设置组件可见
     */
    public void show(boolean animate)
    {
        show(animate, fadeIn);
    }

    /**
     * @param anim：自定义动画
     * @notice 设置组件可见
     */
    public void show(Animation anim)
    {
        show(true, anim);
    }

    /**
     * @notice 设置组件不可见
     */
    public void hide()
    {
        hide(false, null);
    }

    /**
     * @param animate：true则为默认的渐变动画
     * @notice 设置组件不可见
     */
    public void hide(boolean animate)
    {
        hide(animate, fadeOut);
    }

    /**
     * @param anim：自定义动画
     * @notice 设置组件不可见
     */
    public void hide(Animation anim)
    {
        hide(true, anim);
    }

    /**
     * @notice 开关
     */
    public void toggle()
    {
        toggle(false, null, null);
    }

    /**
     * @param animate：true则为默认的渐变动画
     * @notice 开关带动画
     */
    public void toggle(boolean animate)
    {
        toggle(animate, fadeIn, fadeOut);
    }

    /**
     * @param animIn：开的动画
     * @param animOut：关的动画
     * @notice 开关带动画
     */
    public void toggle(Animation animIn, Animation animOut)
    {
        toggle(true, animIn, animOut);
    }

    @SuppressWarnings("deprecation")
    private void show(boolean animate, Animation anim)
    {
        if (getBackground() == null)
        {
            if (badgeBg == null)
            {
                badgeBg = getDefaultBackground();
            }
            setBackgroundDrawable(badgeBg);
        }
        applyLayoutParams();
        if (animate)
        {
            this.startAnimation(anim);
        }
        this.setVisibility(View.VISIBLE);
        isShown = true;
    }

    private void hide(boolean animate, Animation anim)
    {
        this.setVisibility(View.GONE);
        if (animate)
        {
            this.startAnimation(anim);
        }
        isShown = false;
    }

    private void toggle(boolean animate, Animation animIn, Animation animOut)
    {
        if (isShown)
        {
            hide(animate && (animOut != null), animOut);
        } else
        {
            show(animate && (animIn != null), animIn);
        }
    }

    /**
     * @param offset
     * @return int
     * @notice 计数增加
     */
    public int increment(int offset)
    {
        CharSequence txt = getText();
        int i;
        if (txt != null)
        {
            try
            {
                i = Integer.parseInt (txt.toString ());
            } catch (NumberFormatException e)
            {
                i = 0;
            }
        } else
        {
            i = 0;
        }
        i = i + offset;
        setText(String.valueOf (i));
        return i;
    }

    /**
     * @param offset
     * @return int
     * @notice 计数减少
     */
    public int decrement(int offset)
    {
        return increment(-offset);
    }

    private ShapeDrawable getDefaultBackground()
    {
        int r = Tools.dp2px (context, DEFAULT_CORNER_RADIUS_DIP);
        float[] outerR = new float[]{r, r, r, r, r, r, r, r};
        RoundRectShape rr = new RoundRectShape (outerR, null, null);
        ShapeDrawable drawable = new ShapeDrawable (rr);
        drawable.getPaint().setColor(badgeColor);
        return drawable;
    }

    private void applyLayoutParams()
    {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        switch (badgePosition)
        {
            case POSITION_TOP_LEFT:
                lp.gravity = Gravity.LEFT | Gravity.TOP;
                lp.setMargins(badgeMarginH, badgeMarginV, 0, 0);
                break;
            case POSITION_TOP_RIGHT:
                lp.gravity = Gravity.RIGHT | Gravity.TOP;
                lp.setMargins(0, badgeMarginV, badgeMarginH, 0);
                break;
            case POSITION_BOTTOM_LEFT:
                lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
                lp.setMargins(badgeMarginH, 0, 0, badgeMarginV);
                break;
            case POSITION_BOTTOM_RIGHT:
                lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
                lp.setMargins(0, 0, badgeMarginH, badgeMarginV);
                break;
            case POSITION_CENTER:
                lp.gravity = Gravity.CENTER;
                lp.setMargins(0, 0, 0, 0);
                break;
            default:
                break;
        }
        setLayoutParams(lp);
    }

    /**
     * @return View
     * @notice 获取绑定的View
     */
    public View getTarget()
    {
        return target;
    }

    /**
     * @notice 判断badge是否显示
     */
    public boolean isShown()
    {
        return isShown;
    }

    /**
     * @return int位置
     * @notice 获取badge的位置
     */
    public int getBadgePosition()
    {
        return badgePosition;
    }

    /**
     * @param layoutPosition：使用该类的5个参数
     * @notice 设置badge的位置
     */
    public void setBadgePosition(int layoutPosition)
    {
        this.badgePosition = layoutPosition;
    }

    /**
     * @return
     * @notice 获取绑定组件的水平间距
     */
    public int getHorizontalBadgeMargin()
    {
        return badgeMarginH;
    }

    /**
     * @return
     * @notice 获取绑定组件的垂直间距
     */
    public int getVerticalBadgeMargin()
    {
        return badgeMarginV;
    }

    /**
     * @param badgeMargin
     * @notice 设置badge的margin，这里是像素
     */
    public void setBadgeMargin(int badgeMargin)
    {
        this.badgeMarginH = badgeMargin;
        this.badgeMarginV = badgeMargin;
    }

    /**
     * @param horizontal：水平间距
     * @param vertical：垂直间距
     * @notice 设置badge的margin
     */
    public void setBadgeMargin(int horizontal, int vertical)
    {
        this.badgeMarginH = horizontal;
        this.badgeMarginV = vertical;
    }

    /**
     * @return
     * @notice 获取背景色
     */
    public int getBadgeBackgroundColor()
    {
        return badgeColor;
    }

    /**
     * @param badgeColor
     * @notice 设置背景色
     */
    public void setBadgeBackgroundColor(int badgeColor)
    {
        this.badgeColor = badgeColor;
        badgeBg = getDefaultBackground();
    }

}
