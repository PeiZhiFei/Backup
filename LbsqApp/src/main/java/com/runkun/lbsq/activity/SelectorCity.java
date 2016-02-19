package com.runkun.lbsq.activity;

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

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.runkun.lbsq.R;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feifei.project.util.L;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class SelectorCity extends BaseAcitivity
{
    List<City> list = new ArrayList<City>();
    ExpandableListView expandableListView;
    double lon;
    double lat;
    private TextView emptyTextView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        Intent intent = getIntent();
        lon = intent.getDoubleExtra("lon", 0.0);
        lat = intent.getDoubleExtra("lat", 0.0);
        initActionbar();
        setTitles(Tools.getStr(activity, R.string.TSELECTCITY));
        tint();
        dialogInit();

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
                intent.putExtra("tests", ((City) adapter.getChild(
                        groupPosition, childPosition)).getCityid());
                intent.putExtra("mail", ((City) adapter.getChild(groupPosition,
                        childPosition)).getCityname());
                setResult(MyConstant.RESULT_SHOPLIST, intent);
                AnimUtil.animBackSlideFinish(activity);
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
        dialogProgress(activity,
                Tools.getStr(activity, R.string.LOADING));
        HttpUtils localHttpUtils = new HttpUtils();
        localHttpUtils.send(HttpMethod.POST, MyConstant.URLSELECTCITY,
                new RequestCallBack<String>()
                {
                    @Override
                    public void onFailure(HttpException paramHttpException,
                                          String paramString)
                    {
                        emptyTextView.setText(Tools.getStr(activity,
                                R.string.NETERRORCLICK));
                        Tools.toast(activity,
                                Tools.getStr(activity, R.string.NETWORKERROR));
                        dialogDismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> paramResponseInfo)
                    {
                        String result = paramResponseInfo.result;
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
                            Tools.toast(activity,
                                    Tools.getStr(activity, R.string.JSONERROR));
                            e.printStackTrace();
                            dialogDismiss();
                        }
                        dialogDismiss();
                    }
                });
    }

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

    class City
    {
        String cityname = "";
        String cityid = "";
        String firstLettor = "";
        String hotCity = "";
        public boolean hasChild = false;
        List<City> citysub = new ArrayList<City>();

        public void addSub(City subCity)
        {
            citysub.add(subCity);
        }

        public boolean isHasChild()
        {
            return hasChild;
        }

        public void setHasChild(boolean hasChild)
        {
            this.hasChild = hasChild;
        }

        public List<City> getCitysub()
        {
            return citysub;
        }

        public void setCitysub(List<City> citysub)
        {
            this.citysub = citysub;
        }

        public String getFirstLettor()
        {
            return firstLettor;
        }

        public void setFirstLettor(String firstLettor)
        {
            this.firstLettor = firstLettor;
        }

        public String getHotCity()
        {
            return hotCity;
        }

        public void setHotCity(String hotCity)
        {
            this.hotCity = hotCity;
        }

        City(String cityname, String cityid)
        {
            this.cityname = cityname;
            this.cityid = cityid;

        }

        public String getCityname()
        {
            return cityname;
        }

        public void setCityname(String cityname)
        {
            this.cityname = cityname;
        }

        public String getCityid()
        {
            return cityid;
        }

        public void setCityid(String cityid)
        {
            this.cityid = cityid;
        }

    }
}
