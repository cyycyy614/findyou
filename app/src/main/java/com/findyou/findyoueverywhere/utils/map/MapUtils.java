package com.findyou.findyoueverywhere.utils.map;

import com.baidu.mapapi.model.LatLng;

public class MapUtils {
    /**
     * 计算两点之间距离
     * @param start
     * @param end
     * @return 米
     */
    public String getDistance(LatLng start, LatLng end){
        double lat1 = (Math.PI/180)*start.latitude;
        double lat2 = (Math.PI/180)*end.latitude;

        double lon1 = (Math.PI/180)*start.longitude;
        double lon2 = (Math.PI/180)*end.longitude;

        //地球半径
        double R = 6371;

        //两点间距离 km，如果想要米的话，结果*1000
        double d =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*R;
        if(d<1)
            return (int)d*1000+"m";
        else
            return String.format("%.2f",d)+"km";
    }
}
