package com.findyou.findyoueverywhere.wxapi;

import android.content.Intent;
import android.text.TextUtils;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseActivity;
import com.findyou.findyoueverywhere.constant.SdkConst;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2016/8/16 0016.
 * 微信支付回调页面
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void initView() {
        api = WXAPIFactory.createWXAPI(this, SdkConst.WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        //app.log("onPayFinish errorCode:" + resp.errCode);
        int type = resp.getType();
        //app.log("resp.getType():" + resp.getType());
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0:
                    //请求用户数据
//                    User.getUserInfo(app.me.uid, new User.UserInfoCallBack() {
//                        @Override
//                        public void onResponse(com.douquhd.xiaotu.v2.model.UserInfo info) {
//                            if (info != null) {
//                                showToast("支付成功!");
//                                postEvent(new EventMessenger(EventMessenger.MSG_PAY_WEIXI_COMPLETED));
//                                finish();
//                            }
//                        }
//                        @Override
//                        public void onError(String errorMsg) {
//                            showToast(TextUtils.isEmpty(errorMsg) ? "验证失败,请重新登录" : errorMsg);
//                        }
//                    });
                    break;
                case -1:
                case -2:
                    ToastUtils.showToast(this,"支付失败!");
                    finish();
                    break;
                default:
                    break;
            }
            //errCode码值
            //0 ：支付成功
            //-1 ：发生错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
            //-2：用户取消 发生场景：用户不支付了，点击取消，返回APP。
        }
    }
}
