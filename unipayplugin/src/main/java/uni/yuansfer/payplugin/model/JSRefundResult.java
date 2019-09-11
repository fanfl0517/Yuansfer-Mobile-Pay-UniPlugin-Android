package uni.yuansfer.payplugin.model;

/**
 * @author fly
 * @date 2019-09-11
 * @desc 退款结果
 */
public class JSRefundResult {
    /**
     * 退款状态，0退款成功-1退款失败
     */
    private int retCode;
    private String retMsg;
    private RefundResp result;

    public JSRefundResult(int retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public RefundResp getResult() {
        return result;
    }

    public void setResult(RefundResp result) {
        this.result = result;
    }
}
