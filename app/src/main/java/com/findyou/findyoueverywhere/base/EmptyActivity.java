package com.findyou.findyoueverywhere.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.utils.LoadingUtils;
import com.findyou.findyoueverywhere.utils.fragmentbackkeyhelper.BackHandlerHelper;

import java.util.HashMap;

import butterknife.BindView;

public class EmptyActivity extends BaseActivity {

    @BindView(R.id.system_status_bar)
    LinearLayout system_status_bar;

    @BindView(R.id.system_title_bar)
    FrameLayout system_title_bar;

    @BindView(R.id.tv_back)
    LinearLayout tv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_right)
    TextView tv_right;

    @BindView(R.id.iv_right)
    ImageView iv_right;

    @BindView(R.id.view_title)
    FrameLayout view_title;

//    private View mView;
    private Fragment fragment = null;
    public static HashMap<String, Fragment> fragments = new HashMap<>();
    private String fragmentName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_empty;
    }

    public TextView getTitleView(){
        return tv_title;
    }

    public View getRightView(ViewType type){
        if(type == ViewType.TextView) {
            return tv_right;
        }else {
            return iv_right;
        }
    }

    public void setFullScreen(){

    }

    public void onDestroy(){
        super.onDestroy();
        if(fragment != null){
            fragments.remove(this.fragmentName);
        }
        LoadingUtils.finalClose();
    }

    public void showStatusBar(boolean isShow) {
        if (system_status_bar != null) {
            if (isShow) {
                system_status_bar.setVisibility(View.VISIBLE);
            }else{
                system_status_bar.setVisibility(View.GONE);
            }
        }
    }

    public void showTitleBar(boolean isShow){
        if (system_title_bar != null) {
            if (isShow) {
                system_title_bar.setVisibility(View.VISIBLE);
            }else{
                system_title_bar.setVisibility(View.GONE);
            }
        }
    }

    public FrameLayout getTitleLayout() {
        return view_title;
    }

    @Override
    public void initView() {
        if(tv_back != null){
            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        //接收参数
        Bundle bundle = this.getIntent().getExtras();
        String fragmentName = bundle.getString("FragmentName");
        this.fragmentName = fragmentName;
        try{
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if(fragment != null){
                fragmentTransaction.remove(fragment);
                fragment = null;
            }

            fragment = fragments.get(fragmentName);
            if(fragment == null){
                Class<?> cls = Class.forName(fragmentName);
                fragment = (Fragment) cls.newInstance();
                fragments.put(fragmentName, fragment);
            }

            if(!fragment.isAdded()){
                fragment.setArguments(bundle);
                fragmentTransaction.add(R.id.fragment_container, fragment, fragmentName);
                fragmentTransaction.commit();
            }else{
                fragmentTransaction.show(fragment).commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(fragment != null){
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            //这里重写返回键
            if(fragment != null){
                BaseFragment baseFragment = (BaseFragment)fragment;
                return baseFragment.onKeyDown(keyCode, event);
            }else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            //super.onBackPressed();
            BaseFragment baseFragment = (BaseFragment)fragment;
            baseFragment.onBackPressed();
        }
    }
}
