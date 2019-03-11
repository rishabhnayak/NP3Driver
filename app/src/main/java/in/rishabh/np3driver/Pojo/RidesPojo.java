package in.rishabh.np3driver.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rishabh Nayak on 29-01-2019.
 */

public class RidesPojo {
    @SerializedName("rideid")
    @Expose
    private String rideid;
    @SerializedName("dmobile")
    @Expose
    private String dmobile;
    @SerializedName("cmobile")
    @Expose
    private String cmobile;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("km")
    @Expose
    private String km;
    @SerializedName("status")
    @Expose
    private String status;

    public String getRideid() {
        return rideid;
    }

    public void setRideid(String rideid) {
        this.rideid = rideid;
    }

    public String getDmobile() {
        return dmobile;
    }

    public void setDmobile(String dmobile) {
        this.dmobile = dmobile;
    }

    public String getCmobile() {
        return cmobile;
    }

    public void setCmobile(String cmobile) {
        this.cmobile = cmobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}