package com.runkun.lbsq.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.runkun.lbsq.R;
import com.runkun.lbsq.bean.Address;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feifei.project.util.MyBaseAdapter;
import feifei.project.util.MyViewHolder;
import feifei.project.view.swipmenu.SwipeMenu;
import feifei.project.view.swipmenu.SwipeMenuCreator;
import feifei.project.view.swipmenu.SwipeMenuItem;
import feifei.project.view.swipmenu.SwipeMenuListView;

public class MyAddressActivity extends BaseAcitivity
{

    @ViewInject(R.id.address_list)
    private SwipeMenuListView list;
    private List<Address> listdatas = new ArrayList<>();
    private AddressAdapter adapter;
    private TextView emptyTextView;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_myaddress);
        ViewUtils.inject(this);
        tint();
        initActionbar();
        setTitles(Tools.getStr(this, R.string.TMYADS));
        dialogInit();
        actionbar_right2.setBackgroundResource(R.drawable.myaddressadd);
        actionbar_right2.setVisibility(View.VISIBLE);
        actionbar_right2.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(activity,
                        AddAddressActivity.class), 8);
                AnimUtil.animToSlide(activity);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(MyConstant.FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Tools tools = new Tools();
        View view2 = tools.getEmptyView(this, 0);
        ((ViewGroup) list.getParent()).addView(view2);
        emptyTextView = tools.getEmptyText();
        list.setEmptyView(view2);

        adapter = new AddressAdapter(activity, listdatas, R.layout.item_address);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                Intent intent = new Intent(activity, AddAddressActivity.class);
                intent.putExtra("address",
                        listdatas.get(position));
                startActivityForResult(intent, 5);
                AnimUtil.animToSlide(activity);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator()
        {
            @Override
            public void create(SwipeMenu menu)
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(Tools.dp2px(activity, 70));
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
                deleteAddress(position);

            }
        });
        loadData();
    }

    private void loadData()
    {
        dialogProgress(activity,
                Tools.getStr(this, R.string.LOADING));
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("member_id",
                HttpHelper.getPrefParams(this, MyConstant.KEY_MEMBERID));
        HttpHelper.postByCommand("addresslist", rp,
                new RequestCallBack<String>()
                {
                    @Override
                    public void onFailure(HttpException paramHttpException,
                                          String paramString)
                    {
                        Tools.toast(activity, paramString);
                        emptyTextView.setText(Tools.getStr(activity,
                                R.string.NETERRORGOING));
                        dialogDismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> paramResponseInfo)
                    {
                        try
                        {
                            JSONObject jsonData = new JSONObject(
                                    paramResponseInfo.result);
                            if (HttpHelper.isSuccess(jsonData))
                            {
                                // adapter.setData(JSON.parseArray(jsonData.getString("data"), Address.class));
                                listdatas.addAll(JSON.parseArray(jsonData.getString("data"), Address.class));
                                for (int i = 0; i < listdatas.size(); i++)
                                {
                                    Address myAddress = listdatas.get(i);
                                    if (myAddress.getIsdefault())
                                    {
                                        editor.putString(MyConstant.KEY_ADDCON,
                                                myAddress.getAddress());
                                        editor.putString(MyConstant.KEY_ADDCONTACT,
                                                myAddress.getConsigner());
                                        editor.putString(MyConstant.KEY_ADDMOBILE,
                                                myAddress.getMobile());
                                        editor.putString(MyConstant.KEY_ADDREMARK,
                                                myAddress.getRemark());
                                        editor.apply();
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                emptyTextView.setText(Tools.getStr(activity,
                                        R.string.ALLLOADED));

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

    private void deleteAddress(final int position)
    {
        dialogProgress(activity,
                Tools.getStr(this, R.string.DELETING));
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("id", listdatas.get(position).getId());
        HttpHelper.postByCommand("deladdressinfo", rp,
                new RequestCallBack<String>()
                {
                    @Override
                    public void onFailure(HttpException paramHttpException,
                                          String paramString)
                    {
                        Tools.toast(activity, paramString);
                        emptyTextView.setText(Tools.getStr(activity,
                                R.string.NETERRORGOING));
                        dialogDismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> paramResponseInfo)
                    {
                        try
                        {
                            JSONObject jsonData = new JSONObject(
                                    paramResponseInfo.result);
                            if (HttpHelper.isSuccess(jsonData))
                            {
                                JSONObject data = jsonData
                                        .getJSONObject("data");
                                String re = data.getString("delResult");
                                if (re.equals("true"))
                                {
                                    if (listdatas.get(position).getIsdefault())
                                    {
                                        editor.remove(MyConstant.KEY_ADDCON);
                                        editor.remove(MyConstant.KEY_ADDCONTACT);
                                        editor.remove(MyConstant.KEY_ADDMOBILE);
                                        editor.remove(MyConstant.KEY_ADDREMARK);
                                        editor.apply();
                                    }
                                    listdatas.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Tools.toast(activity, Tools.getStr(
                                            activity, R.string.DELSUCCESS));
                                } else
                                {
                                    Tools.toast(activity, Tools.getStr(
                                            activity, R.string.DELFAIL));
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

    class AddressAdapter extends MyBaseAdapter<Address>
    {

        public AddressAdapter(Context context, List<Address> datas,
                              int layoutId)
        {
            super(context, datas, layoutId);
        }

        @Override
        protected void convert(final MyViewHolder viewHolder,
                               final Address bean)
        {
            final TextView userName = viewHolder.getView(R.id.user_name);
            final TextView addr = viewHolder.getView(R.id.address);
            final TextView phone = viewHolder.getView(R.id.phone);
            final Button isSelected = viewHolder.getView(R.id.is_selected);
            userName.setText(bean.getConsigner());
            addr.setText(bean.getAddress());
            phone.setText(bean.getMobile());
            if (bean.getIsdefault())
            {
                userName.setText("(默认)" + bean.getConsigner());
                userName.setTextColor(context.getResources().getColor(
                        R.color.main_red_color));
            } else
            {
                userName.setText(bean.getConsigner());
                userName.setTextColor(getResources().getColor(
                        R.color.darkgray));
            }
            isSelected
                    .setBackgroundResource(bean.getIsdefault() ? R.drawable.check_select
                            : R.drawable.check_normal);
            isSelected.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    if (bean.getIsdefault())
                    {
                        Tools.toast(activity,
                                Tools.getStr(activity, R.string.ADSALREADYDEF));
                    } else
                    {
                        dialogProgress(activity,
                                Tools.getStr(activity, R.string.ADSSETTING));
                        RequestParams rp = new RequestParams();
                        rp.addQueryStringParameter("member_id",
                                HttpHelper.getPrefParams(activity, MyConstant.KEY_MEMBERID));
                        rp.addQueryStringParameter("id", bean.getId());
                        HttpHelper.postByCommand("moddeaddress", rp,
                                new RequestCallBack<String>()
                                {
                                    @Override
                                    public void onFailure(
                                            HttpException paramHttpException,
                                            String paramString)
                                    {
                                        Tools.toast(activity, paramString);
                                        dialogDismiss();
                                    }

                                    @Override
                                    public void onSuccess(
                                            ResponseInfo<String> paramResponseInfo)
                                    {
                                        try
                                        {
                                            JSONObject jsonData = new JSONObject(
                                                    paramResponseInfo.result);
                                            if (HttpHelper.isSuccess(jsonData))
                                            {
                                                String re = jsonData
                                                        .getString("data");
                                                if (re.equals("true"))
                                                {
                                                    editor.putString(MyConstant.KEY_ADDCON,
                                                            bean.getAddress());
                                                    editor.putString(MyConstant.KEY_ADDCONTACT,
                                                            bean.getConsigner());
                                                    editor.putString(MyConstant.KEY_ADDMOBILE,
                                                            bean.getMobile());
                                                    editor.putString(MyConstant.KEY_ADDREMARK,
                                                            bean.getRemark());
                                                    editor.apply();
                                                    for (int i = 0; i < listdatas
                                                            .size(); i++)
                                                    {
                                                        listdatas.get(i)
                                                                .setIsdefault(false);
                                                    }
                                                    bean.setIsdefault(true);
                                                    adapter.notifyDataSetChanged();

                                                    Tools.toast(activity, "设置成功");
                                                } else
                                                {
                                                    Tools.toast(activity, "设置失败");
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

                }

            });
        }
    }

    public void reloadData()
    {
        listdatas.clear();
        adapter.notifyDataSetChanged();
        loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode == 8 &&
        if (resultCode == 12)
        {
            emptyTextView.setText(Tools.getStr(this, R.string.ADSORDER));
            reloadData();
        }
    }

}
