package com.runkun.lbsq.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.BuyNowActivity;
import com.runkun.lbsq.activity.GoodDetailActivity;
import com.runkun.lbsq.activity.JudgeActivity;
import com.runkun.lbsq.activity.LoginActivity;
import com.runkun.lbsq.adapter.GoodAdapter;
import com.runkun.lbsq.bean.GoodMore;
import com.runkun.lbsq.interfaces.onButtonClick;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feifei.project.util.ConfigUtil;
import feifei.project.util.L;
import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;
import feifei.project.view.smartimage.SmartImageView;
import feifei.project.view.swipmenu.SwipeMenu;
import feifei.project.view.swipmenu.SwipeMenuCreator;
import feifei.project.view.swipmenu.SwipeMenuItem;
import feifei.project.view.swipmenu.SwipeMenuListView;

public class MyBuyBasefragment extends BaseFragment
{
    protected View view;
    protected SwipeMenuListView list;
    protected TextView emptyTextView;
    protected String memberId;

    protected BaseAdapter adaper;
    protected List<GoodMore> useData = new ArrayList<GoodMore>();
    protected String commandload;
    protected String commanddelete;
    @ViewInject(R.id.footer_tv)
    protected TextView footerTV;

    @ViewInject(R.id.footer_pb)
    protected ProgressBar footerPB;

    protected int pageSize = 20;
    protected int pageNum = 1;
    private int type;
    View footer;
    boolean more = true;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        memberId = HttpHelper.getPrefParams(getActivity(), "memberId");
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            type = bundle.getInt("type");
        }
    }

    public static MyBuyBasefragment newInstance(int type)
    {
        MyBuyBasefragment myBuyBasefragment = new MyBuyBasefragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        myBuyBasefragment.setArguments(args);
        return myBuyBasefragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_fav, null);
        dialogInit();

        list = (SwipeMenuListView) view.findViewById(R.id.list);
        if (type == 1)
        {
            list.setDividerHeight(2);
            //todo 一条线
            list.setDivider(getResources().getDrawable(R.drawable.colored_bar));
        }

        Tools tools = new Tools();
        View view2 = tools.getEmptyView(getActivity(), 0);
        ((ViewGroup) list.getParent()).addView(view2);
        emptyTextView = tools.getEmptyText();
        emptyTextView.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                loadDatas();
            }
        });
        list.setEmptyView(view2);

        footer = LayoutInflater.from(getActivity()).inflate(
                R.layout.listview_footer, null);
        footerTV = (TextView) footer.findViewById(R.id.footer_tv);
        footerPB = (ProgressBar) footer.findViewById(R.id.footer_pb);
        list.addFooterView(footer);

        SwipeMenuCreator creator = new SwipeMenuCreator ()
        {
            @Override
            public void create(SwipeMenu menu)
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem (getActivity());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(Tools.dp2px(getActivity(), 70));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        list.setMenuCreator(creator);
        list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener ()
        {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index)
            {
                del(position);
            }

        });
        list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                if (view != footer)
                {
                    Intent intent = new Intent(getActivity(),
                            GoodDetailActivity.class);
                    intent.putExtra("goods_id", useData.get(position)
                            .getGoodsId());
                    getActivity().startActivity(intent);
                }
            }
        });
        list.setOnScrollListener(new OnScrollListener()
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
                            loadDatas();
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

        initType();
        list.setAdapter(adaper);
        loadDatas();
        return view;
    }

    private void initType()
    {
        if (type == 2)
        {
            commandload = "unpaygoods";
            commanddelete = "delnopayorder";
            adaper = new GoodAdapter(getActivity(), useData,
                    R.layout.item_good, false, true, false);
        } else
        {
            commandload = "memberbuygoods";
            commanddelete = "delpayorder";
            adaper = new BuyedAdapter(getActivity(), useData,
                    R.layout.item_bought);
        }
    }

    public void del(final int position)
    {
        if (!hasLogin())
        {
            promtLogin();
            return;
        }
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("member_id", memberId);
        if (position != -1)
        {
            rp.addQueryStringParameter("order_id", useData.get(position)
                    .getOther());
        }
        dialogProgress(Tools.getStr(fragment, R.string.LOADING));
        HttpHelper.postByCommand(commanddelete, rp,
                new RequestCallBack<String>()
                {
                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                        updateFooter(false,
                                Tools.getStr(fragment, R.string.NETERRORGOING));
                        dialogDismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp)
                    {
                        try
                        {
                            JSONObject jsonData = new JSONObject(resp.result);
                            String code = jsonData.getString("code");
                            if (code.equals("200"))
                            {
                                JSONObject datas = jsonData
                                        .getJSONObject("data");
                                String res = datas.getString("delResult");
                                if (res.equals("true"))
                                {
                                    Tools.toast(getActivity(), Tools.getStr(
                                            fragment, R.string.DELSUCCESS));
                                    if (position != -1)
                                    {
                                        useData.remove(position);
                                    } else
                                    {
                                        useData.clear();
                                        emptyTextView.setText("暂无更多");
                                    }
                                    adaper.notifyDataSetChanged();
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

    protected void loadDatas()
    {
        dialogProgress( Tools.getStr(fragment, R.string.LOADING));
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("member_id", ConfigUtil.readString (
                getActivity (), MyConstant.KEY_MEMBERID, ""));
        rp.addQueryStringParameter("pagenumber", String.valueOf(pageNum++));
        rp.addQueryStringParameter("pagesize", String.valueOf(pageSize));
        HttpHelper.postByCommand(commandload, rp,
                new RequestCallBack<String>()
                {
                    @Override
                    public void onStart()
                    {
                        updateFooter(true, null);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                        pageNum--;
                        more = false;
                        updateFooter(false,
                                Tools.getStr(fragment, R.string.NETERRORCLICK));
                        dialogDismiss();
                        emptyTextView.setText(Tools.getStr(fragment,
                                R.string.NETERRORCLICK));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp)
                    {
                        String result = resp.result;
                        if ("".equals(result))
                        {
                            updateFooter(false,
                                    Tools.getStr(fragment, R.string.NOGOODS));
                            emptyTextView.setText(type == 1 ? Tools.getStr(
                                    fragment, R.string.NOALREADYBUYS) : Tools
                                    .getStr(fragment, R.string.NONOPAYS));
                        }
                        try
                        {
                            JSONObject jsonResult = new JSONObject(result);
                            L.l (result);
                            if (HttpHelper.isSuccess(jsonResult))
                            {
                                JSONArray ja = jsonResult.getJSONArray("data");
                                if (ja.length() > 0)
                                {
                                    String haveMore = jsonResult
                                            .getString("haveMore");
                                    list.setTag(haveMore);
                                    for (int x = 0; x < ja.length(); x++)
                                    {
                                        JSONObject data = ja.getJSONObject(x);
                                        GoodMore goodMoreEntity = new GoodMore();
                                        goodMoreEntity.setGoodsName(data
                                                .getString("item_name"));
                                        goodMoreEntity.setGoodsPic(data
                                                .getString("goods_pic"));
                                        goodMoreEntity.setGoodsPrice(data
                                                .getString("price"));
                                        goodMoreEntity.setGoodsId(data
                                                .getString("item_id"));
                                        goodMoreEntity.setStoreName(data
                                                .getString("store_name"));
                                        goodMoreEntity.setOther(data
                                                .getString("order_id"));
                                        goodMoreEntity.setStoreId(data
                                                .getString("store_id"));
                                        useData.add(goodMoreEntity);
                                    }
                                    adaper.notifyDataSetChanged();
                                    if ("true".equals(haveMore))
                                    {
                                        updateFooter(false, Tools.getStr(
                                                fragment,
                                                R.string.CLICKLOADINGMORE));
                                    } else
                                    {
                                        more = false;
                                        updateFooter(false, Tools.getStr(
                                                fragment, R.string.ALLLOADED));
                                    }
                                } else
                                {
                                    more = false;
                                    updateFooter(
                                            false,
                                            type == 1 ? Tools.getStr(fragment,
                                                    R.string.NOALREADYBUYS)
                                                    : Tools.getStr(fragment,
                                                    R.string.NONOPAYS));
                                    emptyTextView.setText(type == 1 ? Tools
                                            .getStr(fragment,
                                                    R.string.NOALREADYBUYS)
                                            : Tools.getStr(fragment,
                                            R.string.NONOPAYS));
                                }
                                dialogDismiss();
                                return;
                            }
                            updateFooter(false,
                                    Tools.getStr(fragment, R.string.JSONERROR));
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            updateFooter(false,
                                    Tools.getStr(fragment, R.string.JSONERROR));
                            dialogDismiss();
                        }
                        dialogDismiss();
                    }

                });
    }

    protected void updateFooter(boolean loading, String info)
    {
        if (loading)
        {
            footerTV.setVisibility(View.GONE);
            footerPB.setVisibility(View.VISIBLE);
        } else
        {
            footerPB.setVisibility(View.GONE);
            footerTV.setVisibility(View.VISIBLE);
            footerTV.setText(info);
        }
    }

    @OnClick({R.id.footer_tv})
    protected void onClick(View view)
    {
        if (view.getId() == R.id.footer_tv && list.getTag() != null
                && list.getTag().equals("true"))
        {
            loadDatas();
        }
    }

    protected boolean hasLogin()
    {
        return !"".equals(memberId);
    }

    protected void promtLogin()
    {
        Tools.dialog(getActivity(),
                Tools.getStr(fragment, R.string.REQUESTLOGIN),
                true, new onButtonClick()
                {
                    @Override
                    public void buttonClick()
                    {
                        startActivity(new Intent(getActivity(),
                                LoginActivity.class));
                    }
                });
    }

    class BuyedAdapter extends MyBaseAdapter<GoodMore>
    {
        Context context;

        public BuyedAdapter(Context context, List<GoodMore> datas,
                            int layoutId)
        {
            super(context, datas, layoutId);
            this.context = context;
        }

        @Override
        protected void convert(final MyViewHolder viewHolder,
                               final GoodMore bean)
        {
            final SmartImageView image = viewHolder.getView(R.id.good_pic);
            final TextView name = viewHolder.getView(R.id.good_name);
            final TextView price = viewHolder.getView(R.id.price);
            final Button again = viewHolder.getView(R.id.again);
            final Button judge = viewHolder.getView(R.id.judge);

            name.setText(bean.getGoodsName());
            price.setText("￥" + bean.getGoodsPrice());
            image.setImageUrl(bean.getGoodsPic(), R.drawable.zhanwei,
                    R.drawable.zhanwei);
            again.setVisibility(View.INVISIBLE);
            again.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, BuyNowActivity.class);
                    intent.putExtra("goodId", bean.getGoodsId());
                    intent.putExtra("goodName", bean.getGoodsName());
                    intent.putExtra("storeId", bean.getStoreId());
                    intent.putExtra("storeName", bean.getStoreName());
                    intent.putExtra("quantity", "1");
                    intent.putExtra("goodPrice", bean.getGoodsPrice());
                    context.startActivity(intent);
                    AnimUtil.animToSlide((Activity) context);

                }
            });
            judge.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, JudgeActivity.class);
                    intent.putExtra("judge", bean);
                    intent.putExtra("type", "good");
                    context.startActivity(intent);
                    AnimUtil.animToSlide((Activity) context);

                }
            });
        }

    }

    public int getCount()
    {
        return useData.size();
    }

}
