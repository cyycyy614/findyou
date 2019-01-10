package com.findyou.findyoueverywhere.controls.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.base.CommonCallback;

import butterknife.BindView;
import butterknife.OnClick;

public class CommomDialog extends BasePopWindow {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_msg)
    TextView tv_msg;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    @BindView(R.id.lin_style_1)
    LinearLayout lin_style_1;

    @BindView(R.id.lin_style_2)
    LinearLayout lin_style_2;

    @BindView(R.id.view_line)
    View view_line;

    private CommonCallback cbOk;
    private CommonCallback cbCancel;
    private CommomDialog target ;

    public CommomDialog(Activity context, String title, String msg, CommonCallback cbOk, CommonCallback cbCancel){
        super(context);
        tv_title.setText(title);
        tv_msg.setText(msg);
        this.cbOk = cbOk;
        this.cbCancel = cbCancel;
        if(lin_style_2 != null){
            lin_style_2.setVisibility(View.GONE);
        }
    }

    public CommomDialog(Activity context, String title, String msg){
        super(context);
        tv_title.setText(title);
        tv_msg.setText(msg);
        this.cbOk = null;
        this.cbCancel = null;
        if(view_line != null){
            view_line.setVisibility(View.GONE);
        }
        if(lin_style_1 != null){
            lin_style_1.setVisibility(View.GONE);
        }
    }

    public int getLayoutId(){
        mIsOutsideClose = false;
        return R.layout.controls_common_dialog;
    }

    public void initView(){
    }

    @OnClick(R.id.tv_cancel)
    public void tv_cancel_Click(View view){
        super.close();
        if(cbCancel != null){
            cbCancel.apply(null);
        }
    }

    @OnClick(R.id.tv_ok)
    public void tv_ok_Click(View view){
        super.close();
        if(cbOk != null){
            cbOk.apply(null);
        }
    }

    @OnClick(R.id.tv_ok_1)
    public void tv_ok_1_Click(View view){
        super.close();
    }

    public void setCancelBtnText(String txt){
        if(tv_cancel != null) {
            tv_cancel.setText(txt);
        }
    }
}
