package com.example.csappjava;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.example.csappjava.Community.CommunityActivity2;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

public class Mappoint extends AppCompatActivity implements OnMapReadyCallback {

    private double cur_lat,cur_lon;
    private FusedLocationSource mLocationSource;
    private NaverMap mNaverMap;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private String mapdata;
    private String[] array;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maptest);

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
        Marker marker = new Marker();
        CameraPosition cameraPosition = naverMap.getCameraPosition();
        mNaverMap = naverMap;
        UiSettings uiSettings = mNaverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true); // 기본값 : false

        mNaverMap.setLocationSource(mLocationSource);


        /*CameraPosition markerPosition = new CameraPosition(
                new LatLng(37.45144690988141, 129.15468689795125),   // 위치 지정
                15,                                     // 줌 레벨
                0,                                       // 기울임 각도
                0                                     // 방향
        );*/

        naverMap.setCameraPosition(cameraPosition);
        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {

                marker.setPosition(new LatLng(latLng.latitude, latLng.longitude));
                marker.setMap(naverMap);
            }
        });

        marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                Log.d("LOGTEST",  marker.getPosition().latitude+ ", " + marker.getPosition().longitude);
                Dialog(marker.getPosition().latitude, marker.getPosition().longitude);
                return false;
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // request code와 권한획득 여부 확인
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }

    void Dialog(double point1, double point2){
        AlertDialog.Builder builder = new AlertDialog.Builder(Mappoint.this)
                .setTitle("위치전송")
                .setMessage("해당위치를 전송하시겠습니까?")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strTemp1 = Double.toString(point1);
                        String strTemp2 = Double.toString(point2);
                        Mydata.setSend("mapmessage"+point1+","+point2);
                        finish();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}