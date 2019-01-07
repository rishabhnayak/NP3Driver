package in.rishabh.np3driver.RegistrationPojo;

/**
 * Created by Rishabh Nayak on 29-09-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("travelmode")
    @Expose
    private String travelmode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("success")
    @Expose
    private String success;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
    public String getTravelmode() {
        return travelmode;
    }

    public void setTravelmode(String travelmode) {
        this.travelmode = travelmode;
    }
}