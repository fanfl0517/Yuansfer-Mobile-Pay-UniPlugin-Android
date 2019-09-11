package uni.yuansfer.payplugin.pay.alipay;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import com.alipay.sdk.app.PayTask;
import com.taobao.weex.bridge.JSCallback;

import java.util.Map;

import uni.yuansfer.payplugin.YuansferPayModule;
import uni.yuansfer.payplugin.pay.IPayStrategy;
import uni.yuansfer.payplugin.model.JSPrepayResult;
import uni.yuansfer.payplugin.pay.PayType;
import uni.yuansfer.payplugin.util.Constants;

/**
 * @Author Fly-Android
 * @CreateDate 2019/5/23 12:01
 * @Desciption 支付宝支付
 */
public class AlipayStrategy implements IPayStrategy<AlipayItem> {

    private static final int PAY_RESULT = 189;

    private static class HandleData {
        public JSCallback jsCallback;
        public Map<String, String> result;

        public HandleData(Map<String, String> result, JSCallback jsCallback) {
            this.result = result;
            this.jsCallback = jsCallback;
        }
    }

    @SuppressLint("HandlerLeak")
    private static Handler sHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PAY_RESULT: {
                    Map<String, String> payResultMap = ((HandleData) msg.obj).result;
                    JSCallback jsCallback = ((HandleData) msg.obj).jsCallback;
                    if (YuansferPayModule.sDebug) {
                        Log.d(Constants.SDK_TAG, "alipya result=" + payResultMap);
                    }
                    if (jsCallback == null) {
                        return;
                    }
                    String resultStatus = payResultMap.get("resultStatus");
                    // 状态码详见：https://global.alipay.com/doc/global/mobile_securitypay_pay_cn
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档:
                    //https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.IXE2Zj&treeId=59&articleId=103671&docType=1
                    if (TextUtils.equals(resultStatus, "9000")) {
                        jsCallback.invoke(new JSPrepayResult(PayType.ALIPAY, Constants.JS_CALL_SUCCESS_CODE, Constants.JS_PAYMENT_SUCCESS));
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // 6001为主动取消支付
                        if (TextUtils.equals(resultStatus, "6001")) {
                            jsCallback.invoke(new JSPrepayResult(PayType.ALIPAY, Constants.JS_CALL_CANCEL_CODE, Constants.JS_PAYMENT_CANCEL));
                        } else {
                            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            // 其他值就可以判断为支付失败，或者系统返回的错误
                            jsCallback.invoke(new JSPrepayResult(PayType.ALIPAY, Constants.JS_CALL_FAIL_CODE, payResultMap.get("memo")));
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void startPay(final Activity activity, final AlipayItem alipayItem, final JSCallback jsCallback) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(activity);
                // 调用支付接口，获取支付结果
                Map<String, String> result = payTask.payV2(alipayItem.getOrderInfo(), true);
                Message msg = new Message();
                msg.what = PAY_RESULT;
                msg.obj = new HandleData(result, jsCallback);
                sHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
        if (YuansferPayModule.sDebug) {
            Log.d(Constants.SDK_TAG, "alipay started");
        }
    }

}
