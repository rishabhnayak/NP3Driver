package in.rishabh.np3driver;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.rishabh.np3driver.RegistrationPojo.Success;


public class EnquiyActivity extends AppCompatActivity {
    EditText subjectE, messageE;
    String subject, message;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiy);
        getSupportActionBar().setHomeButtonEnabled(true);
        subjectE = findViewById(R.id.sub);
        messageE = findViewById(R.id.mes);
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject=subjectE.getText().toString();
                message = messageE.getText().toString();
                // Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#5e5e5e\">" + "NP3 Driver" + "</font>")));
                if(subject.length()==0){
                    subjectE.setError("This Field must not be empty");
                }else if(message.length()==0){
                    messageE.setError("This Field must not be empty");
                }
                else {
                    volley("http://139.59.66.55/mobiapp/npcomplaint");
                    loading("yes");
                }

            }
        });
    }
    public void volley(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("yhi hai response....." + response);
                        loading("no");
                        Gson gson=new Gson();
                        Success successObject=gson.fromJson(response,Success.class);
                        String success=successObject.getSuccess();
                        switch (success){
                            case "success":
                                successAlert();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Server Error Please Retry", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        loading("no");
                        Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("subject",subject);
                map.put("message",message);
                //    Toast.makeText(ComplintActivity.this, d_id, Toast.LENGTH_SHORT).show();
                return map;
            }
        };

        queue.add(postRequest);
    }
    public void loading(String show) {
        if (show == "yes") {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#0ab184"));
            pDialog.setTitleText("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        } else {
            pDialog.cancel();
        }
    }
    public void successAlert(){
        final SweetAlertDialog sweetAlertDialog=  new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Sent Successfully");
        sweetAlertDialog.show();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sweetAlertDialog.cancel();
                finish();
            }
        },1500);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
