package com.findyou.findyoueverywhere.ui.main.find.popwindow;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiCommon;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.bean.StoryInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;
import razerdp.util.SimpleAnimationUtils;

public class OptPopWindow extends BasePopWindow {
    @BindView(R.id.lin_opt)
    LinearLayout lin_opt;

    @BindView(R.id.lin_like)
    LinearLayout lin_like;

    @BindView(R.id.lin_comment)
    LinearLayout lin_comment;

    private StoryInfoBean item;
    public OptPopWindow(Activity context, StoryInfoBean item) {
        super(context);
        this.item = item;
    }

    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate

    public int getLayoutId() {
        mIsOutsideClose = false;
        isDropDown = true;
        return R.layout.opt_pop_window;
    }

    public void initView(){
        lin_like.setOnClickListener((v)->{
            if(item.likes == null){
                item.likes = new ArrayList<>();
            }
            StoryInfoBean.LikeInfoBean likeInfoBean = item.getLikeBean();
            //app.me.nickname = "hello";
            likeInfoBean.uid = app.me.uid;
            likeInfoBean.name = app.me.nickname;
            boolean isfind = false;
            for(int i=0; i<item.likes.size(); i++){
                StoryInfoBean.LikeInfoBean bean = item.likes.get(i);
                if(app.me.uid == bean.uid){
                    isfind = true;
                    item.likes.remove(i);
                    break;
                }
            }
            if(!isfind){
                item.likes.add(likeInfoBean);
                ToastUtils.showToast(getContext(), "点赞成功!");
            }else{
                ToastUtils.showToast(getContext(), "取消点赞!");
            }
            String like = JsonUtils.toString(item.likes);
            ApiCommon.likeStoryItems(item.id, like, (res)->{
                postEvent(new EventMessenger(EventConst.API_STORY_LIKE_SUCCESSFUL, item.position));
                close();
            });
        });
    }

    public void showPopWindow(View view){
        mView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int width = mView.getMeasuredWidth();
        int space = UIUtils.dp2px(getContext(), 10);
        int xoff = 0 - width - space;
        int yspace = UIUtils.dp2px(getContext(), 1);
        int yoff = space;
        int px = UIUtils.dp2px(getContext(), 300);
        this.showPopWindow(view, xoff, 0, Gravity.NO_GRAVITY);

        ObjectAnimator a = ObjectAnimator.ofFloat(lin_opt, "translationX", width, 0);
        LinearInterpolator lir = new LinearInterpolator();
        a.setInterpolator(lir);
        a.setDuration(320);
        a.start();
    }

    @OnClick(R.id.lin_comment)
    public void lin_comment_Click(View view){
        postEvent(new EventMessenger(EventConst.API_STORY_COMMENT, item.position));
        close();
    }

    @OnClick(R.id.lin_share)
    public void lin_share_Click(View view){
        Activity activity = ActivityManagerUtils.getInstance().getCurrentActivity();
        SharePopWindow popWindow = new SharePopWindow(activity, item.type);
        popWindow.showFromBottom();
        close();
    }
}
