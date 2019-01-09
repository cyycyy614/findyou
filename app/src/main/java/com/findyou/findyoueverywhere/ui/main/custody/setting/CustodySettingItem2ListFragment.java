package com.findyou.findyoueverywhere.ui.main.custody.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiGuardian;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.bean.ChildInfoBean;
import com.findyou.findyoueverywhere.bean.GuardianInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.controls.MyListView;
import com.findyou.findyoueverywhere.ui.main.custody.CustodySettingFragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.custody.AddCustodySettingItem2Fragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.custody.EditCustodySettingItem2Fragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CustodySettingItem2ListFragment extends BaseFragment {
    @BindView(R.id.listView)
    MyListView listView;

    public int id;
    public String name;

    public void initSaveBar(){
        TextView textView = (TextView)getRightView(ViewType.TextView);
        if(textView != null){
            textView.setText("添加");
            textView.setTextColor(Color.WHITE);
            textView.setOnClickListener((View view)-> {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putString("name", name);
                ActivityManagerUtils.startActivity(getActivity(), AddCustodySettingItem2Fragment.class, bundle);
            });
        }
    }

    private List<GuardianInfoBean> data;
    private int pageIndex = 0;
    private CommonAdapter adapter;

    public int getLayoutId() {
        setTitle("授权监护人");
        registerEvent();
        return R.layout.fragment_custody_setting_item2;
    }

    public void initView(){
        initSaveBar();
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        name = bundle.getString("name");
        data = new ArrayList<>();
        adapter = new CommonAdapter<GuardianInfoBean>(getContext(), R.layout.fragment_custody_setting_item2_item, data) {
            @Override
            protected void convert(ViewHolder holder, GuardianInfoBean item, int position) {
                TextView tv_name = holder.getView(R.id.tv_name);
                tv_name.setText(item.name);
                TextView tv_phone = holder.getView(R.id.tv_phone);
                tv_phone.setText(item.phone);
                TextView tv_auth_time = holder.getView(R.id.tv_auth_time);
                tv_auth_time.setText(item.auth_time);
                ImageView iv_status = holder.getView(R.id.iv_status);
                if(item.status == 1){
                    iv_status.setImageResource(R.drawable.ic_select_hover);
                }else{
                    iv_status.setImageResource(R.drawable.ic_select_normal);
                }
            }
        };
        listView.setAdapter(adapter);
        listView.setCallback(new MyListView.ListViewCallback() {
            @Override
            public void onRefresh() {
                pageIndex = 0;
                loadData();
            }
            @Override
            public void onLoadMore() {
                pageIndex++;
                loadData();
            }
            @Override
            public void onClickItem(AdapterView<?> parent, View view, int position, long id) {
                GuardianInfoBean bean = data.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("obj", new Gson().toJson(bean).toString());
                bundle.putString("childname", CustodySettingItem2ListFragment.this.name);
                ActivityManagerUtils.startActivity(getContext(), EditCustodySettingItem2Fragment.class, bundle);
            }
        });
        loadData();
    }

    public void loadData(){
        ApiGuardian.getItems(id, pageIndex, (res)->{
            List<GuardianInfoBean> list = JsonUtils.convert_array(res.data, GuardianInfoBean.class);
            if(list == null){
                if(listView != null)
                    listView.setEmptyView(true);
                return;
            }
            if(list.size() == 0){
                if(listView != null)
                    listView.setEmptyView(true);
            }else {
                if(listView != null)
                    listView.setEmptyView(false);
            }
            //if(pageIndex == 0) {
                data.clear();
            //}
            data.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }

    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_ADD_GUARDIAN_SUCCESSFUL:
                loadData();
                break;
            case EventConst.API_UPDATE_GUARDIAN_SUCCESSFUL:
                loadData();
                break;
        }
    }
}
