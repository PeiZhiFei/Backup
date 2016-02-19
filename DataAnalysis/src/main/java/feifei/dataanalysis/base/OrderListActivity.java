package feifei.dataanalysis.base;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

import butterknife.ButterKnife;
import butterknife.InjectView;
import feifei.dataanalysis.R;
import feifei.project.view.PagerSlidingTabStrip;

public class OrderListActivity extends BaseActivity
{

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @InjectView(R.id.pager)
    ViewPager pager;
    MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        {
            setSwipeBackEnable(false);
        }
        ButterKnife.inject(this);
        tint();
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setTextColor (new ColorStateList (new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_selected},
                new int[]{}
        },
                new int[]{
                        getResources ().getColor (R.color.main_green),
                        getResources ().getColor (R.color.main_green),
                        getResources ().getColor (R.color.darkgray2)
                }));
        tabs.setViewPager(pager);


    }

    public long exitTime = 0;

    public class MyPagerAdapter extends FragmentPagerAdapter
    {

        private final String[] TITLES = {"今天", "本周", "本月"};

        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return TITLES[position];
        }

        @Override
        public int getCount()
        {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position)
        {
            return OrderFragment.newInstance(position);
//            return TestHorizontalFragmentMoney.newInstance (position);
        }

    }


}
