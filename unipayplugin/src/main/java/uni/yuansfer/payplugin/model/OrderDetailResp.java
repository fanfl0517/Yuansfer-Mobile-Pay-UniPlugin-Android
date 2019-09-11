package uni.yuansfer.payplugin.model;

public class OrderDetailResp {

    private String reference;
    private String transactionId;
    private double amount;
    private String status;
    private String currency;
    private OrderDetailRefund refundInfo;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public OrderDetailRefund getRefundInfo() {
        return refundInfo;
    }

    public void setRefundInfo(OrderDetailRefund refundInfo) {
        this.refundInfo = refundInfo;
    }
}
