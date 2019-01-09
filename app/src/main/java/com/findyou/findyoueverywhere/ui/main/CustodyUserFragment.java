package com.findyou.findyoueverywhere.ui.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiChild;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.bean.ChildInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.controls.MyListView;
import com.findyou.findyoueverywhere.ui.main.custody.setting.child.AddChildSettingItem1Fragment;
import com.findyou.findyoueverywhere.ui.main.custody.CustodySettingFragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.PicUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.findyou.findyoueverywhere.utils.log.log;
import com.squareup.otto.Subscribe;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//被监护
public class CustodyUserFragment extends BaseFragment {
    public class ItemInfoBean{
        public int id;
        public String title;
        public String time;
    }

    @BindView(R.id.system_status_bar)
    LinearLayout system_status_bar;

    @BindView(R.id.listView)
    MyListView listView;

    private List<ChildInfoBean> data;
    private int pageIndex = 0;
    private CommonAdapter adapter;

    public int getLayoutId() {
        registerEvent();
        return R.layout.fragment_custody_user;
    }

    public void initView(){
        int statusHeight = UIUtils.getStatusHeight(getContext());
        system_status_bar.getLayoutParams().height = statusHeight;
        pageIndex = 0;
        data = new ArrayList<>();
//        for(int i=0; i<10; i++){
//            ItemInfoBean bean = new ItemInfoBean();
//            bean.title = "维修和洗护品类报价单上传规则的说明";
//            bean.time = "2017.08.21";
//            data.add(bean);
//        }

        adapter = new CommonAdapter<ChildInfoBean>(getContext(), R.layout.fragment_custody_user_item, data) {
            @Override
            protected void convert(ViewHolder holder, ChildInfoBean item, int position) {
                TextView tv_name = holder.getView(R.id.tv_name);
                tv_name.setText(item.name);
                ImageView iv_headimage = holder.getView(R.id.iv_headimage);
                PicUtils.loadImage(getContext(), item.headimage, iv_headimage);
                TextView tv_imie = holder.getView(R.id.tv_imie);
                if(TextUtils.isEmpty(item.imei)) {
                    tv_imie.setText("IME: 未绑定");
                    TextView tv_time = holder.getView(R.id.tv_time);
                    tv_time.setText("绑定时间: 无");
                }else {
                    tv_imie.setText("IME: " + item.imei);
                    TextView tv_time = holder.getView(R.id.tv_time);
                    tv_time.setText("绑定时间: " + item.create_time);
                }
                TextView tv_custody = holder.getView(R.id.tv_custody);
                if(TextUtils.isEmpty(item.guardian)) {
                    tv_custody.setText("被授权监护人:未设置");
                }else {
                    tv_custody.setText("被授权监护人: " + item.guardian);
                }

//                TextView tv_time = holder.getView(R.id.tv_time);
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(item.update_time*1000);
//                String time = format.format(date);
//                tv_time.setText(time);
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
                ChildInfoBean bean = data.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("id", bean.id);
                bundle.putString("name", bean.name);
                bundle.putString("imei", bean.imei);
                bundle.putString("headimage", bean.headimage);
                bundle.putString("deviceId", bean.deviceId);
                bundle.putString("device_address", bean.device_address);
                ActivityManagerUtils.startActivity(getContext(), CustodySettingFragment.class, bundle);
            }
        });
        loadData();
    }

    public void loadData(){
        ApiChild.getItems(pageIndex,(res)->{
            List<ChildInfoBean> list = JsonUtils.convert_array(res.data, ChildInfoBean.class);
            if(list == null){
                listView.setEmptyView(true);
                return;
            }
            if(list.size() == 0){
                listView.setEmptyView(true);
            }else {
                listView.setEmptyView(false);
            }
            //log.write("list.size:" + list.size());
            if(pageIndex == 0) {
                data.clear();
            }
            data.addAll(list);
            //log.write("data.size:" + data.size());
            adapter.notifyDataSetChanged();
        });
    }

    @OnClick(R.id.lin_add_custody)
    public void lin_add_custody_Click(View view){
        ActivityManagerUtils.startActivity(getContext(), AddChildSettingItem1Fragment.class, null);
    }

    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_CHANGE_IMEI:
                loadData();
                break;
            case EventConst.API_UNBIND_DEVICE:
                loadData();
                break;
            case EventConst.API_ADD_CHILD_SUCCESSFUL:
                loadData();
                break;
            case EventConst.API_UPDATE_CHILD_SUCCESSFUL:
                loadData();
                break;
        }
    }
}
