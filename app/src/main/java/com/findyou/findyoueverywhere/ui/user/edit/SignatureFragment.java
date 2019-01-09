package com.findyou.findyoueverywhere.ui.user.edit;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.ViewType;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class SignatureFragment extends BaseFragment {
    @BindView(R.id.et_signature)
    EditText et_signature;

    public int getLayoutId(){
        return R.layout.fragment_me_signature;
    }

    public void initSaveBar(){
        TextView tv_save = (TextView) getRightView(ViewType.TextView);
        if(tv_save != null){
            tv_save.setText("保存");
            tv_save.setOnClickListener((View view)-> {
                String str = et_signature.getText().toString();
//                ApiUser.doUpdateSign(str, (res)->{
//                    app.Me.sign = str;
//                    //成功
//                    ToastUtils.showShortToast(getContext(), "签名保存成功!");
//                    if(getActivity() != null) {
//                        getActivity().finish();
//                    }
//                });
            });
        }
    }

    public void initView(){
        setTitle("编辑签名");
        initSaveBar();

        if(et_signature != null){
            et_signature.setText(app.me.sign);
        }
    }
}
