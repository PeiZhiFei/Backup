package feifei.library.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import feifei.library.R;


public class ToastUtil
{

    public static void toast (Context context, CharSequence string)
    {
        toastShow (context, string, Gravity.CENTER);
    }

    public static void toast (Context context, CharSequence string, int g)
    {
        toastShow (context, string, g);
    }

    public static void toast (Context context, int string)
    {
        toastShow (context, context.getResources ().getString (string),
                Gravity.CENTER);
    }

    private static void toastShow (final Context context,
                                   final CharSequence string, final int g)
    {
        Toastlayout toastLayout = Toastlayout.toastlayout (context);
        RelativeLayout layout = toastLayout.getLayout ();
        TextView textView = toastLayout.getTextView ();
        ImageView imageView = toastLayout.getImageView ();
        Toast toast = new Toast (context);
        textView.setText (TextUtils.isEmpty (string) ? "" : string);
        imageView.setVisibility (View.VISIBLE);
        // imageView.setImageResource(R.drawable.icon_notice);
        imageView.setVisibility (View.GONE);
        toast.setView (layout);
//        		toast.setGravity(g, 0, 200);
//        toast.setGravity (g, 0, 0);
        toast.setDuration (Toast.LENGTH_SHORT);
        //todo 关闭toast的方法
//        L.l (((Activity) context).isFinishing ());
        if ( !((Activity) context).isFinishing () )
        {
            toast.show ();
        }

    }

    static class Toastlayout
    {
        Context context;
        RelativeLayout layout;
        ImageView imageView;
        TextView textView;

        public RelativeLayout getLayout ()
        {
            return layout;
        }

        public ImageView getImageView ()
        {
            return imageView;
        }

        public TextView getTextView ()
        {
            return textView;
        }

        public Toastlayout (Context context)
        {
            this.context = context;
            final int IMAGEID = 0x000012;

            layout = new RelativeLayout (context);
            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams (
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParam.addRule (RelativeLayout.CENTER_VERTICAL);
            layoutParam.addRule (RelativeLayout.CENTER_HORIZONTAL);
            layout.setGravity (Gravity.CENTER);
            layout.setPadding (15, 15, 15, 15);
            // layout.setBackgroundResource(R.drawable.white_blue_line);
            layout.setBackgroundResource (R.drawable.toast_dark);
            layout.setLayoutParams (layoutParam);

            imageView = new ImageView (context);
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams (
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            imageParams.setMargins (8, 0, 0, 0);
            imageParams.addRule (RelativeLayout.CENTER_VERTICAL);
            imageView.setLayoutParams (imageParams);
            // noinspection ResourceType
            imageView.setId (IMAGEID);

            textView = new TextView (context);
            RelativeLayout.LayoutParams textParam = new RelativeLayout.LayoutParams (
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            textParam.setMargins (8, 0, 0, 0);
            textParam.addRule (RelativeLayout.CENTER_VERTICAL);
            textParam.addRule (RelativeLayout.RIGHT_OF, IMAGEID);
            // textView.setTextColor(context.getResources().getColor(
            // R.color.main_red_color));
            textView.setTextColor (context.getResources ()
                    .getColor (R.color.colorPrimary));
            textView.setTextSize (16);
            textView.setPadding (0, 0, 8, 0);
            textView.setMaxLines (3);
            textView.setLayoutParams (textParam);
            layout.addView (imageView);
            layout.addView (textView);
        }

        public static Toastlayout toastlayout (Context context)
        {
            Toastlayout toastlayout = new Toastlayout (context);
            return toastlayout;
        }
    }
}
