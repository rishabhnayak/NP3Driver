package in.rishabh.np3driver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().hide();

        findViewById(R.id.terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://np3.in/Terms-and-conditions";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        findViewById(R.id.policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://np3.in/Privacy-policy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        findViewById(R.id.website).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://np3.in";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }


}
