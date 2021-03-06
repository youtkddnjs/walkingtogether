package com.swsoft.walkingtogether;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CreatRoom extends AppCompatActivity {

    Button creat;
    Spinner spinner_personnel;
    Button time;
    int ahour=0;
    int aminute=0;
    EditText creatRoomTitle;
    SupportMapFragment mapFragment;
    LocationManager locationManager;
    GoogleMap mapMarker;

    TextView tv_hour;
    TextView tv_minute;

    double latitude;
    double longitude;
    double mapLatitude;
    double mapLongitude;
    int user;

    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatroom);

        //### Toolbar
        Toolbar createRoomToolBar = findViewById(R.id.creatRoomToolbar);
        setSupportActionBar(createRoomToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Toolbar ###

        tv_hour = findViewById(R.id.tv_hour);
        tv_minute = findViewById(R.id.tv_minute);
        time = findViewById(R.id.time);
        spinner_personnel = findViewById(R.id.spinner_personnel);
        creat = findViewById(R.id.creat);
        creatRoomTitle = findViewById(R.id.createRoomTitle);

        //Google Map ?????? ??????
        FragmentManager fragmentManager = getSupportFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.fragment_map);

        //????????????????????? ??????
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //????????????????????? ????????? ????????? ???????????? ???????????? ???????????? ??????

        Criteria criteria = new Criteria();// Criteria ?????????????????? ???????????? ?????? ?????? ?????????
        criteria.setCostAllowed(true); // ???????????? ?????? ??????
        criteria.setAccuracy(Criteria.NO_REQUIREMENT); //???????????? ?????????????
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT); //????????? ?????????
        criteria.setAltitudeRequired(true); //????????? ?????? ??????????????? ?????????????

        //bestProvider ????????? ????????? ????????????
        String bestProvider = locationManager.getBestProvider(criteria, true);

        //????????????????????? ?????? ??????????????? ??????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //????????? ???????????? ???????????? ???????????? ??????
            int checkResult = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkResult == PackageManager.PERMISSION_DENIED) { //?????? ???????????????
                String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, 0);
            }//if
        }//if


        //### ?????? ?????? spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CreatRoom.this, R.array.spinner_personner_array,R.layout.spinner_selected);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_personnel.setAdapter(adapter);

        spinner_personnel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); //?????? ?????? spinner ###

        //###?????? ??????
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(CreatRoom.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay == 0 ){
                            tv_hour.setText(hourOfDay+"0");
                        }else{tv_hour.setText(hourOfDay+"");}
                        if(minute == 0){
                            tv_minute.setText(minute+"0");
                        }else{tv_minute.setText(minute+"");}
                    }
                },ahour,aminute,false);
                tpd.show();
            }
        }); //?????? ??????###

        
        //?????? ????????????
        Location location = null;

        //????????????????????? ??????
        if (locationManager.isProviderEnabled("gps")) {
            //??????????????? ??????????????? ????????? ????????? ????????? ????????? ???????????? ?????? ????????? ????????? ?????????.
            if (ActivityCompat.checkSelfPermission(CreatRoom.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreatRoom.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //5????????? ?????? 2M?????????????????? ????????? locationListener()??? ??????.
            locationManager.requestLocationUpdates("gps",5000,2, locationListener);
            location = locationManager.getLastKnownLocation("gps"); //????????????????????? gps ??????
        }else if(locationManager.isProviderEnabled("network")){
            locationManager.requestLocationUpdates("network",5000,2, locationListener);
            location = locationManager.getLastKnownLocation("network"); //????????????????????? network ??????
        }

        //????????? ??????
        if(location == null) {
            Toast.makeText(CreatRoom.this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
        }
        else{
                    latitude = location.getLatitude();//??????
                    longitude = location.getLongitude();//??????
        }



        //????????? ??????(?????? ?????????)?????? ?????????????????? ??????????????? ??????
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                LatLng latLng = new LatLng(latitude,longitude);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                if (ActivityCompat.checkSelfPermission(CreatRoom.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreatRoom.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);
                mapMarker = googleMap;
                mapMarker.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {

                        mapMarker.clear();

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title("??????");

                        mapLatitude = latLng.latitude; // ??????
                        mapLongitude = latLng.longitude; // ??????
                        // ????????? ?????????(????????? ?????????) ??????
//                        markerOptions.snippet(latitude.toString() + ", " + longitude.toString());
                        // LatLng: ?????? ?????? ?????? ?????????
                        markerOptions.position(new LatLng(mapLatitude, mapLongitude));

                        mapMarker.addMarker(markerOptions);

                    }//onMapClick
                });//setOnMapClickListener
            }
        });//map

        //????????? ???????????? ??????
        locationManager.removeUpdates(locationListener);

        //???????????????
        creat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("ddhhss");

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                DatabaseReference createRoomRef = firebaseDatabase.getReference("room");

                String title = creatRoomTitle.getText().toString();
                String hour = tv_hour.getText().toString();
                String minute = tv_minute.getText().toString();
                String latitude = mapLatitude+"";
                String longitude = mapLongitude+"";
                String chatroom = hour+minute+sdf.format(new Date())+"";
                ChatRoom.name = chatroom;
                CreatRoomItem item = new CreatRoomItem(title, hour, minute, latitude, longitude,chatroom, user);

                createRoomRef.child(chatroom).setValue(item);
                Log.i("creat name:", chatroom);
//                DatabaseReference readyRef = firebaseDatabase.getReference("ready/"+chatroom);
//                readyRef.setValue(user);

                Intent intent = new Intent(getApplicationContext(), Waiting.class);

                intent.putExtra("chatroom",chatroom);

                startActivity(intent);

                finish();
            }
        });
    } //oncreate

    //locationListener
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    };//locationListener

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), RoomList.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), RoomList.class);
        startActivity(intent);
        super.onBackPressed();
    }

    //requestPermissions()???????????? ????????? ?????? ????????? ?????????????????? ????????? ????????????
    //???????????? ???????????? ???????????????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 0:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(CreatRoom.this, "????????????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                }else{
                    finish();
                }
                break;
        }

    }//onRequestPermissionsResult

}//createroom



