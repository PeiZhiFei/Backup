package feifei.project.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import java.util.List;

import feifei.project.R;

public abstract class MyBaseAdapter<T> extends BaseAdapter
{

    public Context context;
    public List<T> datas;
    public int layoutId;
    protected Animation push_left_in, push_right_in;
    boolean animate = false;

    public MyBaseAdapter (Context context, List<T> datas, int layoutId)
    {
        this (context, datas, layoutId, false);
    }

    public MyBaseAdapter (Context context, List<T> datas, int layoutId,
                          boolean animate)
    {
        this.context = context;
        this.datas = datas;
        this.layoutId = layoutId;
        this.animate = animate;
        push_left_in = AnimationUtils.loadAnimation (context,
                R.anim.activity_in_from_left);
        push_right_in = AnimationUtils.loadAnimation (context,
                R.anim.activity_in_from_right);
    }


    @Override
    public int getCount ()
    {
        return datas.size ();
    }

    @Override
    public T getItem (int position)
    {
        return datas.get (position);
    }

    @Override
    public long getItemId (int position)
    {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent)
    {
        MyViewHolder viewHolder = MyViewHolder.getViewHolder (context, layoutId,
                convertView, parent, position);
        convert (viewHolder, getItem (position));
        if (animate)
        {
            if (viewHolder.getPosition () % 2 == 0)
            {
                viewHolder.getmContentView ().setAnimation (push_left_in);
            } else
            {
                viewHolder.getmContentView ().setAnimation (push_right_in);
            }
        }
        return viewHolder.getmContentView ();
    }

    protected abstract void convert (MyViewHolder viewHolder, T bean);

    public void setData(List<T> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<T> datas) {
        if (datas != null) {
            this.datas.addAll(datas);
        }

        this.notifyDataSetChanged();
    }

    public void addData(T data) {
        this.datas.add(data);
        this.notifyDataSetChanged();
    }

    public List<T> getAllData() {
        return this.datas;
    }


    public void remove(T elem) {
        datas.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        datas.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem) {
        datas.clear();
        datas.addAll(elem);
        notifyDataSetChanged();
    }

}
