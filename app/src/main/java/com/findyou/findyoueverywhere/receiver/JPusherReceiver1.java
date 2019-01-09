//package com.findyou.findyoueverywhere.receiver;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import com.blankj.utilcode.util.AppUtils;
//import com.blankj.utilcode.util.Utils;
//import cn.jpush.android.api.JPushInterface;
//
///**
// * Created by Administrator on 2016/7/1 0001.
// */
//// 极光 推送 的 广播接受者
//public class JPusherReceiver extends BroadcastReceiver {
//    private static final String TAG = "JPusherReceiver";
//
//    private int type;
//
//    public void onReceive(Context context, Intent intent) {
//        Bundle bundle = intent.getExtras();
////        String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
////        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
////        int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
////        boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//
//        String action = intent.getAction();
//        if (Const.DBG) {
//            NSLog.w(TAG, "JPusherReceiver{} ... onReceive() --> bundle = " + bundle + "; action = " + action);
//        }
//
//        if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {
//            NSLog.w(TAG, " handled intent - " + action);
//        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
//            NSLog.w(TAG, "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
//        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {
//            NSLog.w(TAG, "====== 收到了通知 ======");
//            // 在这里可以做些统计，或者做些其他工作
//        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
//            NSLog.w(TAG, "====== 用户点击打开了通知 ======");
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            ResponseInfo responseInfo = new ResponseInfo(extras);
//            int pushType = responseInfo.getInt("pushType");
//            if (pushType == 1) {
//                //观看
//                String uid = responseInfo.getString("uid");
//                String videoFlv = responseInfo.getString("videoFlv");
//                type = responseInfo.getInt("type");
//
//                onWatchLive(context, uid, videoFlv);
//            } else if (pushType == 2) {
//                //开播
//                onPlay(context);
//            } else {
//
//                if (!AppUtils.isAppForeground()) {
//                    AppUtils.launchApp(Utils.getApp().getPackageName());
//                }
//            }
//
//        } else {
//            app.log("Unhandled intent:" + action);
//        }
//    }
//
//
//    // 打印所有的 intent extra 数据
////    private static String printBundle(Bundle bundle) {
////        StringBuilder sb = new StringBuilder();
////        for (String key : bundle.keySet()) {
////            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
////                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
////            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
////                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
////            }
////            else {
////                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
////            }
////        }
////        return sb.toString();
////    }
//
//    private void onPlay(Context context) {
////        //开播前的处理
////        handerActivity();
////        //开始开启新的页面
////        Intent mainIntent = new Intent(context, MainActivity.class);
////        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        Intent liveRoomIntent = new Intent(context, LiveRoomActivity.class);
////        Intent[] intents = {mainIntent, liveRoomIntent};
////        context.startActivities(intents);
//
//
//        ApiLive.dogetLiveinfo(new RxSubscribe<LiveInfoBean>() {
//
//            @Override
//            protected void _onNext(LiveInfoBean liveInfoBean) {
//                if (null == liveInfoBean) {
//                    ToastUtil.showShortToastSafe(context.getString(R.string.get_live_info_fatal));
//                    return;
//                }
//                //开播前的处理
//                handerActivity();
//                //开始开启新的页面
//                Intent mainIntent = new Intent(context, MainActivity.class);
//                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Intent liveRoomIntent = new Intent(context, LiveRoomActivityV2.class);
////                liveRoomIntent.putExtra("liveInfoBean", liveInfoBean);
//                //----------------
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("liveInfoBean", liveInfoBean);
//                liveRoomIntent.putExtra("liveInfoBean_bundle", bundle);
//                //----------------
//                Intent[] intents = {mainIntent, liveRoomIntent};
//                context.startActivities(intents);
//                RoomInfoUtil.setShareUrl(liveInfoBean.shareUrl + "?uid=" + liveInfoBean.roomId);
//            }
//        });
//    }
//
//    public interface GetUserInfoCallback {
//        public void onSuccess(UserInfo info);
//    }
//
//    public void requestUserInfo(String uid, final GetUserInfoCallback callback) {
//        UserInfoRequest.requestUserInfo(uid, new UserInfoRequest.CallBack() {
//            @Override
//            public void success(Object obj) {
//                if (obj != null) {
//                    UserInfo userInfo = (UserInfo) obj;
//                    callback.onSuccess(userInfo);
//                }
//            }
//
//            @Override
//            public void neterror(int errorCode, String message) {
//            }
//
//            @Override
//            public void error(int errCode) {
//            }
//        });
//    }
//
//    private void handerActivity() {
//        //开播前的处理
//        Activity currentActivity = ActivityManagerUtil.getInstance().getTopActivity();
//        if (currentActivity instanceof LiveRoomActivityV2) {
//            LiveRoomActivityV2 a = (LiveRoomActivityV2) currentActivity;
////            a.jpushReceiver();
////            a.closeLiveRoom();
//            a.closeLiveRoom(false);
//        } else if (currentActivity instanceof LiveWatchActivityV2) {
//            LiveWatchActivityV2 a = (LiveWatchActivityV2) currentActivity;
////            a.jpushReceiver();
//            a.iv_closeRoom_Clicked();
//        }
//    }
//
//    private void onWatchLive(final Context context, String uid, final String videoFlv) {
//        requestUserInfo(uid, new GetUserInfoCallback() {
//            @Override
//            public void onSuccess(UserInfo userInfo) {
//
//                handerActivity();
//
//                Intent mainIntent = new Intent(context, MainActivity.class);
//                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                Intent intent = new Intent(context, LiveWatchActivityV2.class);
////                intent.putExtra("userInfo", userInfo);
////                intent.putExtra("liveDomin", "");
////                intent.putExtra("videoFlv", videoFlv);
////                intent.putExtra("liveImg", userInfo.livimage);
////                intent.putExtra("userCount", 0);
//
//                //--------------
//                RecommendCellBean recommendCellBean = new RecommendCellBean();
////                recommendCellBean.uid = userInfo.uid;
//                recommendCellBean.uid = Integer.parseInt(userInfo.uid);
////                recommendCellBean.videoFlv = userInfo.videoFlv;
//                recommendCellBean.mobileliveimg = userInfo.livimage;
//                recommendCellBean.headimage = userInfo.headimage;
//                recommendCellBean.type = type;
//
//                intent.putExtra("recommendCellBean", recommendCellBean);
//                //--------------
//
//
//                Intent[] intents = {mainIntent, intent};
//                context.startActivities(intents);
//            }
//        });
//    }
//}
