package com.findyou.findyoueverywhere.controls.album;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.utils.UIUtils;
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

public class AlbumImagesFragment extends BaseFragment {

    @BindView(R.id.gridView)
    GridView gridView;

    public List<PhotoUpImageItem> images;

    public void initSaveBar(){
        TextView tv_save = (TextView) getRightView(ViewType.TextView);
        if(tv_save != null){
            tv_save.setText("完成");
            tv_save.setOnClickListener((View view)-> {

            });
        }
    }

    public int getLayoutId(){
        setTitle("选择");
        initSaveBar();
        return R.layout.fragment_album_detail;
    }

    public void initView(){
        images = new ArrayList<>();
        if(AlbumFragment.currSelected != null) {
            images.addAll(AlbumFragment.currSelected.imageList);
        }else {
            images.addAll(AlbumFragment.allImages);
        }
        CommonAdapter mAdapter = new CommonAdapter<PhotoUpImageItem>(getContext(), R.layout.fragment_album_detail_item, images) {
            @Override
            protected void convert(ViewHolder holder, PhotoUpImageItem item, int position) {

                ImageView iv_image = holder.getView(R.id.iv_image);
                iv_image.getLayoutParams().height = UIUtils.getScreenWidth(getContext()) / 3;
                String fileName = item.imagePath;
                File file = new File(fileName);
                Picasso.with(getContext()).load(file).placeholder(R.drawable.default_128).into(iv_image);

                //点击事件
                View itemView = holder.getConvertView();
                itemView.setOnClickListener((View view)->{

                });
            }
        };
        gridView.setAdapter(mAdapter);
    }
}
