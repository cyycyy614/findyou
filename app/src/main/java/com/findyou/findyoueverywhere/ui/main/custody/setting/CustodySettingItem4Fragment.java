package com.findyou.findyoueverywhere.ui.main.custody.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiChild;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.ui.main.map.AddressOfMapFragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.log.log;
import com.squareup.otto.Subscribe;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class CustodySettingItem4Fragment extends BaseFragment {

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_imei)
    TextView tv_imei;

    @BindView(R.id.tv_address)
    TextView tv_address;

    private int id;
    private String name;
    private String imei;
    private String newImei = "";
    private String deviceId;
    private String device_address;

    public void initSaveBar(){
        TextView textView = (TextView)getRightView(ViewType.TextView);
        if(textView != null){
            textView.setText("完成");
            textView.setTextColor(Color.WHITE);
            textView.setOnClickListener((View view)-> {
                onSuhmit();
            });
        }
    }

    public int getLayoutId() {
        setTitle("更换终端");
        registerEvent();
        return R.layout.fragment_custody_setting_item4;
    }

    public void initView(){
        initSaveBar();
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        name = bundle.getString("name");
        imei = bundle.getString("imei");
        deviceId = bundle.getString("deviceId");
        device_address = bundle.getString("device_address");
        if(tv_name != null){
            tv_name.setText(name);
        }
        if(tv_imei != null){
            tv_imei.setText(imei);
        }
        if(tv_address != null){
            tv_address.setText(device_address);
        }
    }
    private int REQUEST_CODE_SCAN = 1000;
    @OnClick(R.id.lin_imei)
    public void lin_imei_Click(View view){
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE).permission(Permission.Group.CAMERA)
                .onGranted(permissions -> {
                    // Storage permission are allowed.
                    Intent intent = new Intent(getActivity(), CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                    ToastUtils.showToast(getContext(), "请开启相机跟SD卡存储权限!");
                })
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                //result.setText("扫描结果为：" + content);
                log.write(content);
                setQrCode(content);
            }
        }
    }

    public void setQrCode(String qrCode){
        String imei = qrCode; //event.obj.toString();
        newImei = imei;
        if(tv_imei != null) {
            tv_imei.setText(imei);
        }
    }

    @OnClick(R.id.lin_address)
    public void lin_address_Click(View view){
        ActivityManagerUtils.startActivity(getActivity(), AddressOfMapFragment.class, null);
    }

    public void onSuhmit(){
        if(TextUtils.isEmpty(newImei)){
            ToastUtils.showToast(getContext(), "请扫码获得新的设备码!");
            return;
        }
        ApiChild.changeImei(id, name, newImei, deviceId, device_address, (res)->{
            ToastUtils.showToast(getContext(), res.message);
            postEvent(new EventMessenger(EventConst.API_CHANGE_IMEI, newImei));
        });
    }

    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_SCAN_QR_CODE_SUCCESSFUL:
                String imei = event.obj.toString();
                newImei = imei;
                if(tv_imei != null) {
                    tv_imei.setText(imei);
                }
                break;
            case EventConst.API_GET_ADDRESS_FROM_BAIDU_MAP:
                String address = event.obj.toString();
                device_address = address;
                if(tv_address != null) {
                    tv_address.setText(address);
                }
                break;
        }
    }
}
