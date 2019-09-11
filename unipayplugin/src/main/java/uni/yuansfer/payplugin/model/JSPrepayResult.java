package uni.yuansfer.payplugin.model;

import uni.yuansfer.payplugin.pay.PayType;

/**
 * @author fly
 * @date 2019-09-10
 * @desc 支付结果
 */
public class JSPrepayResult {

    /**
     * 支付类型
     */
    @PayType
    private int payType;
    /**
     * 支付状态，0成功1取消-1失败
     */
    private int retCode;
    /**
     * 支付结果描述
     */
    private String retMsg;

    public JSPrepayResult(@PayType int payType, int retCode, String retMsg) {
        this.payType = payType;
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
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

}
