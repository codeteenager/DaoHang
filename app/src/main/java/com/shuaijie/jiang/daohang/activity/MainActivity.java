package com.shuaijie.jiang.daohang.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.media.audiofx.BassBoost;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.shuaijie.jiang.daohang.R;
import com.shuaijie.jiang.daohang.adapter.MenuAdapter;
import com.shuaijie.jiang.daohang.bean.ChooseCityInterface;
import com.shuaijie.jiang.daohang.utils.ChooseCityUtil;
import com.shuaijie.jiang.daohang.utils.CommonUtils;
import com.shuaijie.jiang.daohang.utils.MyOrientationListener;
import com.shuaijie.jiang.daohang.utils.ScreenUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private MenuItem cityItem;
    private MapView mMapView = null;
    private FloatingActionButton fab;
    private double latitude;
    private double longitude;
    private BaiduMap mBaiduMap;
    private int currentTheme;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private Button btnNearby, btnRoute, btnNavi;
    private ListView lvMenu;
    private GeoCoder geoCoder;
    private boolean isFirstLocate = true;
    private MyOrientationListener listener;
    private float currentX;
    private String currentCity, currentProvince, currentDistrict, actionbarCity, actionbarProvince, actionbarDistrict;
    private updateNetworkReceiver updateNetworkReceiver;
    private TextView tv_tip_network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = CommonUtils.getSpInt(getApplicationContext(), "currentTheme", 0);
        currentTheme = type;
        setMapCustomFile(this, type);
        makeActionOverflowMenuShown();
        setContentView(R.layout.activity_main);
        initView();
        initBaiduMap();
    }

    private void initBaiduMap() {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                    Toast.makeText(getApplicationContext(), "没有搜索到结果", Toast.LENGTH_SHORT).show();
                    return;
                }
                LatLng latlng = result.getLocation();
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
                mBaiduMap.animateMapStatus(msu);
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

            }
        });
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));//设置地图的缩放级别
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        mMapView.showZoomControls(false);
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                int screenWidth = ScreenUtils.getScreenWidth(MainActivity.this);
                int screenHeight = ScreenUtils.getSreenHeight(MainActivity.this);
                mMapView.setZoomControlsPosition(new Point(screenWidth - 120, screenHeight / 2));
                if (CommonUtils.getSpBool(getApplicationContext(), "showZoom", true)) {
                    mMapView.showZoomControls(true);
                } else {
                    mMapView.showZoomControls(false);
                }
            }
        });
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.map_gps_locked);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescriptor);
        mBaiduMap.setMyLocationConfigeration(config);
        listener = new MyOrientationListener(this);
        listener.setListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChange(float x) {
                currentX = x;
            }
        });
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        tv_tip_network = (TextView) findViewById(R.id.tv_tip_network);
        lvMenu = (ListView) findViewById(R.id.lv_menu);
        lvMenu.setAdapter(new MenuAdapter(this));
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    startActivity(new Intent(MainActivity.this, SelectIndoorActivity.class));
                } else if (i == 1) {
                    startActivity(new Intent(MainActivity.this, PanoActivity.class));
                } else if (i == 2) {
                    startActivity(new Intent(MainActivity.this, OfflineActivity.class));
                } else if (i == 3) {
                    startActivity(new Intent(MainActivity.this, SettingActivity.class));
                }
            }
        });
        btnNearby = (Button) findViewById(R.id.btn_nearby);
        btnRoute = (Button) findViewById(R.id.btn_route);
        btnNavi = (Button) findViewById(R.id.btn_navi);
        btnNearby.setOnClickListener(this);
        btnRoute.setOnClickListener(this);
        btnNavi.setOnClickListener(this);
        tv_tip_network.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        fab = (FloatingActionButton) findViewById(R.id.fab_locate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latitude != 0.0 && longitude != 0.0) {
                    LatLng latlng = new LatLng(latitude, longitude);
                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
                    mBaiduMap.animateMapStatus(msu);
                }
                if (currentCity != null) {
                    cityItem.setTitle(currentCity + "市");
                    actionbarProvince = currentProvince;
                    actionbarCity = currentCity;
                    actionbarDistrict = currentDistrict;
                }
            }
        });
        updateNetworkReceiver = new updateNetworkReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.network");
        registerReceiver(updateNetworkReceiver, intentFilter);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.isNetworkConnected");
        sendBroadcast(intent);
    }

    // 设置个性化地图config文件路径
    private void setMapCustomFile(Context context, int type) {
        String fileName = null;
        switch (type) {
            //String[] data = {"普通模式", "黑夜模式", "清新蓝色", "午夜蓝色"};
            case 0:
                MapView.setMapCustomEnable(false);
                return;
            case 1:
                MapView.setMapCustomEnable(true);
                fileName = "custom_config_black.txt";
                break;
            case 2:
                MapView.setMapCustomEnable(true);
                fileName = "custom_config_refresh_blue.txt";
                break;
            case 3:
                MapView.setMapCustomEnable(true);
                fileName = "custom_config_midnight_blue.txt";
                break;
        }
        FileOutputStream out = null;
        InputStream inputStream = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets()
                    .open("customConfigdir/" + fileName);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            moduleName = context.getFilesDir().getAbsolutePath();
            File f = new File(moduleName + "/" + fileName);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            out = new FileOutputStream(f);
            out.write(b);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MapView.setCustomMapStylePath(moduleName + "/" + fileName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_nearby:
                startActivity(new Intent(MainActivity.this, PoiSearchActivity.class));
                break;
            case R.id.btn_route:
                startActivity(new Intent(MainActivity.this, RouteInfoActivity.class));
                break;
            case R.id.btn_navi:
                startActivity(new Intent(MainActivity.this, NaviActivity.class));
                break;
            case R.id.tv_tip_network:
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
                break;
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                Toast.makeText(MainActivity.this, "GPS定位成功", Toast.LENGTH_SHORT).show();
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                Toast.makeText(MainActivity.this, "网络定位成功", Toast.LENGTH_SHORT).show();
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                Toast.makeText(MainActivity.this, "离线定位成功", Toast.LENGTH_SHORT).show();
//            }
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//            MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
//                    // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(currentX).latitude(location.getLatitude())
//                    .longitude(location.getLongitude()).build();
//            mBaiduMap.setMyLocationData(locData);
//            currentCity = subCityProvince(location.getCity());
//            currentProvince = subCityProvince(location.getProvince());
//            currentDistrict = subCityProvince(location.getDistrict());
//            if (isFirstLocate) {
//                actionbarCity = subCityProvince(location.getCity());
//                actionbarProvince = subCityProvince(location.getProvince());
//                actionbarDistrict = subCityProvince(location.getDistrict());
//                LatLng latlng = new LatLng(latitude, longitude);
//                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
//                mBaiduMap.animateMapStatus(msu);
//                isFirstLocate = false;
//                cityItem.setTitle(location.getCity());
//                Toast.makeText(getApplicationContext(), "当前位置" + location.getAddrStr(), Toast.LENGTH_SHORT).show();
//            }
//            String currentCity = location.getCity();
//            CommonUtils.setSpStr(getApplicationContext(), "currentCity", currentCity);
//            CommonUtils.setSpStr(getApplicationContext(), "currentLatitude", latitude + "");
//            CommonUtils.setSpStr(getApplicationContext(), "currentLongitude", longitude + "");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MapView.setMapCustomEnable(false);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        geoCoder.destroy();
        unregisterReceiver(updateNetworkReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int type = CommonUtils.getSpInt(getApplicationContext(), "currentTheme", 0);
        if (type != currentTheme) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
            System.exit(0);
        }
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        if (CommonUtils.getSpBool(getApplicationContext(), "showZoom", true)) {
            mMapView.showZoomControls(true);
        } else {
            mMapView.showZoomControls(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        listener.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        listener.stop();
    }

    //处理actionbar点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.current_city:
                ChooseCityUtil cityUtil = new ChooseCityUtil();
                String[] oldCityArray = {actionbarProvince, actionbarCity, actionbarDistrict};
                cityUtil.createDialog(this, oldCityArray, new ChooseCityInterface() {
                    @Override
                    public void sure(String[] newCityArray) {
                        //oldCityArray为传入的默认值 newCityArray为返回的结果
                        //cityItem.setTitle(newCityArray[0] + "-" + newCityArray[1] + "-" + newCityArray[2]);
                        cityItem.setTitle(newCityArray[1] + "市");
                        geoCoder.geocode(new GeoCodeOption().city(newCityArray[1]).address(newCityArray[2]));
                        actionbarProvince = newCityArray[0];
                        actionbarCity = newCityArray[1];
                        actionbarDistrict = newCityArray[2];
                        Toast.makeText(getApplicationContext(), "切换到" + newCityArray[0] + "省" + newCityArray[1] + "市" + newCityArray[2] + "县", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.map_normal:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//设置普通地图
                mBaiduMap.setTrafficEnabled(false);
                MapView.setMapCustomEnable(true);
                break;
            case R.id.map_satellite:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);//设置卫星地图
                mBaiduMap.setTrafficEnabled(false);
                MapView.setMapCustomEnable(false);
                break;
            case R.id.map_traffic:
                mBaiduMap.setTrafficEnabled(true);
                break;
        }
        //优先返回抽屉按钮事件处理
        return drawerToggle.onOptionsItemSelected(item) | super.onOptionsItemSelected(item);
    }

    private String subCityProvince(String str) {
        return str.substring(0, str.length() - 1);
    }

    //创建选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        cityItem = menu.findItem(R.id.current_city);
        cityItem.setTitle(CommonUtils.getSpStr(getApplicationContext(), "currentCity", "北京市"));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.fl_menu))) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    private void makeActionOverflowMenuShown() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {

        }
    }

    public class updateNetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String isShow = intent.getStringExtra("msg");
            if (isShow.equals("innet")) {
                tv_tip_network.setVisibility(View.GONE);
            } else {
                tv_tip_network.setVisibility(View.VISIBLE);
            }
        }
    }
}

