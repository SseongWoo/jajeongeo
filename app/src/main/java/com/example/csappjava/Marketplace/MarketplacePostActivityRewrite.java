package com.example.csappjava.Marketplace;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.csappjava.FirebaseID;
import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class MarketplacePostActivityRewrite extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    private EditText mTitle, mContents, mPrice;
    private ImageView imageView;
    private String stringurl;
    private String postid,userid,title,contents,img,time,firstpath,secondpath,price;

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

        getintent();

        mTitle.setText(title);
        mContents.setText(contents);
        mPrice.setText(price);
        loadimg();

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
                    Map<String, Object> data = new HashMap<>();

                    if(imageView.isEnabled()){
                    }
                    else{
                        StorageReference storageRef = firebaseStorage.getReference();               //먼저 있던 사진파일을 삭제
                        StorageReference desertRef = storageRef.child(img);
                        desertRef.delete();

                        StorageReference imgRef= firebaseStorage.getReference("images/marketplace/"+user+filename);

                        UploadTask uploadTask =imgRef.putFile(imgUri);
                        //stringurl = imgRef.getDownloadUrl().toString();
                        stringurl = "images/marketplace/"+user+filename;
                        data.put(FirebaseID.img,stringurl);
                        Log.d("LOGTEST2", "img = " + img + " stringurl = " + stringurl + " upload = " + imgUri);
                    }
                    //String postId = mStore.collection(FirebaseID.postMarket).document(firstpath).collection(secondpath).document().getId(); //중복 방지

                    data.put(FirebaseID.postId,postid.toString());                          //문서id
                    data.put(FirebaseID.userId,mAuth.getCurrentUser().getUid());        //id값
                    data.put(FirebaseID.title,mTitle.getText().toString());                 //제목
                    data.put(FirebaseID.price,mPrice.getText().toString());                 //가격
                    data.put(FirebaseID.contents,mContents.getText().toString());           //내용
                    //data.put(FirebaseID.img,stringurl);                                     //이미지url
                    //data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());           //타임
                    //Log.d("LOGTEST1", "firstpath = " + firstpath + " secondpath = " + secondpath);
                    mStore.collection(FirebaseID.postMarket).document(firstpath).collection(secondpath).document(postid).set(data, SetOptions.merge());  //값넣기
                    //mStore.collection(FirebaseID.postMarket).document(firstpath).collection(secondpath).document(postid).update(data);
                    finish();
                }
            }
        });
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

    void getintent(){
        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        userid = intent.getStringExtra("userid");
        title = intent.getStringExtra("title");
        contents = intent.getStringExtra("contents");
        img = intent.getStringExtra("img");
        time = intent.getStringExtra("time");
        price = intent.getStringExtra("price");
        firstpath = Mydata.getFirstpath();
        secondpath = Mydata.getSecondpath();
        //Log.d("LOGTEST0", "img = " + img);
    }

    void loadimg(){
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://csapp-a3fce.appspot.com/");             //파이어베이스 스토리지 경로지정
        StorageReference storageRef = storage.getReference();
        storageRef.child(img).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시

                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                Toast.makeText(getApplicationContext(), "이미지가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}