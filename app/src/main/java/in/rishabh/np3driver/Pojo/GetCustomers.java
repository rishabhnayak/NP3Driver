package in.rishabh.np3driver.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rishabh Nayak on 01-01-2019.
 */

public class GetCustomers {
    @SerializedName("driver_mobile")
    @Expose
    private String driver_mobile;
    @SerializedName("rideid")
    @Expose
    private String rideid;
    @SerializedName("customer_mobile")
    @Expose
    private String customer_mobile;
    @SerializedName("customer_olat")
    @Expose
    private String customer_olat;
    @SerializedName("customer_olon")
    @Expose
    private String customer_olon;
    @SerializedName("customer_dlat")
    @Expose
    private String customer_dlat;
    @SerializedName("customer_dlon")
    @Expose
    private String customer_dlon;
    @SerializedName("success")
    @Expose
    private String success;

    public String getDriver_mobile() {
        return driver_mobile;
    }

    public void setDriver_mobile(String driver_mobile) {
        this.driver_mobile = driver_mobile;
    }

    public String getRideid() {
        return rideid;
    }

    public void setRideid(String rideid) {
        this.rideid = rideid;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getCustomer_olat() {
        return customer_olat;
    }

    public void setCustomer_olat(String customer_olat) {
        this.customer_olat = customer_olat;
    }

    public String getCustomer_olon() {
        return customer_olon;
    }

    public void setCustomer_olon(String customer_olon) {
        this.customer_olon = customer_olon;
    }

    public String getCustomer_dlat() {
        return customer_dlat;
    }

    public void setCustomer_dlat(String customer_dlat) {
        this.customer_dlat = customer_dlat;
    }

    public String getCustomer_dlon() {
        return customer_dlon;
    }

    public void setCustomer_dlon(String customer_dlon) {
        this.customer_dlon = customer_dlon;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

}
