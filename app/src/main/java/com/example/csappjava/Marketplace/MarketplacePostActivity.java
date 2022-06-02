package com.example.csappjava.Marketplace;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.csappjava.FirebaseID;
import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketplacePostActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    private DatabaseReference mDatabaseRef;     // 실시간 데이터베이스
    private DatabaseReference mDatabaseRef2;     // 실시간 데이터베이스
    private EditText mTitle, mContents, mPrice;
    private ImageView imageView;
    private String stringurl;
    private TextView bname,bprice,bpublisher;

    private DecimalFormat decimalFormat = new DecimalFormat("#,###");       //원화 단위 표시
    private String result="";
    Uri imgUri;

    private List<String> list;          // 데이터를 넣은 리스트변수
    private Button searchbt;
    private TextView searchtv, searchposttv;

    private List<String> list2;          // 데이터를 넣은 리스트변수
    private Button searchbt2, list_seach2;
    private TextView searchtv2, addtv2;
    private RadioGroup radioGroup;

    private Button addlesson;
    private TextView test1_1, test2_1, setlesson, setprofessor;
    private EditText test1_2, test2_2;

    Dialog dialogsearch;    //검색
    Dialog dialogsearch2;    //검색

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace_post);

        mTitle = findViewById(R.id.market_post_title_edit);
        mContents = findViewById(R.id.market_post_contents_edit);
        imageView = findViewById(R.id.marketplace_image);
        mPrice = findViewById(R.id.market_post_price);
        mPrice.addTextChangedListener(watcher);
        bname = findViewById(R.id.bookname);
        bpublisher = findViewById(R.id.bookpublisher);
        bprice = findViewById(R.id.bookprice);
        setlesson = findViewById(R.id.lesson);
        setprofessor = findViewById(R.id.lesson2);

        Intent intent = getIntent();
        String firstpath = Mydata.getFirstpath();
        String secondpath = Mydata.getSecondpath();
        String b1 = intent.getStringExtra("b1");
        String b2 = intent.getStringExtra("b2");
        String b3 = intent.getStringExtra("b3");
        bname.setText(b2);
        bprice.setText(b1);
        bpublisher.setText(b3);

        FirebaseUser user = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("schooldata");
        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("lesson");

        dialogsearch = new Dialog(MarketplacePostActivity.this);       // Dialog 초기화
        dialogsearch.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialogsearch.setContentView(R.layout.activity_marketplace_post_search1);             // xml 레이아웃 파일과 연결
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogsearch.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = dialogsearch.getWindow();
        window.setAttributes(lp);

        dialogsearch2 = new Dialog(MarketplacePostActivity.this);       // Dialog 초기화
        dialogsearch2.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialogsearch2.setContentView(R.layout.activity_marketplace_post_search2);             // xml 레이아웃 파일과 연결
        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
        lp2.copyFrom(dialogsearch.getWindow().getAttributes());
        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window2 = dialogsearch2.getWindow();
        window2.setAttributes(lp2);

        Button imagebt = (Button) findViewById(R.id.market_post_image_button) ;
        imagebt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,10);
                imageView.setEnabled(false);
            }
        }) ;

        Button savebt = (Button) findViewById(R.id.market_post_save_button) ;
        savebt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() != null){
                    SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss");
                    String filename= sdf.format(new Date())+ ".png";//현재 시간과 사용자 고유id로 파일명 지정
                    String user = mAuth.getCurrentUser().getUid().toString();

                    if(imageView.isEnabled()) {     //이미지 없을 때
                        stringurl = "null";
                    }
                    else{       //이미지가 있을 때
                        StorageReference imgRef= firebaseStorage.getReference("/images/marketplace/"+user+filename);

                        UploadTask uploadTask =imgRef.putFile(imgUri);
                        //stringurl = imgRef.getDownloadUrl().toString();
                        stringurl = "/images/marketplace/"+user+filename;
                    }

                    String postId = mStore.collection(FirebaseID.postMarket).document(firstpath).collection
                            (secondpath).document().getId(); //중복 방지
                    Map<String, Object> data = new HashMap<>();
                    data.put(FirebaseID.postId,postId.toString());                          //문서id
                    data.put(FirebaseID.userId,mAuth.getCurrentUser().getUid());        //id값
                    data.put(FirebaseID.title,mTitle.getText().toString());                 //제목
                    data.put(FirebaseID.price,mPrice.getText().toString());                 //가격
                    data.put(FirebaseID.contents,mContents.getText().toString());           //내용
                    data.put(FirebaseID.img,stringurl);                                     //이미지url
                    data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());           //타임
                    data.put(FirebaseID.nickname, Mydata.getMynickname());           //타임
                    data.put(FirebaseID.transaction, "false");           //거래완료 유무

                    mStore.collection(FirebaseID.postMarket).document(firstpath).collection(secondpath).document(postId).set(data, SetOptions.merge());  //값넣기
                    Intent intent = new Intent(MarketplacePostActivity.this, MarketplaceActivity.class);
                    finish();
                    startActivity(intent);

                }
            }
        });

        Button barcoadbt = (Button) findViewById(R.id.market_post_barcode_button) ;
        barcoadbt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MarketplacePostActivity.this, BarcoadActivity.class);
                startActivity(intent3);
            }
        }) ;

        /*Button searchpostbt = (Button) findViewById(R.id.post_searchbt) ;
        searchpostbt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogsearch();
            }
        }) ;

        Button searchpostbt2 = (Button) findViewById(R.id.post_searchbt2) ;
        searchpostbt2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogsearch2();
            }
        }) ;*/

    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(!TextUtils.isEmpty(charSequence.toString()) && !charSequence.toString().equals(result)){
                result = decimalFormat.format(Double.parseDouble(charSequence.toString().replaceAll(",","")));
                mPrice.setText(result);
                mPrice.setSelection(result.length());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {                      //갤러리에서 이미지 가져오기
            case 10:
                if (resultCode == RESULT_OK) {
                    //선택한 이미지의 경로 얻어오기
                    imgUri = data.getData();
                    Glide.with(this).load(imgUri).into(imageView);
                }
                break;
        }
    }

    public void showDialogsearch(){
        dialogsearch.show(); // 다이얼로그 띄우기

        // 리스트를 생성한다.
        list = new ArrayList<String>();

        searchbt = dialogsearch.findViewById(R.id.list_seach);
        searchtv = dialogsearch.findViewById(R.id.tv_search);
        //searchposttv = findViewById(R.id.post_searchtv);

        searchtv.setText("학과 검색");

        // 리스트에 검색될 데이터(단어)를 추가한다.
        settingList();

        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) dialogsearch.findViewById(R.id.autoCompleteTextView);

        // AutoCompleteTextView 에 아답터를 연결한다.
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,  list ));


        searchbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchet = autoCompleteTextView.getText().toString();
                if(list.contains(searchet)){
                    Toast.makeText(MarketplacePostActivity.this,"리스트에 있습니다.",Toast.LENGTH_SHORT).show();
                    searchposttv.setText(searchet);
                    dialogsearch.dismiss();
                }
                else{
                    Toast.makeText(MarketplacePostActivity.this,"리스트에 없습니다.",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void settingList(){
        mDatabaseRef.child(Mydata.getMyschool()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    //---------------------
                    mDatabaseRef.child(Mydata.getMyschool()).child(snapshot.getKey()).child("본분교명").addValueEventListener(new ValueEventListener() {        //학교명 찾는 코드
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshots) {
                            String value = snapshots.getValue(String.class);
                            //Log.d("LOGVALUE", "ValueEventListener : " + snapshot.getKey() + "value" + value);
                            if(value != null){
                                if(value.equals(Mydata.getMycampus())) {               //분교일경우
                                    list.add(snapshot.getKey());
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
    }

    public void showDialogsearch2(){
        dialogsearch2.show(); // 다이얼로그 띄우기


        // 리스트를 생성한다.
        list2 = new ArrayList<String>();

        list_seach2 = dialogsearch2.findViewById(R.id.list_seach2);
        searchtv2 = dialogsearch2.findViewById(R.id.tv_search2);
        addtv2 = dialogsearch2.findViewById(R.id.list_add);


        test1_1 = dialogsearch2.findViewById(R.id.test1_1);
        test2_1 = dialogsearch2.findViewById(R.id.test2_1);
        test1_2 = dialogsearch2.findViewById(R.id.test1_2);
        test2_2 = dialogsearch2.findViewById(R.id.test2_2);
        addlesson = dialogsearch2.findViewById(R.id.addlesson);

        searchtv2.setText(Mydata.getMydepartment() + " 수업 검색");

        test1_1.setVisibility(View.GONE);
        test2_1.setVisibility(View.GONE);
        test1_2.setVisibility(View.GONE);
        test2_2.setVisibility(View.GONE);
        addlesson.setVisibility(View.GONE);


        radioGroup = dialogsearch2.findViewById(R.id.radioGroup);
        radioGroup.check(R.id.rg_btn1);
        settingList2("강의");

        final AutoCompleteTextView autoCompleteTextView2 = (AutoCompleteTextView) dialogsearch2.findViewById(R.id.autoCompleteTextView2);
        // AutoCompleteTextView 에 아답터를 연결한다.
        autoCompleteTextView2.setAdapter(new ArrayAdapter<String>(MarketplacePostActivity.this, android.R.layout.simple_dropdown_item_1line, list2 ));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rg_btn1){
                    list2.clear();
                    settingList2("강의");                                      // 리스트에 검색될 데이터(단어)를 추가한다.
                }
                else if(checkedId == R.id.rg_btn2){
                    list2.clear();
                    settingList2("교수");
                }
                // AutoCompleteTextView 에 아답터를 연결한다.
                autoCompleteTextView2.setAdapter(new ArrayAdapter<String>(MarketplacePostActivity.this, android.R.layout.simple_dropdown_item_1line, list2 ));
            }
        });

        list_seach2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchet = autoCompleteTextView2.getText().toString();
                if(list2.contains(searchet)){
                    Toast.makeText(MarketplacePostActivity.this,"리스트에 있습니다.",Toast.LENGTH_SHORT).show();
                    String result = autoCompleteTextView2.getText().toString();
                    String[] array = result.split(",");
                    //setlesson.setText(result);
                    setlesson.setText(array[0]);
                    setprofessor.setText(array[1]);
                    dialogsearch2.dismiss();
                }
                else{
                    Toast.makeText(MarketplacePostActivity.this,"리스트에 없습니다.",Toast.LENGTH_SHORT).show();
                }

            }
        });

        addtv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(test1_1.getVisibility() == View.GONE){
                    test1_1.setVisibility(View.VISIBLE);
                    test2_1.setVisibility(View.VISIBLE);
                    test1_2.setVisibility(View.VISIBLE);
                    test2_2.setVisibility(View.VISIBLE);
                    addlesson.setVisibility(View.VISIBLE);
                }
                else{
                    test1_2.setText(null);
                    test2_2.setText(null);
                    test1_1.setVisibility(View.GONE);
                    test2_1.setVisibility(View.GONE);
                    test1_2.setVisibility(View.GONE);
                    test2_2.setVisibility(View.GONE);
                    addlesson.setVisibility(View.GONE);
                }

            }
        });

        addlesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lesson = test1_2.getText().toString();
                String Professor = test2_2.getText().toString();

                if(test1_2.getText().toString().equals("") || test2_2.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"입력되지않았습니다.",Toast.LENGTH_SHORT).show();//토스메세지 출력
                }
                else{
                    mDatabaseRef2.child(Mydata.getMyschool()).child(lesson).child("수업명").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String value = snapshot.getValue(String.class);

                            if(value!=null){
                                Toast.makeText(getApplicationContext(),"이미 존재하는 수업입니다.",Toast.LENGTH_SHORT).show();//토스메세지 출력
                            }
                            else{
                                commentaddDialog();
                                autoCompleteTextView2.setAdapter(new ArrayAdapter<String>(MarketplacePostActivity.this, android.R.layout.simple_dropdown_item_1line, list2 ));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void settingList2(String name){
        mDatabaseRef2.child(Mydata.getMyschool()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mDatabaseRef2.child(Mydata.getMyschool()).child(snapshot.getKey()).child("본분교명").addValueEventListener(new ValueEventListener() {        //학교명 찾는 코드
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshots) {
                            String value = snapshots.getValue(String.class);
                            //Log.d("LOGVALUE", "ValueEventListener : " + snapshot.getKey() + "value" + value);
                            if(value != null){
                                if(value.equals(Mydata.getMycampus())) {
                                    Log.d("LOGVALUE", "name" + name);
                                    mDatabaseRef2.child(Mydata.getMyschool()).child(snapshot.getKey()).child(name).addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot sshot) {
                                            String value2 = sshot.getValue(String.class);
                                            list2.add(value2);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
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
    }


    void commentaddDialog(){
        String lesson = test1_2.getText().toString();
        String Professor = test2_2.getText().toString();

        LessonAccount account = new LessonAccount();

        account.set강의(lesson+","+Professor);
        account.set교수(Professor+","+lesson);
        account.set본분교명(Mydata.getMycampus());
        account.set학과명(Mydata.getMydepartment());

        AlertDialog.Builder builder = new AlertDialog.Builder(MarketplacePostActivity.this)
                .setTitle("추가")
                .setMessage("수업명 : " + lesson + "\n" + "교수명 : " + Professor + "\n이 맞습니까?")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"취소하였습니다.",Toast.LENGTH_SHORT).show();//토스메세지 출력

                    }
                })
                .setNegativeButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabaseRef2.child(Mydata.getMyschool()).child(lesson).setValue(account);
                        Toast.makeText(getApplicationContext(),"수업이 추가되었습니다.",Toast.LENGTH_SHORT).show();//토스메세지 출력

                        test1_2.setText(null);
                        test2_2.setText(null);
                        test1_1.setVisibility(View.GONE);
                        test2_1.setVisibility(View.GONE);
                        test1_2.setVisibility(View.GONE);
                        test2_2.setVisibility(View.GONE);
                        addlesson.setVisibility(View.GONE);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public String clock(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getTime = dateFormat.format(date);

        return getTime;
    }

}