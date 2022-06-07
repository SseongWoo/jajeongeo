package com.example.csappjava.Marketplace;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.csappjava.Chatting.ChatActivity_2;
import com.example.csappjava.FirebaseID;
import com.example.csappjava.Mydata;
import com.example.csappjava.ProgressDialog;
import com.example.csappjava.R;
import com.example.csappjava.adapters.ImageSliderAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MarketplaceActivity2_1 extends AppCompatActivity {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    private Button btn_chatstart, postend;
    private String postid,userid,title,contents,img,time,firstpath,secondpath,price,transaction;

    private ViewPager2 sliderViewPager;
    private String[] array;
    private LinearLayout layoutIndicator;
    private ImageSliderAdapter adapter; // 리사이클러뷰에 적용시킬 어댑터

    ProgressDialog progressDialog;
    Dialog dialogreport, dialogimg;    //신고
    String reportitem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace2_1);

        Toolbar toolbar = findViewById (R.id.commenttoolbar);
        setSupportActionBar (toolbar); //액티비티의 앱바(App Bar)로 지정
        ActionBar actionBar = getSupportActionBar (); //앱바 제어를 위해 툴바 액세스
        actionBar.setDisplayHomeAsUpEnabled (true);
        actionBar.setTitle("중고장터");

        Mydata.setCount(1);
        getintent();

        TextView Ttitle = findViewById(R.id.market_title2);
        Ttitle.setText(title);

        TextView Tcontents = findViewById(R.id.market_content2);
        Tcontents.setText(contents);

        TextView Tprice = findViewById(R.id.market_price);
        Tprice.setText(price);


        btn_chatstart = findViewById(R.id.btn_chatstart);
        sliderViewPager = findViewById(R.id.sliderViewPager);
        layoutIndicator = findViewById(R.id.layoutIndicators);


        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);

        if(transaction.equals("true")){
            Ttitle.setPaintFlags(Ttitle.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            Tcontents.setPaintFlags(Tcontents.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            Tprice.setPaintFlags(Tprice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            btn_chatstart.setText("거래가 완료된 게시물입니다.");
        }

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://csapp-a3fce.appspot.com/");             //파이어베이스 스토리지 경로지정
        StorageReference storageRef = storage.getReference();

        if(!transaction.equals("true")){
            btn_chatstart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MarketplaceActivity2_1.this, ChatActivity_2.class);
                    intent.putExtra("userid",userid);
                    startActivity(intent);
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "거래가 완료된 게시물입니다.", Toast.LENGTH_SHORT).show();
        }



        postend = findViewById(R.id.btn_postend);
        postend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postendDialog();
            }
        });

        if(mAuth.getCurrentUser().getUid().equals(userid)){             //게시물 작성자인지 확인하기
            btn_chatstart.setVisibility(View.GONE);
            postend.setVisibility(View.VISIBLE);
        }
        else{
            postend.setVisibility(View.GONE);
            btn_chatstart.setVisibility(View.VISIBLE);
        }

        dialogreport = new Dialog(MarketplaceActivity2_1.this);       // Dialog 초기화
        dialogreport.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialogreport.setContentView(R.layout.dialog_report);             // xml 레이아웃 파일과 연결

        dialogimg = new Dialog(MarketplaceActivity2_1.this);       // Dialog 초기화
        dialogimg.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialogimg.setContentView(R.layout.dialog_photo);             // xml 레이아웃 파일과 연결
        imgdialog = dialogimg.findViewById(R.id.imagedialog);
        dialogimg.setCancelable(false);




        if(!array[0].equals("null")) {
            sliderViewPager.setOffscreenPageLimit(1);
            adapter = new ImageSliderAdapter(this, array);
            sliderViewPager.setAdapter(adapter);

            sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    setCurrentIndicator(position);
                }
            });

            setupIndicators(array.length);

            adapter.setOnItemClickListener(new ImageSliderAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int pos) {
                    showdialogimg(pos);
                }
            });
            Loadingstart();
        }
        else{
            View v = findViewById(R.id.viewFadingEdge);
            sliderViewPager.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
        }
    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                ));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String uid = mAuth.getCurrentUser().getUid();

        if(userid.equals(uid)){                                             //자기가 쓴 게시글일경우 수정,삭제 메뉴 불러오기
            getMenuInflater().inflate (R.menu.mainoption, menu);
        }
        else{                                                               //자기가 쓴 게시글이 아닐경우 신고하기 버튼만 있는 메뉴 불러오기
            getMenuInflater().inflate (R.menu.mainoption2, menu);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home:
                finish ();
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                return true;
            case R.id.option_delete:
                //Toast.makeText(getApplicationContext(), "삭제", Toast.LENGTH_SHORT).show();
                deleteDialog();
                return true;
            case R.id.option_report:
                showDialogreport();
                //Toast.makeText(getApplicationContext(), "신고", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_rewrite:
                Toast.makeText(getApplicationContext(), "수정", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MarketplaceActivity2_1.this, MarketplacePostActivityRewrite.class);
                intent.putExtra("postid", postid);
                intent.putExtra("userid", userid);
                intent.putExtra("title", title);
                intent.putExtra("contents", contents);
                intent.putExtra("img", img);
                intent.putExtra("time", time);
                intent.putExtra("price", price);

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }

    void deleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MarketplaceActivity2_1.this)
                .setTitle("삭제")
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MarketplaceActivity2_1.this, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(userid.equals(mAuth.getCurrentUser().getUid())){
                            mStore.collection(FirebaseID.postMarket).document(postid)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(MarketplaceActivity2_1.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MarketplaceActivity2_1.this, "삭제 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(MarketplaceActivity2_1.this, "글 작성자가 아닙니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showDialogreport(){
        dialogreport.show(); // 다이얼로그 띄우기

        Spinner reportspinner = dialogreport.findViewById(R.id.spinner_report);

        reportspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reportitem = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button noBtn = dialogreport.findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                dialogreport.dismiss(); // 다이얼로그 닫기
                Toast.makeText(MarketplaceActivity2_1.this, " 신고취소 ", Toast.LENGTH_SHORT).show();
            }
        });
        // 신고하기 버튼
        dialogreport.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = dialogreport.findViewById(R.id.report_edittext);
                String reportconetnt = et.getText().toString();

                //String postId = mStore.collection("report").document(firstpath).collection(secondpath).document().getId(); //중복 방지
                String postId = mStore.collection("report").document().getId(); //중복 방지
                Map<String, Object> data = new HashMap<>();
                data.put("1_1_신고글ID",postId);                          //신고글 id
                data.put("1_2_신고자ID",mAuth.getCurrentUser().getUid()); //신고자 id
                data.put("1_3_신고사유",reportitem);                //내용
                data.put("1_4_신고내용",reportconetnt);                //내용

                data.put("2_1_게시글ID",postid);                          //신고게시글 id
                data.put("2_2_게시글작성자ID",userid);                          //신고게시글 유저id
                data.put("2_3_게시글제목",title);                           //신고 게시물 제목

                data.put("3_1_경로1","marketplace");
                data.put("3_2_경로2",firstpath);
                data.put("3_3_경로3",secondpath);
                data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());    //타임

                //mStore.collection("report").document(firstpath).collection(secondpath).document(postId).set(data, SetOptions.merge());  //값넣기
                mStore.collection("report").document(postId).set(data, SetOptions.merge());  //값넣기
                Toast.makeText(MarketplaceActivity2_1.this, " 신고완료 ", Toast.LENGTH_SHORT).show();
                dialogreport.dismiss(); // 다이얼로그 닫기
                // 원하는 기능 구현
                //finish();           // 앱 종료
            }
        });
    }

    void postendDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MarketplaceActivity2_1.this)
                .setTitle("거래 완료")
                .setMessage("거래 완료")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MarketplaceActivity2_1.this, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(userid.equals(mAuth.getCurrentUser().getUid())){
                            Map<String, Object> data = new HashMap<>();
                            data.put("transaction","true");
                            mStore.collection(FirebaseID.postMarket).document(firstpath).collection(secondpath).document(postid).set(data, SetOptions.merge());  //값넣기
                            Toast.makeText(MarketplaceActivity2_1.this, "완료.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MarketplaceActivity2_1.this, "글 작성자가 아닙니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private ImageView imgdialog;

    private PhotoViewAttacher mAttacher;    //이미지 확대하는 기능

    void showdialogimg(int pos){
        dialogimg.show(); // 다이얼로그 띄우기
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://csapp-a3fce.appspot.com/");             //파이어베이스 스토리지 경로지정
        StorageReference storageRef = storage.getReference();
        mAttacher = new PhotoViewAttacher(imgdialog);

        dialogimg.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogimg.dismiss();
            }
        });

        storageRef.child(array[pos]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(imgdialog);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
            }
        });

    }

    private void Loadingstart(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                progressDialog.show();
                Log.d("LOGTEST",  "length :  " + array.length + " count : " + Mydata.getCount());
                if(Mydata.getCount() >= array.length-1){
                    progressDialog.dismiss();
                }
                else{
                    Loadingstart();
                }
            }
        },100);
    }

    void getintent(){
        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        userid = intent.getStringExtra("userid");
        title = intent.getStringExtra("title");
        contents = intent.getStringExtra("contents");
        img = intent.getStringExtra("img");
        time = intent.getStringExtra("time");
        price = intent.getStringExtra("price");
        transaction = intent.getStringExtra("transaction");

        firstpath = Mydata.getFirstpath();
        secondpath = Mydata.getSecondpath();

        img = img.replace("[","");
        img = img.replace("]","");
        img = img.replaceAll(" ","");
        array = img.split(",");

    }


}