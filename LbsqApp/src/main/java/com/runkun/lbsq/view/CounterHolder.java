package com.runkun.lbsq.view;

import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.runkun.lbsq.R;

public class CounterHolder
{
    @ViewInject(R.id.order_count)
    TextView countTV;

    float mCount = 0;
    float mStep = 0;
    float mMin = 0;
    float mMax = 0;
    boolean fromShopCard = false;
    CounterCallback mCallback;
    boolean plus = false;

    public CounterHolder(float count, float step, float min, float max, CounterCallback callback)
    {
        mCount = count;
        mStep = step;
        mMin = min;
        mMax = max;
        mCallback = callback;
        fromShopCard = false;
    }


    public CounterHolder(float count, float step, float min, float max, CounterCallback callback, boolean fromShopCard)
    {
        mCount = count;
        mStep = step;
        mMin = min;
        mMax = max;
        mCallback = callback;
        this.fromShopCard = fromShopCard;
    }

    @OnClick({R.id.minus_btn, R.id.plus_btn})
    public void onClick(View v)
    {
        int id = v.getId ();

        switch (id)
        {
            case R.id.minus_btn:
                plus = false;
                if (mCount - mStep < mMin)
                {
                    return;
                }
                mCount = mCount - mStep;
                break;
            case R.id.plus_btn:
                plus = true;
                if (mCount + mStep > mMax)
                {
                    return;
                }
                mCount = mCount + mStep;
                break;
        }

        if (!fromShopCard)
        {
            setCount (mCount);
            mCallback.newCount (mCount);
        } else
        {
            mCallback.requestShopCard (plus,mCount);
        }


    }

    public void setCount(float count)
    {
        mCount = count;
        int iCount = (int) count;
        if (iCount == count)
        {
            countTV.setText (String.valueOf (iCount));
        } else
        {
            countTV.setText (String.valueOf (count));
        }
    }

    public void setMax(float max)
    {
        mMax = max;
    }

    public interface CounterCallback
    {
        void newCount(float newCount);

        void requestShopCard(boolean plus,float mCount);
    }
}
