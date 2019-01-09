package com.findyou.findyoueverywhere.ui.user.popwindow;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiUser;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.BindView;
import butterknife.OnClick;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * Created by Administrator on 2018/1/22 0022.
 */

public class BirthdayPopWindow extends BasePopWindow implements OnWheelChangedListener {

    @BindView(R.id.id_year)
    WheelView mYear;
    @BindView(R.id.id_month)
    WheelView mMonth;
    @BindView(R.id.id_day)
    WheelView mDay;

    private int mMyYear = 2006;
    private int mMyMonth = 1;
    private int mMyDay = 1;
    private String birthday;

    public BirthdayPopWindow(Activity context, String birthday){
        super(context);
        this.birthday = birthday;
    }

    public int getLayoutId(){
        return R.layout.fragment_me_birthday;
    }

    public void initView(){
        initArgData();
    }

    private void initArgData() {

        //ArrayList year = new ArrayList<String>();
        String month[] = new String[12];
        String day[] = new String[31];

        Date curDate = new Date();
        Time time = new Time("GMT+8");
        time.setToNow();
        int count = time.year - 1960 + 1 - 10;
        String year[] = new String[count];
        int j = 0;
        for (int i = 1960; i < time.year + 1 - 10; i++) {
            year[count - j - 1] = i + "";
            j++;
        }

        for (int i = 0; i < 12; i++) {
            month[i] = (i + 1) + "";
        }

        for (int i = 0; i < 31; i++) {
            day[i] = (i + 1) + "";
        }

        ArrayWheelAdapter yearAdapter = new ArrayWheelAdapter<String>(mContext, year);
        yearAdapter.setTextSize(14);
        yearAdapter.setItemResource(R.layout.controls_wheel_view_item);
        yearAdapter.setItemTextResource(R.id.wheel_view_item_text);

        ArrayWheelAdapter monthAdapter = new ArrayWheelAdapter<String>(mContext, month);
        monthAdapter.setTextSize(14);
        monthAdapter.setItemResource(R.layout.controls_wheel_view_item);
        monthAdapter.setItemTextResource(R.id.wheel_view_item_text);

        ArrayWheelAdapter dayAdapter = new ArrayWheelAdapter<String>(mContext, day);
        dayAdapter.setTextSize(14);
        dayAdapter.setItemResource(R.layout.controls_wheel_view_item);
        dayAdapter.setItemTextResource(R.id.wheel_view_item_text);

        mYear.setViewAdapter(yearAdapter);
        mMonth.setViewAdapter(monthAdapter);
        mDay.setViewAdapter(dayAdapter);

        mYear.addChangingListener(this);
        mMonth.addChangingListener(this);
        mDay.addChangingListener(this);

        // 设置可见条目数量
        mYear.setVisibleItems(7);
        mMonth.setVisibleItems(7);
        mDay.setVisibleItems(7);

        if (!TextUtils.isEmpty(birthday)) {
            String[] split = birthday.split("-");
            mYear.setCurrentItem(Integer.parseInt(year[0]) - Integer.parseInt(split[0]));
            mMonth.setCurrentItem(Integer.parseInt(split[1]) - Integer.parseInt(month[0]));
            mDay.setCurrentItem(Integer.parseInt(split[2]) - Integer.parseInt(day[0]));
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        //年月日生日
        if (wheel == mYear) {
            int defaultYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date()));
            mMyYear = (defaultYear - 10 - newValue );
        } else if (wheel == mMonth) {
            mMyMonth = (newValue + 1);
        } else if (wheel == mDay) {
            mMyDay = (newValue + 1);
        }
    }

    @OnClick(R.id.tv_ok)
    public void tv_save_Clicked(View view){
        String str = String.format("%02d", mMyYear) + "-" + String.format("%02d", mMyMonth) + "-" + String.format("%02d", mMyDay);
//        ApiUser.doUpdateBirthday(str, (res)->{
//            App.Me.birthday = str;
//        });
        postEvent(new EventMessenger(EventConst.API_EDIT_BIRTHDAY, str));
        //成功
        //ToastUtils.showShortToast(mContext, "生日保存成功!");
        close();
    }

    @OnClick(R.id.tv_cancel)
    public void tv_cancel_Click(View view){
        close();
    }

}
