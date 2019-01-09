package com.findyou.findyoueverywhere.ui.main.map.popwindow;

import android.app.Activity;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.utils.log.log;

import java.security.Policy;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class SelectTimePopWindow extends BasePopWindow implements OnWheelChangedListener {
    @BindView(R.id.id_year)
    WheelView id_year;
    @BindView(R.id.id_month)
    WheelView id_month;
    @BindView(R.id.id_day)
    WheelView id_day;
    @BindView(R.id.id_hour)
    WheelView id_hour;
    @BindView(R.id.id_minute)
    WheelView id_minute;
    @BindView(R.id.id_second)
    WheelView id_second;

    @BindView(R.id.tv_title)
    TextView tv_title;

    String stryear;
    String strmonth;
    String strday;
    String strhour;
    String strminute;
    String strsecond;
    String datetime;
    String title;
    int type;

    int nyear;
    int nmonth = 1;
    int nday = 1;
    int nhour = 1;
    int nminute = 1;
    int nsecond = 1;

    public int getLayoutId() {
        mIsShowBackground = true;
        mIsOutsideClose = false;
        return R.layout.select_time_pop_window;
    }

    public SelectTimePopWindow(Activity activity, String title, int type){
        super(activity);
        this.title = title;
        this.type = type;
    }

    public void initView(){
        tv_title.setText(this.title);
        initArgData();
    }

    private void initArgData() {

        //ArrayList year = new ArrayList<String>();
        String month[] = new String[12];
        String day[] = new String[31];
        String hour[] = new String[12];
        String minute[] = new String[60];
        String second[] = new String[60];

        Date curDate = new Date();
        Time time = new Time("GMT+8");
        time.setToNow();
        nyear = time.year;
        int count = time.year - 1960 + 1;
        String year[] = new String[count];
        int j = 0;
        for (int i = 1960; i < time.year + 1; i++) {
            year[count - j - 1] = i + "";
            j++;
        }

        for (int i = 0; i < 12; i++) {
            month[i] = (i + 1) + "";
        }

        for (int i = 0; i < 31; i++) {
            day[i] = (i + 1) + "";
        }

        for (int i = 0; i < 12; i++) {
            hour[i] = (i + 1) + "";
        }

        for (int i = 0; i < 60; i++) {
            minute[i] = (i + 1) + "";
        }

        for (int i = 0; i < 60; i++) {
            second[i] = (i + 1) + "";
        }

        stryear = year[0];
        strmonth = month[0];
        strday = day[0];
        strhour = hour[0];
        strminute = minute[0];
        strsecond = second[0];

        datetime = String.format("%d-%02d-%02d %02d:%02d:%02d", nyear, nmonth, nday, nhour, nminute, nsecond);

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

        ArrayWheelAdapter hourAdapter = new ArrayWheelAdapter<String>(mContext, day);
        hourAdapter.setTextSize(14);
        hourAdapter.setItemResource(R.layout.controls_wheel_view_item);
        hourAdapter.setItemTextResource(R.id.wheel_view_item_text);

        ArrayWheelAdapter minuteAdapter = new ArrayWheelAdapter<String>(mContext, day);
        minuteAdapter.setTextSize(14);
        minuteAdapter.setItemResource(R.layout.controls_wheel_view_item);
        minuteAdapter.setItemTextResource(R.id.wheel_view_item_text);

        ArrayWheelAdapter secondAdapter = new ArrayWheelAdapter<String>(mContext, day);
        secondAdapter.setTextSize(14);
        secondAdapter.setItemResource(R.layout.controls_wheel_view_item);
        secondAdapter.setItemTextResource(R.id.wheel_view_item_text);

        id_year.setViewAdapter(yearAdapter);
        id_month.setViewAdapter(monthAdapter);
        id_day.setViewAdapter(dayAdapter);
        id_hour.setViewAdapter(hourAdapter);
        id_minute.setViewAdapter(minuteAdapter);
        id_second.setViewAdapter(secondAdapter);

        id_year.addChangingListener(this);
        id_month.addChangingListener(this);
        id_day.addChangingListener(this);
        id_hour.addChangingListener(this);
        id_minute.addChangingListener(this);
        id_second.addChangingListener(this);
        // 设置可见条目数量
        id_year.setVisibleItems(7);
        id_month.setVisibleItems(7);
        id_day.setVisibleItems(7);
        id_hour.setVisibleItems(7);
        id_minute.setVisibleItems(7);
        id_second.setVisibleItems(7);

        if(app.me != null)
            if (!TextUtils.isEmpty(app.me.birthday)) {
                String[] split = app.me.birthday.split("-");
//                id_year.setCurrentItem(Integer.parseInt(year[0]) - Integer.parseInt(split[0]));
//                id_month.setCurrentItem(Integer.parseInt(split[1]) - Integer.parseInt(month[0]));
//                id_day.setCurrentItem(Integer.parseInt(split[2]) - Integer.parseInt(day[0]));
            }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        //年月日生日

        if (wheel == id_year) {
            int defaultYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date()));
            nyear = (defaultYear - newValue );
        } else if (wheel == id_month) {
            nmonth = (newValue + 1);
        } else if (wheel == id_day) {
            nday = (newValue + 1);
        } else if (wheel == id_hour) {
            nhour = (newValue + 1);
        } else if (wheel == id_minute) {
            nminute = (newValue + 1);
        } else if (wheel == id_second) {
            nsecond = (newValue + 1);
        }
        datetime = String.format("%d-%02d-%02d %02d:%02d:%02d", nyear, nmonth, nday, nhour, nminute, nsecond);
        log.write("datetime:" + datetime);
    }

    @OnClick(R.id.tv_ok)
    public void tv_ok_Click(View view){
        if(type == 0) {
            postEvent(new EventMessenger(EventConst.API_SELECT_START_TIME, datetime));
        }else {
            postEvent(new EventMessenger(EventConst.API_SELECT_END_TIME, datetime));
        }
        close();
    }

    @OnClick(R.id.tv_cancel)
    public void tv_cancel_Click(View view){
        close();
    }
}
