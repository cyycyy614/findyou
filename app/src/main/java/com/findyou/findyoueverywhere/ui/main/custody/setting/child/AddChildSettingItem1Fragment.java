package com.findyou.findyoueverywhere.ui.main.custody.setting.child;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiChild;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.bean.ChildInfoBean;
import com.findyou.findyoueverywhere.bean.PropertyInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.controls.album.AlbumFragment;
import com.findyou.findyoueverywhere.ui.main.map.HomeFragment;
import com.findyou.findyoueverywhere.ui.user.edit.BirthdayPopWindow;
import com.findyou.findyoueverywhere.ui.user.popwindow.SexPopWindow;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.AndroidBug5497Workaround;
import com.findyou.findyoueverywhere.utils.BitmapUtils;
import com.findyou.findyoueverywhere.utils.LoadingUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.ToolUtils;
import com.findyou.findyoueverywhere.utils.http.HttpClient;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.findyou.findyoueverywhere.utils.log.log;
import com.findyou.findyoueverywhere.utils.photo.PhotoUpImageItem;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class AddChildSettingItem1Fragment extends BaseFragment {
    @BindView(R.id.tv_name)
    EditText tv_name;

    @BindView(R.id.tv_category)
    TextView tv_category;

    @BindView(R.id.tv_sex)
    TextView tv_sex;

    @BindView(R.id.tv_birthday)
    TextView tv_birthday;

    @BindView(R.id.tv_phone)
    EditText tv_phone;

    @BindView(R.id.tv_address)
    EditText tv_address;

    @BindView(R.id.tv_health)
    TextView tv_health;

    @BindView(R.id.tv_looks)
    TextView tv_looks;

    @BindView(R.id.tv_contact)
    TextView tv_contact;

    @BindView(R.id.tv_contact_phone)
    TextView tv_contact_phone;

    @BindView(R.id.iv_headimage)
    ImageView iv_headimage;

    @BindView(R.id.tv_imei)
    TextView tv_imei;

    private ChildInfoBean info;
    private int guardianid;
    private int REQUEST_CODE_SCAN = 1000;

    public int getLayoutId() {
        setTitle("添加被监护人");
        registerEvent();
        return R.layout.fragment_add_custody_user;
    }

    public void initSaveBar(){
        TextView textView = (TextView)getRightView(ViewType.TextView);
        if(textView != null){
            textView.setText("保存");
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setOnClickListener((View view)-> {
                onSubmit();
            });
        }
    }

    private List<String> category;
    private List<String> health;
    private List<String> looks;
    private List<String> sex;

    public void initView(){
        initSaveBar();
        info = new ChildInfoBean();
        category = new ArrayList<>();
        health = new ArrayList<>();
        looks = new ArrayList<>();
        sex = new ArrayList<>();
        sex.add("女");
        sex.add("男");
        AndroidBug5497Workaround.assistActivity(getActivity(), new AndroidBug5497Workaround.OnKeyboardToggleListener() {
            @Override
            public void onKeyboardShow(int keyboardSize) {
            }
            @Override
            public void onKeyboardHide(int keyboardSize) {
            }
        });

        ApiChild.getConstant((res)->{
            List<PropertyInfoBean> list = JsonUtils.convert_array(res.data, PropertyInfoBean.class);
            if(list == null){
                return;
            }
            for(int i=0; i<list.size(); i++){
                PropertyInfoBean item = list.get(i);
                if(item.type == 1){
                    //类别
                    category.add(item.name);
                }else if(item.type == 2){
                    //健康
                    health.add(item.name);
                }else if(item.type == 3){
                    //体貌
                    looks.add(item.name);
                }
            }
        });
    }

    @OnClick(R.id.layout_headimage)
    public void layout_headimage_Click(View view){
        ActivityManagerUtils.startActivity(getActivity(), AlbumFragment.class, null);
    }

    @OnClick(R.id.lin_category)
    public void lin_category_Click(View view){
        CategoryPopWindow popWindow = new CategoryPopWindow(getActivity(), category, 0);
        popWindow.showFromBottom();
    }

    @OnClick(R.id.lin_sex)
    public void lin_sex_Click(View view){
        SexPopWindow popWindow = new SexPopWindow(getActivity(), info.sex);
        popWindow.showFromBottom();
    }

    @OnClick(R.id.lin_birthday)
    public void lin_birthday_Click(View view){
        BirthdayPopWindow popWindow = new BirthdayPopWindow(getActivity(), info.birthday);
        popWindow.showFromBottom();
    }

    @OnClick(R.id.lin_health)
    public void lin_health_Click(View view){
        HealthPopWindow popWindow = new HealthPopWindow(getActivity(), health, 0);
        popWindow.showFromBottom();
    }

    @OnClick(R.id.lin_looks)
    public void lin_lin_looks(View view){
        LooksPopWindow popWindow = new LooksPopWindow(getActivity(), looks, 0);
        popWindow.showFromBottom();
    }

    @OnClick(R.id.lin_qr_code)
    public void lin_qr_code_Click(View view){
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
        info.imei = imei;
        if(tv_imei != null) {
            tv_imei.setText(info.imei);
        }
    }

    public void onSubmit(){
        if(TextUtils.isEmpty(info.headimage)){
            ToastUtils.showToast(getContext(), "请上传头像!");
            return;
        }
        if(TextUtils.isEmpty(tv_name.getText().toString())){
            ToastUtils.showToast(getContext(), "请输入姓名!");
            return;
        }
        if(info.category == -1){
            ToastUtils.showToast(getContext(), "请选择类别!");
            return;
        }
        if(info.sex == -1){
            ToastUtils.showToast(getContext(), "请选择性别!");
            return;
        }
        if(info.birthday.equals("0000-00-00")){
            ToastUtils.showToast(getContext(), "请选择生日!");
            return;
        }
        if(TextUtils.isEmpty(tv_imei.getText().toString())){
            ToastUtils.showToast(getContext(), "请扫描设备二维码!");
            return;
        }
        //必填
        if(!TextUtils.isEmpty(tv_phone.getText().toString())) {
            if (!ToolUtils.isPhoneNumber(tv_phone.getText().toString())) {
                ToastUtils.showToast(getContext(), "请输入有效的电话号码!");
                return;
            }
        }
        info.name = tv_name.getText().toString();
        info.category = info.category;
        info.imei = tv_imei.getText().toString();
        info.sex = info.sex;
        info.birthday = info.birthday;
        info.phone = tv_phone.getText().toString();
        info.address = tv_address.getText().toString();
        info.looks = info.looks;
        info.health = info.health;
        info.contact = tv_contact.getText().toString();
        info.contact_phone = tv_contact_phone.getText().toString();
        info.device_address = HomeFragment.addressName;
        ApiChild.add(info, (res)->{
            ToastUtils.showToast(getContext(), "被监护人添加成功!");
            postEvent(new EventMessenger(EventConst.API_ADD_CHILD_SUCCESSFUL));
            if(getActivity() != null) {
                getActivity().finish();
            }
        });
    }

    @Subscribe
    public void onEventMessage(EventMessenger event) {
        int index = 0;
        switch (event.from) {
            case EventConst.API_EDIT_SEX:
                int sex = Integer.parseInt(event.obj.toString());
                info.sex = sex;
                if(sex == 1){
                    if(tv_sex != null){
                        tv_sex.setText("男");
                    }
                }else {
                    if(tv_sex != null) {
                        tv_sex.setText("女");
                    }
                }
                break;
            case EventConst.API_EDIT_BIRTHDAY:
                info.birthday = event.obj.toString();
                if(tv_birthday != null) {
                    tv_birthday.setText(info.birthday);
                }
                break;
            case EventConst.API_EDIT_CHILD_CATEGORY:
                info.category = Integer.parseInt(event.obj.toString());
                String str = category.get(info.category);
                if(tv_category != null) {
                    tv_category.setText(str);
                }
                break;
            case EventConst.API_EDIT_CHILD_LOOKS:
                index = Integer.parseInt(event.obj.toString());
                info.looks = index;
                if(tv_looks != null) {
                    tv_looks.setText(looks.get(info.looks));
                }
                break;
            case EventConst.API_EDIT_CHILD_HEALTH:
                index = Integer.parseInt(event.obj.toString());
                info.health = index;
                if(tv_health != null) {
                    tv_health.setText(health.get(info.health));
                }
                break;
            case EventConst.API_SCAN_QR_CODE_SUCCESSFUL:
                String imei = event.obj.toString();
                info.imei = imei;
                if(tv_imei != null) {
                    tv_imei.setText(info.imei);
                }
                break;
            case EventConst.API_UPLOAD_FILES:
                if(event.obj == null){
                    return;
                }
                List<PhotoUpImageItem> list = JsonUtils.convert_array(event.obj.toString(), PhotoUpImageItem.class);
                int index1 = images.size() - 1;
                if(index1 < 0){
                    index1 = 0;
                }
                images.clear();
                images.addAll(index1, list);
                //LoadingUtils.show("上传头像...");
                uploadfiles();
                break;
        }
    }

    int uploadNumber = 0;
    private List<PhotoUpImageItem> images = new ArrayList<>();
    public void uploadfiles(){
        uploadNumber = 0;
        //上传图片
        for(int i=0; i<images.size(); i++){
            String image = images.get(i).imagePath;
            if(image == ""){
                continue;
            }
            try {
                String fileName = BitmapUtils.scale(image, 540, 540);
                BitmapUtils.compress(getContext(), fileName, 32, (obj) -> {
                    File upfile = (File) obj;
                    List<File> files = new ArrayList<>();
                    files.add(upfile);
                    Map<String,String> map = new HashMap<>();
                    map.put("uid", String.valueOf(app.me.uid));
                    map.put("fileName", ToolUtils.getGuid());
                    HttpClient.uploadImages(files, map, (res) -> {
                        if(res.data != null) {
                            info.headimage = res.data;
                            uploadNumber++;
                        }
                        if(uploadNumber == images.size()){
                            ToastUtils.showToast(getContext(), "保存成功!");
                            LoadingUtils.close();
                            if(iv_headimage != null){
                                iv_headimage.post(()->{
                                    Picasso.with(getContext()).load(upfile).into(iv_headimage);
                                });
                            }
                        }
                    });
                });
            }catch (Exception e){
                e.printStackTrace();
                LoadingUtils.close();
            }
        }
    }
}
