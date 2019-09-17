package uni.yuansfer.payplugin;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import uni.yuansfer.payplugin.api.ApiService;
import uni.yuansfer.payplugin.api.ApiUrl;
import uni.yuansfer.payplugin.model.AlipayResultResp;
import uni.yuansfer.payplugin.model.JSOrderDetailResult;
import uni.yuansfer.payplugin.model.JSRefundResult;
import uni.yuansfer.payplugin.model.OrderDetailReq;
import uni.yuansfer.payplugin.model.OrderDetailResp;
import uni.yuansfer.payplugin.model.OrderDetailResultResp;
import uni.yuansfer.payplugin.model.PrepayReq;
import uni.yuansfer.payplugin.model.RefundReq;
import uni.yuansfer.payplugin.model.RefundResultResp;
import uni.yuansfer.payplugin.model.WechatResp;
import uni.yuansfer.payplugin.model.WechatResultResp;
import uni.yuansfer.payplugin.okhttp.FastjsonResponseHandler;
import uni.yuansfer.payplugin.pay.IPayStrategy;
import uni.yuansfer.payplugin.pay.PayItem;
import uni.yuansfer.payplugin.model.JSPrepayResult;
import uni.yuansfer.payplugin.pay.PayType;
import uni.yuansfer.payplugin.pay.alipay.AlipayItem;
import uni.yuansfer.payplugin.pay.alipay.AlipayStrategy;
import uni.yuansfer.payplugin.pay.wxpay.WxPayItem;
import uni.yuansfer.payplugin.pay.wxpay.WxPayStrategy;
import uni.yuansfer.payplugin.util.Constants;
import uni.yuansfer.payplugin.util.YuansferException;

/**
 * @Author Fly-Android
 * @CreateDate 2019/5/23 16:43
 * @Desciption yuansfer pay api base web-app
 */
public class YuansferPayModule extends WXSDKEngine.DestroyableModule {

    public static boolean sDebug;

    /**
     * set test env
     *
     * @param jsonObject{debug:true}
     */
    @JSMethod(uiThread = false)
    public void setDebug(JSONObject jsonObject) {
        sDebug = jsonObject.getBoolean("debug");
        if (sDebug) {
            ApiUrl.setTestMode();
        }
    }

    /**
     * 预下单(order prepay)
     *
     * @param jsonObject {
     *                   merchantNo:'200043',
     *                   storeNo:'300014',
     *                   token:'4dc2f2281d1f51fe137eafb914106524',
     *                   payType:1, //1支付宝2微信
     *                   amount:0.01,
     *                   ipnUrl:'http://www.yuansfer.com',
     *                   reference: '12345',
     *                   note:'note',
     *                   description:'description',
     *                   terminal:'APP',
     *                   currency:'USD'}
     * @param jsCallback {
     *                   payType:1, //1支付宝2微信
     *                   retCode:200, //200成功201失败202取消
     *                   retMsg: "payment success"   // 结果描述
     *                   }
     */
    @JSMethod(uiThread = true)
    public void prepay(JSONObject jsonObject, final JSCallback jsCallback) {
        PrepayReq prepayInfo = JSON.parseObject(jsonObject.toJSONString(), PrepayReq.class);
        if (prepayInfo.getPayType() == PayType.ALIPAY) {
            ApiService.prepay(mWXSDKInstance.getContext(), prepayInfo.getToken(), prepayInfo, new FastjsonResponseHandler<AlipayResultResp>() {
                @Override
                public void onFailure(int statusCode, String errorMsg) {
                    jsCallback.invoke(new JSPrepayResult(PayType.ALIPAY, Constants.JS_CALL_FAIL_CODE, errorMsg));
                }

                @Override
                public void onSuccess(int statusCode, AlipayResultResp response) {
                    if (Constants.API_CALL_SUCCESS_CODE.equals(response.getRet_code())) {
                        startPay(new AlipayItem(response.getResult().getPayInfo()), jsCallback);
                    } else {
                        jsCallback.invoke(new JSPrepayResult(PayType.ALIPAY, Constants.JS_CALL_FAIL_CODE, response.getRet_msg()));
                    }
                }
            });
        } else {
            ApiService.prepay(mWXSDKInstance.getContext(), prepayInfo.getToken(), prepayInfo, new FastjsonResponseHandler<WechatResultResp>() {
                @Override
                public void onFailure(int statusCode, String errorMsg) {
                    jsCallback.invoke(new JSPrepayResult(PayType.ALIPAY, Constants.JS_CALL_FAIL_CODE, errorMsg));
                }

                @Override
                public void onSuccess(int statusCode, WechatResultResp response) {
                    if (Constants.API_CALL_SUCCESS_CODE.equals(response.getRet_code())) {
                        WechatResp wechatInfo = response.getResult();
                        WxPayStrategy.newInstance(mWXSDKInstance.getContext(), wechatInfo.getAppid());
                        PayItem payItem = new WxPayItem.Builder()
                                .setAppId(wechatInfo.getAppid())
                                .setPackageValue(wechatInfo.getPackageName())
                                .setPrepayId(wechatInfo.getPrepayid())
                                .setPartnerId(wechatInfo.getPartnerid())
                                .setNonceStr(wechatInfo.getNoncestr())
                                .setSign(wechatInfo.getSign())
                                .setTimestamp(wechatInfo.getTimestamp()).build();
                        startPay(payItem, jsCallback);
                    } else {
                        jsCallback.invoke(new JSPrepayResult(PayType.ALIPAY, Constants.JS_CALL_FAIL_CODE, response.getRet_msg()));
                    }
                }
            });
        }
    }

    /**
     * 退款(order refund)
     *
     * @param jsonObject {
     *                   merchantNo:'200043',
     *                   storeNo:'300014',
     *                   token:'4dc2f2281d1f51fe137eafb914106524',
     *                   amount:amount,
     *                   reference: reference},
     * @param jsCallback { retCode:200, //200成功201失败202取消
     *                   retMsg: "refund success"   // 结果描述
     *                   result: object:{
     *                   "amount": "0.01",
     *                   "status": "success",
     *                   "currency": "CNY",
     *                   "reference": "44444",
     *                   "refundAmount": "0.01",
     *                   "refundTransactionId": "297245675773380538",
     *                   "refundReference": "123123",
     *                   "oldTransactionId": "297245675773319174"
     *                   }
     *                   }
     */
    @JSMethod(uiThread = true)
    public void refund(JSONObject jsonObject, final JSCallback jsCallback) {
        final RefundReq refundInfo = JSON.parseObject(jsonObject.toJSONString(), RefundReq.class);
        if (!TextUtils.isEmpty(refundInfo.getReference()) && !TextUtils.isEmpty(refundInfo.getTransactionNo())) {
            jsCallback.invoke(new JSRefundResult(Constants.JS_CALL_FAIL_CODE, mWXSDKInstance.getContext().getString(R.string.refund_params_error)));
            return;
        }
        if (refundInfo.getAmount() > 0.00D && refundInfo.getRmbAmount() > 0.00D) {
            jsCallback.invoke(new JSRefundResult(Constants.JS_CALL_FAIL_CODE, mWXSDKInstance.getContext().getString(R.string.refund_amount_error)));
            return;
        }
        ApiService.refund(mWXSDKInstance.getContext(), refundInfo.getToken(), refundInfo, new FastjsonResponseHandler<RefundResultResp>() {
            @Override
            public void onFailure(int statusCode, String errorMsg) {
                jsCallback.invoke(new JSRefundResult(Constants.JS_CALL_FAIL_CODE, errorMsg));
            }

            @Override
            public void onSuccess(int statusCode, RefundResultResp response) {
                if (Constants.API_CALL_SUCCESS_CODE.equals(response.getRet_code())) {
                    JSRefundResult result = new JSRefundResult(Constants.JS_CALL_SUCCESS_CODE, response.getRet_msg());
                    result.setResult(response.getResult());
                    jsCallback.invoke(result);
                } else {
                    jsCallback.invoke(new JSRefundResult(Constants.JS_CALL_FAIL_CODE, response.getRet_msg()));
                }
            }
        });
    }

    /**
     * 查询订单(query order)
     *
     * @param jsonObject  {
     *                    merchantNo:'200043',
     *                    storeNo:'300014',
     *                    token:'4dc2f2281d1f51fe137eafb914106524',
     *                    reference: reference}
     * @param jsCallback{ retCode:200, //200成功201失败202取消
     *                    retMsg: "query success"   // 结果描述
     *                    result: object:{
     *                    "reference": "test20180801006",
     *                    "transactionId": "297553569604475564",
     *                    "amount": "2.00",
     *                    "refundInfo":[
     *                    {
     *                    "refundTransactionId": "297553569604658581",
     *                    "refundAmount": "1.50"
     *                    },
     *                    {
     *                    "refundTransactionId": "297553569604680588",
     *                    "refundAmount": "0.50"
     *                    }
     *                    ],
     *                    "currency": "CNY",
     *                    "status": "success"
     *                    }
     *                    }
     */
    @JSMethod(uiThread = true)
    public void queryOrder(JSONObject jsonObject, final JSCallback jsCallback) {
        OrderDetailReq orderReq = JSON.parseObject(jsonObject.toJSONString(), OrderDetailReq.class);
        if (!TextUtils.isEmpty(orderReq.getReference()) && !TextUtils.isEmpty(orderReq.getTransactionNo())) {
            jsCallback.invoke(new JSOrderDetailResult(Constants.JS_CALL_FAIL_CODE, mWXSDKInstance.getContext().getString(R.string.target_param_no_error)));
            return;
        }
        ApiService.orderStatus(mWXSDKInstance.getContext(), orderReq.getToken(), orderReq, new FastjsonResponseHandler<OrderDetailResultResp>() {
            @Override
            public void onFailure(int statusCode, String errorMsg) {
                jsCallback.invoke(new JSOrderDetailResult(Constants.JS_CALL_FAIL_CODE, errorMsg));
            }

            @Override
            public void onSuccess(int statusCode, OrderDetailResultResp response) {
                if (Constants.API_CALL_SUCCESS_CODE.equals(response.getRet_code())) {
                    JSOrderDetailResult result = new JSOrderDetailResult(Constants.JS_CALL_SUCCESS_CODE, response.getRet_msg());
                    result.setResult(response.getResult());
                    jsCallback.invoke(result);
                } else {
                    jsCallback.invoke(new JSRefundResult(Constants.JS_CALL_FAIL_CODE, response.getRet_msg()));
                }
            }
        });
    }

    /**
     * 发起支付(start pay)
     *
     * @param payItem
     * @param jsCallback
     */
    private void startPay(PayItem payItem, JSCallback jsCallback) {
        Context context = mWXSDKInstance.getContext();
        if (payItem.getPayType() == PayType.ALIPAY) {
            if (!(payItem instanceof AlipayItem)) {
                if (YuansferPayModule.sDebug) {
                    Log.d(Constants.SDK_TAG, context.getString(R.string.pay_item_wrong));
                }
                throw new YuansferException(context.getString(R.string.pay_item_wrong));
            }
            IPayStrategy<AlipayItem> payStrategy = new AlipayStrategy();
            if (context instanceof Activity) {
                payStrategy.startPay((Activity) context, (AlipayItem) payItem, jsCallback);
            }
        } else if (payItem.getPayType() == PayType.WXPAY) {
            if (!(payItem instanceof WxPayItem)) {
                if (YuansferPayModule.sDebug) {
                    Log.d(Constants.SDK_TAG, context.getString(R.string.pay_item_wrong));
                }
                throw new YuansferException(context.getString(R.string.pay_item_wrong));
            }
            IPayStrategy<WxPayItem> payStrategy = WxPayStrategy.getInstance();
            if (context instanceof Activity) {
                payStrategy.startPay((Activity) context, (WxPayItem) payItem, jsCallback);
            }
        } else {
            throw new YuansferException(context.getString(R.string.pay_type_not_support));
        }
    }

    @Override
    public void destroy() {

    }

}
