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

public class NicknameFragment extends BaseFragment {

    @BindView(R.id.et_nickname)
    EditText et_nickname;

    public int getLayoutId(){
        return R.layout.fragment_me_nickname;
    }

    public void initSaveBar(){
        TextView tv_save = (TextView) getRightView(ViewType.TextView);
        if(tv_save != null){
            tv_save.setText("保存");
            tv_save.setOnClickListener((View view)-> {
                String str = et_nickname.getText().toString();
//                ApiUser.doUpdateNickname(str, (res)->{
//                    app.Me.nickname = str;
//                    //成功
//                    ToastUtils.showToast(getContext(), "昵称保存成功!");
//                    if(getActivity() != null) {
//                        getActivity().finish();
//                    }
//                });
            });
        }
    }

    public void initView(){
        setTitle("编辑昵称");
        initSaveBar();
        if(et_nickname != null){
            et_nickname.setText(app.me.nickname);
        }
    }
}
