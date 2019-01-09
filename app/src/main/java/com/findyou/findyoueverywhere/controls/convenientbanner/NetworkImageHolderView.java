package com.findyou.findyoueverywhere.controls.convenientbanner;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.findyou.findyoueverywhere.controls.convenientbanner.holder.Holder;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        if(null == context || null == imageView || TextUtils.isEmpty(data)) return;
        Picasso.with(context)
                .load(data)
                .into(imageView);
    }
}
