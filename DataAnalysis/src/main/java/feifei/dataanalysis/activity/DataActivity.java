package feifei.dataanalysis.activity;

import org.json.JSONArray;
import org.json.JSONException;

import feifei.dataanalysis.base.NetActivity;
import feifei.project.util.Tools;

public abstract class DataActivity extends NetActivity {
    public float[] orderCount7 = new float[7];
    public float[] orderMoney7 = new float[7];
    public String[] orderTime7 = new String[7];

    public float[] orderCount30 = new float[30];
    public float[] orderMoney30 = new float[30];
    public String[] orderTime30 = new String[30];

    protected String todayCount = "……";
    protected String todayMoney = "……";
    protected String allStore = "……";
    protected String allCount = "……";
    protected String allMoney = "……";

    protected void getData(boolean first) {
        getTodayData();
        get30Data(first);
    }

    public void getTodayData() {
        url = Tools.URL + "getTodayData";
        // TODO  url = "http://192.168.21 .37/lingbushequ/src/lso2o/mobile/apiallstore_order.php?commend=" + "getTodayData";
        loadData(false, "simple");
    }

    public void get30Data(boolean first) {
        url = Tools.URL + "getMonthData";
        loadData(first, "30");
    }

    @Override
    protected void getData(JSONArray jsonArray, String tag) throws JSONException {

        switch (tag) {
            case "simple":
//                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
//                todayCount = jsonObject.getString("order_count");
//                todayMoney = jsonObject.getString("order_money");
//                allStore = jsonObject.getString("store_count");
//                allCount = jsonObject.getString("all_count");
//                allMoney = jsonObject.getString("all_money");

                // TODO: 2015/12/20 测试代码
                todayCount = "13";
                todayMoney = "530";
                allStore = "25";
                allCount = "17654";
                allMoney = "438564";
                onResult(1);
                break;
            case "30":
                // TODO: 15-9-30  测试代码
                for (int i = 0; i < orderCount7.length; i++) {
                    orderCount7[i]=5+i*i*i;
                    orderMoney7[i]=1000+i*i*i+3;
                    orderTime7[i]="090"+i;
                    
//                    orderCount7[i] = Float.valueOf(jsonArray.getJSONObject(i).getString("order_count"));
//                    orderMoney7[i] = Float.valueOf(jsonArray.getJSONObject(i).getString("order_money"));
//                    orderTime7[i] = Tools.formatMysqlTimestamp(jsonArray.getJSONObject(i).getString("order_time"), "MM-dd");
                }
                for (int i = 0; i < orderCount30.length; i++) {
                    orderCount30[i]=5+i*i*i;
                    orderMoney30[i]=1000+i*i*i+3;
                    orderTime30[i]="090"+i;
                    
//                    orderCount30[i] = Float.valueOf(jsonArray.getJSONObject(i).getString("order_count"));
//                    orderMoney30[i] = Float.valueOf(jsonArray.getJSONObject(i).getString("order_money"));
//                    orderTime30[i] = Tools.formatMysqlTimestamp(jsonArray.getJSONObject(i).getString("order_time"), "MM-dd");
                }
                onResult(2);
                break;
        }
    }

    @Override
    protected void onfinish() {
        super.onfinish();
//        if (temp != 0 && temp % 2 == 0) {
//            onResult();
//        } else if (allStore==null&&orderTime7[0]==null){
//            onError();
//        }
    }

    protected abstract void onResult(int type);

    protected abstract void onError();
}
