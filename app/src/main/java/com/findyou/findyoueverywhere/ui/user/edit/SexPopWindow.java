package com.findyou.findyoueverywhere.ui.user.edit;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.constant.EventConst;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/21 0021.
 */

public class SexPopWindow extends BasePopWindow {

    @BindView(R.id.layout_cancel)
    LinearLayout layout_cancel;

    @BindView(R.id.layout_man)
    LinearLayout layout_man;

    @BindView(R.id.layout_woman)
    LinearLayout layout_woman;

    @BindView(R.id.tv_man)
    TextView tv_man;

    @BindView(R.id.tv_woman)
    TextView tv_woman;

    int mSex = 1;
    ;
    public SexPopWindow(Activity context){
        super(context);
    }

    public int getLayoutId(){
        return R.layout.fragment_me_sex;
    }

    public void initView() {
        if (app.me == null) {
            return;
        }
        if (app.me.sex == 1) {
            layout_man_Clicked(null);
        } else {
            layout_woman_Clicked(null);
        }
    }

    @OnClick(R.id.layout_cancel)
    public void layout_cancel_Clicked(View view){
        this.close();
    }

    @OnClick(R.id.layout_man)
    public void layout_man_Clicked(View view){
        if(layout_man != null && layout_woman != null){
            layout_man.setBackgroundColor(Color.parseColor("#f2f2f2"));
            layout_woman.setBackgroundColor(Color.parseColor("#ffffff"));
            mSex = 1;
        }
    }

    @OnClick(R.id.layout_woman)
    public void layout_woman_Clicked(View view){
        if(layout_man != null && layout_woman != null) {
            layout_man.setBackgroundColor(Color.parseColor("#ffffff"));
            layout_woman.setBackgroundColor(Color.parseColor("#f2f2f2"));
            mSex = 0;
        }
    }

    @OnClick(R.id.tv_save)
    public void tv_save_Clicked(View view){
//        ApiUser.doUpdateSex(mSex, (res)->{
//            app.Me.sex = mSex;
//            postEvent(new EventMessenger(EventConst.API_EDIT_SEX));
//            //成功
//            ToastUtils.showShortToast(mContext, "生日保存成功!");
//        });
        //app.me.sex = mSex;
        postEvent(new EventMessenger(EventConst.API_EDIT_SEX, mSex));
        close();
    }
}
