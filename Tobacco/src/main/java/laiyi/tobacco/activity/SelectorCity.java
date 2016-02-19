package laiyi.tobacco.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feifei.library.util.AnimUtil;
import feifei.library.util.L;
import feifei.library.util.Tools;
import laiyi.tobacco.R;
import laiyi.tobacco.bean.City;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class SelectorCity extends BaseActivity
{
    List<City> list = new ArrayList<>();
    ExpandableListView expandableListView;
    double lon;
    double lat;
    private TextView emptyTextView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_city);
        Intent intent = getIntent();
        lon = intent.getDoubleExtra("lon", 0.0);
        lat = intent.getDoubleExtra("lat", 0.0);
        initActionbar ();
        setLeftBack();
        setTitles ("黔西南");
        tint ();
//        dialogInit();

        expandableListView = (ExpandableListView) findViewById(R.id.list);
        expandableListView.setGroupIndicator(null);

        Tools tools = new Tools();
        View view2 = tools.getEmptyView(this, 0);
        ((ViewGroup) expandableListView.getParent()).addView(view2);
        emptyTextView = tools.getEmptyText();
        emptyTextView.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                initData();
            }
        });
        expandableListView.setEmptyView(view2);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnGroupClickListener(new OnGroupClickListener()
        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id)
            {
                // ((BaseExpandableListAdapter) adapter).notifyDataSetChanged();
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new OnChildClickListener()
        {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id)
            {
                Intent intent = new Intent();
                intent.putExtra("city", ((City) adapter.getChild(
                        groupPosition, childPosition)).getCityname ());
                setResult(RESULT_OK, intent);
                AnimUtil.animBackFinish (activity);
                // list.get(groupPosition).getCitysub().get(childPosition).getCityid();
                // child_groupId = groupPosition;
                // child_childId = childPosition;
                // ((BaseExpandableListAdapter) adapter).notifyDataSetChanged();
                return false;
            }
        });
        initData();

    }

    private void initData()
    {
////        dialogProgress(activity,
////                Tools.getStr(activity, R.string.LOADING));
//        HttpUtils localHttpUtils = new HttpUtils();
//        localHttpUtils.send(HttpRequest.HttpMethod.POST, "url",
//                new RequestCallBack<String> ()
//                {
//                    @Override
//                    public void onFailure(HttpException paramHttpException,
//                                          String paramString)
//                    {
//                        emptyTextView.setText ("网络异常，请重试");
//                        Tools.toast (activity,"网络异常");
////                        dialogDismiss();
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> paramResponseInfo)
//                    {
//                        String result = paramResponseInfo.result;
                        String result = "{\"code\":\"200\",\"datas\":[{\"area_id\":\"43\",\"area_name\":\"黔西南\",\"first_letter\":\"J\",\"hot_city\":\"1\",\"subclass\":[{\"area_id\":\"396\",\"area_name\":\"都匀\",\"first_letter\":\"T\",\"hot_city\":\"0\"},{\"area_id\":\"382\",\"area_name\":\"福泉\",\"first_letter\":\"L\",\"hot_city\":\"0\"},{\"area_id\":\"385\",\"area_name\":\"惠水\",\"first_letter\":\"L\",\"hot_city\":\"0\"},{\"area_id\":\"386\",\"area_name\":\"龙里\",\"first_letter\":\"G\",\"hot_city\":\"0\"},{\"area_id\":\"389\",\"area_name\":\"独山\",\"first_letter\":\"S\",\"hot_city\":\"0\"},{\"area_id\":\"389\",\"area_name\":\"贵定\",\"first_letter\":\"S\",\"hot_city\":\"0\"},{\"area_id\":\"389\",\"area_name\":\"平塘\",\"first_letter\":\"S\",\"hot_city\":\"0\"},{\"area_id\":\"389\",\"area_name\":\"长顺\",\"first_letter\":\"S\",\"hot_city\":\"0\"},{\"area_id\":\"389\",\"area_name\":\"瓮安\",\"first_letter\":\"S\",\"hot_city\":\"0\"},{\"area_id\":\"389\",\"area_name\":\"罗甸\",\"first_letter\":\"S\",\"hot_city\":\"0\"},{\"area_id\":\"389\",\"area_name\":\"荔波\",\"first_letter\":\"S\",\"hot_city\":\"0\"},{\"area_id\":\"389\",\"area_name\":\"三都\",\"first_letter\":\"S\",\"hot_city\":\"0\"}]},{\"area_id\":\"382\",\"area_name\":\"独山\",\"first_letter\":\"L\",\"hot_city\":\"0\",\"subclass\":[]},{\"area_id\":\"51\",\"area_name\":\"\\u4e34\\u6c82\",\"first_letter\":\"L\",\"hot_city\":\"0\",\"subclass\":[]},{\"area_id\":\"385\",\"area_name\":\"\\u5386\\u4e0b\\u533a\",\"first_letter\":\"L\",\"hot_city\":\"0\",\"subclass\":[]},{\"area_id\":\"62\",\"area_name\":\"\\u9752\\u5c9b\",\"first_letter\":\"Q\",\"hot_city\":\"0\",\"subclass\":[]},{\"area_id\":\"65\",\"area_name\":\"\\u65e5\\u7167\",\"first_letter\":\"R\",\"hot_city\":\"0\",\"subclass\":[]},{\"area_id\":\"389\",\"area_name\":\"\\u5e02\\u4e2d\\u533a\",\"first_letter\":\"S\",\"hot_city\":\"0\",\"subclass\":[]},{\"area_id\":\"396\",\"area_name\":\"\\u5929\\u6865\\u533a\",\"first_letter\":\"T\",\"hot_city\":\"0\",\"subclass\":[]},{\"area_id\":\"71\",\"area_name\":\"\\u6cf0\\u5b89\",\"first_letter\":\"T\",\"hot_city\":\"0\",\"subclass\":[]},{\"area_id\":\"263\",\"area_name\":\"\\u6f4d\\u574a\",\"first_letter\":\"W\",\"hot_city\":\"0\",\"subclass\":[]},{\"area_id\":\"394\",\"area_name\":\"\\u6dc4\\u535a\",\"first_letter\":\"Z\",\"hot_city\":\"0\",\"subclass\":[]}]}\n";
                        L.l (result);
                        try
                        {
                            JSONObject jsonResul = new JSONObject(result);
                            String isResul = jsonResul.getString("code");
                            if (isResul.equals("200"))
                            {
                                JSONObject jsonReult = new JSONObject(result);
                                final JSONArray allData = jsonReult
                                        .getJSONArray("datas");
                                JSONObject jsonRow = null;
                                for (int x = 0; x < allData.length(); x++)
                                {
                                    jsonRow = allData.getJSONObject(x);
                                    City city = new City(jsonRow
                                            .getString("area_name"), jsonRow
                                            .getString("area_id"));
                                    city.setFirstLettor(jsonRow
                                            .getString("first_letter"));
                                    city.setFirstLettor(jsonRow
                                            .getString("hot_city"));
                                    JSONArray jsonArray = jsonRow
                                            .getJSONArray("subclass");
                                    if (jsonArray.length() > 0)
                                    {
                                        city.setHasChild(true);
                                        JSONObject jsonsub = null;
                                        for (int y = 0; y < jsonArray.length(); y++)
                                        {
                                            jsonsub = jsonArray
                                                    .getJSONObject(y);
                                            City subcity = new City(
                                                    jsonsub.getString("area_name"),
                                                    jsonsub.getString("area_id"));
                                            subcity.setFirstLettor(jsonsub
                                                    .getString("first_letter"));
                                            subcity.setFirstLettor(jsonsub
                                                    .getString("hot_city"));
                                            city.addSub(subcity);
                                        }
                                    }
                                    list.add(city);
                                }
                                notice();
                            }
                        } catch (JSONException e)
                        {
                            Tools.toast (activity, "数据解析错误");
                            e.printStackTrace ();
//                            dialogDismiss();
                        }
//                        dialogDismiss();
                    }
//                });
//    }

    List<Integer> intlist = new ArrayList<Integer>();

    final ExpandableListAdapter adapter = new BaseExpandableListAdapter()
    {

        @Override
        public int getGroupCount()
        {
            int count = 0;
            for (int i = 0; i < list.size(); i++)
            {
                if (list.get(i).isHasChild())
                {
                    intlist.add(new Integer(i));
                    count++;
                }
            }
            return count;
            // return list.size();
        }

        @Override
        public City getGroup(int groupPosition)
        {
            return list.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition)
        {
            return groupPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            return list.get(intlist.get(groupPosition)).getCitysub().size();
        }

        @Override
        public City getChild(int groupPosition, int childPosition)
        {
            return list.get(intlist.get(groupPosition)).getCitysub()
                    .get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            return childPosition;
        }

        @Override
        public boolean hasStableIds()
        {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent)
        {
            convertView = View.inflate(getBaseContext(),
                    R.layout.item_cityselect_group, null);
            TextView group_title = (TextView) convertView
                    .findViewById(R.id.group_title);
            // group_title.setText(getGroup(groupPosition).getCityname());
            group_title.setText(getGroup(intlist.get(groupPosition))
                    .getCityname());
            // if (getChildrenCount(groupPosition) <= 0) {
            // return null;
            // }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent)
        {
            convertView = View.inflate(getBaseContext(),
                    R.layout.item_cityselect_child_, null);
            TextView child_text = (TextView) convertView
                    .findViewById(R.id.child_text);
            child_text.setText(getChild(intlist.get(groupPosition),
                    childPosition).getCityname());
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition)
        {
            return true;
        }

    };

    public void notice()
    {
        ((BaseExpandableListAdapter) adapter).notifyDataSetChanged();
        int groupCount = expandableListView.getCount();
        for (int i = 0; i < groupCount; i++)
        {
            expandableListView.expandGroup(i);
        }
    }

}
