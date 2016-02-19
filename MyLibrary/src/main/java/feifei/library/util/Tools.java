package feifei.library.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import feifei.library.R;
import feifei.library.view.BadgeView;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class Tools
{
    public static void toast (Context context, CharSequence string)
    {
        if ( context != null )
        {
            ToastUtil.toast (context, string);
        }

    }

    public static void toast (Context context, CharSequence... string)
    {
        if ( context != null )
        {
            StringBuffer s = new StringBuffer ();
            for (int i = 0; i < string.length; i++)
            {
                s.append (string[i] + "\n");
            }
            ToastUtil.toast (context, s.toString ());
        }

    }

    public static void toast (Context context, CharSequence string, int g)
    {
        if ( context != null )
        {
            ToastUtil.toast (context, string, g);
        }

    }

    public static void toast (Context context, int string)
    {
        if ( context != null )
        {
            ToastUtil.toast (context, string);
        }
    }

    public static boolean isEmpty (String s)
    {
        return TextUtils.isEmpty (s) || s.equals ("");
    }

    public static boolean isEditEmpty (EditText s)
    {
        return TextUtils.isEmpty (s.getText ().toString ()) || s.getText ().toString ().equals ("");
    }

    public static boolean isTimeEmpty (String s)
    {
        return TextUtils.isEmpty (s) || s.equals ("") || s.equals ("null");
    }

    public static String formatMysqlTimestamp (String timestamp, String pattern)
    {
        Date date = new Date (Long.valueOf (timestamp) * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat (pattern,
                Locale.getDefault ());
        return sdf.format (date);
    }

    public static boolean checkPhone (String string)
    {
        return match ("^((1[0-9][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", string);
    }

    public static String getStr (Context context, int str)
    {
        return context != null ? context.getString (str) : "111";
    }

    public static boolean isNetworkAvailable (Context context)
    {
        boolean result = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager == null ? null
                : connectivityManager.getActiveNetworkInfo ();
        if ( networkInfo != null && networkInfo.isAvailable () )
        {
            result = true;
            return result;
        }
        return result;
    }

    public static void keyboardShow (TextView view)
    {
        InputMethodManager inputManager = (InputMethodManager) view
                .getContext ().getSystemService (Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput (view, 0);
    }

    private static boolean match (String rule, String string)
    {
        Pattern pattern = Pattern.compile (rule);
        Matcher matcher = pattern.matcher (string);
        return matcher.matches ();
    }

    public static int getWidth (Context context)
    {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics ();
        //        ((Activity) context).getWindowManager ().getDefaultDisplay ()
        //                .getMetrics(mDisplayMetrics);
        WindowManager wm = (WindowManager) context.getSystemService (Context.WINDOW_SERVICE);
        wm.getDefaultDisplay ().getMetrics (mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    public static int getHeight (Context context)
    {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics ();
        ((Activity) context).getWindowManager ().getDefaultDisplay ()
                .getMetrics (mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    protected TextView emptyText;

    public TextView getEmptyText ()
    {
        return emptyText;
    }

    public View getEmptyView (Context context, int dr)
    {
        LinearLayout layout = new LinearLayout (context);
        LinearLayout.LayoutParams layoutParamsss = new LinearLayout.LayoutParams (
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        layout.setOrientation (LinearLayout.VERTICAL);
        layout.setLayoutParams (layoutParamsss);
        emptyText = new TextView (context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams (
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        // layoutParams.gravity = Gravity.CENTER;
        layoutParams.weight = 1;
        layoutParams.setMargins (5, dp2px (context, 120), 5, 5);
        emptyText.setLayoutParams (layoutParams);
        emptyText.setText ("暂无已下载离线地图");
        emptyText.setTextSize (16);
        emptyText.setTextColor (context.getResources ().getColor (
                R.color.main_green));
        emptyText.setGravity (Gravity.CENTER_HORIZONTAL);
        Drawable drawable;
        if ( dr != 0 )
        {
            drawable = context.getResources ().getDrawable (dr);
        } else
        {
            drawable = context.getResources ().getDrawable (R.drawable.nodata);
        }
        drawable.setBounds (0, 0, drawable.getMinimumWidth (),
                drawable.getMinimumHeight ());
        emptyText.setCompoundDrawables (null, drawable, null, null);
        emptyText.setCompoundDrawablePadding (30);
        layout.setVisibility (View.GONE);
        layout.addView (emptyText, layoutParams);
        View zhanwei = new View (context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams (-1, 200);
        p.weight = 1;
        layout.addView (zhanwei, p);
        return layout;
    }

    public static int dp2px (Context context, float dp)
    {
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int dp2px (Context context, int dp)
    {
        return (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources ().getDisplayMetrics ());
    }


    public static void shake (View view)
    {
        //		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        //			shakeOne(view);
        //		} else {
        AnimUtil.animShakeText (view);
        //		}
    }

    public static float scale (float fee)
    {
        BigDecimal bd = new BigDecimal (fee);
        return bd.setScale (2, BigDecimal.ROUND_HALF_UP).floatValue ();
    }

    public static double scale (double fee)
    {
        BigDecimal bd = new BigDecimal (fee);
        //直接截断
        return bd.setScale (2, BigDecimal.ROUND_DOWN).doubleValue ();
    }

    public static void animateTo (Activity activity, View aniView,
                                  int[] startLoc, int[] endLoc, int durationMills,
                                  Animation.AnimationListener listener)
    {
        ViewGroup rootView = (ViewGroup) activity.getWindow ().getDecorView ();
        LinearLayout animLayout = new LinearLayout (activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams (
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams (lp);
        animLayout.setId (Integer.MAX_VALUE);
        animLayout.setBackgroundResource (android.R.color.transparent);
        rootView.addView (animLayout);

        int x = startLoc[0];
        int y = startLoc[1];
        LinearLayout.LayoutParams aniViewLP = new LinearLayout.LayoutParams (
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        aniViewLP.leftMargin = x;
        aniViewLP.topMargin = y;
        aniView.setLayoutParams (aniViewLP);
        animLayout.addView (aniView);

        int endX = endLoc[0] - startLoc[0];
        int endY = endLoc[1] - startLoc[1];
        TranslateAnimation translateAnimationX = new TranslateAnimation (0,
                endX, 0, 0);
        translateAnimationX.setInterpolator (new LinearInterpolator ());
        translateAnimationX.setRepeatCount (0);
        translateAnimationX.setFillAfter (true);

        TranslateAnimation translateAnimationY = new TranslateAnimation (0, 0,
                0, endY);
        translateAnimationY.setInterpolator (new AccelerateInterpolator ());
        translateAnimationY.setRepeatCount (0);
        translateAnimationX.setFillAfter (true);

        Animation mScaleAnimation = new ScaleAnimation (1f, 0.1f, 1f, 0.1f,
                Animation.RELATIVE_TO_PARENT, Animation.RELATIVE_TO_PARENT);
        mScaleAnimation.setDuration (500);
        mScaleAnimation.setFillAfter (true);
        AnimationSet mAnimationSet = new AnimationSet (false);
        mAnimationSet.addAnimation (mScaleAnimation);
        mAnimationSet.setFillAfter (false);
        mAnimationSet.addAnimation (translateAnimationX);
        mAnimationSet.addAnimation (translateAnimationY);
        mAnimationSet.setDuration (500);

        if ( listener != null )
        {
            mAnimationSet.setAnimationListener (listener);
        }

        aniView.startAnimation (mAnimationSet);
    }

    public static boolean isSuccess (JSONObject result)
    {
        try
        {
            return 100 == result.getInt ("Code");
        } catch (JSONException e)
        {
            e.printStackTrace ();
        }
        return false;
    }

    protected static final String PREFS_FILE = "get_device_id.xml";
    protected static final String PREFS_DEVICE_ID = "get_device_id";
    protected static String uuid;

    public static String getUDID (Context context)
    {
        if ( uuid == null )
        {
            final SharedPreferences prefs = context.getSharedPreferences (
                    PREFS_FILE, 0);
            final String id = prefs.getString (PREFS_DEVICE_ID, null);
            if ( id != null )
            {
                uuid = id;
            } else
            {
                final String androidId = Settings.Secure.getString (context.getContentResolver (),
                        Settings.Secure.ANDROID_ID);
                try
                {
                    if ( !"9774d56d682e549c".equals (androidId) )
                    {
                        uuid = UUID.nameUUIDFromBytes (androidId.getBytes ("utf8")).toString ();
                    } else
                    {
                        final String deviceId = ((TelephonyManager) context.getSystemService (
                                Context.TELEPHONY_SERVICE)).getDeviceId ();
                        uuid = deviceId != null ?
                                UUID.nameUUIDFromBytes (deviceId.getBytes ("utf8")).toString () :
                                UUID.randomUUID ().toString ();
                    }
                } catch (UnsupportedEncodingException e)
                {
                    throw new RuntimeException (e);
                }
                prefs.edit ().putString (PREFS_DEVICE_ID, uuid).apply ();
            }
        }
        return uuid;
    }

    public static void badgeView (final Context context, final View view, final int number)
    {
        ((Activity) context).runOnUiThread (new Runnable ()
        {
            public void run ()
            {
                BadgeView badgeView = new BadgeView (context, view);
                if ( number != 0 )
                {
                    badgeView.setText (String.valueOf (number));
                    badgeView.show (true);
                }
                if ( number == 0 )
                {
                    badgeView.hide (true);
                }
            }
        });
    }

}