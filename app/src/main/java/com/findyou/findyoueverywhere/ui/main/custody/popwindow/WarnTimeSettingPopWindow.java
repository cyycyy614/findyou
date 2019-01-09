package com.findyou.findyoueverywhere.ui.main.custody.popwindow;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.constant.EventConst;

import butterknife.BindView;
import butterknife.OnClick;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class WarnTimeSettingPopWindow extends BasePopWindow implements OnWheelChangedListener {
    @BindView(R.id.wv_time)
    WheelView wv_time;

    private int timeIndex;

    public int getLayoutId() {
        mIsShowBackground = true;
        mIsOutsideClose = false;
        return R.layout.pop_window_warn_time_setting;
    }

    public WarnTimeSettingPopWindow(Activity activity){
        super(activity);
    }

    private int count = 20;
    public void initView(){
        String data[] = new String[count];
        for(int i=0; i<count; i++){
            int dis = (i + 1);
            String item = String.valueOf(dis) + "天";
            data[i] = item;
        }
        ArrayWheelAdapter adapter = new ArrayWheelAdapter<String>(mContext, data);
        adapter.setTextSize(14);
        adapter.setItemResource(R.layout.controls_wheel_view_item);
        adapter.setItemTextResource(R.id.wheel_view_item_text);
        wv_time.setViewAdapter(adapter);
        wv_time.addChangingListener(this);
        wv_time.setVisibleItems(7);
        if(app.me != null) {
            if (!TextUtils.isEmpty(app.me.birthday)) {
                String[] split = app.me.birthday.split("-");
                //mYear.setCurrentItem(Integer.parseInt(year[0]) - Integer.parseInt(split[0]));
            }
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        //年月日生日
        if (wheel == wv_time) {
            //int defaultYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date()));
            //mMyYear = (defaultYear - 10 - newValue );
            timeIndex = newValue;
        }
    }

    @OnClick(R.id.tv_cancel)
    public void tv_cancel_Click(View view){
        close();
    }

    @OnClick(R.id.tv_ok)
    public void tv_ok_Click(View view){
        int dis = (timeIndex + 1);
        postEvent(new EventMessenger(EventConst.API_CHANGE_WARN_TIME, dis));
        close();
    }


//    public class WheelAdapter<T> extends AbstractWheelTextAdapter {
//
//        // items
//        private T items[];
//
//        /**
//         * Constructor
//         * @param context the current context
//         * @param items the items
//         */
//        public ArrayWheelAdapter(Context context, T items[]) {
//            super(context);
//
//            //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
//            this.items = items;
//        }
//
//        @Override
//        public CharSequence getItemText(int index) {
//            if (index >= 0 && index < items.length) {
//                T item = items[index];
//                if (item instanceof CharSequence) {
//                    return (CharSequence) item;
//                }
//                return item.toString();
//            }
//            return null;
//        }
//
//        @Override
//        public int getItemsCount() {
//            return items.length;
//        }
//    }

}
