package com.shuaijie.jiang.daohang.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuaijie.jiang.daohang.R;

import java.util.List;
import java.util.Map;

/**
 * 作者:姜帅杰
 * 版本:1.0
 * 创建日期:2016/9/5:10:44.
 */
public class CityAdapter extends BaseAdapter {
    private List<Map> cityData;
    private Context context;

    public CityAdapter(Context context, List<Map> cityData) {
        this.cityData = cityData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cityData.size();
    }

    @Override
    public Object getItem(int i) {
        return cityData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.city_list_item, null);
            viewHolder.cityId = (TextView) view.findViewById(R.id.city_list_id);
            viewHolder.cityName = (TextView) view.findViewById(R.id.city_list_name);
            viewHolder.download = (ImageView) view.findViewById(R.id.city_list_download);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();
        final TextView cityId = viewHolder.cityId;
        cityId.setText((Integer) cityData.get(i).get("cityId") + "");
        viewHolder.cityName.setText((String) cityData.get(i).get("city"));
        viewHolder.download.setImageResource(R.mipmap.ic_download_black);
        return view;
    }

    public static class ViewHolder {
        TextView cityId;
        TextView cityName;
        ImageView download;
    }
}
