package feifei.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import feifei.library.R;

public class ClearEditText extends EditText implements OnFocusChangeListener,
        TextWatcher
{

    private Drawable mClearDrawable;
    onRightClick onRightClick;

    private boolean hasFoucs;

    public ClearEditText (Context context) {
        this(context, null);
    }

    public ClearEditText (Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText);
            int image = array.getResourceId(R.styleable.ClearEditText_right_image, R.drawable.edittext_pressed);
            array.recycle();

            mClearDrawable = getResources().getDrawable(image);
        }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getMinimumWidth(),
                mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    public void setListener(onRightClick onRightClick) {
        this.onRightClick = onRightClick;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");

                }
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    //改进版的清除按钮
    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() <= 0) {
            if (onRightClick != null) {
                onRightClick.rightClick();
            }
        }
    }


    public interface onRightClick {
        public void rightClick ();
    }

}
