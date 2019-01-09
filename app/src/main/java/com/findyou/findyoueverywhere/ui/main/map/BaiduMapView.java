package com.findyou.findyoueverywhere.ui.main.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.bean.ChildInfoBean;
import com.findyou.findyoueverywhere.utils.AssertUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaiduMapView extends LinearLayout {
    @BindView(R.id.mapView)
    MapView mapView;
    ////////////////////////////////////////////////////////////////////////////////
    //防止每次定位都重新设置中心点和marker
    private boolean isFirstLocation = true;
    //初始化LocationClient定位类
    private LocationClient mLocationClient = null;
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口，原有BDLocationListener接口
    private BDLocationListener myListener = new MyLocationListener();
    //经纬度
    private double lat;
    private double lon;

    private BaiduMap baiduMap;
    private List<ChildInfoBean> data;
    private LocationManager locationManager;
    private String provider;
    private LatLng myPosition;
    private String addressName;

    public BaiduMapView(Context context) {
        super(context);
        init();
    }

    public BaiduMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        View view = inflate(getContext(), R.layout.view_baidu_map, null);
        ButterKnife.bind(this, view);
        initMap();
    }

    /**
     * 初始化地图
     */
    public void initMap(){
        //得到地图实例
        baiduMap = mapView.getMap();
        /*
        设置地图类型
         */
        //普通地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //卫星地图
        //baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //空白地图, 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
        //baiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
        //开启交通图
        baiduMap.setTrafficEnabled(true);
        //关闭缩放按钮
        mapView.showZoomControls(false);
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        //Marker
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle = marker.getExtraInfo();
                if(bundle != null){
                    int id = bundle.getInt("id");
                    if(id == -1){
                        //我的
                        //ChildInfoPopWindow popWindow = new ChildInfoPopWindow(getActivity());
                        //popWindow.showFromBottom();
                    }else{
                        //监护人的
                        switch (id){
                            case 0:
                        }
                    }
                }
                return false;
            }
        });
        //声明LocationClient类
        mLocationClient = new LocationClient(app.getContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        //开始定位
        mLocationClient.start();
    }

    /**
     * 配置定位参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        int span = 5000;
        option.setScanSpan(span);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取定位结果
            location.getTime();    //获取定位时间
            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
            location.getLocType();    //获取定位类型
            location.getLatitude();    //获取纬度信息
            location.getLongitude();    //获取经度信息
            location.getRadius();    //获取定位精准度
            location.getAddrStr();    //获取地址信息
            location.getCountry();    //获取国家信息
            location.getCountryCode();    //获取国家码
            location.getCity();    //获取城市信息
            location.getCityCode();    //获取城市码
            location.getDistrict();    //获取区县信息
            location.getStreet();    //获取街道信息
            location.getStreetNumber();    //获取街道码
            location.getLocationDescribe();    //获取当前位置描述信息
            location.getPoiList();    //获取当前位置周边POI信息

            location.getBuildingID();    //室内精准定位下，获取楼宇ID
            location.getBuildingName();    //室内精准定位下，获取楼宇名称
            location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息
            //经纬度
            lat = location.getLatitude();
            lon = location.getLongitude();

            //这个判断是为了防止每次定位都重新设置中心点和marker
            //if (isFirstLocation) {
            isFirstLocation = false;
            //设置并显示中心点
            setPosition2Center(baiduMap, location, true);
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            myPosition = point;
            addressName = getAddressName(location.getLatitude(), location.getLongitude());
            setMyLocMarker(point);
        }
    }

    public void setMyLocMarker(LatLng point){
        //构建Marker图标,显示自定义图标
        //定义Maker坐标点
        //LatLng point = ll; // new LatLng(39.963175, 116.400244);
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.ic_location_me);
        Bitmap src = BitmapFactory.decodeResource(app.getContext().getResources(), R.drawable.ic_location_me);
        int width = UIUtils.dp2px(getContext(), 36);
        int height = (width * src.getWidth() ) / src.getHeight();
        float zoom = (float) width / (float) src.getWidth();
        Bitmap des = AssertUtils.getScaleBitemap(src, zoom, zoom);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromBitmap(des);
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromView(iv_image);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        Bundle bundle = new Bundle();
        bundle.putInt("id", -1);
        baiduMap.addOverlay(option).setExtraInfo(bundle);
    }

    /**
     * 设置中心点和添加marker
     *
     * @param map
     * @param bdLocation
     * @param isShowLoc
     */
    public void setPosition2Center(BaiduMap map, BDLocation bdLocation, Boolean isShowLoc) {
        MyLocationData locData = new MyLocationData.Builder()
//                .accuracy(bdLocation.getRadius()) //圈
//                .direction(bdLocation.getRadius()).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        map.setMyLocationData(locData);
        if (isShowLoc) {
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            float zoomlevel = baiduMap.getMaxZoomLevel();
            builder.target(ll).zoom(zoomlevel - 3);
            map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    public static String getAddressName(double latitude, double longitude){
        Address address = getAddress(latitude, longitude);
        return address == null ? "unknown" : address.getAddressLine(0);
    }

    public static Address getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(app.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
