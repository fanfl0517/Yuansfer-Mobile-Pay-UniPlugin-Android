package uni.yuansfer.payplugin.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
* @Author Fly-Android
* @CreateDate 2019/5/27 9:31
* @Desciption 登录
*/
public class SignReq extends ParamInfo implements Parcelable {

    private String merchantNo;
    private String storeNo;

    public SignReq() {}

    public SignReq(Parcel in) {
        this.setMerchantNo(in.readString());
        this.setStoreNo(in.readString());
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    @Override
    public HashMap<String, String> toHashMap() {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("merchantNo", merchantNo);
        paramMap.put("storeNo", storeNo);
        return paramMap;
    }

    public static final Creator<SignReq> CREATOR = new Creator<SignReq>() {
        public SignReq createFromParcel(Parcel source) {
            return new SignReq(source);
        }

        public SignReq[] newArray(int size) {
            return new SignReq[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getMerchantNo());
        dest.writeString(getStoreNo());
    }

}
