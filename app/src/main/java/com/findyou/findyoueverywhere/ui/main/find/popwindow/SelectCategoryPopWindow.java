package com.findyou.findyoueverywhere.ui.main.find.popwindow;

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

public class SelectCategoryPopWindow extends BasePopWindow implements OnWheelChangedListener {
    @BindView(R.id.wheelView)
    WheelView wheelView;

    private int mIndex;
    private String[] items;

    public int getLayoutId(){
        return R.layout.select_category_pop_window;
    }

    public SelectCategoryPopWindow(Activity activity, String[] items, int type){
        super(activity);
        this.items = items;
        mIndex = type;
    }

    public void initView(){
//        String data[] = new String[items.size()];
//        for(int i=0; i<items.size(); i++){
//            String item = items.get(i); // String.valueOf(dis);
//            data[i] = item;
//        }
        ArrayWheelAdapter adapter = new ArrayWheelAdapter<String>(mContext, items);
        adapter.setTextSize(14);
        adapter.setItemResource(R.layout.controls_wheel_view_item);
        adapter.setItemTextResource(R.id.wheel_view_item_text);
        wheelView.setViewAdapter(adapter);
        wheelView.addChangingListener(this);
        wheelView.setVisibleItems(5);
        wheelView.setShadowsCount(2);
        wheelView.setCurrentItem(mIndex);
//        if(app.me != null) {
//            if (!TextUtils.isEmpty(app.me.birthday)) {
//                String[] split = app.me.birthday.split("-");
//                //mYear.setCurrentItem(Integer.parseInt(year[0]) - Integer.parseInt(split[0]));
//            }
//        }
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
        String str = items[mIndex];
        postEvent(new EventMessenger(EventConst.API_STORY_SELECT_CATEGORY, mIndex));
        close();
    }
}
