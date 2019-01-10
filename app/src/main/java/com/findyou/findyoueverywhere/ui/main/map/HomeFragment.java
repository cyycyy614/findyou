package com.findyou.findyoueverywhere.ui.main.map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.model.LatLng;
import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiChild;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.bean.ChildInfoBean;
import com.findyou.findyoueverywhere.ui.main.map.popwindow.BindDeviceTipsPopWindow;
import com.findyou.findyoueverywhere.ui.main.map.popwindow.ChildInfoPopWindow;
import com.findyou.findyoueverywhere.ui.main.map.popwindow.ChildItemsPopWindow;
import com.findyou.findyoueverywhere.ui.main.map.popwindow.SelectDeviceModePopWindow;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.AssertUtils;
import com.findyou.findyoueverywhere.utils.BitmapUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.system_status_bar)
    LinearLayout system_status_bar;

    @BindView(R.id.mmap)
    MapView mapView;

    private BaiduMap baiduMap;
    private List<ChildInfoBean> data;
    private LocationManager locationManager;
    private String provider;
    private LatLng myPosition;
    public static String addressName;
    public List<LatLng> points;

    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    public void initView(){
        int statusHeight = UIUtils.getStatusHeight(getContext());
        system_status_bar.getLayoutParams().height = statusHeight;
        addressName = "";

        points = new ArrayList<>();
        LatLng p1 = new LatLng(32.272661, 111.66223);
        LatLng p2 = new LatLng(32.277347, 111.66595);
        LatLng p3 = new LatLng(32.285123, 111.65954);
        LatLng p4 = new LatLng(32.287665, 111.67092);
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);

//        //百度地图
//        baiduMap = mapView.getMap();
//        baiduMap.setMyLocationEnabled(true);
//        //普通地图
//        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//        //开启交通图
//        baiduMap.setTrafficEnabled(true);
//        //城市热力图
//        baiduMap.setBaiduHeatMapEnabled(true);
//        //
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        List<String> list = locationManager.getProviders(true);
//        if (list.contains(LocationManager.GPS_PROVIDER)) {
//            provider = LocationManager.GPS_PROVIDER;
//        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
//            provider = LocationManager.NETWORK_PROVIDER;
//        } else {
//            //Toast.makeText(this, "当前不能提供位置信息", Toast.LENGTH_LONG).show();
//            ToastUtils.showToast(getContext(),"当前不能提供位置信息");
//            return;
//        }
//
//        if(ContextCompat.checkSelfPermission(app.getContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
//            Location location = locationManager.getLastKnownLocation(provider);
//            if (location != null) {
//                navigateTo(location);
//            }
//            locationManager.requestLocationUpdates(provider, 5000, 1,
//                    locationListener);
//        }

        initMap();
//
//        //请求数据
//        loadData();
    }

    public void loadData(){
        //请求被监护人列表
        data = new ArrayList<>();
        ApiChild.getItems(0, (res)->{
            List<ChildInfoBean> list1 = JsonUtils.convert_array(res.data, ChildInfoBean.class);
            if(list1 == null){
                return;
            }
            data.clear();
            data.addAll(list1);
            boolean isBindDevice = false;
            for(int i=0; i<data.size(); i++){
                ChildInfoBean info = data.get(i);
                if(!TextUtils.isEmpty(info.imei)){
                    isBindDevice = true;
                    break;
                }
            }
            if(!isBindDevice){
                //弹出popwindow提示用户未绑定设备
//                BindDeviceTipsPopWindow popWindow = new BindDeviceTipsPopWindow(getActivity());
//                popWindow.showFromCenter();
            }
            if(data.size() > 0){
                ChildInfoBean childInfoBean = data.get(0);
            }
            if(data.size() > 0) {
                setChildsLocMarker();
            }else {
                setMyLoc();
            }
        });
    }

    private boolean isFrist = true;
    private void navigateTo(Location location) {
        String addressName = getAddressName(location.getLatitude(), location.getLongitude());

        // 按照经纬度确定地图位置
        LatLng point = new LatLng(location.getLatitude(),
                location.getLongitude());
        if (isFrist) {
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
            // 移动到某经纬度
            baiduMap.animateMapStatus(update);
            float f = baiduMap.getMaxZoomLevel();
            update = MapStatusUpdateFactory.zoomBy(f);
            // 放大
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point, f-2);
            baiduMap.animateMapStatus(u);
            isFrist = false;
        }
        // 显示个人位置图标
        MyLocationData.Builder builder = new MyLocationData.Builder().accuracy(100);
        builder.latitude(location.getLatitude());
        builder.longitude(location.getLongitude());
        MyLocationData data = builder.build();
        baiduMap.setMyLocationData(data);
    }

    public void setMyLocMarker(LatLng point){
        //构建Marker图标,显示自定义图标
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
        bundle.putInt("maker_index", -1);
        baiduMap.addOverlay(option).setExtraInfo(bundle);
    }

    public void setLocMarker(LatLng point, int position){
        ChildInfoBean item = this.data.get(position);
        new Thread(()->{
            Bitmap headbitmap = BitmapUtils.getBitmap(item.headimage);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.child_info_track_distance_marker, null, false);
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
            bundle.putInt("maker_index", position);
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

    public void setChildsLocMarker(){
        this.data.clear();
        for(int i=0; i<points.size(); i++){
            LatLng point = points.get(i);
            ChildInfoBean item = new ChildInfoBean();
            item.name = "张三" + i;
            item.latitude = point.latitude;
            item.longitude = point.longitude;
            item.headimage = "https://ps.ssl.qhimg.com/dmfd/417_417_/t01cdf17f58c08fc41c.jpg";
            this.data.add(item);
            setLocMarker(point, i);
        }
        if(points.size() > 0) {
            LatLng point = points.get(0);
            BDLocation location = new BDLocation();
            location.setLatitude(point.latitude);
            location.setLongitude(point.longitude);
            setPosition2Center(baiduMap, location, true);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onLocationChanged(Location arg0) {
            // TODO Auto-generated method stub
            // 位置改变则重新定位并显示地图
            navigateTo(arg0);
        }
    };

    @Override
    public void onDestroy() {
        // 释放资源
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 退出时销毁定位
        if(mLocationClient != null){
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient.stop();
        }

        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        if(mapView != null) {
            mapView.onDestroy();
            mapView = null;
        }
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
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
                    int maker_index = bundle.getInt("maker_index");
                    if(maker_index == -1){
                        //我的
//                        Bundle bundle1 = new Bundle();
//                        bundle1.putString("child_info", JsonUtils.toString(data.get(0)));
//                        bundle1.putString("childs", JsonUtils.toString(data));
//                        bundle1.putInt("index", 0);
//                        ActivityManagerUtils.startActivity(getContext(), ChildMapFragment.class, bundle1);
//                        ChildInfoPopWindow popWindow = new ChildInfoPopWindow(getActivity());
//                        popWindow.showFromBottom();
                    }else{
                        //监护人的
                        //String item = bundle.getString("data");
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("childs", JsonUtils.toString(data));
                        bundle1.putInt("index", maker_index);
                        ActivityManagerUtils.startActivity(getContext(), ChildMapFragment.class, bundle1);
                    }
                }
                return false;
            }
        });
        //定位我的位置
        //setMyLoc();
    }

    public void setMyLoc(){
        //声明LocationClient类
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
            loadData();
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
            //if (isFirstLocation) {
                isFirstLocation = false;
                //设置并显示中心点
                setPosition2Center(baiduMap, location, true);
                LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                myPosition = point;
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

    private Marker trackTextMarker;
    //追踪
    private void displayTextMarker(LatLng point){
        View view = this.getLayoutInflater().inflate(R.layout.child_info_track_distance_marker, null, false);
        TextView tv_txt = view.findViewById(R.id.tv_text);
        ImageView iv_image = view.findViewById(R.id.iv_image);
        ImageView iv_headimage = view.findViewById(R.id.iv_headimage);
        Bitmap src = BitmapFactory.decodeResource(app.getContext().getResources(), R.drawable.ic_marker_bk);
        int width = UIUtils.dp2px(getContext(), 42);
        float zoom = (float) width / (float) src.getWidth();
        Bitmap des = AssertUtils.getScaleBitemap(src, zoom, zoom);
        iv_image.setImageBitmap(des);
        Bitmap headbitmap = BitmapUtils.getBitmap("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543407653332&di=100b3665b3c8bebd303fd23d42500547&imgtype=0&src=http%3A%2F%2F8.pic.pc6.com%2Fup%2F2017-2%2F2017214946580971970.png");
        iv_headimage.setImageBitmap(headbitmap);

        Bitmap bitmap1 = viewToBitmap(getContext(), view);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromBitmap(bitmap1);

        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        Bundle bundle = new Bundle();
        bundle.putInt("id", 1000);
        if(trackTextMarker != null){
            trackTextMarker.remove();
        }
        trackTextMarker = (Marker) baiduMap.addOverlay(option);
        trackTextMarker.setExtraInfo(bundle);
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
        ChildItemsPopWindow popWindow = new ChildItemsPopWindow(getActivity(), data);
        popWindow.showPopWindow(iv_items);
    }

    @OnClick(R.id.iv_refresh)
    public void iv_refresh_Click(View view){
        SelectDeviceModePopWindow popWindow = new SelectDeviceModePopWindow(getActivity());
        popWindow.showFromCenter();
    }

    Thread thread;
    @OnClick(R.id.iv_track)
    public void iv_track_Click(View view){
        if(thread != null){
            thread.interrupt();
            thread = null;
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                displayTextMarker(myPosition);
            }
        });
        thread.start();
    }
}
