package uni.yuansfer.payplugin.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;


import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import uni.yuansfer.payplugin.YuansferPayModule;
import uni.yuansfer.payplugin.model.OrderDetailReq;
import uni.yuansfer.payplugin.model.ParamInfo;
import uni.yuansfer.payplugin.model.PrepayReq;
import uni.yuansfer.payplugin.model.RefundReq;
import uni.yuansfer.payplugin.okhttp.IResponseHandler;
import uni.yuansfer.payplugin.okhttp.LoggerInterceptor;
import uni.yuansfer.payplugin.okhttp.OkHttpUtils;
import uni.yuansfer.payplugin.okhttp.UnsafeSSLFactory;
import uni.yuansfer.payplugin.util.Constants;
import uni.yuansfer.payplugin.util.MD5;

/**
 * @Author Fly-Android
 * @CreateDate 2019/5/24 11:43
 * @Desciption Yuansfer api service
 */
public class ApiService {

    private static final int CONN_TIMEOUT = 15;
    private static final int RW_TIMEOUT = 20;

    static {
        UnsafeSSLFactory.TrustAllManager trustAllManager = UnsafeSSLFactory.getTrustAllManager();
        OkHttpUtils.initClient(new OkHttpClient.Builder()
                .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(RW_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(RW_TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(UnsafeSSLFactory.createTrustAllSSLFactory(trustAllManager), trustAllManager)
                .hostnameVerifier(UnsafeSSLFactory.createTrustAllHostnameVerifier())
                .addInterceptor(new LoggerInterceptor(Constants.SDK_TAG, YuansferPayModule.sDebug))
                .build());
    }

    /**
     * 生成参数签名
     *
     * @param token     token
     * @param paramInfo 参数实例
     */
    private static HashMap<String, String> generateSignatureMap(@NonNull String token, @NonNull ParamInfo paramInfo) {
        HashMap<String, String> paramMap = paramInfo.toHashMap();
        String[] keyArrays = paramMap.keySet().toArray(new String[]{});
        Arrays.sort(keyArrays);
        StringBuffer psb = new StringBuffer();
        for (String key : keyArrays) {
            psb.append(key).append("=")
                    .append(paramMap.get(key)).append("&");
        }
        psb.append(MD5.encrypt(token));
        paramMap.put("verifySign", MD5.encrypt(psb.toString()));
        Log.e("kkk,verifySign:", paramMap.get("verifySign"));
        return paramMap;
    }

    /**
     * 预下单
     *
     * @param context
     * @param token
     * @param prepayInfo
     * @param responseCallback
     */
    public static void prepay(Context context, String token, PrepayReq prepayInfo, IResponseHandler responseCallback) {
        OkHttpUtils.get().post(context, ApiUrl.getPrePayUrl()
                , generateSignatureMap(token, prepayInfo), responseCallback);
    }

    /**
     * 查询订单状态
     */
    public static void orderStatus(Context context, String token, OrderDetailReq orderInfo, IResponseHandler responseHandler) {
        OkHttpUtils.get().post(context, ApiUrl.getOrderStatusUrl(), generateSignatureMap(token, orderInfo), responseHandler);
    }

    /**
     * 退款
     */
    public static void refund(Context context, String token, RefundReq refundInfo, IResponseHandler responseHandler) {
        OkHttpUtils.get().post(context, ApiUrl.getRefundUrl(), generateSignatureMap(token, refundInfo), responseHandler);
    }

}
