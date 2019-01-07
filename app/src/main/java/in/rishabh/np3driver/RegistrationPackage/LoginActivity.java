package in.rishabh.np3driver.RegistrationPackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.rishabh.np3driver.MainActivity;
import in.rishabh.np3driver.R;
import in.rishabh.np3driver.RegistrationPojo.Login;


public class LoginActivity extends AppCompatActivity {
    EditText id,password;
    String userId,userPassword;
    Button login;
    SweetAlertDialog pDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        AppUpdater appUpdater = new AppUpdater(this);
//        appUpdater.start();

     initViews();
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        String name=pref.getString("mobile",null);
        if (name!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        id=findViewById(R.id.id);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        getSupportActionBar().hide();
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId=id.getText().toString();
                userPassword=password.getText().toString();
                if (userId.length()==0){
                    id.setError("Please enter user id");
                }
                else if (password.length()==0){
                    password.setError("Please enter valid password");
                }
                else{
                    volley("http://139.59.66.55/mobiapp/npdriverlogin");
                    loading("yes");
                }

            }
        });

  //      permission();
    }

    public void volley(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("yhi hai response....." + response);
                        loading("no");
                        Login loginResult=new Gson().fromJson(response,Login.class);
                        String success=loginResult.getSuccess();
                        switch (success){
                            case "success":
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                editor.putString("cid",loginResult.getCid());
                                editor.putString("mobile",loginResult.getMobile());
                                editor.putString("name",loginResult.getName());
                                editor.putString("travelmode",loginResult.getTravelmode());
                                editor.commit();
                                finish();
                                break;
                            default:
                                password.setError("Please enter valid password");
                                //  Toast.makeText(LoginActivity.this, "Enter valid Id and Password", Toast.LENGTH_SHORT).show();

                        }

                    }



                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                 //       Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_SHORT).show();
                         loading("no");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("mobile",userId);
                map.put("pwd",userPassword);
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

//    public void permission(){
//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.CAMERA)
//                .withListener(new PermissionListener() {
//                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
//                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
//                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
//                }).check();
//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
//                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
//                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
//                }).check();
//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
//                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
//                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
//                }).check();
//    }
@Override
public void onBackPressed() {
    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
    homeIntent.addCategory( Intent.CATEGORY_HOME );
    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(homeIntent);
}

    public void forgetPassword(View view) {
//        startActivity(new Intent(getApplicationContext(),ForgetPasswordActivity.class));
    }
    private void initViews() {
        Dexter.withActivity(this)
                .withPermissions(android.Manifest.permission.INTERNET,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_NETWORK_STATE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Toast.makeText(getApplicationContext(), "checked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

    }
}