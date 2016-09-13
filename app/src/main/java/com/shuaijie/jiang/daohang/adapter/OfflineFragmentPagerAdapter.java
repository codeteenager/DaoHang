package com.shuaijie.jiang.daohang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 作者:姜帅杰
 * 版本:1.0
 * 创建日期:2016/9/12:23:45.
 */
public class OfflineFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;
    private ArrayList<Fragment> fragments;

    public OfflineFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
