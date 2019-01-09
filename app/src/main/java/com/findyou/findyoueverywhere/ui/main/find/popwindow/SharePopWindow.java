package com.findyou.findyoueverywhere.ui.main.find.popwindow;

import android.app.Activity;
import android.view.View;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.base.CommonCallback2;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.utils.json.JObject;
import com.findyou.findyoueverywhere.utils.sdk.sina.content.ShareContent;
import com.findyou.findyoueverywhere.utils.share.ShareChannel;
import com.findyou.findyoueverywhere.utils.share.ShareUtils;

import butterknife.OnClick;

public class SharePopWindow extends BasePopWindow {
    private int type;

    public SharePopWindow(Activity context, int type) {
        super(context);
        this.type = type;
    }

    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate

    public int getLayoutId() {
        mIsOutsideClose = false;
        mIsShowBackground = true;
        return R.layout.share_pop_window;
    }

    public void initView(){
    }

    @OnClick(R.id.lin_share_weixin)
    public void lin_share_weixin_Click(View view){
        JObject jo = new JObject();
        jo.put("channel", 0);
        jo.put("type", this.type);
        postEvent(new EventMessenger(EventConst.API_STORY_ITEM_SHARE, jo.toString()));
    }

    @OnClick(R.id.lin_share_weixin_friends)
    public void lin_share_weixin_friends_Click(View view) {
        JObject jo = new JObject();
        jo.put("channel", 1);
        jo.put("type", this.type);
        postEvent(new EventMessenger(EventConst.API_STORY_ITEM_SHARE, jo.toString()));
    }

    @OnClick(R.id.lin_share_qq)
    public void lin_share_qq(View view){
        postEvent(new EventMessenger(EventConst.API_STORY_ITEM_SHARE,2));
    }

    @OnClick(R.id.lin_share_weibo)
    public void lin_share_weibo(View view){
        postEvent(new EventMessenger(EventConst.API_STORY_ITEM_SHARE,3));
    }
}
