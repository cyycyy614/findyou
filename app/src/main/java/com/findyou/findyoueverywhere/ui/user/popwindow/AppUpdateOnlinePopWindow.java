package com.findyou.findyoueverywhere.ui.user.popwindow;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.AppUpdateOnlineHelper;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.base.WebViewFragment;
import com.findyou.findyoueverywhere.bean.common.AppInfoBean;
import com.findyou.findyoueverywhere.bean.common.ChannelInfoBean;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class AppUpdateOnlinePopWindow extends BasePopWindow {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    AppUpdateOnlineHelper helper;
    AppInfoBean appInfo;

    public int getLayoutId() {
        mIsShowBackground = true;
        return R.layout.pop_window_app_update_online;
    }

    public AppUpdateOnlinePopWindow(Activity activity, AppInfoBean appInfo){
        super(activity);
        this.appInfo = appInfo;
    }

    public void initView(){
        helper = new AppUpdateOnlineHelper(mContext);
        progressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_cancel)
    public void tv_cancel_Click(View view){
        close();
    }

    @OnClick(R.id.tv_install)
    public void tv_install_Click(View view){
        if (TextUtils.isEmpty(appInfo.channel.url)) {
            return;
        }
        if (appInfo.channel.urlType == ChannelInfoBean.UrlType.Apk.ordinal()) {
            progressBar.setVisibility(View.VISIBLE);
            helper.downloadFile(appInfo.channel.url, new AppUpdateOnlineHelper.OnDownloadListener() {
                @Override
                public void onDownloadSuccess() {
                    helper.install();
                }
                @Override
                public void onDownloading(int progress) {
                    progressBar.setProgress(progress);
                }
                @Override
                public void onDownloadFailed() {
                    ToastUtils.showToast(mContext, "下载失败");
                }
            });
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("title", "");
            bundle.putString("url", appInfo.channel.url);
            ActivityManagerUtils.startActivity(mContext, WebViewFragment.class, bundle);
        }
    }
}
