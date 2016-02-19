package com.runkun.lbsq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.runkun.lbsq.bean.Good;
import com.runkun.lbsq.fragment.GoodsCategoryFragment;
import com.runkun.lbsq.view.GoodsCategoryCombineView;

import java.util.LinkedList;
import java.util.List;

public class GoodListAdapter extends BaseAdapter
{

    private LayoutInflater inflater;
    private List<Good> goods;
    private GoodsCategoryFragment gcf;
    private Context context;

    public GoodListAdapter(Context context, GoodsCategoryFragment gcf)
    {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        goods = new LinkedList<Good>();
        this.gcf = gcf;
        this.context = context;
    }

    public void addGood(Good entity)
    {
        goods.add(entity);
    }

    public void clear()
    {
        goods.clear();
    }

    public void setGoodsCategoryFragment(GoodsCategoryFragment gcf)
    {
        this.gcf = gcf;
    }

    @Override
    public int getCount()
    {
        return goods.size();
    }

    @Override
    public Object getItem(int pos)
    {
        return goods.get(pos);
    }

    @Override
    public long getItemId(int pos)
    {
        return pos;
    }

    @Override
    public View getView(int pos, View convert, ViewGroup root)
    {
        if (convert == null)
        {
            convert = new GoodsCategoryCombineView(context);
        }
        ((GoodsCategoryCombineView) convert).setTitleForText(goods.get(pos), gcf);
        return convert;
    }

}
