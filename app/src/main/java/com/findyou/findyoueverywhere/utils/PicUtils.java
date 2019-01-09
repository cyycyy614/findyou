package com.findyou.findyoueverywhere.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.findyou.findyoueverywhere.R;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class PicUtils {
    public static void loadRoundedImage(Context context, String imageUrl, ImageView iv_image, int radius){
        if(TextUtils.isEmpty(imageUrl)){
            return;
        }
        Transformation trans = new RoundedCornersTransformation(radius, 0);
        Picasso.with(context)
                .load(imageUrl).transform(trans).placeholder(R.drawable.default_512)
                .error(R.drawable.default_512)
                .config(Bitmap.Config.RGB_565)
                .into(iv_image);
    }

    public static void loadRoundedImage(Context context, String imageUrl, ImageView iv_image, int width, int height, int radius){
        if(TextUtils.isEmpty(imageUrl)){
            return;
        }
        Transformation trans = new RoundedCornersTransformation(radius, 0);
        Picasso.with(context)
                .load(imageUrl).transform(trans).placeholder(R.drawable.default_512)
                .error(R.drawable.default_512)
                .config(Bitmap.Config.RGB_565)
                //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .resize(width, height)
                .into(iv_image);
    }

    public static void loadImage(Context context, String imageUrl, ImageView iv_image){
        try {
            if(TextUtils.isEmpty(imageUrl)){
                iv_image.setImageResource(R.drawable.default_512);
                return;
            }
            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.default_512)
                    .error(R.drawable.default_512)
                    .config(Bitmap.Config.RGB_565)
                    .into(iv_image);
        }catch (Exception e){
            if(iv_image != null) {
                iv_image.setImageResource(R.drawable.default_512);
            }
            e.printStackTrace();
        }
    }

    public static void loadImageNoCache(Context context, String imageUrl, ImageView iv_image){
        try {
            if(TextUtils.isEmpty(imageUrl)){
                return;
            }
            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.default_512)
                    .error(R.drawable.default_512)
                    .config(Bitmap.Config.RGB_565)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                    .into(iv_image);
        }catch (Exception e){
            if(iv_image != null) {
                iv_image.setImageResource(R.drawable.default_512);
            }
            e.printStackTrace();
        }
    }

    public static void loadImage(Context context, String imageUrl, ImageView iv_image, int placeHolder){
        if(TextUtils.isEmpty(imageUrl)){
            return;
        }
        Picasso.with(context)
                .load(imageUrl)
                .placeholder(placeHolder)
                .error(placeHolder)
                .config(Bitmap.Config.RGB_565)
                .into(iv_image);
    }

    public static void simpleLoadImage(Context context, String imageUrl, ImageView iv_image){
        if(TextUtils.isEmpty(imageUrl)){
            return;
        }
        Picasso.with(context)
                .load(imageUrl)
                .config(Bitmap.Config.RGB_565)
                .into(iv_image);
    }
}
