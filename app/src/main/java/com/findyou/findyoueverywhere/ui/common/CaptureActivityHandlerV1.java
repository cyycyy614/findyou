//package com.findyou.findyoueverywhere.ui.common;
//
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Message;
//
//import com.google.zxing.Result;
//import com.yzq.zxinglibrary.android.CaptureActivity;
//import com.yzq.zxinglibrary.camera.CameraManager;
//import com.yzq.zxinglibrary.decode.DecodeThread;
//import com.yzq.zxinglibrary.view.ViewfinderResultPointCallback;
//
//public final class CaptureActivityHandlerV1 extends Handler {
//    private static final String TAG = CaptureActivityHandlerV1.class.getSimpleName();
//    private final ScanQRcodeActivityV2 activity;
//    private final DecodeThread decodeThread;
//    private CaptureActivityHandlerV1.State state;
//    private final CameraManager cameraManager;
//
//    public CaptureActivityHandlerV1(ScanQRcodeActivityV2 activity, CameraManager cameraManager) {
//        this.activity = activity;
//        this.decodeThread = new DecodeThread(activity, new ViewfinderResultPointCallback(activity.getViewfinderView()));
//        this.decodeThread.start();
//        this.state = CaptureActivityHandlerV1.State.SUCCESS;
//        this.cameraManager = cameraManager;
//        cameraManager.startPreview();
//        this.restartPreviewAndDecode();
//    }
//
//    public void handleMessage(Message message) {
//        switch(message.what) {
//            case 2:
//                this.state = CaptureActivityHandlerV1.State.PREVIEW;
//                this.cameraManager.requestPreviewFrame(this.decodeThread.getHandler(), 1);
//                break;
//            case 3:
//                this.state = CaptureActivityHandlerV1.State.SUCCESS;
//                this.activity.handleDecode((Result)message.obj);
//            case 4:
//            case 5:
//            default:
//                break;
//            case 6:
//                this.restartPreviewAndDecode();
//                break;
//            case 7:
//                this.activity.setResult(-1, (Intent)message.obj);
//                this.activity.finish();
//                break;
//            case 8:
//                this.activity.switchFlashImg(8);
//                break;
//            case 9:
//                this.activity.switchFlashImg(9);
//        }
//
//    }
//
//    public void quitSynchronously() {
//        this.state = CaptureActivityHandlerV1.State.DONE;
//        this.cameraManager.stopPreview();
//        Message quit = Message.obtain(this.decodeThread.getHandler(), 5);
//        quit.sendToTarget();
//
//        try {
//            this.decodeThread.join(500L);
//        } catch (InterruptedException var3) {
//            ;
//        }
//
//        this.removeMessages(3);
//        this.removeMessages(2);
//    }
//
//    public void restartPreviewAndDecode() {
//        if (this.state == CaptureActivityHandlerV1.State.SUCCESS) {
//            this.state = CaptureActivityHandlerV1.State.PREVIEW;
//            this.cameraManager.requestPreviewFrame(this.decodeThread.getHandler(), 1);
//            this.activity.drawViewfinder();
//        }
//
//    }
//
//    private static enum State {
//        PREVIEW,
//        SUCCESS,
//        DONE;
//
//        private State() {
//        }
//    }
//}
