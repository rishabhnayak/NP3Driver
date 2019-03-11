package in.rishabh.np3driver;

/**
 * Created by Rishabh Nayak on 15-01-2019.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;

import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.rishabh.np3driver.Pojo.RidesPojo;


public class RidesActivity extends AppCompatActivity {
     String URL;
    android.support.v7.widget.Toolbar toolbar;
    SearchView searchView;
    List<RidesPojo> main_list;
    List<RidesPojo> myList;
    RideListAdapter adapter;
    RecyclerView link;
    Context context;
    RidesPojo[] data;
    SweetAlertDialog pDialog;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalorder_list_recycler);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#5e5e5e\">" + "Your Rides" + "</font>")));
        URL="http://139.59.66.55/mobiapp/driverrides?dmobile="+pref.getString("mobile",null);
       volley();
       swipeRefreshLayout=findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                volley();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
    public void volley(){
        loading("yes");
        link = (RecyclerView) findViewById(R.id.finalorder_list_recycler);

        link.setLayoutManager(new LinearLayoutManager(this));

        StringRequest request=new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                loading("no");
                swipeRefreshLayout.setRefreshing(false);
                try{
                    data = new Gson().fromJson(response, RidesPojo[].class);
                    //   Arrays.sort(station, (x,y) -> Integer.compare(x.getName().length(),y.getName().length()));
                    main_list = new ArrayList<RidesPojo>(Arrays.asList(data));
                    List<RidesPojo> main_list= new ArrayList<RidesPojo>(Arrays.asList(data));
                    System.out.println("Yhi haiiiii..........."+main_list.get(0).getDmobile());
                    link.setAdapter(new RideListAdapter(getApplicationContext(), main_list));
                }
                catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "somthing went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
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
}