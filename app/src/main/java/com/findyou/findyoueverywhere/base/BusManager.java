package com.findyou.findyoueverywhere.base;

import com.squareup.otto.Bus;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class BusManager {
    private static Bus bus = new Bus();

    public static void register(Object obj){
        if (bus != null) {
            bus.register(obj);
        }
    }

    public static void unregister(Object obj){
        if (bus != null) {
            bus.unregister(obj);
        }
    }

    public static void postEvent(EventMessenger event) {
        if(bus != null){
            bus.post(event);
        }
    }
}
