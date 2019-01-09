package com.findyou.findyoueverywhere.ui.main.custody.setting;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiEfence;
import com.findyou.findyoueverywhere.api.http.ApiWarning;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.bean.GuardianInfoBean;
import com.findyou.findyoueverywhere.bean.WarHistoryInfoBean;
import com.findyou.findyoueverywhere.controls.MyListView;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

public class CustodySettingItem9Fragment extends BaseFragment {

    @BindView(R.id.listView)
    MyListView listView;

    public int id;
    public String name;
    private List<WarHistoryInfoBean> data;
    private int pageIndex = 0;
    private CommonAdapter adapter;

    public int getLayoutId() {
        setTitle("历史报警");
        return R.layout.fragment_custody_setting_item9;
    }

    public void initView(){
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        name = bundle.getString("name");
        data = new ArrayList<>();
        adapter = new CommonAdapter<WarHistoryInfoBean>(getContext(), R.layout.fragment_custody_setting_item9_item, data) {
            @Override
            protected void convert(ViewHolder holder, WarHistoryInfoBean item, int position) {
                LinearLayout lin_line = holder.getView(R.id.lin_line);
                if(position == 0){
                    lin_line.setVisibility(View.INVISIBLE);
                }else {
                    lin_line.setVisibility(View.VISIBLE);
                }
                TextView tv_msg = holder.getView(R.id.tv_msg);
                tv_msg.setText(item.msg);
                TextView tv_speed = holder.getView(R.id.tv_speed);
                tv_speed.setText(item.speed);
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
//                GuardianInfoBean bean = data.get(position);
//                Bundle bundle = new Bundle();
//                bundle.putString("obj", new Gson().toJson(bean).toString());
//                bundle.putString("childname", CustodySettingItem2ListFragment.this.name);
//                ActivityManagerUtils.startActivity(getContext(), EditCustodySettingItem2Fragment.class, bundle);
            }
        });
        loadData();
    }

    public void loadData(){
        ApiWarning.getItems(id, "", (res)->{
            List<WarHistoryInfoBean> list = JsonUtils.convert_array(res.data, WarHistoryInfoBean.class);
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
}
