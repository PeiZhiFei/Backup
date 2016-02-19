package feifei.dataanalysis.base;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import feifei.dataanalysis.R;
import feifei.dataanalysis.bean.Order2;
import feifei.dataanalysis.bean.OrderAdapter2;

public class OrderListActivity2 extends BaseActivity {

    @InjectView(R.id.list)
    ListView listView;
    List<Order2> datas = new ArrayList<>();
    OrderAdapter2 adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list2);
        ButterKnife.inject(this);
        tint();

        for (int i = 0; i < 20; i++) {
            datas.add(new Order2());
        }

        adapter2 = new OrderAdapter2(this, datas);
        listView.setAdapter(adapter2);

    }


}
