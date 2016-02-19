package laiyi.tobacco.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import feifei.library.util.AnimUtil;
import feifei.library.util.L;
import feifei.library.util.Tools;
import feifei.library.view.swipmenu.SwipeMenu;
import feifei.library.view.swipmenu.SwipeMenuCreator;
import feifei.library.view.swipmenu.SwipeMenuItem;
import feifei.library.view.swipmenu.SwipeMenuListView;
import laiyi.tobacco.R;
import laiyi.tobacco.adapter.OrderAdapter;
import laiyi.tobacco.bean.Land;

public class ListViewActivity extends DataActivity
{
    List<Land> datas = new ArrayList<> ();
    OrderAdapter adapter;
    @InjectView(R.id.list)
    SwipeMenuListView list;
    Dao<Land, Integer> mLandDao;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_list_view);
        ButterKnife.inject (this);
        tint ();
        initActionbar ();
        setLeftBack();
        setTitles ("地块列表");
        try
        {
            mLandDao = mDbHelper.getLandDao ();
        } catch (SQLException e)
        {
            e.printStackTrace ();
        }
        L.l (1);
        initData ();
        adapter = new OrderAdapter (this, datas);

        actionbar_right2.setVisibility (View.VISIBLE);
        actionbar_right2.setBackgroundResource (R.drawable.ic_add);
        actionbar_right2.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View view)
            {
                Intent intent = new Intent (activity, DetailActivity.class);
                intent.putExtra ("type", "add");
                startActivity (intent);
                AnimUtil.animTo (activity);
            }
        });
        L.l (2);
        list.setAdapter (adapter);
        list.setOnItemClickListener (new AdapterView.OnItemClickListener ()
        {
            @Override
            public void onItemClick (AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent (activity, DetailActivity.class);
                intent.putExtra ("type", "edit");
                intent.putExtra ("data", datas.get (i));
                startActivity (intent);
                AnimUtil.animTo (activity);
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator ()
        {
            @Override
            public void create (SwipeMenu menu)
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem (activity);
                deleteItem.setBackground (new ColorDrawable (Color.rgb (0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth (Tools.dp2px (activity, 70));
                deleteItem.setIcon (R.drawable.ic_delete);
                menu.addMenuItem (deleteItem);
            }
        };
        list.setMenuCreator (creator);
        list.setOnMenuItemClickListener (new SwipeMenuListView.OnMenuItemClickListener ()
        {
            @Override
            public void onMenuItemClick (int position, SwipeMenu menu, int index)
            {
                //                try
                //                {
                ////                    lan = mLandDao.queryForId(datas.get (position).getId ());
                //                } catch (SQLException e)
                //                {
                //                    e.printStackTrace ();
                //                }
                Land land = datas.get (position);
                land.setState (3);
                try
                {
                    mLandDao.createOrUpdate (land);
                } catch (SQLException e)
                {
                    e.printStackTrace ();
                }
                datas.remove (position);
                adapter.notifyDataSetChanged ();
                Tools.toast (activity, "删除成功");
                //                datas = mLandDao.queryBuilder ().where ().notIn ("State", 3).query ();

            }

        });

        //        adapter.notifyDataSetChanged ();
    }

    @Override
    protected void onResume ()
    {
        super.onResume ();
        initData ();
        adapter = new OrderAdapter (this, datas);
        list.setAdapter (adapter);
        //        adapter.notifyDataSetChanged ();

    }

    private void initData ()
    {
        try
        {
            datas = mLandDao.queryBuilder ().where ().notIn ("State", 3).query ();
            L.l ("datas.size()==" + datas.size ());
            for (int i = 0; i < datas.size (); i++)
            {
                L.l (datas.get (i).getLandName ());
            }
        } catch (SQLException e)
        {
            e.printStackTrace ();
        }

    }

}
