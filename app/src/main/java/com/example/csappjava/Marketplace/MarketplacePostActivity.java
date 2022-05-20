package com.example.csappjava.Marketplace;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.csappjava.FirebaseID;
import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MarketplacePostActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    private EditText mTitle, mContents, mPrice;
    private ImageView imageView;
    private String stringurl;
    private TextView bname,bprice,bpublisher;

    private DecimalFormat decimalFormat = new DecimalFormat("#,###");       //원화 단위 표시
    private String result="";
    Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_marketplace_post);

        mTitle = findViewById(R.id.market_post_title_edit);
        mContents = findViewById(R.id.market_post_contents_edit);
        imageView = findViewById(R.id.marketplace_image);
        mPrice = findViewById(R.id.market_post_price);
        mPrice.addTextChangedListener(watcher);
        bname = findViewById(R.id.bookname);
        bpublisher = findViewById(R.id.bookpublisher);
        bprice = findViewById(R.id.bookprice);

        Intent intent = getIntent();
        String firstpath = Mydata.getFirstpath();
        String secondpath = Mydata.getSecondpath();
        String b1 = intent.getStringExtra("b1");
        String b2 = intent.getStringExtra("b2");
        String b3 = intent.getStringExtra("b3");
        bname.setText(b2);
        bprice.setText(b1);
        bpublisher.setText(b3);

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

                    String postId = mStore.collection(FirebaseID.postMarket).document(firstpath).collection(secondpath).document().getId(); //중복 방지
                    Map<String, Object> data = new HashMap<>();
                    data.put(FirebaseID.postId,postId.toString());                          //문서id
                    data.put(FirebaseID.userId,mAuth.getCurrentUser().getUid());        //id값
                    data.put(FirebaseID.title,mTitle.getText().toString());                 //제목
                    data.put(FirebaseID.price,mPrice.getText().toString());                 //가격
                    data.put(FirebaseID.contents,mContents.getText().toString());           //내용
                    data.put(FirebaseID.img,stringurl);                                     //이미지url
                    data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());           //타임

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

    public String clock(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getTime = dateFormat.format(date);

        return getTime;
    }

}