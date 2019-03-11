package in.rishabh.np3driver.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rishabh Nayak on 06-02-2019.
 */

public class Go {
    @SerializedName("success")
@Expose
private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

}