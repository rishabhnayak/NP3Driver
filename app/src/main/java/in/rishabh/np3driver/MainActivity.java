package in.rishabh.np3driver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import in.rishabh.np3driver.Pojo.GetCustomers;
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
    private static final int[] COLORS = new int[]{R.color.route, R.color.routeborder, R.color.colorPrimary, R.color.colorPrimary, R.color.primary_dark_material_light};
    private String rid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



//        FloatingActionButton fab =  findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
        getCustomers();

      findViewById(R.id.paymentdone).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

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

        if (id == R.id.action_settings) {
            return true;
        }

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
            startActivity(new Intent(getApplicationContext(),YourRidesActivity.class));
        }else if (id == R.id.nav_send) {

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
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);rlp.setMargins(0,0,30,30);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timer();
                locationButton.performClick();
//                Toast.makeText(
//                 MainActivity.this, "working", Toast.LENGTH_SHORT).show();
            }
        },6000);
    }

    public void timer(){


        timer = new CountDownTimer(3000, 20) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                try{
                    sendCurrentlocation();
                    getCustomers();
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rideStatus();
                        }
                    },1000);
                    timer.start();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();
    }

    public void sendCurrentlocation(){
      //  System.out.println(String.valueOf(mMap.getMyLocation().getLatitude()));
   try{     RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
       System.out.println(String.valueOf(mMap.getMyLocation().getLatitude())+""+lon);
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/driverlocationupdate?driverlon="+mMap.getMyLocation().getLatitude()+"&driverlat="+mMap.getMyLocation().getLongitude()+"&dmobile="+dMobile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("yhi hai response....." + response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                //        Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
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
    }catch (Exception e){

   }
    }

    public void getCustomers(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/checkride?dmobile="+dMobile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("yhi hai response....." + response);
                        Gson gson=new Gson();
                        final GetCustomers success=gson.fromJson(response, GetCustomers.class);
                        String result= success.getSuccess();

                        if (success.getCustomer_mobile()==null){

                        }else {
                            ((TextView)findViewById(R.id.customerMobile)).setText("Mobile No."+success.getCustomer_mobile());
                        }

                        switch (result){
                            case "success" :

                                success.getCustomer_dlat();
                                success.getCustomer_dlon();
                                success.getCustomer_mobile();
                                success.getDriver_mobile();
                                rid=success.getRideid();
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

                                             //   Toast.makeText(MainActivity.this, String.valueOf(route.get(0).getDistanceText()), Toast.LENGTH_SHORT).show();
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




                              //  Toast.makeText(MainActivity.this, "customer coordinates "+success.getCustomer_olat()+" "+success.getCustomer_olon(), Toast.LENGTH_SHORT).show();

                                ((ExpandableLayout)findViewById(R.id.expandable_layout2)).expand();
                                findViewById(R.id.acceptRequest).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(MainActivity.this, "Otp send successfully", Toast.LENGTH_SHORT).show();
                                        mMap.clear();
                                        ((ExpandableLayout)findViewById(R.id.expandable_layout2)).collapse();
                                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/sendotp?cmobile="+success.getCustomer_mobile()+"&dmobile="+success.getDriver_mobile()+"&rideid="+success.getRideid(),
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        System.out.println("yhi hai response....." + response);

                                                        Gson gson1=new Gson();
                                                        Success success1=gson1.fromJson(response,Success.class);

                                                        switch(success1.getSuccess()){
                                                            case "success":
                                                                findViewById(R.id.popup).setVisibility(View.VISIBLE);
                                                               findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View view) {
                                                                       String otp = ((EditText)findViewById(R.id.otp)).getText().toString();
                                                                       System.out.println(otp);
                                                                       System.out.println(success.getRideid());
//                                                                       Toast.makeText(MainActivity.this, otp+success.getRideid(), Toast.LENGTH_SHORT).show();
                                                                   sendOtp(otp,success.getRideid());

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
                                                        Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
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
                                });
                                findViewById(R.id.cancelRequest).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ((ExpandableLayout)findViewById(R.id.expandable_layout2)).collapse();
                                        getCustomers();
                                        cancelRide();
                                    }
                                });
                              //  Toast.makeText(MainActivity.this, "aa gya", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
try{
    getCustomers();
}catch (Exception e){

}
                                            }
                                        },
                                        5000
                                );

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        getCustomers();
                                    }
                                },
                                5000
                        );
                     //   Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();

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
    public void checkLocation(){
        mMap.getMyLocation().getLongitude();
        if (String.valueOf(mMap.getMyLocation().getLongitude())==null){
        checkLocation();
        }else {
            locationButton.performClick();
        }
    }

    public void sendOtp(String otp, final String rideid){

       final String rid=rideid,Otp=otp;
    //    Toast.makeText(this, rid+otp, Toast.LENGTH_SHORT).show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/enterotp?rideid="+rid+"&otp="+Otp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("yhi hai response....." + response);
                      try {
                          Gson gson=new Gson();
                          origin=new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude());
                        Success successObject=gson.fromJson(response,Success.class);
                        String success=successObject.getSuccess();
                        LatLong ll=gson.fromJson(response,LatLong.class);
                        cMobile=ll.getCmobile();
                        rideid1=ll.getRideid();

                        switch (success){
                            case "success":
                                findViewById(R.id.endRide).setVisibility(View.VISIBLE);
                                findViewById(R.id.endRide).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        findViewById(R.id.endRide).setVisibility(View.GONE);
                                        destination=new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude());
//                                        Toast.makeText(MainActivity.this, "Ride Ended", Toast.LENGTH_SHORT).show();
                                      if (origin.latitude==destination.latitude&&destination.latitude==destination.longitude){
                                          Toast.makeText(MainActivity.this, "Source and Destination place can't be the same.", Toast.LENGTH_SHORT).show();
                                      }else {
                                          try{
                                              rideEnd();
                                          }catch (Exception e){

                                          }
                                      }

                                    }
                                });

                                findViewById(R.id.popup).setVisibility(View.GONE);
                                String uri = "http://maps.google.com/maps?saddr="+""+ll.getOlat()+","+ll.getOlon()+""+"&daddr="+""+ll.getDlat()+","+ll.getDlon()+"";
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                                startActivity(intent);
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Server Error Please Retry", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                      }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("otp",Otp);
                map.put("rideid ",rid);
                //    Toast.makeText(ComplintActivity.this, d_id, Toast.LENGTH_SHORT).show();
                return map;
            }
        };

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
                        Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
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
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/checkOngoingRide?dMobile="+dMobile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                            System.out.println("thats it....." + response);
                        try{
                            Gson gson=new Gson();
                            RideStatus rideStatus=gson.fromJson(response,RideStatus.class);

                            if (rideStatus.getDmobile().length()==0){

                            }else {
                                ((ExpandableLayout)findViewById(R.id.expandable_layout2)).expand();
                                ((TextView)findViewById(R.id.customerMobile)).setText(rideStatus.getCmobile());
                                findViewById(R.id.acceptRequest).setVisibility(View.GONE);
                                findViewById(R.id.cancelRequest).setVisibility(View.GONE);
                                findViewById(R.id.endRide).setVisibility(View.VISIBLE);
//                                ((TextView)findViewById(R.id.customerMobile)).setText(rideStatus.getCmobile());
                            }
                        }catch (Exception e){

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();

    }
    public void rideEnd(){
//        Toast.makeText(this, String.valueOf(origin.longitude+","+origin.longitude), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, String.valueOf(destination.longitude+","+destination.longitude), Toast.LENGTH_SHORT).show();

//        LatLng a=new LatLng(21.2692645,81.668486);
//        LatLng b=new LatLng(21.2603002,81.6604469);
        ((ExpandableLayout)findViewById(R.id.expandable_layout2)).collapse();
        Routing routing = new Routing.Builder()
                .key("AIzaSyCv_imK5ydtkdWnGJP1Dbt-DT07UdvyDeo")
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(origin, destination)
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
        String distance = route.get(minIndex).getDistanceText();
        String duration = route.get(minIndex).getDurationText();
        int routeno = minIndex;

       // Toast.makeText(this, distance+ "   " + duration, Toast.LENGTH_SHORT).show();
        RequestQueue queue = Volley.newRequestQueue(this);



       String amount = "0";
        String price=distance.replace(" km","");
        try{
            switch (travelmode){
                case "0":
//                    Double price=35+(distance*6)+2;
                    amount=String.valueOf(35+(Math.round(Double.valueOf(price)*6))+(Math.round(Double.valueOf(price)*2)));
                    break;
                case "1":
//                    Double price=45+(distance*10)+2;
                    amount=String.valueOf(45+(Math.round(Double.valueOf(price)*10))+(Math.round(Double.valueOf(price)*2)));
                    break;
                case "2":
//                    Double price=55+(distance*12)+2;
                    amount=String.valueOf(55+Math.round(Double.valueOf(price)*12)+(Math.round(Double.valueOf(price)*2)));
                    break;
                case "3":
//                    Double price=10+(distance*4)+1.5;
                    amount=String.valueOf(10+(Math.round(Double.valueOf(price)*4))+(Math.round(Double.valueOf(price)*2)));
                    break;
                case "4":
//                    Double price=10+(distance*3)+1.5;
                    amount=String.valueOf(2+(Math.round(Double.valueOf(price)*3))+(Math.round(Double.valueOf(price)*2)));
                    break;
                default:
            }
        }catch (Exception e){

        }

        ((TextView)findViewById(R.id.amount)).setText("Rs."+amount);




        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/RideEnd?rideid="+rideid1+"&Amount="+amount+"&km="+distance.replace(" km","")+"&dmobile"+dMobile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("yhi hai response....." + response);

                        findViewById(R.id.payment).setVisibility(View.VISIBLE);
                        findViewById(R.id.paymentdone).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                findViewById(R.id.payment).setVisibility(View.GONE);
                                findViewById(R.id.paymentdone).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        paymentDone();
                                    }
                                });
                            }
                        });

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
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

    public void paymentDone(){

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://139.59.66.55/mobiapp/paymentDone?rideid="+rid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("yhi hai response....." + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
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
}
