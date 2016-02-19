package feifei.dataanalysis.bean;

import android.content.Context;

import java.util.List;

import feifei.dataanalysis.R;
import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;

public class OrderAdapter2 extends MyBaseAdapter<Order2>
{

    public OrderAdapter2(Context context, List<Order2> datas) {
        super(context, datas, R.layout.item_order_card, false);
    }

    @Override
    protected void convert(final MyViewHolder viewHolder, final Order2 bean) {
//        final TextView store_name = viewHolder.getView(R.id.store_name);
//        final TextView order_money_today = viewHolder.getView(R.id.order_money_today);
//        final TextView order_count_today = viewHolder.getView(R.id.order_count_today);
//        final TextView order_money_week = viewHolder.getView(R.id.order_money_week);
//        final TextView order_count_week = viewHolder.getView(R.id.order_count_week);
//        final TextView order_money_month = viewHolder.getView(R.id.order_money_month);
//        final TextView order_count_month = viewHolder.getView(R.id.order_count_month);


//        viewHolder.setText(R.id.store_name, bean.getStore_name());
//        viewHolder.setText(R.id.order_money_today, bean.getOrder_money_today());
//        viewHolder.setText(R.id.order_count_today, bean.getOrder_count_today());
//        viewHolder.setText(R.id.order_money_week, bean.getOrder_money_week());
//        viewHolder.setText(R.id.order_count_week, bean.getOrder_count_week());
//        viewHolder.setText(R.id.order_money_month, bean.getOrder_money_month());
//        viewHolder.setText(R.id.order_count_month, bean.getOrder_count_month());



//        store_name.setText (bean.getStore_name ());
//        if (type == 1)
//        {
//            String time = bean.getStore_name();
//            order_time.setText(Tools.isTimeEmpty (time) ? "" : Tools.formatMysqlTimestamp(time, "yyyy-MM-dd"));
//        } else
//        {
//            String startTime = bean.getOrder_time_start();
//            String lastTime = bean.getOrder_time_end();
//            order_time.setText((Tools.isTimeEmpty(startTime) || Tools.isTimeEmpty(lastTime)) ? "" : (Tools.formatMysqlTimestamp(startTime, "MM-dd") + "~" + Tools.formatMysqlTimestamp(lastTime, "MM-dd")));
//
//        }
//        SpannableString spannableString = new SpannableString("订单总量：" + bean.getOrder_count());
//        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#39ac69")), 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        order_count.setText(spannableString);
//
//        SpannableString spannableString2 = new SpannableString("交易总额：￥" + bean.getOrder_money());
//        spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")), 5, spannableString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        order_price.setText(spannableString2);
    }
}
