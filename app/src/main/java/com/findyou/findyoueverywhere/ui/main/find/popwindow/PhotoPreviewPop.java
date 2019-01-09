package com.findyou.findyoueverywhere.ui.main.find.popwindow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.constant.ApiConst;
import com.findyou.findyoueverywhere.controls.InterceptedRelativeLayout;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;

public class PhotoPreviewPop extends BasePopWindow {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.view_root)
    InterceptedRelativeLayout view_root;

    private ItemsPagerAdapter adapter;
    private List<String> images;
    private int position;

    public PhotoPreviewPop(Activity context, List<String> images, int position){
        super(context);
        this.images = images;
        this.position = position;
    }

    public int getLayoutId(){
        mIsShowBackground = true;
        mBackgroundColor = "#ee000000";
        mAnimatorDurtion = 500;
        return R.layout.pop_photo_preview;
    }

    public void initView(){
//        for(int i=0; i<20; i++){
//            images.add("http://appimage.douquzhibo.com/2524911?v=1492143204&imageMogr2/thumbnail/540");
//        }
        //setRootViewTouchListener();
        adapter = new ItemsPagerAdapter(images);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(this.position);
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int arg0) {
//                // TODO Auto-generated method stub
//                //Toast.makeText(MainActivity.this, "您选择了："+arg0+"页面", 0).show();
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//                // TODO Auto-generated method stub
//
//            }
//        });
    }

    //点击全屏隐藏键盘
    public void setRootViewTouchListener() {
        // 触摸软键盘以外的区域时关闭软键盘
        view_root.setOnInterceptTouchListener(new InterceptedRelativeLayout.OnInterceptedTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                close();
                return true;
            }
        });
    }

//    @OnClick(R.id.viewPager)
//    public void view_root_Click(View view){
//        ToastUtils.showToast(getContext(), "close");
//        this.close();
//    }
//
//    @OnClick(R.id.view_root)
//    public void view_root1_Click(View view){
//        ToastUtils.showToast(getContext(), "close");
//        this.close();
//    }

    public class ItemsPagerAdapter extends PagerAdapter {
        List<String> images;

        ItemsPagerAdapter(List<String> images){
            this.images = images;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0==arg1;
        }
        //有多少个切换页
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return images.size();
        }

        //对超出范围的资源进行销毁
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            // TODO Auto-generated method stub
            //super.destroyItem(container, position, object);
            //container.removeView(views.get(position));
            container.removeView(container.findViewById(position));
        }
        //对显示的资源进行初始化
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.pop_photo_preview_item, null);
            view.setOnClickListener((view1)->{
                close();
            });
            initPreviewPhoto(view, position);
            view.setId(position);
            container.addView(view);
            return view;
        }

        private void initPreviewPhoto(View view, int position){
            String image = images.get(position);
            if (null != image) {
                //主播 背景头像地址
                ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                if (iv_image != null) {
                    iv_image.setAlpha(1.0f);
                    iv_image.setVisibility(View.VISIBLE);
                    Picasso.with(app.getContext())
                            .load(image)
                            .placeholder(R.drawable.default_512)
                            .config(Bitmap.Config.RGB_565)
                            .into(iv_image);
                }
            }
        }
    }
}
