package feifei.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import feifei.library.R;
import feifei.library.util.Tools;


public class CradleBall extends View
{
    private int width;
    private int height;

    private Paint paint;
    private Paint paint2;

    private String text;

    public CradleBall (Context context)
    {
        this (context, null);
    }

    public CradleBall (Context context, AttributeSet attrs)
    {
        this (context, attrs, 0);
    }

    public CradleBall (Context context, AttributeSet attrs, int defStyleAttr)
    {
        super (context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes (attrs, R.styleable.CradleBall);
        int color = array.getColor (R.styleable.CradleBall_loading_color, Color.YELLOW);
        text = array.getString (R.styleable.CradleBall_loading_text);
        array.recycle ();

        paint = new Paint ();
        paint.setColor (color);
        paint.setStyle (Paint.Style.FILL);
        paint.setAntiAlias (true);

        paint2 = new Paint ();
        paint2.setColor (Color.WHITE);
        paint2.setStyle (Paint.Style.FILL);
        paint2.setAntiAlias (true);
        paint2.setTextSize (Tools.dp2px (context, 16));
        paint2.setTextAlign (Paint.Align.CENTER);

    }


    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged (w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw (Canvas canvas)
    {
        super.onDraw (canvas);
        canvas.drawCircle (width / 2, height / 2, width / 2, paint);
        canvas.drawText (text, getWidth ()/2, (float) (getHeight ()/1.5), paint2);
    }
}
