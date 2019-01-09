package com.findyou.findyoueverywhere.ui.main.custody.setting.device;

import android.app.Activity;
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
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class TrackPopWindow extends BasePopWindow implements OnWheelChangedListener {
    @BindView(R.id.wheelView)
    WheelView wheelView;

    private int mIndex;

    public int getLayoutId() {
        mIsShowBackground = true;
        mIsOutsideClose = false;
        return R.layout.track_pop_window;
    }

    public TrackPopWindow(Activity activity){
        super(activity);
    }

    private int count = 20;
    public void initView(){
        String data[] = new String[count];
        for(int i=0; i<count; i++){
            int dis = (i + 1) * 10;
            String item = String.valueOf(dis);
            data[i] = item;
        }
        ArrayWheelAdapter adapter = new ArrayWheelAdapter<String>(mContext, data);
        adapter.setTextSize(14);
        adapter.setItemResource(R.layout.controls_wheel_view_item);
        adapter.setItemTextResource(R.id.wheel_view_item_text);
        wheelView.setViewAdapter(adapter);
        wheelView.addChangingListener(this);
        wheelView.setVisibleItems(7);
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
        if (wheel == wheelView) {
            //int defaultYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date()));
            //mMyYear = (defaultYear - 10 - newValue );
            mIndex = newValue;
        }
    }

    @OnClick(R.id.tv_cancel)
    public void tv_cancel_Click(View view){
        close();
    }

    @OnClick(R.id.tv_ok)
    public void tv_ok_Click(View view){
        int item = (mIndex + 1) * 10;
        postEvent(new EventMessenger(EventConst.API_DEVICE_SET_TRACK, item));
        close();
    }
}
