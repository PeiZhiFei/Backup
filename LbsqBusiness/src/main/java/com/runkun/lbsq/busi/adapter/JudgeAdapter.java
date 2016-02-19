package com.runkun.lbsq.busi.adapter;

import android.content.Context;
import android.widget.RatingBar;
import android.widget.TextView;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.bean.Judge;
import com.runkun.lbsq.busi.util.Tools;

import java.util.List;

import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;

public class JudgeAdapter extends MyBaseAdapter<Judge>
{
    Context context;
    int type;

    public JudgeAdapter(Context context, List<Judge> datas,
                        int layoutId, int type)
    {
        super(context, datas, layoutId);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(final MyViewHolder viewHolder,
                           final Judge bean)
    {
//        final SmartImageView image = viewHolder.getView(R.id.judge_photo_img);
        final RatingBar ratingBar = viewHolder.getView(R.id.rationbar);
        final TextView userName = viewHolder.getView(R.id.judge_user_nick);
        final TextView date = viewHolder.getView(R.id.judge_date);
        final TextView judgeContent = viewHolder.getView(R.id.judge_content);

//        image.setImageUrl (bean.getStore_pic());
        judgeContent.setText(bean.getComment());
        ratingBar.setRating(bean.getFlower_num());
        userName.setText(bean.getOrder_sn());
        String time = bean.getAdd_time();

        date.setText(Tools.isTimeEmpty(time) ? "" : Tools.formatMysqlTimestamp(time, "yyyy-MM-dd"));
    }

}