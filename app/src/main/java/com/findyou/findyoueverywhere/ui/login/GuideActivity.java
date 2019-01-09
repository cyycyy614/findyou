package com.findyou.findyoueverywhere.ui.login;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.base.BaseActivity;
import com.findyou.findyoueverywhere.controls.adapter.ViewPageChangeListener;
import com.findyou.findyoueverywhere.ui.main.MainActivity;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.LocalStorage;
import com.findyou.findyoueverywhere.utils.ResUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GuideActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.ll_viewPageIndicator)
    LinearLayout ll_viewPageIndicator;

//    @BindView(R.id.iv_login)
//    ImageView iv_login;

    List<View> list = new ArrayList<>();
    GuidePagerAdapter adapter;
    int pageCount = 3;

    public int getLayoutId(){
        return R.layout.activity_guide;
    }

    public void initView(){
        for(int i=0; i<pageCount-1; i++){
            ImageView view = new ImageView(this);
            String str = "guide_" + i;
            int resid = ResUtils.getDrableId(this, str);
            view.setBackgroundResource(resid);
            list.add(view);
        }
        View view = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.activity_guide_last, null, false);
        list.add(view);
        ImageView imageView = view.findViewById(R.id.iv_guide_last);
        String str = "guide_" + (pageCount - 1);
        int resid = ResUtils.getDrableId(this, str);
        imageView.setBackgroundResource(resid);
        ImageView iv_login = view.findViewById(R.id.tv_login);
        iv_login.setOnClickListener((view1)->{
            LocalStorage.setItem("first_application", "1"); //第一次使用标记
            LoginUtils.defaultLogin();
        });
        initPageIndicator();
        adapter = new GuidePagerAdapter(list);
        viewPager.setAdapter(adapter);
        ViewPageChangeListener pageChangeListener = new ViewPageChangeListener(ll_viewPageIndicator);
        viewPager.addOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(0);
    }

    private void initPageIndicator() {
        // add 页码
        ll_viewPageIndicator.removeAllViews();
        int px = UIUtils.dp2px(this, 5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(px, px);
        layoutParams.setMargins(0, 0, px, 0);
        for (int i = 0; i < pageCount; i++) {
            ImageView view = new ImageView(this);
            view.setLayoutParams(layoutParams);
            ll_viewPageIndicator.addView(view);
        }
    }
}
