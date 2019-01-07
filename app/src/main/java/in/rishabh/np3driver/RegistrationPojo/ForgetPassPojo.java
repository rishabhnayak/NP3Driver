package in.rishabh.np3driver.RegistrationPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rishabh Nayak on 12-12-2018.
 */

public class ForgetPassPojo {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("password")
    @Expose
    private String password;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}