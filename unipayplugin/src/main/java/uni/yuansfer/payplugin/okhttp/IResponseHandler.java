package uni.yuansfer.payplugin.okhttp;

public interface IResponseHandler {

    void onFailure(int statusCode, String errorMsg);

    void onProgress(long var1, long var3);
}
