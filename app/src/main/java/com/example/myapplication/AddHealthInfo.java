package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.example.myapplication.adapter.BaseFragmentAdapter;
import com.example.myapplication.bean.HealthHistoryBean;
import com.example.myapplication.db.HistoryDao;
import com.example.myapplication.fragment.NewsFragment;
import com.example.myapplication.utils.DoubleClickHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddHealthInfo extends AppCompatActivity implements AMapLocationListener, LocationSource {

    //定位需要的声明,初始化的配置
    private AMapLocationClient mLocationClient = null;          //发起定位
    private AMapLocationClientOption mLocationOption = null;    //参数设置

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private TextView tvLocal;
    private EditText etName, etTem;
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    HealthHistoryBean item;
    private boolean setLocalText = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        XXPermissions.with(this).permission(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION).request(new OnPermission() {
            @Override
            public void hasPermission(List<String> granted, boolean all) {

            }

            @Override
            public void noPermission(List<String> denied, boolean never) {

            }
        });
        tvLocal = findViewById(R.id.tv_local);
        etName = findViewById(R.id.et_name);
        etTem = findViewById(R.id.et_tem);
        mapView = findViewById(R.id.map);
        item = (HealthHistoryBean) getIntent().getSerializableExtra("item");
        if (item != null) {
            tvLocal.setText(item.getLocation());
            etName.setText(item.getName());
            etTem.setText(item.getTem());
            setLocalText = false;
        } else {

        }
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        aMap.setLocationSource(this);
        uiSettings.setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        findViewById(R.id.iv_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocalText = true;
                initLocation();
            }
        });
        //开始定位
        initLocation();
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式
        //Hight_Accuracy为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    //定位回调函数
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();     //获取纬度
                amapLocation.getLongitude();    //获取经度
                amapLocation.getAccuracy();     //获取精度信息
                amapLocation.getCity();         //城市信息

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    aMap.moveCamera(CameraUpdateFactory.zoomBy(17));
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    mListener.onLocationChanged(amapLocation);
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    isFirstLoc = false;
                    if (setLocalText) {
                        tvLocal.setText(buffer.toString());
                    }
                }
            } else {
                tvLocal.setText("定位失败");

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_save://保存
                saveHistory();
                break;
            case R.id.action_new_giveup://放弃保存
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveHistory() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(etTem.getText().toString())) {
            Toast.makeText(this, "请输入体温", Toast.LENGTH_SHORT).show();
            return;
        }
        if (item == null) {
            HealthHistoryBean healthHistoryBean = new HealthHistoryBean();
            healthHistoryBean.setName(etName.getText().toString());
            healthHistoryBean.setTem(etTem.getText().toString());
            healthHistoryBean.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
            healthHistoryBean.setLocation(tvLocal.getText().toString());
            new HistoryDao(this).insertNote(healthHistoryBean);
        } else {

            item.setName(etName.getText().toString());
            item.setTem(etTem.getText().toString());
            item.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
            item.setLocation(tvLocal.getText().toString());
            new HistoryDao(this).updateNote(item);
        }
        finish();
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void deactivate() {
        mListener = null;
    }
}