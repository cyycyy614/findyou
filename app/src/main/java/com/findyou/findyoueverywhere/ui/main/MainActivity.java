package com.findyou.findyoueverywhere.ui.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.base.BaseActivity;
import com.findyou.findyoueverywhere.controls.NoScrollViewPager;
import com.findyou.findyoueverywhere.controls.TabLayoutCallback;
import com.findyou.findyoueverywhere.controls.TabLayoutView;
import com.findyou.findyoueverywhere.controls.dialog.CommomDialog;
import com.findyou.findyoueverywhere.receiver.MyNotificationManager;
import com.findyou.findyoueverywhere.ui.main.find.FindFragment;
import com.findyou.findyoueverywhere.ui.main.map.HomeFragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.NotificationUtils;
import com.findyou.findyoueverywhere.utils.PermissionsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp_viewpager)
    NoScrollViewPager vp_viewpager;

    @BindView(R.id.tab_layout)
    TabLayoutView tab_layout;

    private ArrayList<Fragment> fragments;

    private int resimg[][] = {
            {R.drawable.ic_home_hover, R.drawable.ic_home_normal},
            {R.drawable.ic_find_hover, R.drawable.ic_find_normal},
            {R.drawable.ic_msg_hover, R.drawable.ic_msg_normal},
            {R.drawable.ic_baba_hover, R.drawable.ic_baba_normal},
            {R.drawable.ic_me_hover, R.drawable.ic_me_normal},
    };
    private String titles[] = {
            "首页","发现","消息","被监护人","我的"
    };
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    public void initView(){
        fragments = new ArrayList<Fragment>();
        HomeFragment homeFragment = new HomeFragment();
        fragments.add(homeFragment);

        FindFragment homeFragment1 = new FindFragment();
        fragments.add(homeFragment1);

        MessageFragment homeFragment2 = new MessageFragment();
        fragments.add(homeFragment2);

        CustodyUserFragment homeFragment3 = new CustodyUserFragment();
        fragments.add(homeFragment3);

        UserCenterFragment homeFragment4 = new UserCenterFragment();
        fragments.add(homeFragment4);

        ItemsPagerAdapter adapter = new ItemsPagerAdapter(getSupportFragmentManager(), this, fragments);
        vp_viewpager.setAdapter(adapter);
        vp_viewpager.setCurrentItem(0);
        vp_viewpager.setOffscreenPageLimit(5);
        tab_layout.setTabData(resimg, titles, 0); //titles为空则没有字
        tab_layout.setCallback(new TabLayoutCallback() {
            @Override
            public void onClickItem(View view, int position) {
                vp_viewpager.setCurrentItem(position, false);
            }
            @Override
            public void onNormalStyle(View view){
            }
            @Override
            public void onHoverStyle(View view){
            }
        });

        //请求权限
        PermissionsUtils.requestPermissionLocation(this);

        String jpushid = JPushInterface.getRegistrationID(getApplicationContext());
        String str = jpushid;
        //ActivityManagerUtils.startActivity(this, SelectTimeFragment.class, null);
        NotificationUtils.getInstance().createNotificationChannel(); //创建通知渠道
        //MyNotificationManager.initNotificationChannel(this);
    }

    public class ItemsPagerAdapter extends FragmentPagerAdapter {
        Context mContext;
        List<Fragment> fragments;
        public ItemsPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragments) {
            super(fm);
            mContext = context;
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public void onBackPressed() {
        //返回键处理
        CommomDialog dlg = new CommomDialog(this, "信息提示", "是否退出App?", (obj)->{
            //OK
            ActivityManagerUtils.getInstance().exitApp(); //退出app.
        },(obj1)->{
            //取消
        });
        //dlg.setCancelBtnText("退出");
        dlg.showFromCenter();
    }
}