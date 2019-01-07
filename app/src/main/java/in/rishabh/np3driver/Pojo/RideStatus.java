package in.rishabh.np3driver.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rishabh Nayak on 03-01-2019.
 */

public class RideStatus {

    @SerializedName("cmobile")
    @Expose
    private String cmobile;
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
    @SerializedName("dmobile")
    @Expose
    private String dmobile;

    public String getCmobile() {
        return cmobile;
    }

    public void setCmobile(String cmobile) {
        this.cmobile = cmobile;
    }

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

    public String getDmobile() {
        return dmobile;
    }

    public void setDmobile(String dmobile) {
        this.dmobile = dmobile;
    }

}