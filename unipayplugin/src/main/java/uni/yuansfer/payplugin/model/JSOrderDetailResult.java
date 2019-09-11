package uni.yuansfer.payplugin.model;

/**
 * @author fly
 * @date 2019-09-11
 * @desc 订单详情结果
 */
public class JSOrderDetailResult {
    /**
     * 订单查询状态，0查询成功-1查询失败
     */
    private int retCode;
    private String retMsg;
    private OrderDetailResp result;

    public JSOrderDetailResult(int retCode, String retMsg) {
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

    public OrderDetailResp getResult() {
        return result;
    }

    public void setResult(OrderDetailResp result) {
        this.result = result;
    }
}
