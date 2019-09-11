package uni.yuansfer.payplugin.okhttp;

import org.json.JSONObject;

public abstract class JsonResponseHandler implements IResponseHandler {
    public JsonResponseHandler() {
    }

    public abstract void onSuccess(int statusCode, JSONObject var2);

    public void onProgress(long currentBytes, long totalBytes) {
    }
}
