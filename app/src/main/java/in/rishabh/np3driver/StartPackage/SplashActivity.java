package in.rishabh.np3driver.StartPackage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import in.rishabh.np3driver.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            startActivity(new Intent(getApplicationContext(),DotsActivity.class));
            finish();
            }
        },1600);
    }

    @Override
    public void onBackPressed() {
    }
}
