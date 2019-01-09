package com.findyou.findyoueverywhere.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/5/22 0022.
 */
public class BaseFragment extends Fragment implements FragmentUserVisibleController.UserVisibleCallback
{
    private int layoutid = -1;
    private Unbinder unbinder;
    private FragmentUserVisibleController userVisibleController;
    protected View mView;
    protected boolean isRegisterBus = false;

    public BaseFragment() {
        userVisibleController = new FragmentUserVisibleController(this, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        userVisibleController.activityCreated();
        beforCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    public void beforCreate(Bundle savedInstanceState){
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        userVisibleController.activityCreated();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null){
            if(getLayoutId() == -1){
                mView = getDefaultView();
            }else {
                mView = inflater.inflate(getLayoutId(), container, false);
            }
        }else {
        }
        unbinder = ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    public View getDefaultView(){
        return new View(getContext());
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        mView = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        //取消总线
        unregisterEvent();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        userVisibleController.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        userVisibleController.pause();
        super.onPause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        userVisibleController.setUserVisibleHint(isVisibleToUser);
    }

    public int getLayoutId(){
        return layoutid;
    }

    public void initView(){
    }

    public void postEvent(EventMessenger event) {
        BusManager.postEvent(event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    public void onBackPressed() {
        //getActivity().onBackPressed();
        if(getActivity() != null) {
            getActivity().finish();
        }
    }

    public void setTitle(String title){
        Activity activity = getActivity();
        if(activity instanceof EmptyActivity){
            EmptyActivity emptyActivity = (EmptyActivity)activity;
            TextView tv_title = emptyActivity.getTitleView();
            if(tv_title != null){
                tv_title.setText(title);
            }
        }
    }

    public void setTitle(int resid){
        String title = getResources().getString(resid);
        setTitle(title);
    }

    public View getRightView(ViewType type){
        Activity activity = getActivity();
        View tv_right = null;
        if(activity instanceof EmptyActivity){
            EmptyActivity emptyActivity = (EmptyActivity)activity;
            tv_right = emptyActivity.getRightView(type);
        }
        return tv_right;
    }

    public void showTitleBar(boolean isShow){
        Activity activity = getActivity();
        if(activity instanceof EmptyActivity){
            EmptyActivity emptyActivity = (EmptyActivity)activity;
            emptyActivity.showTitleBar(isShow);
        }
    }

    public void showStatusBar(boolean isShow) {
        Activity activity = getActivity();
        if(activity instanceof EmptyActivity){
            EmptyActivity emptyActivity = (EmptyActivity)activity;
            emptyActivity.showStatusBar(isShow);
        }
    }

    public FrameLayout getTitleLayout(){
        Activity activity = getActivity();
        if(activity instanceof EmptyActivity){
            EmptyActivity emptyActivity = (EmptyActivity)activity;
            return emptyActivity.getTitleLayout();
        }
        return null;
    }

    public void onVisibleChanged(boolean isVisible){
    }

    public void registerEvent(){
        if(!isRegisterBus) {
            isRegisterBus = true;
            BusManager.register(this);
        }
    }

    public void unregisterEvent(){
        if(isRegisterBus){
            isRegisterBus = false;
            BusManager.unregister(this);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    //--------------------- FragmentUserVisibleController callback start --------------------------
    @Override
    public void setWaitingShowToUser(boolean waitingShowToUser) {
        userVisibleController.setWaitingShowToUser(waitingShowToUser);
    }

    @Override
    public boolean isWaitingShowToUser() {
        return userVisibleController.isWaitingShowToUser();
    }

    @Override
    public boolean isVisibleToUser() {
        return userVisibleController.isVisibleToUser();
    }

    @Override
    public void callSuperSetUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 当Fragment对用户可见或不可见的就会回调此方法，你可以在这个方法里记录页面显示日志或初始化页面
     * @param isVisibleToUser
     * @param invokeInResumeOrPause
     */
    @Override
    public void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        onVisibleChanged(isVisibleToUser);
    }
    //--------------------- FragmentUserVisibleController callback end --------------------------
}
