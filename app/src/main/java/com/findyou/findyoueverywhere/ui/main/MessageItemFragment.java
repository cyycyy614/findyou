package com.findyou.findyoueverywhere.ui.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiCommon;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.bean.MsgInfoBean;
import com.findyou.findyoueverywhere.controls.MyListView;
import com.findyou.findyoueverywhere.controls.MyRecyclerView;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

public class MessageItemFragment extends BaseFragment {
    @BindView(R.id.listView)
    MyRecyclerView recyclerView;

    private List<MsgInfoBean> data;
    private int pageIndex = 0;
    private CommonAdapter adapter;
    private int type;

    public void setType(int type){
        this.type = type;
    }

    public int getLayoutId() {
        return R.layout.message_item_fragment;
    }

    public void initView(){
        data = new ArrayList<>();
        for(int i=0; i<10; i++){
            MsgInfoBean bean = new MsgInfoBean();
            bean.title = "维修和洗护品类报价单上传规则的说明";
            bean.create_time = "2017.08.21";
            data.add(bean);
        }

        RecyclerViewFinal recyclerViewFinal = recyclerView.getRecyclerViewFinal();
        recyclerViewFinal.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerViewFinal.setLayoutManager(layoutManager);

        adapter = new CommonAdapter<MsgInfoBean>(getContext(), R.layout.fragment_message_item, data) {
            @Override
            protected void convert(ViewHolder holder, MsgInfoBean item, int position) {
                TextView tv_title = holder.getView(R.id.tv_title);
                tv_title.setText(item.msg);
                TextView tv_time = holder.getView(R.id.tv_time);
                tv_time.setText(item.create_time);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setCallback(new MyRecyclerView.RecyclerViewCallback() {
            @Override
            public void onRefresh() {
                pageIndex = 0;
                loadData(type);
            }

            @Override
            public void onLoadMore() {
                pageIndex++;
                loadData(type);
            }

            @Override
            public void onClickItem(RecyclerView.ViewHolder holder, int position) {

            }
        });
        loadData(0);
    }

    private void loadData(int type){
        ApiCommon.getMsgItems(type, pageIndex, (res)->{
            List<MsgInfoBean> list = JsonUtils.convert_array(res.data, MsgInfoBean.class);
            if(list == null){
                recyclerView.setEmptyView(true);
                return;
            }
            if(list.size() == 0){
                recyclerView.setEmptyView(true);
            }else {
                recyclerView.setEmptyView(false);
            }
            if(pageIndex == 0) {
                data.clear();
            }
            data.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }
}
