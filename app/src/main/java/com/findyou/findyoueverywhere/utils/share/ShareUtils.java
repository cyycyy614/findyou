package com.findyou.findyoueverywhere.utils.share;

import android.app.Activity;
import android.app.PictureInPictureParams;
import android.app.job.JobInfo;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.findyou.findyoueverywhere.base.CommonCallback2;
import com.findyou.findyoueverywhere.utils.PicUtils;
import com.findyou.findyoueverywhere.utils.json.JObject;
import com.findyou.findyoueverywhere.utils.sdk.qq.QQSDK;
import com.findyou.findyoueverywhere.utils.sdk.weixin.WeiXinSDK;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

public class ShareUtils {

    public static void share(Activity activity, ShareChannel channel, JObject json, CommonCallback2 callback2){
        if(channel == ShareChannel.SHARE_CHANNEL_QQ || channel == ShareChannel.SHARE_CHANNEL_QZONE){
            QQSDK.getInstance().share(activity, channel, json, callback2);
        }else if(channel == ShareChannel.SHARE_CHANNEL_WECHAT_MOMENT || channel == ShareChannel.SHARE_CHANNEL_WECHAT_WECHAT) {
//            JObject json = new JObject();
//            json.put("type", 1);
//            json.put("title", richShareInfoBean.getTitle());
//            json.put("content", richShareInfoBean.getContext());
//            json.put("url", richShareInfoBean.getUrl());
            String imageUrl = json.getString("image");
            Picasso.with(activity)
                    .load(imageUrl)
                    .resize(90, 90)
                    .centerCrop()
                    .into(new Target() {
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            WeiXinSDK.getInstance().share(channel, json, bitmap, callback2);
                        }
                    });
        }else if(channel == ShareChannel.SHARE_CHANNEL_SINAWB){
            String imageUrl = json.getString("image");
            Picasso.with(activity)
                    .load(imageUrl)
                    .resize(90, 90)
                    .centerCrop()
                    .into(new Target() {
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        }
                    });



        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        QQSDK.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
