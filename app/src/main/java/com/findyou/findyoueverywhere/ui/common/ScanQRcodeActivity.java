//package com.findyou.findyoueverywhere.ui.common;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Vibrator;
//import android.support.v4.app.ActivityCompat;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
//import com.findyou.findyoueverywhere.R;
//import com.findyou.findyoueverywhere.base.BaseActivity;
//import com.findyou.findyoueverywhere.base.BaseFragment;
//import com.findyou.findyoueverywhere.base.BasePopWindow;
//import com.findyou.findyoueverywhere.base.BusManager;
//import com.findyou.findyoueverywhere.base.EventMessenger;
//import com.findyou.findyoueverywhere.constant.EventConst;
//
//import java.util.List;
//
//import butterknife.BindView;
//import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
//import cn.bingoogolapple.qrcode.core.BarcodeType;
//import cn.bingoogolapple.qrcode.core.QRCodeView;
//import cn.bingoogolapple.qrcode.zxing.ZXingView;
//import pub.devrel.easypermissions.AfterPermissionGranted;
//import pub.devrel.easypermissions.EasyPermissions;
//
//public class ScanQRcodeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks,QRCodeView.Delegate {
//    @BindView(R.id.zxingview)
//    ZXingView zxingview;
//
//    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
//    private static final int REQUEST_CODE_CAMERA = 999;
//    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
//
//    public int getLayoutId() {
//        return R.layout.scan_qrcode_fragment;
//    }
//
//    public void initView(){
//        zxingview.setDelegate(this);
//        //ViewGroup.LayoutParams layoutParams = zxingview.getCameraPreview().getLayoutParams();
//        //ViewGroup.LayoutParams layoutParams = zxingview.getScanBoxView().getLayoutParams();
//        //layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM);
//        //layoutParams.width = 200;
//        //zxingview.getScanBoxView().setLayoutParams(layoutParams);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        zxingview.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
////        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
//        //zxingview.startSpotAndShowRect(); // 显示扫描框，并且延迟0.1秒后开始识别
//        zxingview.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//        zxingview.setType(BarcodeType.ONLY_QR_CODE, null); // 只识别 QR_CODE
//        zxingview.startSpotAndShowRect(); // 显示扫描框，并且延迟0.1秒后开始识别
//    }
//
//    @Override
//    protected void onStop() {
//        zxingview.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
//        super.onStop();
//    }
//
//    @Override
//    public void onDestroy() {
//        zxingview.onDestroy(); // 销毁二维码扫描控件
//        super.onDestroy();
//    }
//
//    private void vibrate() {
//        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        vibrator.vibrate(200);
//    }
//
//    @Override
//    public void onScanQRCodeSuccess(String result) {
//        //Log.i(TAG, "result:" + result);
//        setTitle("扫描结果为：" + result);
//        vibrate();
//        //zxingview.startSpot(); // 延迟0.1秒后开始识别
//        BusManager.postEvent(new EventMessenger(EventConst.API_SCAN_QR_CODE_SUCCESSFUL, result));
//        finish();
//    }
//
//    @Override
//    public void onCameraAmbientBrightnessChanged(boolean isDark) {
//        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
//        String tipText = zxingview.getScanBoxView().getTipText();
//        if(tipText == null){
//            return;
//        }
//        String ambientBrightnessTip = "环境过暗，请打开闪光灯";
////        if (isDark) {
////            if (!tipText.contains(ambientBrightnessTip)) {
////                zxingview.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
////            }
////        } else {
////            if (tipText.contains(ambientBrightnessTip)) {
////                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
////                zxingview.getScanBoxView().setTipText(tipText);
////            }
////        }
//    }
//
//    @Override
//    public void onScanQRCodeOpenCameraError() {
//        //Log.e(TAG, "打开相机出错");
//        String str = "打开相机出错";
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        zxingview.startSpotAndShowRect(); // 显示扫描框，并且延迟0.1秒后开始识别
//
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
//            final String picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0);
//            // 本来就用到 QRCodeView 时可直接调 QRCodeView 的方法，走通用的回调
//            zxingview.decodeQRCode(picturePath);
//
//            /*
//            没有用到 QRCodeView 时可以调用 QRCodeDecoder 的 syncDecodeQRCode 方法
//            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
//            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github
//            .com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
//             */
////            new AsyncTask<Void, Void, String>() {
////                @Override
////                protected String doInBackground(Void... params) {
////                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
////                }
////
////                @Override
////                protected void onPostExecute(String result) {
////                    if (TextUtils.isEmpty(result)) {
////                        Toast.makeText(TestScanActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
////                    } else {
////                        Toast.makeText(TestScanActivity.this, result, Toast.LENGTH_SHORT).show();
////                    }
////                }
////            }.execute();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//        if (requestCode == REQUEST_CODE_CAMERA) {
//            zxingview.startCamera();
//            zxingview.startSpot();
//        }
//    }
//
//    @Override
//    public void onPermissionsGranted(int requestCode, List<String> perms) {
//    }
//
//    @Override
//    public void onPermissionsDenied(int requestCode, List<String> perms) {
//    }
//
//    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
//    private void requestCodeQRCodePermissions() {
//        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
//        if (!EasyPermissions.hasPermissions(this, perms)) {
//            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
//        }
//    }
//}
