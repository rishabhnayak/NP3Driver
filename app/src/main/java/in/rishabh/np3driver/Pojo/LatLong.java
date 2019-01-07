package in.rishabh.np3driver.Pojo;

/**
 * Created by Rishabh Nayak on 02-01-2019.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatLong {

    @SerializedName("rideid")
    @Expose
    private String rideid;
    @SerializedName("olat")
    @Expose
    private String olat;
    @SerializedName("olon")
    @Expose
    private String olon;
    @SerializedName("dlat")
    @Expose
    private String dlat;
    @SerializedName("dlon")
    @Expose
    private String dlon;
    @SerializedName("cmobile")
    @Expose
    private String cmobile;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success")
    @Expose
    private String success;

    public String getRideid() {
        return rideid;
    }

    public void setRideid(String rideid) {
        this.rideid = rideid;
    }

    public String getOlat() {
        return olat;
    }

    public void setOlat(String olat) {
        this.olat = olat;
    }

    public String getOlon() {
        return olon;
    }

    public void setOlon(String olon) {
        this.olon = olon;
    }

    public String getDlat() {
        return dlat;
    }

    public void setDlat(String dlat) {
        this.dlat = dlat;
    }

    public String getDlon() {
        return dlon;
    }

    public void setDlon(String dlon) {
        this.dlon = dlon;
    }

    public String getCmobile() {
        return cmobile;
    }

    public void setCmobile(String cmobile) {
        this.cmobile = cmobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

}