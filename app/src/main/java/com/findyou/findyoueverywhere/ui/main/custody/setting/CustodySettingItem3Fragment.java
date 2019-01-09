package com.findyou.findyoueverywhere.ui.main.custody.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiGuardian;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.bean.GuardianInfoBean;
import com.findyou.findyoueverywhere.controls.MyListView;
import com.findyou.findyoueverywhere.ui.main.custody.setting.custody.AddCustodySettingItem2Fragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.custody.EditCustodySettingItem2Fragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.google.gson.Gson;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CustodySettingItem3Fragment extends BaseFragment {
    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.tv_child_name)
    TextView tv_child_name;

    public int id;
    public String name;
    private List<GuardianInfoBean> data;
    private int pageIndex = 1;
    private CommonAdapter adapter;
    private View lastView;
    private GuardianInfoBean lastItem;

    public int getLayoutId() {
        setTitle("指定主监护人");
        return R.layout.fragment_custody_setting_item3;
    }
    public void initSaveBar(){
        TextView textView = (TextView)getRightView(ViewType.TextView);
        if(textView != null){
            textView.setText("确定");
            textView.setTextColor(Color.WHITE);
            textView.setOnClickListener((View view)-> {
                onSuhmit();
            });
        }
    }
    public void initView(){
        initSaveBar();
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        name = bundle.getString("name");
        if(tv_child_name != null){
            tv_child_name.setText(name);
        }
        data = new ArrayList<>();
        adapter = new CommonAdapter<GuardianInfoBean>(getContext(), R.layout.fragment_custody_setting_item3_item, data) {
            @Override
            protected void convert(ViewHolder holder, GuardianInfoBean item, int position) {
                TextView tv_name = holder.getView(R.id.tv_name);
                tv_name.setText(item.name);
                ImageView iv_status = holder.getView(R.id.iv_status);
                View view = holder.getConvertView();
                if(item.ismain){
                    iv_status.setImageResource(R.drawable.ic_radio_hover);
                    lastView = view;
                    lastItem = item;
                }else{
                    iv_status.setImageResource(R.drawable.ic_radio_normal);
                }
                view.setOnClickListener((v)->{
                    for(int i=0; i<data.size(); i++){
                        data.get(i).ismain = false;
                    }
                    if(lastView != null){
                        ImageView iv_ismain = lastView.findViewById(R.id.iv_status);
                        if(iv_ismain != null){
                            iv_ismain.setImageResource(R.drawable.ic_radio_normal);
                        }
                        if(lastItem != null){
                            lastItem.ismain = false;
                        }
                    }
                    iv_status.setImageResource(R.drawable.ic_radio_hover);
                    lastView = v;
                    item.ismain = true;
                    lastItem = item;
                    notifyDataSetChanged();
                });
            }
        };
        listView.setAdapter(adapter);
        ApiGuardian.getItems(id, pageIndex, (res)->{
            List<GuardianInfoBean> list = JsonUtils.convert_array(res.data, GuardianInfoBean.class);
            if(list == null){
                return;
            }
            data.clear();
            for(int i=0; i<list.size(); i++){
                if(list.get(i).status == 1) {
                    data.add(list.get(i));
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

    public void onSuhmit(){
        if(lastItem != null){
            ApiGuardian.setMain(lastItem.id, id, (res)->{
                ToastUtils.showToast(getContext(), res.message);
            });
        }
    }
}
