package com.findyou.findyoueverywhere.ui.main.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiChild;
import com.findyou.findyoueverywhere.api.http.ApiDeviceSetting;
import com.findyou.findyoueverywhere.api.http.ApiUser;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.CommonCallback;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.bean.ChildInfoBean;
import com.findyou.findyoueverywhere.bean.DeviceSettingInfoBean;
import com.findyou.findyoueverywhere.bean.common.UserInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.ui.main.SelectTimeFragment;
import com.findyou.findyoueverywhere.ui.main.map.popwindow.BindDeviceTipsPopWindow;
import com.findyou.findyoueverywhere.ui.main.map.popwindow.ChildInfoPopWindow;
import com.findyou.findyoueverywhere.ui.main.map.popwindow.ChildItemsPopWindow;
import com.findyou.findyoueverywhere.ui.main.map.popwindow.SelectDeviceModePopWindow;
import com.findyou.findyoueverywhere.ui.main.map.view.ChildInfoView;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.AssertUtils;
import com.findyou.findyoueverywhere.utils.BitmapUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.ToolUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;
import com.findyou.findyoueverywhere.utils.http.QueryString;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.findyou.findyoueverywhere.utils.log.log;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class ChildMapFragment extends BaseFragment {
//    @BindView(R.id.system_status_bar)
//    LinearLayout system_status_bar;

    @BindView(R.id.mmap)
    MapView mapView;

    @BindView(R.id.view_child)
    ChildInfoView view_child;

    private BaiduMap baiduMap;
    private List<ChildInfoBean> data;
    private int mIndex = 0;
    private LocationManager locationManager;
    private String provider;
    private LatLng myPosition;
    public static String addressName;
    ImageView imageView;
    private DeviceSettingInfoBean deviceInfoBean;

    public int getLayoutId() {
        registerEvent();
        return R.layout.child_map_fragment;
    }

    public void initSaveBar(){
        imageView = (ImageView)getRightView(ViewType.ImageView);
        if(imageView != null) {
            imageView.setImageResource(R.drawable.ic_time_white);
            imageView.setOnClickListener((View view) -> {
                ActivityManagerUtils.startActivity(getContext(), SelectTimeFragment.class, null);
            });
        }
    }

    private ChildInfoBean childInfoBean;
    //private LatLng childLoc; //当前位置
    private LatLng childLoc; //上一次的位置, 监护人的当前位置
    private LatLng lastMyLoc; //上一次的位置

    public void initView(){
        Bundle bundle = getArguments();
        if(bundle != null) {
            String childs = bundle.getString("childs");
            this.data = JsonUtils.convert_array(childs, ChildInfoBean.class);
            this.mIndex = bundle.getInt("index");
        }
        childInfoBean = this.data.get(this.mIndex); //JsonUtils.convert(data, ChildInfoBean.class);
        if(childInfoBean != null){
            setTitle(childInfoBean.name);
            view_child.setChildInfo(childInfoBean);
            childLoc = new LatLng(childInfoBean.latitude, childInfoBean.longitude);
        }
        initSaveBar();
        if(imageView != null) {
            imageView.setVisibility(View.GONE); //追踪模式下,不显示这个图标。
        }
        addressName = "";

        initMap();
//        //请求数据
//        loadData();
        //获取与监护人对应的设备的信息
        ApiDeviceSetting.getItem(childInfoBean.id, (res)->{
            deviceInfoBean = JsonUtils.convert(res.data, DeviceSettingInfoBean.class);
        });
    }

    public void setLocation(double latitude, double longitude){
        LatLng point = new LatLng(latitude, longitude);
        //toLoc(point); //得到地址信息
        BDLocation location = new BDLocation();
        location.setLatitude(point.latitude);
        location.setLongitude(point.longitude);
        setPosition2Center(baiduMap, location, true);
        setLocMarker(point, childInfoBean);
    }

    public void toLoc(LatLng point){
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_position);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        addressName = getAddressName(point.latitude, point.longitude);
        BDLocation location = new BDLocation();
        location.setLatitude(point.latitude);
        location.setLongitude(point.longitude);
        setMyLocMarker(point);
        GeoCoder geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener(){
            /*
             * (non-Javadoc)
             *
             * @see com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener#
             * onGetGeoCodeResult(com.baidu.mapapi.search.geocode.GeoCodeResult)
             */
            @Override
            public void onGetGeoCodeResult(GeoCodeResult arg0) {
                // TODO Auto-generated method stub
            }
            /*
             * (non-Javadoc)
             *
             * @see com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener#
             * onGetReverseGeoCodeResult(com.baidu.mapapi.search.geocode.
             * ReverseGeoCodeResult)
             */
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                addressName = result.getAddress();//解析到的地址
                postEvent(new EventMessenger(EventConst.API_GET_BAIDU_MAP_ADDRESS, addressName));
            }
        });
        // 反向地理解析
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(point));
    }

    public void loadData(){
        //请求被监护人列表
        data = new ArrayList<>();
//        ApiChild.getItems(0, (res)->{
//            List<ChildInfoBean> list1 = JsonUtils.convert_array(res.data, ChildInfoBean.class);
//            if(list1 == null){
//                return;
//            }
//            data.clear();
//            data.addAll(list1);
//            boolean isBindDevice = false;
//            for(int i=0; i<data.size(); i++){
//                ChildInfoBean info = data.get(i);
//                if(!TextUtils.isEmpty(info.imei)){
//                    isBindDevice = true;
//                    break;
//                }
//            }
//            if(!isBindDevice){
//                //弹出popwindow提示用户未绑定设备
//                BindDeviceTipsPopWindow popWindow = new BindDeviceTipsPopWindow(getActivity());
//                popWindow.showFromCenter();
//            }
//            if(data.size() > 0){
//                ChildInfoBean childInfoBean = data.get(0);
//
//            }
//        });
    }

    private boolean isFrist = true;

    Marker markerMe;
    private LatLng myLocation;
    public void setMyLocMarker(LatLng point){
        myLocation = point;
        Bitmap src = BitmapFactory.decodeResource(app.getContext().getResources(), R.drawable.ic_location_me);
        int width = UIUtils.dp2px(getContext(), 36);
        int height = (width * src.getWidth() ) / src.getHeight();
        float zoom = (float) width / (float) src.getWidth();
        Bitmap des = AssertUtils.getScaleBitemap(src, zoom, zoom);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromBitmap(des);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        Bundle bundle = new Bundle();
        bundle.putInt("id", -1);
        markerMe = (Marker) baiduMap.addOverlay(option);
        markerMe.setExtraInfo(bundle);
        if(selectMode != 1){
            markerMe.setVisible(true);
        }else {
            markerMe.setVisible(true);
        }
    }

    public void setLocMarker(LatLng point, String headimage1){
        String headimage = headimage1;
        //得到bitmap
        //构建Marker图标,显示自定义图标
        Bitmap src = BitmapFactory.decodeResource(app.getContext().getResources(), R.drawable.ic_marker_bk);
        int width = UIUtils.dp2px(getContext(), 36);
        int height = (width * src.getWidth() ) / src.getHeight();
        float zoom = (float) width / (float) src.getWidth();
        Bitmap des = AssertUtils.getScaleBitemap(src, zoom, zoom);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromBitmap(des);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        Bundle bundle = new Bundle();
//        bundle.putInt("id", item.id);
//        bundle.putString("data", JsonUtils.toString(item));
        baiduMap.addOverlay(option).setExtraInfo(bundle);
    }

    public void setLocMarker(LatLng point, ChildInfoBean item){
        new Thread(()->{
            Bitmap headbitmap = BitmapUtils.getBitmap(item.headimage);
            View view = this.getLayoutInflater().inflate(R.layout.child_info_track_distance_marker, null, false);
            LinearLayout lin_text = view.findViewById(R.id.lin_text);
            lin_text.setVisibility(View.GONE);
            TextView tv_txt = view.findViewById(R.id.tv_text);
            tv_txt.setText(item.name);
            ImageView iv_image = view.findViewById(R.id.iv_image);
            ImageView iv_headimage = view.findViewById(R.id.iv_headimage);
            Bitmap src = BitmapFactory.decodeResource(app.getContext().getResources(), R.drawable.ic_marker_bk);
            int width = UIUtils.dp2px(getContext(), 42);
            float zoom = (float) width / (float) src.getWidth();
            Bitmap des = AssertUtils.getScaleBitemap(src, zoom, zoom);
            iv_image.setImageBitmap(des);
            iv_headimage.setImageBitmap(headbitmap);

            Bitmap bitmap1 = viewToBitmap(getContext(), view);
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromBitmap(bitmap1);

            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            Bundle bundle = new Bundle();
            //bundle.putInt("maker_index", position);
            baiduMap.addOverlay(option).setExtraInfo(bundle);
        }).start();

        //构建Marker图标,显示自定义图标
//        Bitmap src = BitmapFactory.decodeResource(app.getContext().getResources(), R.drawable.ic_location_me);
//        int width = UIUtils.dp2px(getContext(), 36);
//        int height = (width * src.getWidth() ) / src.getHeight();
//        float zoom = (float) width / (float) src.getWidth();
//        Bitmap des = AssertUtils.getScaleBitemap(src, zoom, zoom);
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromBitmap(des);
//        OverlayOptions option = new MarkerOptions()
//                .position(point)
//                .icon(bitmap);
//        //在地图上添加Marker，并显示
//        Bundle bundle = new Bundle();
////        bundle.putInt("id", item.id);
////        bundle.putString("data", JsonUtils.toString(item));
//        bundle.putInt("marker_index", position);
//        baiduMap.addOverlay(option).setExtraInfo(bundle);
    }

    @Override
    public void onDestroy() {
        // 释放资源
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 退出时销毁定位
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient.stop();
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        if(mapView != null) {
            mapView.onDestroy();
            mapView = null;
        }
        if(mTrackHandler!=null) {
            mTrackHandler.removeCallbacks(r);
        }
        super.onDestroy();
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

    public static String getAddressName(double latitude, double longitude){
        Address address = getAddress(latitude, longitude);
        return address == null ? "unknown" : address.getAddressLine(0);
    }

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
                    int index = bundle.getInt("timer",0);
                    if(index > 0){
                        onStart4(index);
                    }
                }
                return false;
            }
        });
        baiduMap.clear();
        setMyLocation();
        setLocation(childLoc.latitude, childLoc.longitude);
    }

    public void setMyLocation(){
        mLocationClient = new LocationClient(app.getContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        //开始定位
        mLocationClient.start();
    }

    public void onVisibleChanged(boolean isVisible){
        if(isVisible){
            //设置并显示中心点
            //setPosition2Center(baiduMap, myPosition, true);
            //loadData();
        }
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

    /**
     * 实现定位监听 位置一旦有所改变就会调用这个方法
     * 可以在这个方法里面获取到定位之后获取到的一系列数据
     */
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
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            myPosition = point;
            //if (isFirstLocation) {
            isFirstLocation = false;
            //设置并显示中心点
            //setPosition2Center(baiduMap, location, true);
            addressName = getAddressName(location.getLatitude(), location.getLongitude());
            setMyLocMarker(point);
            //}
        }
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
            builder.target(ll).zoom(zoomlevel - 5);
            map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

//    baiduMap.setOnMapClickListener(new OnMapClickListener() {
//        @Override
//        public boolean onMapPoiClick(MapPoi arg0) {
//            return false;
//        }
//        @Override
//        public void onMapClick(LatLng arg0) {
//            rl_marker.setVisibility(View.GONE);
//        }
//    });

    //private Marker trackTextMarker;
    //追踪
    private void setLoc1Marker(LatLng point, String distance, String time){
        //距您300米 2分钟
        String str = String.format("距您%s,%s", distance, time);
        new Thread(()->{
            Bitmap headbitmap = BitmapUtils.getBitmap(childInfoBean.headimage);
            View view = this.getLayoutInflater().inflate(R.layout.child_info_track_distance_marker, null, false);
            LinearLayout lin_text = view.findViewById(R.id.lin_text);
            //lin_text.setVisibility(View.GONE);
            TextView tv_txt = view.findViewById(R.id.tv_text);
            tv_txt.setText(str);
            ImageView iv_image = view.findViewById(R.id.iv_image);
            ImageView iv_headimage = view.findViewById(R.id.iv_headimage);
            Bitmap src = BitmapFactory.decodeResource(app.getContext().getResources(), R.drawable.ic_marker_bk);
            int width = UIUtils.dp2px(getContext(), 42);
            float zoom = (float) width / (float) src.getWidth();
            Bitmap des = AssertUtils.getScaleBitemap(src, zoom, zoom);
            iv_image.setImageBitmap(des);
            iv_headimage.setImageBitmap(headbitmap);

            Bitmap bitmap1 = viewToBitmap(getContext(), view);
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromBitmap(bitmap1);

            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            Bundle bundle = new Bundle();
            //bundle.putInt("maker_index", position);
            baiduMap.addOverlay(option).setExtraInfo(bundle);
        }).start();
    }

    public Bitmap viewToBitmap(Context context, View view){
        Bitmap bitmap = null;
        try {
//            DanmakuItemView itemView = new DanmakuItemView(context);
//            itemView.setItemInfo(headimg, danmakuItemInfo); // 添加 弹幕
//            View view = itemView.getView();
            view.setDrawingCacheEnabled(true);
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            bitmap = view.getDrawingCache();
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //按钮逻辑
    @BindView(R.id.iv_items)
    ImageView iv_items;
    @OnClick(R.id.iv_items)
    public void iv_items_Click(View view){
        if(this.data.size() > 1) {
            ChildItemsPopWindow popWindow = new ChildItemsPopWindow(getActivity(), data);
            popWindow.showPopWindow(iv_items);
        }
    }

    @OnClick(R.id.iv_refresh)
    public void iv_refresh_Click(View view){
        //刷新
        //initMap();
        switch (selectMode){
            case 1: //追踪
                onStart1();
                break;
            case 2: //智能
                onStart2();
                break;
            case 3: //启停
                onStart3();
                break;
            case 4: //定时
                onStart4(0);
                break;
            case 5: //历史
                break;
        }
    }

    Thread threadTrack;
    @OnClick(R.id.iv_track)
    public void iv_track_Click(View view){
        BDLocation location = new BDLocation();
        location.setLatitude(childLoc.latitude);
        location.setLongitude(childLoc.longitude);
        setPosition2Center(baiduMap, location, true);
    }

    private int selectMode = 0;
    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_MAP_DISPLAY_MODE_TRACK:
                imageView.setVisibility(View.GONE);
                selectMode = 1;
                onStart1();
                break;
            case EventConst.API_MAP_DISPLAY_MODE_OTHER:
                imageView.setVisibility(View.VISIBLE);
                String str = event.obj.toString();
                if(ToolUtils.isNumeric(str)){
                    mTrackHandler.removeCallbacks(r);
                    int index = Integer.parseInt(str);
                    selectMode = index;
                    switch (index){
                        case 2: //智能
                            onStart2();
                            break;
                        case 3: //启停
                            onStart3();
                            break;
                        case 4: //定时
                            onStart4(0);
                            break;
                        case 5: //历史
                            break;
                    }
                }
                break;
            case EventConst.API_MAP_SELECT_CHILD:
                String str1 = event.obj.toString();
                if(ToolUtils.isNumeric(str1)) {
                    this.mIndex = Integer.parseInt(str1);
                    setArguments(null);
                    initView();
                }
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //追踪模式
    //childLoc为child当前的位置
    public void onTrack(LatLng curLoc){

        if(markerMe != null){
            markerMe.setVisible(true);
        }
        //计算我的速度
        double mydis = DistanceUtil.getDistance(lastMyLoc, myLocation); //计算自己这段时间内走过的距离
        int speed = (int) (mydis / refreshTimeInteval); //计算速度,每秒多少米
        lastMyLoc = myLocation; //保存上一点

        //计算两点位置
        //LatLng childLocation = new LatLng(childInfoBean.latitude, childInfoBean.longitude);
        int childDis = (int) DistanceUtil.getDistance(myLocation, curLoc); //监护人到我的距离
        int second = 0;
        if(speed != 0) {
            second = (int) (childDis / speed); //计算我到监护人那里多少秒
        }
        //时间
        String time = "";
        if(second < 60){
            time = second + "秒";
        }else if(second > 60 && second < 3600){
            int m1 = second / 60;
            int m2 = second % 60;
            time = String.format("%d分钟%d秒", m1, m2);
        }else {
            int h1 = second / 3600;
            int h2 = second % 3600;
            int m1 = h2 / 60;
            int m2 = h2 % 60;
            time = String.format("%d小时%d分钟%d秒", h1, m1, m2);
        }
        //距离
        String dis = "";
        if(childDis < 1000){
            dis = childDis + "米";
        }else if(childDis > 1000){
            int d1 = (int)(childDis / 1000);
            int d2 = (int) (childDis % 1000);
            dis = String.format("%s公里,%s米",d1, d2);
        }

        setLoc1Marker(curLoc, dis, time);
        //规划线路
        MyRouteSearch routeSearch = new MyRouteSearch();
        routeSearch.search(baiduMap, myLocation, curLoc);
    }

    private int refreshTimeInteval = 10;
    public class ChildLocInfo{
        public double latitude;
        public double longitude;
    }

    Handler mTrackHandler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            //do something
            //刷新我的位置
            setMyLocation();

            //得到最新的位置
            getChildLoc(childInfoBean.id, (res)->{
                ChildLocInfo info = new ChildLocInfo();
                info.latitude = childLoc.latitude;
                info.longitude = childLoc.longitude;
                LatLng curLoc = new LatLng(info.latitude, info.longitude);
                ChildMapFragment.this.childLoc = curLoc;
                onTrack(curLoc);
            });

            //每隔1s循环执行run方法
            if(deviceInfoBean != null){
                if(deviceInfoBean.track != 0){
                    refreshTimeInteval = deviceInfoBean.track;
                }
            }

            mTrackHandler.postDelayed(this, refreshTimeInteval * 1000);
        }
    };

    boolean isExitThread = false;
    private void onStart1() {
        baiduMap.clear();
        try{
            mTrackHandler.removeCallbacks(r);
            mTrackHandler.postDelayed(r, 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getChildLoc(int id, CommonCallback callback){
        if(callback != null){
            callback.apply(null);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //显示智能上报点
    public List<LatLng> points;
    public void onStart2(){
//        if(threadTrack != null){
//            threadTrack.interrupt();
//        }
        baiduMap.clear();
        points = new ArrayList<>();
        LatLng p1 = new LatLng(32.272661, 111.66223);
        LatLng p2 = new LatLng(32.277347, 111.66595);
        LatLng p3 = new LatLng(32.285123, 111.65954);
        LatLng p4 = new LatLng(32.287665, 111.67092);
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        //获取智能上报点
        getChildLoc2(childInfoBean.id, (res)->{
            drawMyRoute(points);
            //drawMyRoute2(points);
            for(int i=0; i<points.size(); i++){
                LatLng point = points.get(i);
                boolean isShowHead = false;
                if(i == points.size() - 1){
                    isShowHead = true;
                }
                setLoc2Marker(point, "", i + 1, isShowHead);
            }
            if(points.size() > 0) {
                ChildMapFragment.this.childLoc = points.get(points.size() - 1);
            }
        });
    }

    public void getChildLoc2(int id, CommonCallback callback){
        if(callback != null){
            callback.apply(null);
        }
    }

    public void setLoc2Marker(LatLng point, String headimage, int position, boolean isShowHead){
        //距您300米 2分钟
        View view = this.getLayoutInflater().inflate(R.layout.child_location_info_2, null, false);
        TextView tv_num = view.findViewById(R.id.tv_num);
        tv_num.setText(position + "");
        ImageView iv_headimage = view.findViewById(R.id.iv_headimage);

        iv_headimage.setVisibility(View.GONE);
        if(isShowHead) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap headbitmap = BitmapUtils.getBitmap("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543407653332&di=100b3665b3c8bebd303fd23d42500547&imgtype=0&src=http%3A%2F%2F8.pic.pc6.com%2Fup%2F2017-2%2F2017214946580971970.png");
                    iv_headimage.setImageBitmap(headbitmap);
                    iv_headimage.setVisibility(View.VISIBLE);
                    displayMarkerByView(point, view, true);
                }
            }).start();
        }else {
            displayMarkerByView(point, view, true);
        }
//        if(trackTextMarker != null){
//            trackTextMarker.remove();
//        }
//        trackTextMarker = (Marker) baiduMap.addOverlay(option);
//        trackTextMarker.setExtraInfo(bundle);
    }

    public void displayMarkerByView(LatLng point, View view){
        Bitmap bitmap1 = viewToBitmap(getContext(), view);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromBitmap(bitmap1);

        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);

        //在地图上添加Marker，并显示
        Bundle bundle = new Bundle();
        bundle.putInt("id", 1000);
        baiduMap.addOverlay(option).setExtraInfo(bundle);
    }

    public void displayMarkerByView(LatLng point, View view, boolean isanchor){
        Bitmap bitmap1 = viewToBitmap(getContext(), view);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromBitmap(bitmap1);

        OverlayOptions option = new MarkerOptions()
                .position(point)
                .anchor(0.5f,0.5f)
                .icon(bitmap);

        //在地图上添加Marker，并显示
        Bundle bundle = new Bundle();
        bundle.putInt("id", 1000);
        baiduMap.addOverlay(option).setExtraInfo(bundle);
    }

    /**
     * 根据数据绘制轨迹
     *
     * @param points2
     */
    protected void drawMyRoute(List<LatLng> points2) {
        OverlayOptions options = new PolylineOptions().color(0xAAFF0000)
                .width(10).points(points2);
        baiduMap.addOverlay(options);
    }

    //架车
    protected void drawMyRoute2(List<LatLng> points2) {
        for(int i=0; i<points2.size() - 1; i++){
            LatLng start = points2.get(i);
            LatLng end = points2.get(i+1);
            MyRouteSearch route = new MyRouteSearch();
            route.searchWalk(baiduMap, start, end);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void onStart3(){
//        if(threadTrack != null){
//            threadTrack.interrupt();
//        }
        baiduMap.clear();
        points = new ArrayList<>();
        LatLng p1 = new LatLng(32.272661, 111.66223);
        LatLng p2 = new LatLng(32.277347, 111.66595);
        LatLng p3 = new LatLng(32.285123, 111.65954);
        LatLng p4 = new LatLng(32.287665, 111.67092);
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);

        //获取智能上报点
        getChildLoc3(childInfoBean.id, (res)->{
            for(int i=0; i<points.size(); i++){
                LatLng point = points.get(i);
                boolean isShowHead = false;
                if(i == points.size() - 1){
                    isShowHead = true;
                }
                setLoc3Marker(point, "", i + 1, isShowHead);
            }
            if(points.size() > 0) {
                ChildMapFragment.this.childLoc = points.get(points.size() - 1);
            }
        });
    }

    public void getChildLoc3(int id, CommonCallback callback){
        if(callback != null){
            callback.apply(null);
        }
    }

    public void setLoc3Marker(LatLng point, String headimage, int position, boolean isShowHead){
        View view = this.getLayoutInflater().inflate(R.layout.child_location_info_3, null, false);
        TextView tv_num = view.findViewById(R.id.tv_num);
        tv_num.setText(position + "");
        ImageView iv_headimage = view.findViewById(R.id.iv_headimage);
        new Thread(()->{
            Bitmap headbitmap = BitmapUtils.getBitmap("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543407653332&di=100b3665b3c8bebd303fd23d42500547&imgtype=0&src=http%3A%2F%2F8.pic.pc6.com%2Fup%2F2017-2%2F2017214946580971970.png");
            iv_headimage.setImageBitmap(headbitmap);
            iv_headimage.setVisibility(View.VISIBLE);
            displayMarkerByView(point, view);
        }).start();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void onStart4(int index){
        points = new ArrayList<>();
        LatLng p1 = new LatLng(32.272661, 111.66223);
        LatLng p2 = new LatLng(32.277347, 111.66595);
        LatLng p3 = new LatLng(32.285123, 111.65954);
        LatLng p4 = new LatLng(32.287665, 111.67092);
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        //获取智能上报点
        getChildLoc4(childInfoBean.id, (res)->{
            baiduMap.clear();
            for(int i=0; i<points.size(); i++){
                LatLng point = points.get(i);
                boolean isShowHead = false;
                if(index == i+1){
                    isShowHead = true;
                }
                setLoc4Marker(point, "", i + 1, isShowHead);
            }
            if(points.size() > 0) {
                ChildMapFragment.this.childLoc = points.get(points.size() - 1);
            }
        });
        if(index > 0){
            //得到地址
            if(points.size() > 0){
                LatLng point = points.get(index - 1);
                getAddress(point);
            }
        }
    }

    public void getChildLoc4(int id, CommonCallback callback){
        if(callback != null){
            callback.apply(null);
        }
    }

    public void setLoc4Marker(LatLng point, String headimage, int position, boolean isShowHead){
        //距您300米 2分钟
        View view = this.getLayoutInflater().inflate(R.layout.child_location_info_4, null, false);
        ImageView iv_headimage = view.findViewById(R.id.iv_headimage);
        ImageView iv_headimage_big = view.findViewById(R.id.iv_headimage_big);
        iv_headimage.setVisibility(View.GONE);
        iv_headimage_big.setVisibility(View.GONE);
        new Thread(()->{
            Bitmap headbitmap = BitmapUtils.getBitmap("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543407653332&di=100b3665b3c8bebd303fd23d42500547&imgtype=0&src=http%3A%2F%2F8.pic.pc6.com%2Fup%2F2017-2%2F2017214946580971970.png");
            if(isShowHead){
                iv_headimage_big.setImageBitmap(headbitmap);
                iv_headimage_big.setVisibility(View.VISIBLE);
            }else {
                iv_headimage.setImageBitmap(headbitmap);
                iv_headimage.setVisibility(View.VISIBLE);
            }
            displayMarkerByView4(point, view, position);
        }).start();
    }

    public void displayMarkerByView4(LatLng point, View view, int position){
        Bitmap bitmap1 = viewToBitmap(getContext(), view);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromBitmap(bitmap1);

        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);

        //在地图上添加Marker，并显示
        Bundle bundle = new Bundle();
        bundle.putInt("timer", position);
        baiduMap.addOverlay(option).setExtraInfo(bundle);
    }

    public void getAddress(LatLng point){
        String address = "";
        GeoCoder geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener(){
            /*
             * (non-Javadoc)
             *
             * @see com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener#
             * onGetGeoCodeResult(com.baidu.mapapi.search.geocode.GeoCodeResult)
             */
            @Override
            public void onGetGeoCodeResult(GeoCodeResult arg0) {
                // TODO Auto-generated method stub
            }
            /*
             * (non-Javadoc)
             *
             * @see com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener#
             * onGetReverseGeoCodeResult(com.baidu.mapapi.search.geocode.
             * ReverseGeoCodeResult)
             */
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                addressName = result.getAddress();//解析到的地址
                postEvent(new EventMessenger(EventConst.API_GET_BAIDU_MAP_ADDRESS, addressName));
            }
        });
        // 反向地理解析
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(point));
    }
}
