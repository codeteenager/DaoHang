package com.shuaijie.jiang.daohang.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuaijie.jiang.daohang.R;

/**
 * 作者:姜帅杰
 * 版本:1.0
 * 创建日期:2016/9/3:23:19.
 */
public class MenuAdapter extends BaseAdapter {
    private String[] menu = {"室内地图", "全景图", "离线包下载", "设置"};
    private int[] resId = {R.mipmap.ic_home_black, R.mipmap.ic_place_black, R.mipmap.ic_download_black, R.mipmap.ic_settings};
    private Context context;

    public MenuAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return menu.length;
    }

    @Override
    public Object getItem(int i) {
        return menu[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View menuItem = View.inflate(context, R.layout.menu_item, null);
        TextView tv_menu = (TextView) menuItem.findViewById(R.id.tv_menu);
        ImageView iv_menu = (ImageView) menuItem.findViewById(R.id.iv_menu);
        iv_menu.setImageResource(resId[i]);
        tv_menu.setText(menu[i]);
        return menuItem;
    }
}
