package com.findyou.findyoueverywhere.utils.sdk.sina.content;

import android.graphics.Bitmap;

public class ShareContent {
    /**
     * 文字
     */
    public static final int SINA_SHARE_WAY_TEXT = 1;
    /**
     * 图片
     */
    public static final int SINA_SHARE_WAY_PIC = 2;
    /**
     * 链接
     */
    public static final int SINA_SHARE_WAY_WEBPAGE = 3;

    public int type = SINA_SHARE_WAY_WEBPAGE; //
    public String title;
    public String content;
    public String url;
    public int picResource;
    public Bitmap bitmap;

    public ShareContent(int type, String title, String content, String url, Bitmap bitmap){
        this.type = type;
        this.title = title;
        this.content = content;
        this.url = url;
        this.bitmap = bitmap;
    }

}
