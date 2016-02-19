package feifei.dataanalysis.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import feifei.dataanalysis.R;
import feifei.project.util.Tools;

public class OrderFragment2 extends NetFragment {
    protected View view;

    protected int type;

    //需要子类补全的参数
    protected String url;
    protected Map<String, String> mParams = new HashMap<>();

    OrderListActivity orderListActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        orderListActivity = (OrderListActivity) activity;
    }

    public static OrderFragment2 newInstance(int type) {
        OrderFragment2 myBuyBasefragment = new OrderFragment2();
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
        initType();
        loadData(true, "1");
        return view;
    }


    protected void initType() {
        switch (type) {
            case 0:
                url = Tools.URL + "getDay";
                break;
            case 1:
                url = Tools.URL + "getWeek";
                break;
            case 2:
                url = Tools.URL + "getMonth";
                break;
        }
    }


    @Override
    protected void getData(JSONArray jsonArray, String tag) throws JSONException {

    }
}
