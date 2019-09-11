package uni.yuansfer.payplugin.model;


import com.alibaba.fastjson.annotation.JSONField;

public class WechatResp {

    private String appid;
    @JSONField(name="package")
    private String packageName;
    private String timestamp;
    private String noncestr;
    private String prepayid;
    private String sign;
    private String partnerid;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    @Override
    public String toString() {
        return "WechatResp{" +
                "appid='" + appid + '\'' +
                ", packageName='" + packageName + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", sign='" + sign + '\'' +
                ", partnerid='" + partnerid + '\'' +
                '}';
    }
}
