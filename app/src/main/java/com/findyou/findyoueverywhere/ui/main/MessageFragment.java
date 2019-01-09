package com.findyou.findyoueverywhere.ui.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiCommon;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.bean.MsgInfoBean;
import com.findyou.findyoueverywhere.controls.MyListView;
import com.findyou.findyoueverywhere.controls.MyTabLayoutV1;
import com.findyou.findyoueverywhere.controls.TabLayoutCallback;
import com.findyou.findyoueverywhere.controls.adapter.FragmentAdapter;
import com.findyou.findyoueverywhere.ui.main.find.FindFragment;
import com.findyou.findyoueverywhere.ui.main.find.category.Category1Fragment;
import com.findyou.findyoueverywhere.utils.UIUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MessageFragment extends BaseFragment {
    public class ItemInfoBean{
        public int id;
        public String title;
        public String time;
    }

    @BindView(R.id.system_status_bar)
    LinearLayout system_status_bar;

    @BindView(R.id.tabLayout)
    MyTabLayoutV1 tabLayout;

    @BindView(R.id.listView)
    MyListView listView;

    @BindView(R.id.vp_viewpager)
    ViewPager vp_viewpager;

    private List<MsgInfoBean> data;
    private int pageIndex = 0;
    private CommonAdapter adapter;
    private String[] titles = {"报警消息", "系统消息"};
    private int type = 0;
    private List<Fragment> fragments;

    public int getLayoutId() {
        return R.layout.fragment_message;
    }

    public void initView(){
        int statusHeight = UIUtils.getStatusHeight(getContext());
        system_status_bar.getLayoutParams().height = statusHeight;

        fragments = new ArrayList<>();
        MessageItemFragment c1 = new MessageItemFragment();
        c1.setType(0);
        MessageItemFragment c2 = new MessageItemFragment();
        c2.setType(1);
        fragments.add(c1);
        fragments.add(c2);

        FragmentPagerAdapter adapter = new FragmentAdapter(getChildFragmentManager(), this.getContext(), fragments);
        vp_viewpager.setAdapter(adapter);
        vp_viewpager.setOffscreenPageLimit(3);

        tabLayout.setLayoutId(R.layout.fragment_message_tab_item);
        tabLayout.setTabData(titles);
        tabLayout.setCallback(new TabLayoutCallback() {
            @Override
            public void onClickItem(View view, int position) {
//                type = position;
//                pageIndex = 0;
//                loadData(position);
                vp_viewpager.setCurrentItem(position, true);
            }
            @Override
            public void onNormalStyle(View view){
                TextView tv_title = view.findViewById(R.id.tv_title);
                tv_title.setTextColor(app.getContext().getColor(R.color.colorGray9));
                View line = view.findViewById(R.id.view_line);
                line.setVisibility(View.GONE);
            }
            @Override
            public void onHoverStyle(View view){
                TextView tv_title = view.findViewById(R.id.tv_title);
                tv_title.setTextColor(app.getContext().getColor(R.color.defaultColor));
                View line = view.findViewById(R.id.view_line);
                line.setVisibility(View.VISIBLE);
            }
        });
        tabLayout.setCurrentItem(0);
        vp_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //加载数据
        data = new ArrayList<>();
//        for(int i=0; i<10; i++){
//            MsgInfoBean bean = new MsgInfoBean();
//            bean.title = "维修和洗护品类报价单上传规则的说明";
//            bean.create_time = "2017.08.21";
//            data.add(bean);
//        }
//        adapter = new CommonAdapter<MsgInfoBean>(getContext(), R.layout.fragment_message_item, data) {
//            @Override
//            protected void convert(ViewHolder holder, MsgInfoBean item, int position) {
//                TextView tv_title = holder.getView(R.id.tv_title);
//                tv_title.setText(item.msg);
//                TextView tv_time = holder.getView(R.id.tv_time);
//                tv_time.setText(item.create_time);
//            }
//        };
//        listView.setAdapter(adapter);
//        listView.setCallback(new MyListView.ListViewCallback() {
//            @Override
//            public void onRefresh() {
//                pageIndex = 0;
//                loadData(type);
//            }
//            @Override
//            public void onLoadMore() {
//                pageIndex++;
//                loadData(type);
//            }
//            @Override
//            public void onClickItem(AdapterView<?> parent, View view, int position, long id) {
////                ArticleInfoBean bean = data.get(position);
////                Bundle bundle = new Bundle();
////                bundle.putInt("id", bean.id);
////                ActivityManagerUtils.startActivity(getContext(), PublicNotifyDetailFragment.class, bundle);
//            }
//        });
//        loadData(0);
    }

    private void loadData(int type){
        ApiCommon.getMsgItems(type, pageIndex, (res)->{
            List<MsgInfoBean> list = JsonUtils.convert_array(res.data, MsgInfoBean.class);
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
