package com.findyou.findyoueverywhere.ui.main.find.popwindow;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.constant.FragmentConst;
import com.findyou.findyoueverywhere.controls.album.AlbumFragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;

import butterknife.OnClick;

public class DynamicPublishPop extends BasePopWindow {
    public DynamicPublishPop(Activity context){
        super(context);
    }

    public int getLayoutId(){
        mIsShowBackground = true;
        return R.layout.pop_dynamic_publish;
    }

    public void initView(){

    }

    @OnClick(R.id.tv_take_phone)
    public void tv_take_phone_Click(View view){
    }

    @OnClick(R.id.tv_album_select)
    public void tv_album_select_Click(View view){
        Bundle bundle = new Bundle();
        ActivityManagerUtils.startActivity(getContext(), AlbumFragment.class, bundle);
        close();
    }

}
