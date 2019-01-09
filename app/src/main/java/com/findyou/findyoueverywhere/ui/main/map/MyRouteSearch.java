package com.findyou.findyoueverywhere.ui.main.map;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.findyou.findyoueverywhere.R;

public class MyRouteSearch {
    RoutePlanSearch mSearch;
    BaiduMap baiduMap;
    public void search(BaiduMap baiduMap, LatLng start, LatLng end){
        this.baiduMap = baiduMap;
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(listener);
        PlanNode stNode = PlanNode.withLocation(start);
        PlanNode enNode = PlanNode.withLocation(end);
        //步行
//        mSearch.walkingSearch((new WalkingRoutePlanOption())
//                .from(stNode)
//                .to(enNode));

        //骑行
        //通过设ridingType，可以区分普通自行车，和电动车线路
//        ridingType(int ridingType)
////        mSearch.bikingSearch((new BikingRoutePlanOption())
////                .from(stNode)
////                .to(enNode));
        //架车
        try {
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
        }catch (Exception e){
            e.printStackTrace();
        }
//        //公交
//        mSearch.transitSearch(
//                new TransitRoutePlanOption()
//                        .from(stNode)
//                        .to(enNode));
    }

    public void searchWalk(BaiduMap baiduMap, LatLng start, LatLng end) {
        this.baiduMap = baiduMap;
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(listener);
        PlanNode stNode = PlanNode.withLocation(start);
        PlanNode enNode = PlanNode.withLocation(end);
        //步行
        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode)
                .to(enNode));
    }

    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        //获取步行线路规划结果
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            drawWalkLine(result);
        }
        //获取综合公共交通线路规划结果
        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        }
        //获取**跨城**综合公共交通线路规划结果
        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
        }
        //获取驾车线路规划结果
        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
            drawLine(drivingRouteResult);
        }
        //室内路线规划结果
        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
        }
        //获取普通骑行路规划结果
        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        }
    };

    public void drawLine(DrivingRouteResult drivingRouteResult){
        if (drivingRouteResult == null || drivingRouteResult.error !=   SearchResult.ERRORNO.NO_ERROR) {
            //Toast.makeText(MapRoadActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            drivingRouteResult.getSuggestAddrInfo();
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            if (drivingRouteResult.getRouteLines().size() >= 1) {
                MyDrivingRouteOverlay overlay = new MyDrivingRouteOverlay(baiduMap);
                baiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(drivingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                //overlay.zoomToSpan();
                //mSearch.destroy();
            } else {
                //Log.d("route result", "结果数<0");
                return;
            }
        }
    }

    public void drawWalkLine(WalkingRouteResult drivingRouteResult){
        if (drivingRouteResult == null || drivingRouteResult.error !=   SearchResult.ERRORNO.NO_ERROR) {
            //Toast.makeText(MapRoadActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            drivingRouteResult.getSuggestAddrInfo();
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            if (drivingRouteResult.getRouteLines().size() >= 1) {
                MyWalkRouteOverlay overlay = new MyWalkRouteOverlay(baiduMap);
                baiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(drivingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                //overlay.zoomToSpan();
                //mSearch.destroy();
            } else {
                //Log.d("route result", "结果数<0");
                return;
            }
        }
    }

    boolean useDefaultIcon = false;//使用默认ICON
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {
        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }
        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_location_me);
            }
            return null;
        }
        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_location_me);
            }
            return null;
        }
    }

    private class MyWalkRouteOverlay extends WalkingRouteOverlay {
        public MyWalkRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
            useDefaultIcon = true;
        }
        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.empty);
            }
            return null;
        }
        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.empty);
            }
            return null;
        }
    }
}
