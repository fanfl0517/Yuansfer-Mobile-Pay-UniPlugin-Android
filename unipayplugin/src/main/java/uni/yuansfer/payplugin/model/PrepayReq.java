package uni.yuansfer.payplugin.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;


import java.util.HashMap;

import uni.yuansfer.payplugin.pay.PayType;

/**
 * @Author Fly-Android
 * @CreateDate 2019/5/27 9:30
 * @Desciption 预下单请求
 */
public class PrepayReq extends SignReq {

    private int payType;
    private String token;
    private double amount;
    private String ipnUrl;
    private String reference;
    private String description;
    private String note;
    private String appid;
    private String terminal = "APP";
    private String currency = "USD";

    public PrepayReq() {
    }

    public PrepayReq(Parcel in) {
        super(in);
        setPayType(in.readInt());
        setAmount(in.readDouble());
        setIpnUrl(in.readString());
        setReference(in.readString());
        setDescription(in.readString());
        setNote(in.readString());
        setAppid(in.readString());
        setTerminal(in.readString());
        setCurrency(in.readString());
        setToken(in.readString());
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getIpnUrl() {
        return ipnUrl;
    }

    public void setIpnUrl(String ipnUrl) {
        this.ipnUrl = ipnUrl;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public HashMap<String, String> toHashMap() {
        HashMap<String, String> paramMap = super.toHashMap();
        paramMap.put("vendor", payType == PayType.WXPAY
                ? "wechatpay" : "alipay");
        paramMap.put("amount", amount + "");
        paramMap.put("ipnUrl", ipnUrl);
        paramMap.put("reference", reference);
        paramMap.put("description", description);
        paramMap.put("note", note);
        paramMap.put("terminal", terminal);
        paramMap.put("currency", currency);
        if (payType == PayType.WXPAY && !TextUtils.isEmpty(appid)) {
            paramMap.put("appid", appid);
        }
        return paramMap;
    }

    public static final Parcelable.Creator<PrepayReq> CREATOR = new Parcelable.Creator<PrepayReq>() {
        public PrepayReq createFromParcel(Parcel source) {
            return new PrepayReq(source);
        }

        public PrepayReq[] newArray(int size) {
            return new PrepayReq[size];
        }
    };

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(getPayType());
        dest.writeDouble(getAmount());
        dest.writeString(getIpnUrl());
        dest.writeString(getReference());
        dest.writeString(getDescription());
        dest.writeString(getNote());
        dest.writeString(getTerminal());
        dest.writeString(getCurrency());
        dest.writeString(getToken());
    }

    @Override
    public String toString() {
        return "PrepayReq{" +
                "payType=" + payType +
                ", token='" + token + '\'' +
                ", amount=" + amount +
                ", ipnUrl='" + ipnUrl + '\'' +
                ", reference='" + reference + '\'' +
                ", description='" + description + '\'' +
                ", note='" + note + '\'' +
                ", appid='" + appid + '\'' +
                ", terminal='" + terminal + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }

}
