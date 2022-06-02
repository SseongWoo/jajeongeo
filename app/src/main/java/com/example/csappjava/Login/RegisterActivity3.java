package com.example.csappjava.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csappjava.Community.CommunityActivity;
import com.example.csappjava.FirebaseID;
import com.example.csappjava.MainLoadingActivity;
import com.example.csappjava.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity3 extends AppCompatActivity {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private DatabaseReference mDatabaseRef;     // 실시간 데이터베이스
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();         // 파이어베이스 인증
    private String school2;
    private List<String> FirstspinnerArray =  new ArrayList<String>();
    private List<String> NullspinnerArray =  new ArrayList<String>();
    private List<String> spinnerArray =  new ArrayList<String>();
    private List<String> spinnerArray1 =  new ArrayList<String>();
    private List<String> spinnerArray2 =  new ArrayList<String>();
    private List<String> spinnerArray3 =  new ArrayList<String>();
    private List<String> spinnerArray4 =  new ArrayList<String>();
    ArrayAdapter<String> adapter2;
    TextView schoolname;
    EditText nicket;
    Button completregisterbt;
    String sc0 = "",sc1 = "",sc2 = "",sc3 = "",sc4 = "";
    int one = 0, two = 0, three = 0, four = 0, zero = 0;
    private String department;
    private String campus;
    String affiliation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        Intent intent = getIntent();
        String school = intent.getStringExtra("school");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("schooldata");

        mDatabaseRef.child(school).child("학교명").addValueEventListener(new ValueEventListener() {        //학교명 찾는 코드
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                school2 = value;
                schoolname = findViewById(R.id.schoolname);
                schoolname.setText(school2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        mDatabaseRef.child(school).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    //---------------------
                    mDatabaseRef.child(school).child(snapshot.getKey()).child("본분교명").addValueEventListener(new ValueEventListener() {        //학교명 찾는 코드
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshots) {
                            String value = snapshots.getValue(String.class);
                            //Log.d("LOGVALUE", "ValueEventListener : " + snapshot.getKey() + "value" + value);
                            if(value != null){
                                if(sc0.equals("") || sc0.equals(value)){
                                    sc0 = value;
                                }
                                else
                                {
                                    if(!sc0.equals(value) && sc1.equals("")){
                                        sc1 = value;
                                    }
                                    else{
                                        if(!sc1.equals(value) && sc2.equals("")){
                                            sc2 = value;
                                        }
                                        else{
                                            if(!sc2.equals(value) && sc3.equals("")){
                                                sc3 = value;
                                            }
                                            else{
                                                if(!sc3.equals(value) && sc4.equals("")){
                                                    sc4 = value;
                                                }
                                            }
                                        }
                                    }
                                }

                                if(value.equals(sc1)){               //분교일경우
                                    spinnerArray1.add(snapshot.getKey());
                                    one++;
                                    if(one == 1){                                       //값을 한번만 넣기 위해서 사용
                                        FirstspinnerArray.add(sc1);
                                        //Log.d("캠퍼스", "1교 " + sc1 + ", ");
                                    }
                                }
                                else if(value.equals(sc2)){          //제2캠퍼스일경우ㅠ
                                    spinnerArray2.add(snapshot.getKey());
                                    two++;
                                    if(two == 1){
                                        FirstspinnerArray.add(sc2);
                                        //Log.d("캠퍼스", "2교 " + sc2 + ", ");
                                    }
                                }
                                else if(value.equals(sc3)){          //제3캠퍼스일경우
                                    spinnerArray3.add(snapshot.getKey());
                                    three++;
                                    if(three == 1){
                                        FirstspinnerArray.add(sc3);
                                        //Log.d("캠퍼스", "3교 " + sc3 + ", ");
                                    }
                                }
                                else if(value.equals(sc4)){          //제4캠퍼스일경우
                                    spinnerArray4.add(snapshot.getKey());
                                    four++;
                                    if(four == 1){
                                        FirstspinnerArray.add(sc4);
                                        //Log.d("캠퍼스", "4교 " + sc4 + ", ");
                                    }
                                }
                                else{                                               //본교일경우
                                    spinnerArray.add(snapshot.getKey());
                                    zero++;
                                    if(zero == 1){
                                        FirstspinnerArray.add(sc0);
                                        //Log.d("캠퍼스", "본교 " + sc0 + ", ");
                                    }
                                }
                            }
                        }
                       @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirstspinnerArray.add("캠퍼스");
        //FirstspinnerArray.add(FirebaseID.school0);
        //FirstspinnerArray.add(sc0);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, FirstspinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner_school);
        sItems.setAdapter(adapter);

        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, NullspinnerArray);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems2 = (Spinner) findViewById(R.id.spinner_department);
        sItems2.setAdapter(adapter2);

        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(RegisterActivity3.this,"선택된 아이템 : "+sItems.getItemAtPosition(i),Toast.LENGTH_SHORT).show();
                sp(sItems.getItemAtPosition(i).toString());
                campus = sItems.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sItems2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                department = sItems2.getItemAtPosition(i).toString();
                mDatabaseRef.child(school).child(department).child("표준분류소계열").addValueEventListener(new ValueEventListener() {        //학과명을 보고 학과계열을 찾는 코드
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.getValue(String.class);
                        moveaffiliation(value);
                        //Log.d("ENDLOG1", "value : " + value);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        nicket = findViewById(R.id.register_nick);
        completregisterbt = findViewById(R.id.btn_completregister);
        completregisterbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nickedit = nicket.getText().toString();
                //Log.d("ENDLOG1", "department : " + department + "nick" + nickedit);
                if(department != null && nicket.length() > 1){
                    String userdata = user.getUid().toString();

                    Map<String,Object> userMap3 = new HashMap<>();
                    userMap3.put(FirebaseID.department, department);
                    userMap3.put(FirebaseID.nickname, nickedit);
                    userMap3.put(FirebaseID.schoolKR,school2);
                    userMap3.put(FirebaseID.campus,campus);
                    userMap3.put(FirebaseID.affiliation,affiliation);
                    Log.d("ENDLOG1", "affiliation" + affiliation);
                    //Log.d("ENDLOG2", "user : " + user.getUid() + "usermap" + userMap3);
                    mStore.collection(FirebaseID.user).document(userdata).update(userMap3);
                    finish();
                    Intent intent = new Intent(RegisterActivity3.this, MainLoadingActivity.class);
                    startActivity(intent);
                    Toast.makeText(RegisterActivity3.this,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegisterActivity3.this,"양식이 틀렸습니다 다시 확인해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void sp(String item){

        if(item.equals(sc0)){
            adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        }
        else if(item.equals(sc1)){
            adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray1);
        }
        else if(item.equals(sc2)){
            adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray2);
        }
        else if(item.equals(sc3)){
            adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray3);
        }
        else if(item.equals(sc4)){
            adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray4);
        }
        else {
            adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, NullspinnerArray);
        }
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems2 = (Spinner) findViewById(R.id.spinner_department);
        sItems2.setAdapter(adapter2);

    }
    void moveaffiliation(String a){
        affiliation = a;
        //Log.d("ENDLOG1", "affiliation" + affiliation + " a : " + a);
    }
}