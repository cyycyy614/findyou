package com.findyou.findyoueverywhere.ui.user;

import android.os.Bundle;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiCommon;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.bean.common.HelpInfoBean;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;

import butterknife.BindView;

public class HelpItemFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_content)
    TextView tv_content;

    public int getLayoutId() {
        setTitle("帮助内容");
        return R.layout.fragment_help_item_content;
    }

    public void initView(){
        Bundle bundle = getArguments();
        int id = bundle.getInt("id");
        ApiCommon.getHelpItem(id,(res)->{
            HelpInfoBean item = JsonUtils.convert(res.data, HelpInfoBean.class);
            if(item == null){
                return;
            }
            if(tv_title != null){
                tv_title.setText(item.title);
            }
            if(tv_content != null){
                tv_content.setText(item.content);
            }
        });
    }
}
