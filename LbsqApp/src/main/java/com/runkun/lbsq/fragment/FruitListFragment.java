package com.runkun.lbsq.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.BaseAcitivity;
import com.runkun.lbsq.activity.GoodDetailActivity;
import com.runkun.lbsq.activity.LoginActivity;
import com.runkun.lbsq.activity.MainActivity;
import com.runkun.lbsq.interfaces.onButtonClick;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;
import com.runkun.lbsq.utils.XUtilsImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import feifei.project.util.ConfigUtil;
import feifei.project.util.ToastUtil;

public class FruitListFragment extends BaseFragment
{

    @ViewInject(R.id.fruit_list)
    ListView listView;

    @ViewInject(R.id.footer_tv)
    TextView textView;

    MyAdapter adapter;

    String store_id;

    boolean more = true;

    private int pageNumber = 1;
    private MainActivity mainActivity;


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup viewGroup, Bundle bundle)
    {
        store_id = getArguments().getString("store_id");
        adapter = new MyAdapter();

        View view = inflater.inflate(R.layout.fragment_fruit,
                viewGroup, false);
        ViewUtils.inject(this, view);
        dialogInit();

        listView.setOnScrollListener(new OnScrollListener()
        {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE)
                {
                    if (view.getLastVisiblePosition() == view.getCount() - 1)
                    {
                        if (more)
                        {
                            query();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount)
            {
            }
        });

        listView.setAdapter(adapter);

        query();

        return view;
    }

    protected void query()
    {
        dialogProgress("请稍候");
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("store_id", store_id);
        rp.addQueryStringParameter("pagenumber", String.valueOf(pageNumber++));

        HttpHelper.postByCommand("fruitlistmore", rp, new RequestCallBack<String>()
        {

            @Override
            public void onFailure(HttpException arg0, String arg1)
            {
                pageNumber--;
                dialogDismiss();
            }

            @Override
            public void onSuccess(ResponseInfo<String> resp)
            {
                String result = resp.result;
                try
                {
                    JSONObject jsonResult = new JSONObject(result);
                    if (HttpHelper.isSuccess(jsonResult))
                    {
                        JSONArray datas = jsonResult.getJSONArray("datas");
                        if (datas.length() == 0)
                        {
                            more = false;

                        } else
                        {
                            for (int i = 0; i < datas.length(); i++)
                            {
                                adapter.data.put(datas.get(i));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    dialogDismiss();
                }
                dialogDismiss();
            }

        });
    }

    public int addOrSub(int count, Boolean isSub)
    {
        if (null != isSub && isSub)
        {
            count--;

        } else
        {
            count++;

        }
        return count++;
    }

    public class MyAdapter extends BaseAdapter
    {
        JSONArray data;

        public MyAdapter()
        {
            data = new JSONArray();
        }

        @Override
        public int getCount()
        {
            return data.length();
        }

        @Override
        public JSONObject getItem(int position)
        {
            try
            {
                return data.getJSONObject(position);
            } catch (JSONException e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = View.inflate(getActivity(),
                        R.layout.item_fruitgood, null);
                new ViewHolder(convertView);
            }
            try
            {
                final ViewHolder holder = (ViewHolder) convertView.getTag();
                holder.goodName.setText(data.getJSONObject(position).getString(
                        "goods_name"));
                holder.price
                        .setText("￥"
                                + data.getJSONObject(position).getString(
                                "goods_price"));
                XUtilsImageLoader xut = new XUtilsImageLoader(getActivity(),
                        holder.goodImage.getMeasuredWidth(),
                        holder.goodImage.getMeasuredHeight());
                xut.display(holder.goodImage, data.getJSONObject(position)
                        .getString("goods_pic"));
                final String goodsId = data.getJSONObject(position).getString(
                        "goods_id");
                String sc = holder.newcount.getText().toString().trim();
                if (sc == null || "".endsWith(sc))
                {
                    holder.newcount.setText("1");
                }
                holder.goodImage.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent();
                        intent.putExtra("goods_id", goodsId);
                        intent.setClass(getActivity(), GoodDetailActivity.class);
                        startActivity(intent);

                    }
                });

                holder.add.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        String count = holder.newcount.getText().toString()
                                .trim();
                        int result = addOrSub(
                                null == count || "".endsWith(count) ? 0
                                        : Integer.valueOf(count), null);
                        holder.newcount.setText(String.valueOf(result));

                    }
                });
                holder.sub.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        String count = holder.newcount.getText().toString();
                        if (null != count && "0".equals(count))
                        {
                            ToastUtil.toast (getActivity (), "该商品已经清空！！");
                        } else
                        {
                            int result = addOrSub(
                                    null == count || "".endsWith(count) ? 0
                                            : Integer.valueOf(count), true);
                            holder.newcount.setText(String.valueOf(result));

                        }
                    }
                });
                holder.fruitshopcard.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        String memberId = ConfigUtil.readString (getActivity (),
                                MyConstant.KEY_MEMBERID, "");
                        if (Tools.isEmpty(memberId))
                        {
                            Tools.dialog(getActivity(), Tools.getStr(getActivity(), R.string.REQUESTLOGIN),
                                    true, new onButtonClick()
                                    {

                                        @Override
                                        public void buttonClick()
                                        {
                                            getActivity()
                                                    .startActivity(
                                                            new Intent(
                                                                    getActivity(),
                                                                    LoginActivity.class));
                                        }
                                    });
                        } else
                        {
                            try
                            {
                                String count = holder.newcount.getText()
                                        .toString().trim();
                                RequestParams requestParams = new RequestParams();
                                requestParams.addQueryStringParameter(
                                        "goods_id", data
                                                .getJSONObject(position)
                                                .getString("goods_id"));
                                requestParams.addQueryStringParameter(
                                        "quantity",
                                        null == count || "".equals(count) ? "1"
                                                : count);
                                requestParams.addQueryStringParameter(
                                        "member_id", memberId);
                                ImageView aniImg = new ImageView(getActivity());
                                aniImg.setImageDrawable(holder.goodImage.getDrawable());
                                Tools.addShopCard(
                                        (BaseAcitivity) getActivity(),
                                        "addshopcar", holder.goodImage,mainActivity.getShopcardLayout(),
                                        aniImg,requestParams,true );
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder
        {
            ImageView goodImage;
            TextView price;
            TextView goodName;
            TextView newcount;
            ImageButton add;
            ImageButton sub;
            ImageButton fruitshopcard;

            public ViewHolder(View view)
            {
                goodImage = (ImageView) view.findViewById(R.id.good_img);
                goodName = (TextView) view.findViewById(R.id.good_name);
                newcount = (TextView) view.findViewById(R.id.newcount);
                add = (ImageButton) view.findViewById(R.id.add);
                price = (TextView) view.findViewById(R.id.price);
                sub = (ImageButton) view.findViewById(R.id.sub);
                fruitshopcard = (ImageButton) view
                        .findViewById(R.id.fruitshopcard);
                view.setTag(this);
            }
        }

    }
}
