package com.findyou.findyoueverywhere.controls.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.base.BasePopWindow;

import butterknife.BindView;

public class LoadingDialog extends BasePopWindow {

    @BindView(R.id.tv_text)
    TextView tv_text;

    public LoadingDialog(Activity context){
        super(context);
    }

    public int getLayoutId(){
        mIsOutsideClose = false;
        return R.layout.controls_loading_pop_window;
    }

    public void initView(){
    }

    public void setText(String str){
        if(tv_text != null){
            tv_text.setText(str);
        }
    }

    public void hideText(){
        if(tv_text != null){
            tv_text.setVisibility(View.GONE);
        }
    }
}
