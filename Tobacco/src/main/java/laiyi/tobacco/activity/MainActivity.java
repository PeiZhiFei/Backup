package laiyi.tobacco.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import feifei.library.util.AnimUtil;
import feifei.library.view.BadgeView;
import laiyi.tobacco.R;
import laiyi.tobacco.adapter.NewsListAdapter;
import laiyi.tobacco.bean.Land;
import laiyi.tobacco.bean.News;
import laiyi.tobacco.fragment.BlankFragment;

public class MainActivity extends DataActivity implements
        ViewPager.OnPageChangeListener
{
    private ViewPager pager;
    private ListView listView;
    private MyPagerAdapter adapter;
    private ImageView dot, dots[];
    int lenth = 1;
    private NewsListAdapter adapter2;
    private List<News> datas = new ArrayList<> ();
    Dao<Land, Integer> mLandDao;
    public List<Land> m;
    //    BlankFragment blankFragment=BlankFragment.newInstance ("1");
    BlankFragment blankFragment;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        tint ();
        initActionbar ();
        setSwipeBackEnable (false);
        try
        {
            mLandDao = mDbHelper.getLandDao ();
        } catch (SQLException e)
        {
            e.printStackTrace ();
        }

//        actionbar_left.setText (getIntent ().getStringExtra ("city"));
        actionbar_left.setText ("黔南州");
        actionbar_left.setTextSize (14);
        actionbar_left.setTextColor (Color.parseColor ("#ffffff"));
        actionbar_left.setBackgroundColor (Color.parseColor ("#00000000"));
        actionbar_left.setVisibility (View.VISIBLE);
        actionbar_left.setOnClickListener (new View.OnClickListener ()
        {

            @Override
            public void onClick (View v)
            {
                Intent intent = new Intent (activity,
                        SelectorCity.class);
                startActivityForResult (intent, 5);
                AnimUtil.animTo (activity);
            }
        });

        blankFragment = BlankFragment.newInstance ("1");
        initDot ();
        pager = (ViewPager) findViewById (R.id.pager);
        adapter = new MyPagerAdapter (getSupportFragmentManager ());
        pager.setAdapter (adapter);
        pager.setOnPageChangeListener (this);
        pager.setCurrentItem (0);
        listView = (ListView) findViewById (R.id.list);
        adapter2 = new NewsListAdapter (this, datas);
        listView.setAdapter (adapter2);
        listView.setOnItemClickListener (new AdapterView.OnItemClickListener ()
        {
            @Override
            public void onItemClick (AdapterView<?> adapterView, View view, int i, long l)
            {
                startActivity (new Intent (activity, MyWebActivity.class));
                AnimUtil.animTo (activity);
            }
        });
        initData ();
        new Handler ().postDelayed (new Runnable ()
        {
            @Override
            public void run ()
            {
                badgeView = new BadgeView (activity, blankFragment.layout8);
            }
        }, 500);

    }

    @Override
    protected void onResume ()
    {
        super.onResume ();
        try
        {
            m = mLandDao.queryBuilder ().where ().notIn ("State", 0).query ();
            new Handler ().postDelayed (new Runnable ()
            {
                @Override
                public void run ()
                {
                    badgeView (m.size ());
                }
            }, 500);
        } catch (SQLException e)
        {
            e.printStackTrace ();

        }
    }

    BadgeView badgeView;

    private void badgeView (int size)
    {
        if ( size != 0 )
        {
            badgeView.setText (String.valueOf (size));
            badgeView.show (true);
        }
        if ( size == 0 )
        {
            badgeView.hide (true);
        }
    }

    private void initData ()
    {
        for (int i = 0; i < 10; i++)
        {
            News news = new News ();
            news.setNewsID ("123");
            news.setNewsTitle ("贵州省局（公司）统一全省下部烟叶收购等级尺度");
            //            news.setPicURL ("http://www.lagou.com/image1/M00/0A/B7/CgYXBlTwEW-ADfY4AASAE2DHHDA134.png");
            news.setPublishDate ("2015-09-1" + i);
            datas.add (news);
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter
    {
        private final String[] TITLES = {"趋势概览"};
        private int mChildCount = 0;

        public MyPagerAdapter (FragmentManager fm)
        {
            super (fm);
        }

        @Override
        public CharSequence getPageTitle (int position)
        {
            return TITLES[position];
        }

        @Override
        public int getCount ()
        {
            return TITLES.length;
        }

        @Override
        public void notifyDataSetChanged ()
        {
            mChildCount = getCount ();
            super.notifyDataSetChanged ();
        }

        public int getItemPosition (Object object)
        {
            if ( mChildCount > 0 )
            {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition (object);
        }

        @Override
        public Fragment getItem (int position)
        {
            return blankFragment;
        }
    }

    @Override
    public void onPageScrolled (int position, float positionOffset,
                                int positionOffsetPixels)
    {
    }

    @Override
    public void onPageSelected (int position)
    {
        setCurDot (position);
        setCurView (position);
    }

    @Override
    public void onPageScrollStateChanged (int state)
    {
    }

    private void initDot ()
    {
        LinearLayout viewGroup = (LinearLayout) findViewById (R.id.viewGroup);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams (
                15, 15);
        layoutParams.setMargins (5, 5, 5, 5);
        dots = new ImageView[lenth];
        for (int i = 0; i < dots.length; i++)
        {
            dot = new ImageView (this);
            dot.setLayoutParams (layoutParams);
            dots[i] = dot;
            dots[i].setTag (i);
            if ( i == 0 )
            {
                dots[i].setBackgroundResource (R.drawable.shape_circle_green);
            } else
            {
                dots[i].setBackgroundResource (R.drawable.shape_circle_white);
            }
            viewGroup.addView (dots[i]);
        }
    }

    private void setCurDot (int position)
    {
        for (int i = 0; i < dots.length; i++)
        {
            if ( position == i )
            {
                dots[i].setBackgroundResource (R.drawable.shape_circle_green);
            } else
            {
                dots[i].setBackgroundResource (R.drawable.shape_circle_white);
            }
        }
    }

    private void setCurView (int position)
    {
        if ( position < 0 || position > lenth )
        {
            return;
        }
        pager.setCurrentItem (position);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult (requestCode, resultCode, data);
        if ( requestCode == 5 && resultCode == RESULT_OK && data != null )
        {
            actionbar_left.setText (data.getStringExtra ("city"));
        }
    }

}
