package com.shuaijie.jiang.daohang.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.shuaijie.jiang.daohang.R;
import com.shuaijie.jiang.daohang.adapter.OfflineFragmentPagerAdapter;
import com.shuaijie.jiang.daohang.fragment.CityListFragment;
import com.shuaijie.jiang.daohang.fragment.LocalCityFragment;

import java.util.ArrayList;

public class OfflineActivity extends BaseActivity {
    private TabLayout tabLayout;
    public ViewPager vp;
    private CityListFragment cityListFragment;
    private LocalCityFragment localCityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        actionBar.setTitle("离线包下载");
        initView();
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tl_offline);
        vp = (ViewPager) findViewById(R.id.vp_offline);
        String[] titles = {"城市列表", "下载管理"};
        ArrayList<Fragment> fragments = new ArrayList<>();
        cityListFragment = new CityListFragment();
        localCityFragment = new LocalCityFragment();
        fragments.add(cityListFragment);
        fragments.add(localCityFragment);
        vp.setAdapter(new OfflineFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(vp);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 创建选项菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchNote);
        if (searchItem != null) {
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    vp.setCurrentItem(0);
                    cityListFragment.search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    cityListFragment.search(newText);
                    return true;
                }
            });
        }

        return true;
    }
}
