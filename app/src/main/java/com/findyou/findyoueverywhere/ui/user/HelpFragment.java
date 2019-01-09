package com.findyou.findyoueverywhere.ui.user;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiCommon;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.bean.common.HelpInfoBean;
import com.findyou.findyoueverywhere.controls.MyGridView;
import com.findyou.findyoueverywhere.controls.MyListView;
import com.findyou.findyoueverywhere.controls.MyRecyclerView;
import com.findyou.findyoueverywhere.ui.main.MessageFragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

public class HelpFragment extends BaseFragment {
    @BindView(R.id.listView)
    MyRecyclerView listView;

    @BindView(R.id.et_search)
    EditText et_search;

    private List<HelpInfoBean> data;
//    private List<HelpInfoBean> subdata;
    private MultiItemTypeAdapter<HelpInfoBean> adapter;
    private int pageIndex;
    private int count=0;
    private int pid = 0;

    public int getLayoutId() {
        setTitle("帮助中心");
        return R.layout.fragment_help;
    }

    public void tv_search_Click(){
        String str = et_search.getText().toString();
        if(TextUtils.isEmpty(str)){
            loadData();
            return;
        }
        ApiCommon.searchHelpItems(et_search.getText().toString(), (res) -> {
            List<HelpInfoBean> list = JsonUtils.convert_array(res.data, HelpInfoBean.class);
            if(list == null){
                return;
            }
            data.clear();
            data.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }

    public void initView(){
        data = new ArrayList<>();
        et_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                tv_search_Click();
                return true;
            }
            return false;
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_search_Click();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        RecyclerViewFinal recyclerViewFinal = listView.getRecyclerViewFinal();
        recyclerViewFinal.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerViewFinal.setLayoutManager(layoutManager);
        adapter = new MultiItemTypeAdapter<>(getActivity(), data);

        adapter.addItemViewDelegate(new ItemViewDelegate<HelpInfoBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.fragment_help_item;
            }

            @Override
            public boolean isForViewType(HelpInfoBean item, int position) {
                return (item.pid == 0);
            }

            @Override
            public void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, HelpInfoBean item, int position) {
                TextView tv_title = holder.getView(R.id.tv_title);
                tv_title.setText(item.title);
            }
        });
        adapter.addItemViewDelegate(new ItemViewDelegate<HelpInfoBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.fragment_help_item_item;
            }

            @Override
            public boolean isForViewType(HelpInfoBean item, int position) {
                return (item.pid != 0);
            }

            @Override
            public void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, HelpInfoBean item, int position) {
                TextView tv_title = holder.getView(R.id.tv_title);
                tv_title.setText(item.title);
                if(pid != item.pid){
                    View view_line = holder.getView(R.id.view_line);
                    view_line.setVisibility(View.VISIBLE);
                }
                pid = item.pid;
//                View view = holder.getConvertView();
//                view.setOnClickListener((res)->{
//
//                });
            }
        });

        listView.setAdapter(adapter);
        listView.setCallback(new MyRecyclerView.RecyclerViewCallback() {
            @Override
            public void onRefresh() {
                pageIndex = 0;
                //data.clear();
                //loadData(pageIndex);
            }
            @Override
            public void onLoadMore() {
                pageIndex++;
                //loadData(pageIndex);
            }
            @Override
            public void onClickItem(RecyclerView.ViewHolder holder, int position){
                HelpInfoBean bean = data.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("id", bean.id);
                ActivityManagerUtils.startActivity(getContext(), HelpItemFragment.class, bundle);
            }
        });

        loadData();
    }

    public void loadData(){
        ApiCommon.help(0, 0, (res)->{
            List<HelpInfoBean> list = JsonUtils.convert_array(res.data, HelpInfoBean.class);
            if(list == null){
                return;
            }
            data.clear();
            data.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }

}
