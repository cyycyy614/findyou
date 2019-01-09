package com.findyou.findyoueverywhere.ui.main.find;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiCommon;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.WebViewFragment;
import com.findyou.findyoueverywhere.bean.BannerInfoBean;
import com.findyou.findyoueverywhere.bean.NewsInfoBean;
import com.findyou.findyoueverywhere.constant.FragmentConst;
import com.findyou.findyoueverywhere.controls.MyListView;
import com.findyou.findyoueverywhere.controls.convenientbanner.ConvenientBanner;
import com.findyou.findyoueverywhere.controls.convenientbanner.NetworkImageHolderView;
import com.findyou.findyoueverywhere.controls.convenientbanner.holder.CBViewHolderCreator;
import com.findyou.findyoueverywhere.controls.convenientbanner.listener.OnItemClickListener;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.PicUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;
import com.findyou.findyoueverywhere.utils.http.HttpResponse;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FindFragment extends BaseFragment {
    public class ItemInfoBean{
        public int id;
        public String title;
        public String time;
    }

    @BindView(R.id.system_status_bar)
    LinearLayout system_status_bar;

    @BindView(R.id.listView)
    MyListView listView;

    @BindView(R.id.lin_banner)
    LinearLayout lin_banner;

    private List<NewsInfoBean> data;
    private int pageIndex = 1;
    private CommonAdapter adapter;

    public int getLayoutId() {
        return R.layout.fragment_find;
    }

    public void initView(){
        int statusHeight = UIUtils.getStatusHeight(getContext());
        system_status_bar.getLayoutParams().height = statusHeight;

        //Banner
        bannerImages = new ArrayList<>();
        initBanner();

        lin_banner.addView(mConvenientBanner);

        data = new ArrayList<>();
//        for(int i=0; i<10; i++){
////            ItemInfoBean bean = new ItemInfoBean();
////            bean.title = "维修和洗护品类报价单上传规则的说明";
////            bean.time = "2017.08.21";
////            data.add(bean);
//        }

        adapter = new CommonAdapter<NewsInfoBean>(getContext(), R.layout.fragment_find_item, data) {
            @Override
            protected void convert(ViewHolder holder, NewsInfoBean item, int position) {
                TextView tv_title = holder.getView(R.id.tv_title);
                tv_title.setText(item.title);

                TextView tv_time = holder.getView(R.id.tv_time);
                tv_time.setText(item.create_time);

                ImageView iv_image = holder.getView(R.id.iv_image);
                PicUtils.loadImage(getContext(), item.image, iv_image);
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
                NewsInfoBean bean = data.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("id", bean.id);
                ActivityManagerUtils.startActivity(getContext(), ArticleDetailFragment.class, bundle);
            }
        });
        loadData();
    }

    public void loadData(){
        ApiCommon.getNewsItems(0, pageIndex, (res)->{
            List<NewsInfoBean> list = JsonUtils.convert_array(res.data, NewsInfoBean.class);
            if(list == null){
                listView.setEmptyView(true);
                return;
            }
            if(list.size() == 0){
                listView.setEmptyView(true);
            }else {
                listView.setEmptyView(false);
            }
            if(pageIndex == 0){
                data.clear();
            }
            data.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }

    @OnClick(R.id.lin_findStory)
    public void lin_findStory_Click(View view){
        ActivityManagerUtils.startActivity(getContext(), FindStoryFragment.class, null);
    }

    private List<BannerInfoBean> bannerItems;
    private ConvenientBanner<String> mConvenientBanner;
    private List<String> bannerImages;

    private void initBanner(){
        mConvenientBanner = new ConvenientBanner<>(getContext());
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(UIUtils.getScreenWidth(getContext()), UIUtils.dp2px(getContext(),200));
        mConvenientBanner.setLayoutParams(layoutParams);
        ApiCommon.getBanners(1, 4, this::onGetAdItems);
    }

    private void onGetAdItems(HttpResponse res){
        if(res == null){
//            bannerImages.add("http://img.zcool.cn/community/01b34657d653360000012e7e452f0c.jpg@900w_1l_2o_100sh.jpg");
//            bannerImages.add("http://img.zcool.cn/community/01ebb85715d29732f8758c9b35cad4.jpg");
//            bannerImages.add("http://img.zcool.cn/community/0117b95735a0516ac72580ed47a35f.png");
//            setBannerData(bannerImages);
            return;
        }
        bannerItems = JsonUtils.convert_array(res.data, BannerInfoBean.class);
        if(bannerItems == null){
            return;
        }
        for(int i=0; i<bannerItems.size(); i++){
            bannerImages.add(bannerItems.get(i).image);
        }
        setBannerData(bannerImages);
    }

    private void setBannerData(List<String> images) {
        mConvenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView(); //网络加载图片
            }
        }, images)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) { // 点击事件
                        if(bannerItems == null){
                            return;
                        }
                        BannerInfoBean itemBean = bannerItems.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("title", itemBean.name);
                        bundle.putString("url", itemBean.url);
                        ActivityManagerUtils.startActivity(getContext(), WebViewFragment.class, bundle);
                    }
                });
    }
}
