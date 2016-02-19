package com.runkun.lbsq.busi.adapter;

import android.content.Context;
import android.widget.TextView;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.bean.GoodMore;

import java.util.List;

import feifei.project.util.L;
import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;
import feifei.project.view.smartimage.SmartImageView;

public class GoodListAdapterTest extends MyBaseAdapter<GoodMore>
{

    public void clear()
    {
        datas.clear();
        notifyDataSetChanged();
    }

    private List<GoodMore> datas;

    public GoodListAdapterTest(Context context, List<GoodMore> datas)
    {
        super(context, datas, R.layout.item_good, false);
        this.datas = datas;
    }

    @Override
    protected void convert(final MyViewHolder viewHolder, final GoodMore bean)
    {
        final TextView good_price = viewHolder.getView(R.id.good_price);
        final TextView good_name = viewHolder.getView(R.id.good_name);
        final SmartImageView good_image = viewHolder.getView(R.id.good_img);
        final TextView good_state = viewHolder.getView(R.id.good_state);
        L.l ("查询到的店铺id==" + bean.getGoods_id ());
        good_price.setText("￥" + bean.getGoods_price());
        good_name.setText(bean.getGoods_name());
        String s = bean.getGrounding();
        try
        {
            switch (s)
            {
                case "0":
                    good_state.setText("未上架");
                    break;
                case "1":
                    good_state.setText("上架");
                    break;
                case "2":
                    good_state.setText("下架");
                    break;
                default:good_state.setText("未上架");
            }
        }catch (Exception e){
            e.printStackTrace ();
            good_state.setText("未上架");
        };

        good_image.setImageUrl(bean.getGoods_pic());

    }


}
