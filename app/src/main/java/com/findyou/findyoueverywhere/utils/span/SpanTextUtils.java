package com.findyou.findyoueverywhere.utils.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.utils.SDCardUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;

/**
 * Created by Administrator on 2017/7/4 0004.
 */

public class SpanTextUtils {

    public static void insertImage(TextView textView, Drawable drawable, int leftSpace, int rightSpace){
        SpannableString ss = new SpannableString("a");
        int leftPx = UIUtils.dp2px(app.getContext(), leftSpace);
        int rightPx = UIUtils.dp2px(app.getContext(), rightSpace);
        drawable.getBounds().left += leftPx;
        drawable.getBounds().right += rightPx;

        //居中对齐imageSpan
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable);
        ss.setSpan(imageSpan, 0, "a".length(), ImageSpan.ALIGN_BASELINE);
        textView.append(ss);
    }

    public static void insertImage(TextView textView, String fileName, int width, int height){
        Drawable drawable = SDCardUtils.getDrawable(fileName);
        if(drawable == null){
            return;
        }
        drawable.setBounds(0, 0, width, height);
        SpanTextUtils.insertImage(textView, drawable, 5, 5);
    }

    public static void insertImage(TextView textView, int resid, int width, int height){
        Drawable drawable = app.getContext().getResources().getDrawable(resid);
        if(drawable == null){
            return;
        }
        drawable.setBounds(0, 0, width, height);
        SpanTextUtils.insertImage(textView, drawable, 5, 5);
    }

    public static void insertImage(TextView textView, int resid, int width, int height, int leftSpace, int rightSpace){
        Drawable drawable = app.getContext().getResources().getDrawable(resid);
        if(drawable == null){
            return;
        }
        drawable.setBounds(0, 0, width, height);
        SpanTextUtils.insertImage(textView, drawable, leftSpace, rightSpace);
    }

//    private static void insertImage(TextView textView, int resid, int leftSpace){
//        Drawable drawable = MyApp.getContext().getResources().getDrawable(resid);
//        SpannableString ss = new SpannableString("a");
//        int leftPx = UIUtils.dp2px(MyApp.getContext(), leftSpace);
//        drawable.getBounds().left += leftPx;
//        drawable.getBounds().right += leftPx;
//        //居中对齐imageSpan
//        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable);
//        ss.setSpan(imageSpan, 0, "a".length(), ImageSpan.ALIGN_BASELINE);
//        textView.append(ss);
//    }

    public static void insertEmpty(TextView textView, int width){
        Bitmap bitmap = null;
        Context context = app.getContext();
        SpannableString ss = new SpannableString("a");

        int pxWidth = UIUtils.dp2px(app.getContext(), width);
        Drawable drawable = new BitmapDrawable(app.getContext().getResources(), bitmap);
        if(drawable != null) {
            drawable.setBounds(0, 0, pxWidth, 5);
        }
        //居中对齐imageSpan
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable);
        ss.setSpan(imageSpan, 0, "a".length(), ImageSpan.ALIGN_BASELINE);

        textView.append(ss);
    }

    public static void insertText(TextView textView, String text, String color) {
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor(color)), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(ss);
    }

    public static void insertClickText(TextView textView, String text, ClickSpan span) {
        SpannableString ss = new SpannableString(text);
        ss.setSpan(span, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(ss);
    }

    public static void insertText(TextView textView, SpannableString ss) {
        textView.append(ss);
    }
}
