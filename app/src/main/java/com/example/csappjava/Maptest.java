package com.example.csappjava;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

public class Maptest extends AppCompatActivity implements OnMapReadyCallback {

    private double cur_lat,cur_lon;
    private FusedLocationSource mLocationSource;
    private NaverMap mNaverMap;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private String mapdata;
    private String[] array;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maptest);

        Intent intent = getIntent();
        mapdata = intent.getStringExtra("mapdata");
        array = mapdata.split(",");
        cur_lat = Double.parseDouble(array[0]);
        cur_lon = Double.parseDouble(array[1]);

        //map();
        // 지도 객체 생성
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        // getMapAsync를 호출하여 비동기로 onMapReady 콜백 메서드 호출
        // onMapReady에서 NaverMap 객체를 받음
        mapFragment.getMapAsync(this);

        mLocationSource =
                new FusedLocationSource(this, PERMISSION_REQUEST_CODE);



    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        // 지도상에 마커 표시
        CameraPosition cameraPosition = naverMap.getCameraPosition();
        Marker marker = new Marker();
        marker.setPosition(new LatLng(cur_lat, cur_lon));
        marker.setMap(naverMap);

        mNaverMap = naverMap;
        mNaverMap.setLocationSource(mLocationSource);
        CameraPosition markerPosition = new CameraPosition(
                new LatLng(cur_lat, cur_lon),   // 위치 지정
                15,                                     // 줌 레벨
                0,                                       // 기울임 각도
                0                                     // 방향
        );

        naverMap.setCameraPosition(markerPosition);
        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {

            }
        });

    }


    //현재 나의 위치 구하는 기능
    private void map(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location loc_Current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        cur_lat = loc_Current.getLatitude();
        cur_lon = loc_Current.getLongitude();

        Log.d("LOGTEST",  cur_lat+ ", " + cur_lon);
    }
}