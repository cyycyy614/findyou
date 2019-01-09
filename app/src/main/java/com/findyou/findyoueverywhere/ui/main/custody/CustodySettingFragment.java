package com.findyou.findyoueverywhere.ui.main.custody;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiChild;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.bean.ChildInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.ui.main.custody.popwindow.UnbindDeviceClientPopWindow;
import com.findyou.findyoueverywhere.ui.main.custody.setting.CustodySettingItem9Fragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.child.EditChildSettingItem1Fragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.CustodySettingItem2ListFragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.CustodySettingItem3Fragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.CustodySettingItem4Fragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.CustodySettingItem6Fragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.CustodySettingItem7Fragment;
import com.findyou.findyoueverywhere.ui.main.custody.setting.CustodySettingItem8Fragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.PicUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.findyou.findyoueverywhere.utils.log.log;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustodySettingFragment extends BaseFragment {

    @BindView(R.id.iv_headimage)
    CircleImageView iv_headimage;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    private int childid;
    private String name;
    private String imei;
    private String headimage;
    private String deviceId;
    private String device_address;

    public int getLayoutId() {
        setTitle("被监护人设置");
        registerEvent();
        return R.layout.fragment_custody_setting;
    }

    public void initView(){
        Bundle bundle = getArguments();
        childid = bundle.getInt("id");
        name = bundle.getString("name");
        imei = bundle.getString("imei");
        headimage = bundle.getString("headimage");
        deviceId = bundle.getString("deviceId");
        device_address = bundle.getString("device_address");
        PicUtils.loadImage(getContext(), headimage, iv_headimage);
        tv_nickname.setText(name);
    }

    @OnClick(R.id.lin_item_1)
    public void lin_item_1_Click(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("id", childid);
        ActivityManagerUtils.startActivity(getContext(), EditChildSettingItem1Fragment.class, bundle);
    }

    @OnClick(R.id.lin_item_2)
    public void lin_item_2_Click(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("id", childid);
        bundle.putString("name", name);
        ActivityManagerUtils.startActivity(getContext(), CustodySettingItem2ListFragment.class, bundle);
    }

    @OnClick(R.id.lin_item_3)
    public void lin_item_3_Click(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("id", childid);
        bundle.putString("name", name);
        ActivityManagerUtils.startActivity(getContext(), CustodySettingItem3Fragment.class, bundle);
    }

    @OnClick(R.id.lin_item_4)
    public void lin_item_4_Click(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("id", childid);
        bundle.putString("name", name);
        bundle.putString("imei", imei);
        bundle.putString("deviceId", deviceId);
        bundle.putString("device_address", device_address);
        ActivityManagerUtils.startActivity(getContext(), CustodySettingItem4Fragment.class, bundle);
    }

    @OnClick(R.id.lin_item_5)
    public void lin_item_5_Click(View view){
        UnbindDeviceClientPopWindow popWindow = new UnbindDeviceClientPopWindow(getActivity(), childid, name, deviceId);
        popWindow.showFromCenter();
    }

    @OnClick(R.id.lin_item_6)
    public void lin_item_6_Click(View view){
        //终端设置
        Bundle bundle = new Bundle();
        bundle.putInt("id", childid);
        bundle.putString("name", name);
        ActivityManagerUtils.startActivity(getContext(), CustodySettingItem6Fragment.class, bundle);
    }

    @OnClick(R.id.lin_item_7)
    public void lin_item_7_Click(View view){
        //报警设置
        Bundle bundle = new Bundle();
        bundle.putInt("id", childid);
        ActivityManagerUtils.startActivity(getContext(), CustodySettingItem7Fragment.class, bundle);
    }

    @OnClick(R.id.lin_item_8)
    public void lin_item_8_Click(View view){
        //电子围栏
        Bundle bundle = new Bundle();
        bundle.putInt("id", childid);
        ActivityManagerUtils.startActivity(getContext(), CustodySettingItem8Fragment.class, bundle);
    }

    @OnClick(R.id.lin_item_9)
    public void lin_item_9_Click(View view){
        //历史报警
        Bundle bundle = new Bundle();
        bundle.putInt("id", childid);
        ActivityManagerUtils.startActivity(getContext(), CustodySettingItem9Fragment.class, bundle);
    }

    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_CHANGE_IMEI:
                updateChildInfo();
                break;
            case EventConst.API_UNBIND_DEVICE:
                if(getActivity() != null) {
                    getActivity().finish();
                }
                break;
            case EventConst.API_UPDATE_CHILD_SUCCESSFUL:
                updateChildInfo();
                break;
        }
    }

    public void updateChildInfo(){
        ApiChild.getItem(childid, (res1)->{
            ChildInfoBean info = JsonUtils.convert(res1.data, ChildInfoBean.class);
            if(info == null){
                return;
            }
            headimage = info.headimage;
            PicUtils.loadImage(getContext(),info.headimage, iv_headimage);
            name = info.name;
            device_address = info.address;
            deviceId = info.deviceId;
            imei = info.imei;
        });
    }
}
