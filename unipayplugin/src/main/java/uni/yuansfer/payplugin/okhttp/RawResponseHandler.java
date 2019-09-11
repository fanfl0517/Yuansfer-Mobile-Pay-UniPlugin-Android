package uni.yuansfer.payplugin.okhttp;

public abstract class RawResponseHandler implements IResponseHandler {
    public RawResponseHandler() {
    }

    public abstract void onSuccess(int statusCode, String result);

    public void onProgress(long currentBytes, long totalBytes) {
    }
}
