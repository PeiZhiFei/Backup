package com.runkun.lbsq.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runkun.lbsq.R;


@SuppressLint("NewApi")
public class MainFirstStaticFragment extends BaseFragment
{
//    private RefreshScrollView scrollView;
//    private SlowScrollView scrollView;
    int[] ids = new int[]{R.id.fragment0, R.id.fragment1, R.id.fragment2,
            R.id.fragment3, R.id.fragment4};

    Fragment[] fragments;
    MainFirstFragment[] fragments2;
    MainAdsFragment mainAdsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main_first, container,
                false);
//        scrollView = (SlowScrollView) view.findViewById(R.id.scroll_view);
//        scrollView.setMode(RefreshBase.Mode.PULL_FROM_START);
//        scrollView.setScrollingWhileRefreshingEnabled(true);
//        scrollView.setMode(RefreshBase.Mode.DISABLED);
//        scrollView.setOnRefreshListener(new RefreshBase.OnRefreshListener<ScrollView>()
//        {
//            @Override
//            public void onRefresh(RefreshBase<ScrollView> refreshBase)
//            {
//                reloadData();
//                new Handler().postDelayed(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        scrollView.onRefreshComplete();
//                    }
//                }, 2000);
//            }
//        });
        // MainAdsFragment mainAdsFragment = new MainAdsFragment();
        // MainJiushuiFragment mainJiushuiFragment = new MainJiushuiFragment();
        // MainLiangyouFragment mainLiangyouFragment = new
        // MainLiangyouFragment();
        // MainShipinFragment mainShipinFragment = new MainShipinFragment();
        // MainTejiaFragment mainTejiaFragment = new MainTejiaFragment();
        //
        // Fragment[] fragments = { mainAdsFragment, mainTejiaFragment,
        // mainShipinFragment, mainJiushuiFragment, mainLiangyouFragment };
        mainAdsFragment = new MainAdsFragment();
        // MainJiushuiFragment mainJiushuiFragment = new MainJiushuiFragment();
        // MainLiangyouFragment mainLiangyouFragment = new
        // MainLiangyouFragment();
        // MainShipinFragment mainShipinFragment = new MainShipinFragment();
        // MainTejiaFragment mainTejiaFragment = new MainTejiaFragment();
        fragments2 = new MainFirstFragment[]{
                MainFirstFragment.newInstance(0),
                MainFirstFragment.newInstance(1),
                MainFirstFragment.newInstance(2),
                MainFirstFragment.newInstance(3)
        };
        fragments = new Fragment[]{mainAdsFragment,
                fragments2[0], fragments2[1], fragments2[2], fragments2[3]};
        FragmentManager fragmentManager = getFragmentManager();
        for (int i = 0; i < 5; i++)
        {
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            // fragmentTransaction.replace(ids[i], fragments[i]).commit();
            fragmentTransaction.replace(ids[i], fragments[i]).commit();
        }
        return view;
    }

//    public void reloadData()
//    {
//        mainAdsFragment.reloadData();
//        for (int i = 0; i < 4; i++)
//        {
//            fragments2[i].reloadData();
//        }
//    }
}
