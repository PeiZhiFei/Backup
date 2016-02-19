package feifei.dataanalysis.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import feifei.dataanalysis.R;
import feifei.dataanalysis.activity.StoreInfoActivity;
import feifei.dataanalysis.bean.Order;
import feifei.dataanalysis.bean.OrderAdapter;
import feifei.project.util.AnimUtil;
import feifei.project.util.L;
import feifei.project.util.Tools;

/**
 * 这是一个通用的分页的Listview
 */
public class OrderFragment extends NetFragment {
    protected ListView listView;
    protected TextView emptyTextView;
    protected ProgressBar footerPB;
    protected TextView footerTV;
    protected View footer;
    protected View view;

    protected boolean first = true;
    protected int pageSize = 300;
    protected int pageNum = 1;
    protected List<Order> data = new ArrayList<>();
    protected int type;

    //需要子类补全的参数
    protected String url;
    protected Map<String, String> mParams = new HashMap<>();
    protected OrderAdapter adapter;

    OrderListActivity orderListActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        orderListActivity = (OrderListActivity) activity;
    }

    public static OrderFragment newInstance(int type) {
        OrderFragment myBuyBasefragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        myBuyBasefragment.setArguments(args);
        return myBuyBasefragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_base, null);
        dialogInit();
        initView();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                scroll = false;
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //小细节
                        if (listView.getTag().equals("true")) {
                            if (!scroll) {
                                load(true);
                            }
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                scroll = true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (Float.valueOf(data.get(position).getOrder_money()) > 0) {
                    BaseActivity.data(fragment, "订单列表有数据" + data.get(position).getStore_name());
                    Intent intent = new Intent(fragment, StoreInfoActivity.class);
                    intent.putExtra("id", data.get(position).getStore_id());
                    intent.putExtra("storeName", data.get(position).getStore_name());
                    startActivity(intent);
                    AnimUtil.animTo((Activity) fragment);
                } else {
                    Tools.toast(fragment, "该超市暂无营业额");
                    BaseActivity.data(fragment, "订单列表无数据" + data.get(position).getStore_name());
                }
            }
        });
        initType();
        adapter = new OrderAdapter(fragment, data);
        listView.setAdapter(adapter);
        if (type == 1) {
            load(true);
        } else {
            load(false);
        }
        return view;
    }

    boolean scroll = false;


    //子类设置rp，list，adapter,class实体类
    protected void initType() {
        switch (type) {
            case 0:
                url = Tools.URL + "getDay";
//                url = "http://192.168.21.37/lingbushequ/src/lso2o/mobile/apiallstore_order.php?commend=" + "getDay";
                break;
            case 1:
                url = Tools.URL + "getWeek";
//                url = "http://192.168.21.37/lingbushequ/src/lso2o/mobile/apiallstore_order.php?commend=" + "getWeek";
                break;
            case 2:
                url = Tools.URL + "getMonth";
//                url = "http://192.168.21.37/lingbushequ/src/lso2o/mobile/apiallstore_order.php?commend=" + "getMonth";
                break;
        }
    }

    protected void initView() {
        listView = (ListView) view.findViewById(R.id.list);
        Tools tools = new Tools();
        View view2 = tools.getEmptyView(fragment, 0);
        ((ViewGroup) listView.getParent()).addView(view2);
        emptyTextView = tools.getEmptyText();
        emptyTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //todo emptyview的点击事件
                load(true);
            }
        });
        listView.setEmptyView(view2);
        footer = LayoutInflater.from(fragment).inflate(
                R.layout.listview_footer, null);
        footerTV = (TextView) footer.findViewById(R.id.footer_tv);
        footerPB = (ProgressBar) footer.findViewById(R.id.footer_pb);
        listView.addFooterView(footer);
    }

    //原来加了一个type作为tag
    protected void load(final boolean dialog) {
        if (!Tools.isNetworkAvailable(fragment)) {
            error("网络开小差了");
        } else {
            mParams.put("page_number", String.valueOf(pageNum++));
            mParams.put("page_size", String.valueOf(pageSize));
            PostRequest request = new PostRequest(url, mParams, new Listener<String>() {

                @Override
                public void onPreExecute() {
                    super.onPreExecute();
                    if (dialog && first) {
                        dialogProgress();
                    }
                    updateFooter(true, "加载中");
                    emptyTextView.setText("加载中");
                }

                @Override
                public void onFinish() {
                    super.onFinish();
//                    if (dialog && first) {
                    if (dialog) {
                        dialogDismiss();
                    }
                }

                @Override
                public void onCancel() {
                    super.onCancel();
                }

                @Override
                public void onProgressChange(long fileSize, long downloadedSize) {
                    super.onProgressChange(fileSize, downloadedSize);
                    L.l(downloadedSize / fileSize);
                }

                @Override
                public void onSuccess(String response) {
                    L.l(response);
                    L.l(url);
                    try {
                        JSONObject jsonResult = new JSONObject(response);
                        //返回200
                        if (Tools.isSuccess(jsonResult)) {
                            JSONArray jsonArray = jsonResult.getJSONArray("datas");

                            if (jsonArray.length() > 0) {
                                String haveMore = jsonResult
                                        .getString("haveMore");
                                listView.setTag(haveMore);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Order order = new Order();
                                    order.setStore_name(jsonObject.getString("store_name"));
                                    order.setStore_id(jsonObject.getString("store_id"));
                                    order.setOrder_money(jsonObject.getString("store_money"));
                                    order.setOrder_count(jsonObject.getString("store_count"));
                                    data.add(order);
                                }
                                adapter.notifyDataSetChanged();
                                if ("false".equals(haveMore)) {
                                    updateFooter(false, "已加载全部");
                                }
                            } else {
                                //listview为空就没有footer
                                emptyTextView.setText("暂无信息");
                            }
                        } else {
                            error(null);
                            emptyTextView.setText("数据解析异常");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //                        error ("数据解析错误");
                        //                        updateFooter(false, "数据解析异常");
                        updateFooter(false, "");
                        emptyTextView.setText("数据解析异常");
                    }
                    first = false;
                }

                @Override
                public void onError(NetroidError error) {
                    error.printStackTrace();

                    // TODO: 2015/12/19 测试代码
                    if (!MyApplication.NEEDNET) {
                        first = false;
                        updateFooter(false, "已加载全部");
                        listView.setTag(false);
                        for (int i = 0; i < 20; i++) {
                            Order order = new Order();
                            order.setStore_name("超市" + i);
                            order.setStore_id("store_id" + i);
                            order.setOrder_money("" + i * 2531);
                            order.setOrder_count("" + i * 6783);
                            data.add(order);
                        }
                        adapter.notifyDataSetChanged();
                        return;
                    }

                    error("网络开小差了");
                    pageNum--;
                    if (dialog) {
                        dialogDismiss();
                    }
                    updateFooter(false, "网络开小差了");
                    emptyTextView.setText("网络开小差了\n点击重试");
                }
            }
            );
            MyApplication.mRequestQueue.add(request);
        }
    }


    @Override
    protected void error(String s) {
        if ((System.currentTimeMillis() - orderListActivity.exitTime) > 2000) {
            orderListActivity.exitTime = System.currentTimeMillis();
            if (s != null) {
                Tools.toast(fragment, s);
            }
        }
    }


    protected void updateFooter(boolean loading, String info) {
        footerPB.setVisibility(loading ? View.VISIBLE : View.GONE);
        footerTV.setText(info);
    }

    @Override
    protected void getData(JSONArray jsonArray, String tag) throws JSONException {
    }
}
