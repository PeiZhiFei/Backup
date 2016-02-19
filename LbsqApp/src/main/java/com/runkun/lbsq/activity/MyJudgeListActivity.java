package com.runkun.lbsq.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.runkun.lbsq.R;
import com.runkun.lbsq.bean.Comment;
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

public class MyJudgeListActivity extends BaseAcitivity
{
    private SwipeMenuListView judgeList;
    @ViewInject(R.id.footer_tv)
    private TextView footerTV;

    @ViewInject(R.id.footer_pb)
    private ProgressBar footerPB;

    private MyJudgeAdapter adapter;
    private List<Comment> comments = new ArrayList<Comment> ();
    private int pageSize = 20;
    private int pageNum = 1;
    protected boolean more = true;
    protected TextView emptyTextView;
    protected View fv;

    @Override
    protected void onCreate (Bundle paramBundle)
    {
        super.onCreate (paramBundle);
        setContentView (R.layout.activity_my_judge);
        ViewUtils.inject (this);
        judgeList = (SwipeMenuListView) findViewById (R.id.judge_list);
        tint ();
        initActionbar ();
        setTitles (Tools.getStr (activity, R.string.TMYJUDGE));
        dialogInit();
        actionbar_right2.setVisibility (View.VISIBLE);
        actionbar_right2.setBackgroundResource (R.drawable.ic_delete);
        actionbar_right2.setOnClickListener (new OnClickListener ()
        {
            @Override
            public void onClick (View v)
            {
                if ( getCount () > 0 )
                {
                    new AlertDialog.Builder (activity)
                            .setTitle (Tools.getStr (activity, R.string.DTITLE))
                            .setMessage (
                                    Tools.getStr (activity, R.string.DDELJUDGE))
                            .setPositiveButton (
                                    Tools.getStr (activity, R.string.CONFIM),
                                    new DialogInterface.OnClickListener ()
                                    {

                                        @Override
                                        public void onClick (
                                                DialogInterface dialog,
                                                int which)
                                        {
                                            deleteJudge (-1);
                                        }
                                    })
                            .setNegativeButton (
                                    Tools.getStr (activity, R.string.CANCEL),
                                    null).show ();
                } else
                {
                    Tools.toast (activity, "这里是空的");
                }

            }
        });

        Tools tools = new Tools ();
        View view2 = tools.getEmptyView (this, 0);
        ((ViewGroup) judgeList.getParent ()).addView (view2);
        emptyTextView = tools.getEmptyText ();
        emptyTextView.setOnClickListener (new OnClickListener ()
        {

            @Override
            public void onClick (View arg0)
            {
                loadComments ();
            }
        });
        judgeList.setEmptyView (view2);

        fv = LayoutInflater.from (activity).inflate (R.layout.listview_footer,
                null);
        ViewUtils.inject (this, fv);
        judgeList.addFooterView (fv);
        adapter = new MyJudgeAdapter (this, comments, R.layout.item_judge);
        judgeList.setAdapter (adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator ()
        {
            @Override
            public void create (SwipeMenu menu)
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem (
                        getApplicationContext ());
                deleteItem.setBackground (new ColorDrawable (Color.rgb (0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth (Tools.dp2px (activity, 70));
                deleteItem.setIcon (R.drawable.ic_delete);
                menu.addMenuItem (deleteItem);
            }
        };
        judgeList.setMenuCreator (creator);
        judgeList.setOnMenuItemClickListener (new SwipeMenuListView.OnMenuItemClickListener ()
        {
            @Override
            public void onMenuItemClick (int position, SwipeMenu menu, int index)
            {
                deleteJudge (position);
            }
        });
        judgeList.setOnScrollListener (new OnScrollListener ()
        {
            @Override
            public void onScrollStateChanged (AbsListView view, int scrollState)
            {
                if ( scrollState == OnScrollListener.SCROLL_STATE_IDLE )
                {
                    if ( view.getLastVisiblePosition () == view.getCount () - 1 )
                    {
                        if ( more )
                        {
                            loadComments ();
                        }
                    }
                }
            }

            @Override
            public void onScroll (AbsListView view, int firstVisibleItem,
                                  int visibleItemCount, int totalItemCount)
            {
                lastItem = firstVisibleItem + visibleItemCount - 1;
            }
        });
        loadComments ();
    }

    int lastItem;

    private void loadComments ()
    {
       dialogProgress (activity,
                Tools.getStr (activity, R.string.LOADING));
        RequestParams rp = new RequestParams ();
        rp.addQueryStringParameter ("member_id",
                ConfigUtil.readString (this, MyConstant.KEY_MEMBERID, ""));
        rp.addQueryStringParameter ("pagenumber", String.valueOf (pageNum++));
        rp.addQueryStringParameter ("pagesize", String.valueOf (pageSize));

        HttpHelper.postByCommand ("mycommentlist", rp, new RequestCallBack<String> ()
        {
            @Override
            public void onStart ()
            {
                updateFooter (true, null);
            }

            @Override
            public void onFailure (HttpException arg0, String arg1)
            {
                pageNum--;
                updateFooter (false,
                        Tools.getStr (activity, R.string.NETERRORCLICK));
                emptyTextView.setText (Tools.getStr (activity,
                        R.string.NETERRORCLICK));
               dialogDismiss ();
            }

            @Override
            public void onSuccess (ResponseInfo<String> resp)
            {
                String result = resp.result;
                if ( "".equals (result) )
                {
                    more = false;
                    updateFooter (false,
                            Tools.getStr (activity, R.string.NOJUDGE2));
                    emptyTextView.setText (Tools.getStr (activity,
                            R.string.NOJUDGE2));
                    // fv.setVisibility(View.INVISIBLE);
                }
                try
                {
                    JSONObject jsonResult = new JSONObject (result);
                    if ( HttpHelper.isSuccess (jsonResult) )
                    {
                        JSONArray ja = jsonResult.getJSONArray ("datas");
                        if ( ja.length () > 0 )
                        {
                            String haveMore = jsonResult
                                    .getString ("haveMore");
                            judgeList.setTag (haveMore);
                            for (int i = 0; i < ja.length (); i++)
                            {
                                JSONObject jsonObject = ja
                                        .getJSONObject (i);
                                Comment commentEntity = new Comment ();
                                commentEntity.setOrderSn (jsonObject
                                        .getString ("order_sn"));
                                commentEntity.setStoreId (jsonObject
                                        .getString ("store_id"));
                                commentEntity.setMemberName (jsonObject
                                        .getString ("member_name"));
                                commentEntity.setComment (jsonObject
                                        .getString ("comment"));
                                commentEntity.setFlowerNum (jsonObject
                                        .getInt ("flower_num"));
                                commentEntity.setAddTime (jsonObject
                                        .getString ("add_time"));
                                commentEntity.setGoods_id (jsonObject
                                        .getString ("goods_id"));
                                commentEntity.setStorePic (jsonObject
                                        .getString ("store_pic"));
                                comments.add (commentEntity);
                            }
                            adapter.notifyDataSetChanged ();
                            if ( "true".equals (haveMore) )
                            {
                                updateFooter (false, Tools.getStr (
                                        activity,
                                        R.string.CLICKLOADINGMORE));
                            } else
                            {
                                more = false;
                                updateFooter (false, Tools.getStr (
                                        activity, R.string.ALLLOADED));
                            }
                            emptyTextView.setText (Tools.getStr (
                                    activity, R.string.JUSTSO));
                        } else
                        {
                            more = false;
                            updateFooter (false, Tools.getStr (activity,
                                    R.string.NOJUDGE2));
                            emptyTextView.setText (Tools.getStr (
                                    activity, R.string.NOJUDGE2));
                        }
                       dialogDismiss ();
                        return;
                    }
                    updateFooter (false,
                            Tools.getStr (activity, R.string.JSONERROR));
                } catch (JSONException e)
                {
                    e.printStackTrace ();
                    updateFooter (false,
                            Tools.getStr (activity, R.string.JSONERROR));
                    emptyTextView.setText (Tools.getStr (activity, R.string.JSONERROR));
                   dialogDismiss ();
                }
               dialogDismiss ();
            }

        });
    }

    private void updateFooter (boolean loading, String info)
    {
        if ( loading )
        {
            footerTV.setVisibility (View.GONE);
            footerPB.setVisibility (View.VISIBLE);
        } else
        {
            footerPB.setVisibility (View.GONE);
            footerTV.setVisibility (View.VISIBLE);
            footerTV.setText (info);
        }
    }

    @OnClick({R.id.footer_tv})
    public void onClick (View view)
    {
        if ( view.getId () == R.id.footer_tv )
        {
            String haveMore = (String) judgeList.getTag ();
            if ( "true".equals (haveMore) )
            {
                loadComments ();
            }
        }
    }

    private class MyJudgeAdapter extends MyBaseAdapter<Comment>
    {

        public MyJudgeAdapter (Context context, List<Comment> datas,
                               int layoutId)
        {
            super (context, datas, layoutId);
        }

        @Override
        protected void convert (MyViewHolder viewHolder, Comment bean)
        {
            SmartImageView photo = viewHolder.getView (R.id.judge_photo_img);
            RatingBar rb = viewHolder.getView (R.id.rationbar);
            TextView nick = viewHolder.getView (R.id.judge_user_nick);
            TextView content = viewHolder.getView (R.id.judge_content);
            TextView date = viewHolder.getView (R.id.judge_date);
            photo.setImageUrl (bean.getStorePic ());
            content.setText (bean.getComment ());
            rb.setRating (bean.getFlowerNum ());
            nick.setText (bean.getOrderSn ());
            date.setText (bean.getAddTime ());
        }

    }

    private void deleteJudge (final int position)
    {
       dialogProgress (activity,
                Tools.getStr (activity, R.string.LOADING));
        RequestParams rp = new RequestParams ();
        String url;
        if ( position == -1 )
        {
            url = "alldelmycomment";
            rp.addQueryStringParameter ("member_id",
                    HttpHelper.getPrefParams (this, "memberId"));
        } else
        {
            url = "delmycomment";
            rp.addQueryStringParameter ("order_sn", comments.get (position)
                    .getOrderSn ());
        }
        //                HttpHelper.postByCommand (url, rp, new RequestCallBack<String> ()
        //                {

        //        HttpUtils httpUtils = new HttpUtils ();
        //        httpUtils.send (HttpRequest.HttpMethod.POST, MyConstant.API_BASE_URL + url, rp,
        //                new RequestCallBack<String> ()
        //                {

        HttpHelper.postByCommand (url, rp, new RequestCallBack<String> ()
        {

            @Override
            public void onFailure (HttpException arg0, String arg1)
            {
                Tools.toast (activity,
                        Tools.getStr (activity, R.string.NETWORKERROR));
               dialogDismiss ();
            }

            @Override
            public void onSuccess (ResponseInfo<String> resp)
            {
                try
                {
                    L.l (resp.result);
                    JSONObject jsonData = new JSONObject (resp.result);
                    if ( HttpHelper.isSuccess (jsonData) )
                    {
                        JSONObject seData = jsonData.getJSONObject ("data");
                        String r = seData.getString ("delResult");
                        if ( r.equals ("true") )
                        {
                            if ( position == -1 )
                            {
                                comments.clear ();
                            } else
                            {
                                comments.remove (position);
                            }
                            adapter.notifyDataSetChanged ();
                            Tools.toast (activity,
                                    Tools.getStr (activity, R.string.DELSUCCESS));
                        } else
                        {
                            Tools.toast (activity,
                                    Tools.getStr (activity, R.string.DELFAIL));
                        }
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace ();
                   dialogDismiss ();
                }
               dialogDismiss ();
            }
        });
    }

    public int getCount ()
    {
        return comments.size ();
    }

}
