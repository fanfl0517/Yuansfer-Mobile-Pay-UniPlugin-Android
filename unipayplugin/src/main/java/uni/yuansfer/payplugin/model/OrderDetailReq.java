package uni.yuansfer.payplugin.model;

import android.os.Parcel;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * @Author Fly-Android
 * @CreateDate 2019/5/29 15:44
 * @Desciption 订单状态
 */
public class OrderDetailReq extends SignReq {

    private String token;
    private String reference;
    private String transactionNo;

    public OrderDetailReq() {
    }

    public OrderDetailReq(Parcel in) {
        super(in);
        this.setToken(in.readString());
        this.setReference(in.readString());
        this.setTransactionNo(in.readString());
    }

    public String getReference() {
        return reference;
    }

    /**
     * reference和transactionNo二选一
     *
     * @param reference 流水号
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * reference和transactionNo二选一
     *
     * @param transactionNo 订单号
     */
    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    @Override
    public HashMap<String, String> toHashMap() {
        HashMap<String, String> paramMap = super.toHashMap();
        if (!TextUtils.isEmpty(reference)) {
            paramMap.put("reference", reference);
        }
        if (!TextUtils.isEmpty(transactionNo)) {
            paramMap.put("transactionNo", transactionNo);
        }
        return paramMap;
    }

    public static final Creator<OrderDetailReq> CREATOR = new Creator<OrderDetailReq>() {
        public OrderDetailReq createFromParcel(Parcel source) {
            return new OrderDetailReq(source);
        }

        public OrderDetailReq[] newArray(int size) {
            return new OrderDetailReq[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(getToken());
        dest.writeString(getReference());
        dest.writeString(getTransactionNo());
    }

}
