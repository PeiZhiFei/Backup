package com.runkun.lbsq.adapter;

import android.content.Context;
import android.widget.TextView;

import com.runkun.lbsq.R;
import com.runkun.lbsq.bean.Test;

import java.util.List;

import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;

public class CategoryLeftAdapter extends MyBaseAdapter<Test>
{
    private int selectedPosition = -1;// 选中的位置
    Context context;

    public CategoryLeftAdapter(Context context, List<Test> datas, int layoutId)
    {
        super(context, datas, R.layout.item_left, false);
        this.context = context;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    @Override
    protected void convert(final MyViewHolder viewHolder, final Test bean)
    {
        final TextView tvClassname = viewHolder.getView(R.id.tv_classname);
        tvClassname.setText(bean.getS());
//
//        if (tvClassname.isSelected())
//        {
//            tvClassname.setTextColor(context.getResources().getColor(R.color.green));
//            tvClassname.setBackgroundColor(context.getResources().getColor(R.color.backgroundgray));
//        } else
//        {
//            tvClassname.setTextColor(context.getResources().getColor(R.color.darkgray));
//            tvClassname.setBackgroundColor(context.getResources().getColor(R.color.white));
//        }
        if (bean.isSelect())
        {
            tvClassname.setTextColor(context.getResources().getColor(R.color.green));
            tvClassname.setBackgroundColor(context.getResources().getColor(R.color.backgroundgray));
        } else
        {
            tvClassname.setTextColor(context.getResources().getColor(R.color.darkgray));
            tvClassname.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
//        if (selectedPosition == viewHolder.getPosition())
//        {
//            tvClassname.setSelected(true);
//        } else
//        {
//            tvClassname.setSelected(false);
//        }
    }
}
