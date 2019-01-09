package com.findyou.findyoueverywhere.ui.main.custody.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiEfence;
import com.findyou.findyoueverywhere.api.http.ApiWarning;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.bean.EfenceInfoBean;
import com.findyou.findyoueverywhere.bean.WarHistoryInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.controls.MyListView;
import com.findyou.findyoueverywhere.ui.main.custody.setting.custody.EditCustodySettingItem2Fragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.efence.AddEfenceFragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.efence.EditEfenceFragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CustodySettingItem8Fragment extends BaseFragment {
    @BindView(R.id.listView)
    MyListView listView;

    public int id;
    public String name;
    private List<EfenceInfoBean> data;
    private int pageIndex = 0;
    private CommonAdapter adapter;

    public void initSaveBar(){
        TextView textView = (TextView)getRightView(ViewType.TextView);
        if(textView != null){
            textView.setText("添加");
            textView.setTextColor(Color.WHITE);
            textView.setOnClickListener((View view)-> {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                ActivityManagerUtils.startActivity(getContext(), AddEfenceFragment.class, bundle);
            });
        }
    }

    public int getLayoutId() {
        setTitle("电子围栏");
        registerEvent();
        return R.layout.fragment_custody_setting_item8;
    }

    public void initView(){
        initSaveBar();
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        name = bundle.getString("name");
        data = new ArrayList<>();
        adapter = new CommonAdapter<EfenceInfoBean>(getContext(), R.layout.fragment_custody_setting_item8_item, data) {
            @Override
            protected void convert(ViewHolder holder, EfenceInfoBean item, int position) {
                TextView tv_name = holder.getView(R.id.tv_name);
                TextView tv_address = holder.getView(R.id.tv_address);
                TextView tv_radius = holder.getView(R.id.tv_radius);
                ImageView tv_status = holder.getView(R.id.tv_status);
                tv_name.setText(item.name);
                tv_address.setText(item.address);
                tv_radius.setText(String.valueOf(item.radius)+"米");
                if(item.status){
                    tv_status.setImageResource(R.drawable.ic_select_hover);
                }else{
                    tv_status.setImageResource(R.drawable.ic_select_normal);
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
            public void onClickItem(AdapterView<?> parent, View view, int position, long id){
                EfenceInfoBean bean = data.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("info", new Gson().toJson(bean).toString());
                ActivityManagerUtils.startActivity(getContext(), EditEfenceFragment.class, bundle);
            }
        });
        loadData();
    }

    public void loadData(){
        ApiEfence.getItems(id, pageIndex, (res)->{
            List<EfenceInfoBean> list = JsonUtils.convert_array(res.data, EfenceInfoBean.class);
            if(list == null){
                listView.setEmptyView(true);
                return;
            }
            if(list.size() == 0){
                listView.setEmptyView(true);
            }else {
                listView.setEmptyView(false);
            }
            if(pageIndex == 0) {
                data.clear();
            }
            data.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }

    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_UPDATE_EFENCE_TIME:
                pageIndex = 0;
                loadData();
                break;
        }
    }
}
