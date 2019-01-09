package com.findyou.findyoueverywhere.ui.main.custody.setting.child;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.constant.EventConst;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class HealthPopWindow extends BasePopWindow implements OnWheelChangedListener {
    @BindView(R.id.wheelView)
    WheelView wheelView;

    private int mIndex;
    private List<String> items;

    public int getLayoutId(){
        return R.layout.health_pop_window;
    }

    public HealthPopWindow(Activity activity, List<String> items, int index){
        super(activity);
        this.items = items;
        this.mIndex = index;
    }

    public void initView(){
        String data[] = new String[items.size()];
        for(int i=0; i<items.size(); i++){
            String item = items.get(i); // String.valueOf(dis);
            data[i] = item;
        }
        ArrayWheelAdapter adapter = new ArrayWheelAdapter<String>(mContext, data);
        adapter.setTextSize(14);
        adapter.setItemResource(R.layout.controls_wheel_view_item);
        adapter.setItemTextResource(R.id.wheel_view_item_text);
        wheelView.setViewAdapter(adapter);
        wheelView.addChangingListener(this);
        wheelView.setVisibleItems(5);
        wheelView.setShadowsCount(2);
        wheelView.setCurrentItem(mIndex);
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
        String str = items.get(mIndex);
        postEvent(new EventMessenger(EventConst.API_EDIT_CHILD_HEALTH, mIndex));
        close();
    }
}
