package uni.yuansfer.payplugin.model;

public class RefundResp {

      private double amount;
      private String status;
      private String currency;
      private String reference;
      private double refundAmount;
      private String refundTransactionId;
      private String refundReference;
      private String oldTransactionId;

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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundTransactionId() {
        return refundTransactionId;
    }

    public void setRefundTransactionId(String refundTransactionId) {
        this.refundTransactionId = refundTransactionId;
    }

    public String getRefundReference() {
        return refundReference;
    }

    public void setRefundReference(String refundReference) {
        this.refundReference = refundReference;
    }

    public String getOldTransactionId() {
        return oldTransactionId;
    }

    public void setOldTransactionId(String oldTransactionId) {
        this.oldTransactionId = oldTransactionId;
    }
}
