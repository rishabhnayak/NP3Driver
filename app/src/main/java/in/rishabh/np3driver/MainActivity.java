package in.rishabh.np3driver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.rishabh.np3driver.Pojo.AllStatus;
import in.rishabh.np3driver.Pojo.GetCustomers;
import in.rishabh.np3driver.Pojo.Go;
import in.rishabh.np3driver.Pojo.LatLong;
import in.rishabh.np3driver.Pojo.RideStatus;
import in.rishabh.np3driver.RegistrationPackage.LoginActivity;
import in.rishabh.np3driver.RegistrationPojo.Success;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, RoutingListener {
    private GoogleMap mMap;
    CountDownTimer timer;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    View locationButton;
    double lat, lon;
    String cMobile, dMobile;
    SupportMapFragment mapFragment;
    String endlat,start,endlon;
    String rideid1;
    private LatLng origin,destination;
    private List<Polyline> polylines;
    String travelmode;
    SharedPreferences pref1;
    SharedPreferences.Editor editor1;
    SharedPreferences pref2;
    SharedPreferences.Editor editor2;
    String amount = "0";
    RequestQueue queueget,location;
    String distance;
    int flagotp=0,flaggetcustomer=0;
    int sum = 0;
    private static final int[] COLORS = new int[]{R.color.route, R.color.routeborder, R.color.colorPrimary, R.color.colorPrimary, R.color.primary_dark_material_light};
    private String rid;
    private CountDownTimer timer1;
    private CountDownTimer timer2;
    private RequestQueue ridestatus;
    private StringRequest postRequest;
    private RequestQueue queue;
    private Gson gson;
    private RideStatus rideStatus;
    private GetCustomers success;
    private Success success1;
    private CountDownTimer timer3;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref1 = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor1 = pref1.edit();

        pref2 = getApplicationContext().getSharedPreferences("flagotppref", 0);
        editor2 = pref2.edit();

        View pop = findViewById(R.id.popup);
        if (pop.getVisibility()==View.VISIBLE){
            findViewById(R.id.endRide).setVisibility(View.GONE);
            ((ExpandableLayout)findViewById(R.id.expandable_layout2)).collapse();
        }

        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#5e5e5e\">" + "NP3 Driver" + "</font>")));
        //  System.out.println(String.valueOf(mMap.getMyLocation().getLatitude()));
        location = Volley.newRequestQueue(getApplicationContext());

        ridestatus = Volley.newRequestQueue(this);
        findViewById(R.id.cash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentDone("cash");
            }
        });
        findViewById(R.id.online).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentDone("online");
            }
        });
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        dMobile=pref.getString("mobile",null);
        travelmode=pref.getString("travelmode",null);
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

  //      initViews();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getCustomers();
            }
        },1000);
        findViewById(R.id.cancelRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ExpandableLayout)findViewById(R.id.expandable_layout2)).collapse();
                getCustomers();
                cancelRide();
            }
        });
      findViewById(R.id.cash).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              paymentDone("cash");
          }
      });
        findViewById(R.id.online).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentDone("online");
            }
        });
        findViewById(R.id.acceptRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Otp send successfully", Toast.LENGTH_SHORT).show();
                ((ExpandableLayout)findViewById(R.id.expandable_layout2)).collapse();
                queue = Volley.newRequestQueue(getApplicationContext());
                postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/sendotp?cmobile="+success.getCustomer_mobile()+"&dmobile="+success.getDriver_mobile()+"&rideid="+success.getRideid(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //                   System.out.println("yhi hai response....." + response);

                                gson=new Gson();
                                success1=gson.fromJson(response,Success.class);
                                switch(success1.getSuccess()){
                                    case "success":
                                        flagotp=1;

                                        findViewById(R.id.acceptRequest).setVisibility(View.GONE);
                                        //   Toast.makeText(MainActivity.this, String.valueOf(flagotp), Toast.LENGTH_SHORT).show();
                                        editor2.putString("flagotp",String.valueOf(flagotp));
                                        editor2.commit();
                                        findViewById(R.id.popup).setVisibility(View.VISIBLE);
                                        findViewById(R.id.endRide).setVisibility(View.GONE);
                                        ((ExpandableLayout)findViewById(R.id.expandable_layout2)).collapse();
                                        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String otp = ((EditText)findViewById(R.id.otp)).getText().toString();
                                                //     System.out.println(otp);
                                                //    System.out.println(success.getRideid());
//                                                                       Toast.makeText(MainActivity.this, otp+success.getRideid(), Toast.LENGTH_SHORT).show();
                                            //    Toast.makeText(MainActivity.this, String.valueOf(otp)+"   "+success.getRideid(), Toast.LENGTH_SHORT).show();
                                                sendOtp(otp,pref1.getString("rideid", null));

                                            }
                                        });
                                        break;
                                    default:
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("volley error" + error);
                                // Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                queue.add(postRequest);
            }
        });
        findViewById(R.id.endRide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try {
                        rideEnd();
                    } catch (Exception e) {
                }}
        });
     //   Toast.makeText(this, pref1.getString("rideid", null), Toast.LENGTH_SHORT).show();
         queueget = Volley.newRequestQueue(getApplicationContext());

        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = ((EditText)findViewById(R.id.otp)).getText().toString();
                RequestQueue test = Volley.newRequestQueue(getApplicationContext());
                StringRequest test2 = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/enterotp?rideid="+pref1.getString("rideid", null)+"&otp="+otp,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("yhi hai response....." + response);
                                try {
                                    Gson gson=new Gson();
                                    String lat=pref.getString("olat",null);
                                    String lon=pref.getString("olon",null);
                                    if (pref.getString("olat",null)!=null){
                                        origin=new LatLng(Double.valueOf(lat),Double.valueOf(lon));
                                    }
                                    else {
                                        editor1.putString("olat",String.valueOf(mMap.getMyLocation().getLatitude()));
                                        editor1.putString("olon",String.valueOf(mMap.getMyLocation().getLongitude()));
                                        editor1.commit();
                                    }

                                    Success successObject=gson.fromJson(response,Success.class);
                                    String success=successObject.getSuccess();
                                    LatLong ll=gson.fromJson(response,LatLong.class);
                                    cMobile=ll.getCmobile();
                                    rideid1=ll.getRideid();

                                    switch (success){
                                        case "success":
                                            mMap.clear();
                                            //   Toast.makeText(MainActivity.this, "cleared", Toast.LENGTH_SHORT).show();
                                            editor2.clear();
                                            editor2.commit();
                                            findViewById(R.id.popup).setVisibility(View.GONE);
                                            findViewById(R.id.endRide).setVisibility(View.VISIBLE);
                                            flaggetcustomer=1;
                                            ((ExpandableLayout)findViewById(R.id.expandable_layout2)).expand();
                                            String uri = "http://maps.google.com/maps?saddr="+""+ll.getOlat()+","+ll.getOlon()+""+"&daddr="+""+ll.getDlat()+","+ll.getDlon()+"";
                                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                                            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                                            startActivity(intent);
                                            break;
                                        default:
                                            Toast.makeText(getApplicationContext(), "Please enter valid OTP.", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("volley error" + error);
                                // Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                test.add(test2);

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            gosilent();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            editor.clear();
            editor.commit();
            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
        else if (id == R.id.nav_rides) {
            startActivity(new Intent(getApplicationContext(),RidesActivity.class));
        }else if (id == R.id.nav_send) {

        }
        else if (id == R.id.nav_golive) {
        golive();
        }
       else if (id == R.id.action_settings) {
            gosilent();
        }
        else if (id == R.id.nav_feedback) {
            startActivity(new Intent(getApplicationContext(),EnquiyActivity.class));
        }
        else if (id == R.id.nav_aboutus) {
            startActivity(new Intent(getApplicationContext(),AboutUsActivity.class));
        }
        else if (id == R.id.nav_emergencycall) {
            String phone = "07714267222";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
       locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
try{
    lat=mMap.getMyLocation().getLatitude();
    lon=mMap.getMyLocation().getLongitude();
    System.out.println(" ihi lat hary"+String.valueOf(mMap.getMyLocation().getLatitude()));
}catch (Exception e){

}
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);rlp.setMargins(0,0,30,400);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                timer();
                locationButton.performClick();
//                Toast.makeText(
//                 MainActivity.this, "working", Toast.LENGTH_SHORT).show();
            }
        },3000);
    }



    public void sendCurrentlocation(){


     //  System.out.println(String.valueOf(mMap.getMyLocation().getLatitude())+""+lon);
         postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/driverlocationupdate?driverlon="+mMap.getMyLocation().getLatitude()+"&driverlat="+mMap.getMyLocation().getLongitude()+"&dmobile="+dMobile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                 //       System.out.println("yhi hai response....." + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                //        // Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        location.add(postRequest);
    }

    public void getCustomers(){
       try {
         postRequest = new StringRequest(Request.Method.GET, "http://139.59.66.55/mobiapp/checkride?dmobile="+dMobile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    System.out.println("yhi hai response....." + response);
                       gson=new Gson();
                      success=gson.fromJson(response, GetCustomers.class);
                        result= success.getSuccess();

                        if (success.getCustomer_mobile()==null){

                        }else {
                          try{
                              ((TextView)findViewById(R.id.customerMobile)).setText(success.getCustomer_mobile());
                              ((TextView)findViewById(R.id.customerName)).setText(success.getCustomer_name());
                              findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {
                                      String phone = "+91"+success.getCustomer_mobile();
                                      Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                      startActivity(intent);
                                  }
                              });
                          }catch (Exception e){

                          }

                        }

                        switch (result){
                            case "success" :
                                findViewById(R.id.acceptRequest).setVisibility(View.VISIBLE);
                                success.getCustomer_dlat();
                                success.getCustomer_dlon();
                                success.getCustomer_mobile();
                                success.getDriver_mobile();
                                rid=success.getRideid();
                               try{
                                   editor1.putString("rideid",rid);
                                   editor1.commit();
                               }catch (Exception e){

                               }

                                try{
                                LatLng customer=new LatLng(Double.valueOf(success.getCustomer_olat()),Double.valueOf(success.getCustomer_olon()));
                                LatLng driver=new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude());

                                MarkerOptions options1 = new MarkerOptions();
                                options1.position(customer);
                                options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.customer));
                                mMap.addMarker(options1);

                                Routing routing = new Routing.Builder()
                                        .key("AIzaSyCv_imK5ydtkdWnGJP1Dbt-DT07UdvyDeo")
                                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                                        .withListener(new RoutingListener() {
                                            @Override
                                            public void onRoutingFailure(RouteException e) {

                                            }

                                            @Override
                                            public void onRoutingStart() {

                                            }

                                            @Override
                                            public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
                                                try {
                                                    if (polylines.size() > 0) {
                                                        for (Polyline poly : polylines) {
                                                            poly.remove();
                                                        }
                                                    }
                                                } catch (Exception e) {

                                                }
                                                ArrayList<Integer> arrli = new ArrayList<Integer>();

                                                for (int j = 0; j < route.size(); j++) {

                                                    arrli.add(route.get(j).getDurationValue());

                                                }

                                                int minIndex = arrli.indexOf(Collections.min(arrli));

                                        //      Toast.makeText(MainActivity.this, String.valueOf(route.get(0).getDistanceText()), Toast.LENGTH_SHORT).show();
                                                System.out.println("ihi haray............." + minIndex);
                                                PolylineOptions polyOptions = new PolylineOptions();
                                                polyOptions.color(getResources().getColor(COLORS[1]));
                                                polyOptions.width(15);
                                                polyOptions.addAll(route.get(minIndex).getPoints());
                                                Polyline polyline = mMap.addPolyline(polyOptions);
                                                PolylineOptions polyOptions1 = new PolylineOptions();
                                                polyOptions1.color(getResources().getColor(COLORS[0]));
                                                polyOptions1.width(9);
                                                polyOptions1.addAll(route.get(minIndex).getPoints());
                                                Polyline polylin = mMap.addPolyline(polyOptions1);
try{
    senddrivertocustomerdistance(route.get(0).getDistanceText().replace(" km",""),rid);
}catch (Exception e){

}


                                            }

                                            @Override
                                            public void onRoutingCancelled() {

                                            }
                                        })
                                        .alternativeRoutes(true)
                                        .waypoints(customer, driver)
                                        .build();
                                routing.execute();
                            }catch (Exception e){

                            }
                                ((ExpandableLayout)findViewById(R.id.expandable_layout2)).expand();

                                break;
                            default:

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) ;

        queueget.add(postRequest);
       }catch (Exception e){

       }
    }

    private void senddrivertocustomerdistance(String replace,String rid) {


       postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/customerdistance?rideid="+replace+"&driverTocustomerDistance="+rid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                    }
                }

        )
        ;

        queue.add(postRequest);
    }

    public void sendOtp(String ootp,String rrideid){
      //  Toast.makeText(this, ootp+"   "+rrideid, Toast.LENGTH_SHORT).show();
        postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/enterotp?rideid="+rrideid+"&otp="+ootp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("yhi hai response....." + response);
                      try {
                          Gson gson=new Gson();
                          String lat=pref.getString("olat",null);
                          String lon=pref.getString("olon",null);
                          if (pref.getString("olat",null)!=null){
                              origin=new LatLng(Double.valueOf(lat),Double.valueOf(lon));
                          }
                          else {
                              editor1.putString("olat",String.valueOf(mMap.getMyLocation().getLatitude()));
                              editor1.putString("olon",String.valueOf(mMap.getMyLocation().getLongitude()));
                              editor1.commit();
                          }

                        Success successObject=gson.fromJson(response,Success.class);
                        String success=successObject.getSuccess();
                        LatLong ll=gson.fromJson(response,LatLong.class);
                        cMobile=ll.getCmobile();
                        rideid1=ll.getRideid();

                        switch (success){
                            case "success":
                                mMap.clear();
                             //   Toast.makeText(MainActivity.this, "cleared", Toast.LENGTH_SHORT).show();
                                editor2.clear();
                                editor2.commit();
                                findViewById(R.id.popup).setVisibility(View.GONE);
                                findViewById(R.id.endRide).setVisibility(View.VISIBLE);
                                flaggetcustomer=1;
                                ((ExpandableLayout)findViewById(R.id.expandable_layout2)).expand();
                                String uri = "http://maps.google.com/maps?saddr="+""+ll.getOlat()+","+ll.getOlon()+""+"&daddr="+""+ll.getDlat()+","+ll.getDlon()+"";
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                                startActivity(intent);
                                break;
                            default:
//                                editor1.putString("rideid", rid);
//                                editor1.putString("amount", amount);
//                                editor1.putString("distance",distance.replace(" km",""));
//                                editor1.putString("dmobile",dMobile);
//                                editor1.commit();
                                Toast.makeText(getApplicationContext(), "Please enter valid OTP.", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                      }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        // Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(postRequest);
    }

    public void cancelRide(){

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/ridecancel?cmobile="+cMobile+"&rideid="+rid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("rishabh yhi hai"+response);
                 //       Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                        System.out.println("yhi hai response....." + response+rid);
                        ((ExpandableLayout)findViewById(R.id.expandable_layout2)).collapse();
                        mMap.clear();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        // Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                //    Toast.makeText(ComplintActivity.this, d_id, Toast.LENGTH_SHORT).show();
                return map;
            }
        };

        queue.add(postRequest);
    }

    public void rideStatus(){
       postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/checkOngoingRide?dMobile="+dMobile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            gson=new Gson();
                          rideStatus=gson.fromJson(response,RideStatus.class);

                            if (rideStatus.getDmobile().length()==0){

                            }else {
                                ((ExpandableLayout)findViewById(R.id.expandable_layout2)).expand();
                                ((TextView)findViewById(R.id.customerMobile)).setText(rideStatus.getCname());
                                findViewById(R.id.acceptRequest).setVisibility(View.GONE);
                                findViewById(R.id.cancelRequest).setVisibility(View.GONE);
                                findViewById(R.id.endRide).setVisibility(View.VISIBLE);
                               ((TextView)findViewById(R.id.customerName)).setText(rideStatus.getCmobile());
                                findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String phone = "+91"+rideStatus.getCname();
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                        startActivity(intent);
                                    }
                                });

                            }
                        }catch (Exception e){

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        ridestatus.add(postRequest);
    }

    @Override
    protected void onResume() {


     try{

         timer();
         timer3();
         Handler handler=new Handler();
         handler.postDelayed(new Runnable() {
             @Override
             public void run() {
                 timer2();
             }
         },5000);

         String otpvisibility=pref2.getString("flagotp",null);
      //   Toast.makeText(this, String.valueOf(otpvisibility), Toast.LENGTH_SHORT).show();
         if (otpvisibility.equals("1")==true){
             findViewById(R.id.endRide).setVisibility(View.GONE);
             ((ExpandableLayout)findViewById(R.id.expandable_layout2)).collapse();
             findViewById(R.id.popup).setVisibility(View.VISIBLE);

         }

     }catch (Exception e){

     }
        super.onResume();
    }

    public void rideEnd(){
//        Toast.makeText(this, String.valueOf(origin.longitude+","+origin.longitude), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, String.valueOf(destination.longitude+","+destination.longitude), Toast.LENGTH_SHORT).show();

//        LatLng a=new LatLng(21.2692645,81.668486);
//        LatLng b=new LatLng(21.2603002,81.6604469);

        String lat=pref.getString("olat",null);
        String lon=pref.getString("olon",null);
        destination=new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude());
        System.out.println(lat+" "+lon+"  "+destination.latitude+" "+destination.longitude);
        ((ExpandableLayout)findViewById(R.id.expandable_layout2)).collapse();
        Routing routing = new Routing.Builder()
                .key("AIzaSyCv_imK5ydtkdWnGJP1Dbt-DT07UdvyDeo")
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(new LatLng(Double.valueOf(lat),Double.valueOf(lon)), destination)
                .build();
        routing.execute();

    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<com.directions.route.Route> route, int shortestRouteIndex) {
        try {
            if (polylines.size() > 0) {
                for (Polyline poly : polylines) {
                    poly.remove();
                }
            }
        } catch (Exception e) {
        }


        ArrayList<Integer> arrli = new ArrayList<Integer>();

        for (int j = 0; j < route.size(); j++) {

            arrli.add(route.get(j).getDurationValue());

        }
        int minIndex = arrli.indexOf(Collections.min(arrli));
        System.out.println("ihi haray............." + minIndex);
        distance = route.get(minIndex).getDistanceText();
        String duration = route.get(minIndex).getDurationText();
        int routeno = minIndex;

       // Toast.makeText(this, distance+ "   " + duration, Toast.LENGTH_SHORT).show();
        RequestQueue queue = Volley.newRequestQueue(this);

        String price=distance.replace(" km","");
        try{
            switch (travelmode){
                case "0":
//                    Double price=35+(distance*6)+2;
                    double total=35+Double.valueOf(price)*6+Double.valueOf(price)*3*2;
                    double gst=(0.05*total);
                    double a=Math.round(total+gst);
                  //  double a = Math.round(35 + (Double.valueOf(price) * 6)) + (Double.valueOf(price) * 3 * 2) + (0.05 * (35 + (Double.valueOf(price) * 6)) + (Double.valueOf(price) * 3 * 2));
                    amount=String.valueOf(Math.round(a));

//                    amount=String.valueOf(35+(Math.round(Double.valueOf(price)*6))+(Math.round(Double.valueOf(price)*3*2)));
                    break;
                case "1":
//                    Double price=45+(distance*10)+2;
                    double total1=45+Double.valueOf(price)*10+Double.valueOf(price)*3*2;
                    double gst1=(0.05*total1);
                    double b=Math.round(total1+gst1);
                  //  amount=String.valueOf(Math.round(45+(Math.round(Double.valueOf(price)*10))+(Math.round(Double.valueOf(price)*3*2)))+(0.05*(45+(Math.round(Double.valueOf(price)*10))+(Math.round(Double.valueOf(price)*3*2)))));
                  //  double b=Math.round(45+(Double.valueOf(price)*10))+(Double.valueOf(price)*3*2)+(0.05*(45+(Double.valueOf(price)*10))+(Double.valueOf(price)*3*2));
                    amount=String.valueOf(Math.round(b));

//                    amount=String.valueOf(45+(Math.round(Double.valueOf(price)*10))+(Math.round(Double.valueOf(price)*3*2)));
                    break;
                case "2":
//                    Double price=55+(distance*12)+2;
                   // amount=String.valueOf(Math.round(55+(Math.round(Double.valueOf(price)*12))+(Math.round(Double.valueOf(price)*3*2)))+(0.05*(55+(Math.round(Double.valueOf(price)*12))+(Math.round(Double.valueOf(price)*3*2)))));
                    double total2=55+Double.valueOf(price)*12+Double.valueOf(price)*3*2;
                    double gst2=(0.05*total2);
                    double c=Math.round(total2+gst2);
                   // double c=Math.round(55+(Double.valueOf(price)*12))+(Double.valueOf(price)*3*2)+(0.05*(55+(Double.valueOf(price)*12))+(Double.valueOf(price)*3*2));
                    amount=String.valueOf(Math.round(c));

//                    amount=String.valueOf(55+Math.round(Double.valueOf(price)*12)+(Math.round(Double.valueOf(price)*3*2)));
                    break;
                case "3":
//                    Double price=10+(distance*4)+1.5;
                //    amount=String.valueOf(Math.round(10+(Math.round(Double.valueOf(price)*4))+(Math.round(Double.valueOf(price)*2*1.5)))+(0.05*(10+(Math.round(Double.valueOf(price)*4))+(Math.round(Double.valueOf(price)*2*1.5)))));
                    double total3=10+Double.valueOf(price)*4+Double.valueOf(price)*2*1.5;
                    double gst3=(0.05*total3);
                    double d=Math.round(total3+gst3);
                    //double d=Math.round(10+(Double.valueOf(price)*4))+(Double.valueOf(price)*2*1.5)+(0.05*(10+(Double.valueOf(price)*4))+(Double.valueOf(price)*2*1.5));
                    amount=String.valueOf(Math.round(d));

//                    amount=String.valueOf(10+(Math.round(Double.valueOf(price)*4))+(Math.round(Double.valueOf(price)*2*1.5)));
                    break;
                case "4":
//                    Double price=10+(distance*3)+1.5;
              //      amount=String.valueOf(Math.round(10+(Math.round(Double.valueOf(price)*3))+(Math.round(Double.valueOf(price)*2*1.5)))+(0.05*(10+(Math.round(Double.valueOf(price)*3))+(Math.round(Double.valueOf(price)*2*1.5)))));
                    double total4=10+Double.valueOf(price)*3+Double.valueOf(price)*2*1.5;
                    double gst4=(0.05*total4);
                    double e=Math.round(total4+gst4);
                   // double e=Math.round(10+(Double.valueOf(price)*3))+(Double.valueOf(price)*2*1.5)+(0.05*(10+(Double.valueOf(price)*3))+(Double.valueOf(price)*2*1.5));
                    amount=String.valueOf(Math.round(e));

//                    amount=String.valueOf(10+(Math.round(Double.valueOf(price)*3))+(Math.round(Double.valueOf(price)*2*1.5)));
                    break;
                default:
            }
        }catch (Exception e){

        }

        ((TextView)findViewById(R.id.amount)).setText("Rs."+amount);


        Toast.makeText(this, distance, Toast.LENGTH_SHORT).show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/RideEnd?rideid="+pref1.getString("rideid", null)+"&Amount="+amount+"&km="+distance.replace(" m","").replace(" km","")+"&dmobile="+pref.getString("mobile",null),
        //       StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/RideEnd?rideid="+rideid1+"&Amount="+amount+"&km="+distance.replace(" km","")+"&dmobile="+dMobile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("end ride yhi hai response....." + response);
                        findViewById(R.id.endRide).setVisibility(View.GONE);
                        flaggetcustomer=0;
                        ((ExpandableLayout)findViewById(R.id.expandable_layout2)).collapse();
                        timer1();
                        findViewById(R.id.payment).setVisibility(View.VISIBLE);



                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        // // Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                //Toast.makeText(ComplintActivity.this, d_id, Toast.LENGTH_SHORT).show();
                return map;
            }
        };

        queue.add(postRequest);
    }

    @Override
    public void onRoutingCancelled() {

    }

    public void paymentDone(String mode){
        String pmode=mode;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/paymentDone?rideid="+pref1.getString("rideid", null)+"&paymode="+pmode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("yhi hai response....." + response);
                        findViewById(R.id.payment).setVisibility(View.GONE);
                        editor1.clear();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        // Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                //   Toast.makeText(ComplintActivity.this, d_id, Toast.LENGTH_SHORT).show();
                return map;
            }
        };

        queue.add(postRequest);
    }

    public void gosilent(){
//        http://139.59.66.55/mobiapp/gosilent?dmobile=7000889041

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/gosilent?dmobile="+dMobile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("yhi hai response....." + response);
                        Go[] success=new Gson().fromJson(response,Go[].class);
                        List<Go> goList=new ArrayList<Go>(Arrays.asList(success));
                        try{
                        if (goList.get(0).getSuccess().equals("failure")==true){
                            Toast.makeText(MainActivity.this, "Server error please retry", Toast.LENGTH_SHORT).show();
                        }else {
                            timer.cancel();
                            Toast.makeText(MainActivity.this, "Go Silent", Toast.LENGTH_SHORT).show();
                        }
                        }catch (Exception e){

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        // Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                //   Toast.makeText(ComplintActivity.this, d_id, Toast.LENGTH_SHORT).show();
                return map;
            }
        };

        queue.add(postRequest);
    }

    public void golive(){
//        http://139.59.66.55/mobiapp/golive?dmobile=7000889041
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/golive?dmobile="+dMobile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("yhi hai response....." + response);
                        Go[] success=new Gson().fromJson(response,Go[].class);
                        List<Go> goList=new ArrayList<Go>(Arrays.asList(success));
                        try{
                            if (goList.get(0).getSuccess().equals("failure")==true){
                                Toast.makeText(MainActivity.this, "Server error please retry", Toast.LENGTH_SHORT).show();
                            }else {
                                timer.start();
                                Toast.makeText(MainActivity.this, "Go Live", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        // Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                //   Toast.makeText(ComplintActivity.this, d_id, Toast.LENGTH_SHORT).show();
                return map;
            }
        };

        queue.add(postRequest);
    }

    public void timer(){


        timer = new CountDownTimer(3000, 20) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                try{

                    if (flaggetcustomer==0){
                        getCustomers();
                    }
                    timer.start();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();
    }

    public void timer1(){


        timer1 = new CountDownTimer(3000, 10000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                getRideStatus();
                timer1.start();
            }
        }.start();
    }

    public void timer2(){


        timer2 = new CountDownTimer(3000, 10000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
         try{
             sendCurrentlocation();
         }catch(Exception e){
                }
                timer2.start();
            }
        }.start();
    }

    public void timer3(){


        timer3 = new CountDownTimer(3000, 20) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                try{
                    rideStatus();
                    timer3.start();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();
    }

    public void getRideStatus(){
         queue = Volley.newRequestQueue(this);
        postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/getRideStatus?rideid="+pref1.getString("rideid", null),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("status" + response);

                        final AllStatus[] allStatus= new Gson().fromJson(response,AllStatus[].class);
                        final List<AllStatus> main_list= new ArrayList<AllStatus>(Arrays.asList(allStatus));
//                        Active or
//                        OTP or
//                        OTPVERIFIED or
//                        Finished or
//                        Paid

                        String OTP= "OTP";
                        String Active= "Active";
                        String OTPVERIFIED= "OTPVERIFIED";
                        String Finished= "Finished";
                        String Paid= "Paid";
                        boolean isEqual = Paid.equals(main_list.get(0).getStatus());

                        System.out.println(isEqual); //true
                        if (isEqual==true){
                            findViewById(R.id.payment).setVisibility(View.GONE);
                            timer1.cancel();
                        }
                        else {
                            findViewById(R.id.payment).setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //             System.out.println("volley error" + error);
                        //Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();

                //    Toast.makeText(ComplintActivity.this, d_id, Toast.LENGTH_SHORT).show();
                return map;
            }
        };

        queue.add(postRequest);
    }
//    public void nav() {
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/getuserprofile?cmobile="+dMobile,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println("yhi hai response....." + response);
//
//                        try{
//                            ProfilePOJO[] profilePojo = new Gson().fromJson(response, ProfilePOJO[].class);
//                            List<ProfilePOJO> main_list= new ArrayList<ProfilePOJO>(Arrays.asList(profilePojo));
//                            Handler handler=new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                }
//                            },1000);
//                            ((TextView)findViewById(R.id.nav_name)).setText(main_list.get(0).getName());
//
//                            ((TextView)findViewById(R.id.nav_no)).setText(main_list.get(0).getCmobile());
//
//                        }catch (Exception e){
//
//                        }
//
//                    }
//
//
//
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Handler handler=new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                nav();
//                            }
//                        },5000);
//                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//        queue.add(postRequest);
//    }
}