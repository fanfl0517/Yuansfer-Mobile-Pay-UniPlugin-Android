package uni.yuansfer.payplugin.pay.wxpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.taobao.weex.bridge.JSCallback;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import uni.yuansfer.payplugin.R;
import uni.yuansfer.payplugin.YuansferPayModule;
import uni.yuansfer.payplugin.pay.IPayStrategy;
import uni.yuansfer.payplugin.model.JSPrepayResult;
import uni.yuansfer.payplugin.pay.PayType;
import uni.yuansfer.payplugin.util.Constants;
import uni.yuansfer.payplugin.util.YuansferException;


/**
 * @Author Fly-Android
 * @CreateDate 2019/5/23 12:01
 * @Desciption 微信支付
 */
public class WxPayStrategy implements IPayStrategy<WxPayItem> {

    private IWXAPI sWxAPI;
    private static WxPayStrategy sInstance;
    private static Context sContext;
    private JSCallback jsCallback;

    private WxPayStrategy(Context context, String appId) {
        if (context instanceof Activity) {
            sContext = context.getApplicationContext();
        }
        sWxAPI = WXAPIFactory.createWXAPI(context, appId);
        sWxAPI.registerApp(appId);
    }

    /**
     * 获取实例
     *
     * @return WxPayStrategy实例
     */
    public static WxPayStrategy getInstance() {
        requireNonNull(sInstance, sContext.getString(R.string.wx_pay_not_init));
        return sInstance;
    }

    /**
     * 创建实例
     *
     * @param context 上下文,applicationContext
     * @param appId   应用id
     * @return WxPayStrategy实例
     */
    public static WxPayStrategy newInstance(Context context, String appId) {
        if (sInstance == null) {
            synchronized (WxPayStrategy.class) {
                if (sInstance == null) {
                    sInstance = new WxPayStrategy(context, appId);
                }
            }
        }
        return sInstance;
    }

    /**
     * 处理回调Intent
     *
     * @param intent       意图
     * @param eventHandler 事件handler
     */
    public void handleIntent(Intent intent, IWXAPIEventHandler eventHandler) {
        requireNonNull(sWxAPI, sContext.getString(R.string.wx_not_register));
        sWxAPI.handleIntent(intent, eventHandler);
    }

    /**
     * 是否支付微信支付
     *
     * @return true支持
     */
    private boolean isSupportWxPay() {
        return sWxAPI.isWXAppInstalled() && sWxAPI.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    /**
     * 判空
     *
     * @param obj     对象
     * @param message 空提示
     * @param <T>     原对象
     * @return
     */
    private static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new YuansferException(message);
        }
        return obj;
    }

    /**
     * 调起微信应用发起支付
     *
     * @param activity Context
     * @param request  WxPayItem实例
     */
    @Override
    public void startPay(Activity activity, WxPayItem request, JSCallback jsCallback) {
        requireNonNull(sWxAPI, sContext.getString(R.string.wx_not_register));
        this.jsCallback = jsCallback;
        if (!isSupportWxPay()) {
            if (YuansferPayModule.sDebug) {
                Log.d(Constants.SDK_TAG, sContext.getString(R.string.wx_not_support));
            }
            WxPayStrategy.getInstance().rebackPayResult(Constants.JS_CALL_FAIL_CODE, sContext.getString(R.string.wx_not_support));
            return;
        }
        boolean ret = sWxAPI.sendReq(request.getPayReq());
        if (YuansferPayModule.sDebug) {
            Log.d(Constants.SDK_TAG, "wxpay started:" + ret);
        }
    }

    public void rebackPayResult(int retCode, String retMsg) {
        if (jsCallback != null) {
            jsCallback.invoke(new JSPrepayResult(PayType.WXPAY, retCode, retMsg));
        }
    }

}
