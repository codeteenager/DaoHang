package com.shuaijie.jiang.daohang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.shuaijie.jiang.daohang.R;
import com.shuaijie.jiang.daohang.adapter.CityAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者:姜帅杰
 * 版本:1.0
 * 创建日期:2016/9/12:23:49.
 */
public class CityListFragment extends Fragment implements MKOfflineMapListener {
    private List<Map> cityData;
    private CityAdapter cityAdapter;
    private MKOfflineMap mOffline = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOffline = new MKOfflineMap();
        mOffline.init(this);
        cityData = new ArrayList<>();
        initCityList();
        cityAdapter = new CityAdapter(getActivity(), cityData);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_list, container, false);
        ListView allCityList = (ListView) view.findViewById(R.id.allcitylist);
        allCityList.setAdapter(cityAdapter);
        allCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView cityId = (TextView) view.findViewById(R.id.city_list_id);
                TextView cityName = (TextView) view.findViewById(R.id.city_list_name);
                start(cityId.getText().toString(), cityName.getText().toString());
            }
        });
        return view;
    }

    /**
     * 开始下载
     *
     * @param cityId
     * @param cityName
     */
    public void start(String cityId, String cityName) {
        int cityid = Integer.parseInt(cityId);
        mOffline.start(cityid);
        initCityList();
        cityAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "开始下载离线地图: " + cityName, Toast.LENGTH_SHORT)
                .show();
    }

    private void initCityList() {
        cityData.clear();
        // 获取所有支持离线地图的城市
        ArrayList<MKOLSearchRecord> records2 = mOffline.getOfflineCityList();
        if (records2 != null) {
            for (MKOLSearchRecord r : records2) {
                if (r.cityType == 2) {
                    Map<String, Object> city1 = new HashMap<>();
                    city1.put("city", r.cityName);
                    city1.put("cityId", r.cityID);
                    cityData.add(city1);
                } else if (r.cityType == 1) {
                    ArrayList<MKOLSearchRecord> city = r.childCities;
                    for (MKOLSearchRecord r1 : city) {
                        if (r1.cityType == 2) {
                            Map<String, Object> city2 = new HashMap<>();
                            city2.put("city", r1.cityName);
                            city2.put("cityId", r1.cityID);
                            cityData.add(city2);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 搜索离线需市
     *
     * @param city
     */
    public void search(String city) {
        ArrayList<MKOLSearchRecord> records = mOffline.searchCity(city);
        if (records == null || records.size() != 1) {
            return;
        }
        cityData.clear();
        Map<String, Object> searchCity = new HashMap<>();
        searchCity.put("cityId", records.get(0).cityID);
        searchCity.put("city", records.get(0).cityName);
        cityData.add(searchCity);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {

                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);
                break;
            default:
                break;
        }
    }
}
