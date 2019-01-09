package com.findyou.findyoueverywhere.controls.album;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.PermissionsUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;
import com.findyou.findyoueverywhere.utils.photo.PhotoUpAlbumHelper;
import com.findyou.findyoueverywhere.utils.photo.PhotoUpImageItem;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/22 0022.
 */

public class AlbumFragment extends BaseFragment {

    public enum From{
        Dynamic, //动态
        HeadImage, //头像
        RealNameVerify, //实名验证
        ApplyLive //申请直播
    }

    @BindView(R.id.gridView)
    GridView gridView;

    PhotoUpAlbumHelper photoUpAlbumHelper;
    List<PhotoUpAlbumHelper.PhotoUpImageBucket> album;
    CommonAdapter mAdapter;
    public static PhotoUpAlbumHelper.PhotoUpImageBucket currSelected;
    public static List<PhotoUpImageItem> allImages;
    public static List<PhotoUpImageItem> selectedImages;

    private int maxCount; //最大数量
    private int curCount; //已经选了多少

    public void initSaveBar(){
        TextView tv_save = (TextView)getRightView(ViewType.TextView);
        if(tv_save != null){
            tv_save.setText("完成");
            tv_save.setOnClickListener((View view)-> {
                Gson gson = new Gson();
                String json = gson.toJson(selectedImages);
                postEvent(new EventMessenger(EventConst.API_UPLOAD_FILES, json));
                if(getActivity() != null){
                    getActivity().finish();
                }
            });
        }
    }

    public int getLayoutId(){
        setTitle("相册");
        initSaveBar();
        return R.layout.fragment_album;
    }

    public void initView(){
        PermissionsUtils.checkPermissionsStorage(getActivity());
        Bundle bundle = getArguments();
        curCount = bundle.getInt("curr_count", 0);
        maxCount = bundle.getInt("count", 1);
        album = new ArrayList<>(); //专辑
        if(currSelected != null){
            currSelected = null;
        }
        if(allImages != null){
            allImages.clear();
        }else {
            allImages = new ArrayList<>(); //所有
        }
        if(selectedImages == null){
            selectedImages = new ArrayList<>(); //选择的
        }else{
            selectedImages.clear();
        }
        boolean isShowAll = true; //显示所有
        if(!isShowAll) {
            mAdapter = new CommonAdapter<PhotoUpAlbumHelper.PhotoUpImageBucket>(getContext(), R.layout.fragment_album_item, album) {
                @Override
                protected void convert(ViewHolder holder, PhotoUpAlbumHelper.PhotoUpImageBucket item, int position) {
                    ImageView iv_image = holder.getView(R.id.iv_image);
                    String fileName = item.imageList.get(0).imagePath;
                    File file = new File(fileName);
                    Picasso.with(getContext()).load(file).placeholder(R.drawable.default_128).into(iv_image);

                    TextView tv_name = holder.getView(R.id.tv_name);
                    tv_name.setText(item.bucketName);

                    TextView tv_count = holder.getView(R.id.tv_count);
                    tv_count.setText(item.count + "张");

                    //点击事件
                    View itemView = holder.getConvertView();
                    itemView.setOnClickListener((View view) -> {
                        currSelected = item;
                        //ActivityManagerUtils.startActivity(getContext(), FragmentConst.AlbumImages);
                    });
                }
            };
        }else {
            gridView.setNumColumns(3);
            int space = UIUtils.dp2px(getContext(),2);
            gridView.setVerticalSpacing(space);
            gridView.setHorizontalSpacing(space);
            mAdapter = new CommonAdapter<PhotoUpImageItem>(getContext(), R.layout.fragment_album_detail_item, allImages) {
                @Override
                protected void convert(ViewHolder holder, PhotoUpImageItem item, int position) {

                    ImageView iv_image = holder.getView(R.id.iv_image);
                    iv_image.getLayoutParams().height = UIUtils.getScreenWidth(getContext()) / 3;
                    String fileName = item.imagePath;
                    File file = new File(fileName);
                    Picasso.with(getContext()).load(file).placeholder(R.drawable.default_128).into(iv_image);

                    ImageView iv_selected = holder.getView(R.id.iv_selected);
                    //点击事件
                    View itemView = holder.getConvertView();
                    itemView.setOnClickListener((View view) -> {
                        int total = selectedImages.size() + curCount;
                        if(total >= maxCount && !item.isSelected){
                            ToastUtils.showToast(getContext(), "最多只能选择"+ maxCount+ "张相片!");
                            return;
                        }
                        if(iv_selected.getVisibility() == View.VISIBLE){
                            iv_selected.setVisibility(View.GONE);
                            item.isSelected = false;
                            selectedImages.remove(item);
                        }else {
                            iv_selected.setVisibility(View.VISIBLE);
                            item.isSelected = true;
                            selectedImages.add(item);
                        }
                    });
                }
            };
        }
        gridView.setAdapter(mAdapter);
        loadData();
    }

    private void loadData(){
        photoUpAlbumHelper = PhotoUpAlbumHelper.getHelper();//创建异步线程实例
        photoUpAlbumHelper.init(getActivity());//初始化实例
        //回调接口，创建匿名内部对象，实现接口中的方法，获取到PhotoUpAlbumHelper的接口GetAlbumList所传递的数据
        photoUpAlbumHelper.setGetAlbumList(new PhotoUpAlbumHelper.GetAlbumList() {
            @Override
            public void getAlbumList(List<PhotoUpAlbumHelper.PhotoUpImageBucket> list) {
                album.clear();
                album.addAll(list);
                allImages.clear();
                for(int i=0; i<list.size(); i++){
                    allImages.addAll(list.get(i).imageList);
                }
                mAdapter.notifyDataSetChanged();//更新视图
                //ActivityManagerUtils.startActivity(getContext(), FragmentConst.AlbumImages);
            }
        });
        photoUpAlbumHelper.execute(false);//异步线程执行
    }
}
