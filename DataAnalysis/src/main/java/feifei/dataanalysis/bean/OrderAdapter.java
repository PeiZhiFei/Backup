package feifei.dataanalysis.bean;

import android.content.Context;
import android.widget.TextView;

import java.util.List;

import feifei.dataanalysis.R;
import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;

public class OrderAdapter extends MyBaseAdapter<Order>
{
    int type;

    public OrderAdapter(Context context, List<Order> datas) {
        super(context, datas, R.layout.item_order, false);
        this.type = type;
    }

    @Override
    protected void convert(final MyViewHolder viewHolder, final Order bean) {
        final TextView store_name = viewHolder.getView(R.id.store_name);
        final TextView order_price = viewHolder.getView(R.id.order_price);
        final TextView order_count = viewHolder.getView(R.id.order_count);

        store_name.setText(bean.getStore_name());
        order_count.setText("订单总量：" + bean.getOrder_count());
        order_price.setText("交易总额：￥" + bean.getOrder_money ());
        //暂无营业额
        if (Float.valueOf(bean.getOrder_money()) <= 0) {
            store_name.setTextColor(context.getResources().getColor(R.color.darkgray2));
            order_count.setTextColor(context.getResources().getColor(R.color.darkgray2));
            order_price.setTextColor(context.getResources().getColor(R.color.darkgray2));

        } else {
            store_name.setTextColor(context.getResources().getColor(R.color.pink));
            order_count.setTextColor(context.getResources().getColor(R.color.main_green));
            order_price.setTextColor(context.getResources().getColor(R.color.red));
//            SpannableString spannableString = new SpannableString("订单总量：" + bean.getOrder_count());
//            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#39ac69")), 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            order_count.setText(spannableString);
//            SpannableString spannableString2 = new SpannableString("交易总额：￥" + bean.getOrder_money());
//            spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")), 5, spannableString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            order_price.setText(spannableString2);
        }
    }
}
