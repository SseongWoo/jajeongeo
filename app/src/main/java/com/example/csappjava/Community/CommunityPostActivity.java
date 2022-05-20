package com.example.csappjava.Community;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.csappjava.FirebaseID;
import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommunityPostActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    private EditText mTitle, mContents;
    private ImageView imageView;
    private String stringurl;
    Uri imgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_post);

        mTitle = findViewById(R.id.community_post_title_edit);
        mContents = findViewById(R.id.community_post_contents_edit);
        imageView = findViewById(R.id.community_post_image);

        String firstpath = Mydata.getFirstpath();
        String secondpath = Mydata.getSecondpath();

        Button imagebt = (Button) findViewById(R.id.community_post_image_button) ;
        imagebt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,10);
                imageView.setEnabled(false);
            }
        }) ;

        Button savebt = (Button) findViewById(R.id.community_post_save_button) ;
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
                        StorageReference imgRef= firebaseStorage.getReference("images/community/"+user+filename);

                        UploadTask uploadTask =imgRef.putFile(imgUri);
                        //stringurl = imgRef.getDownloadUrl().toString();
                        stringurl = "images/community"+user+filename;
                    }

                    //String postId = mStore.collection(FirebaseID.post).document().getId(); //중복 방지

                    String postId = mStore.collection(FirebaseID.post).document(firstpath).collection(secondpath).document().getId(); //중복 방지
                    Map<String, Object> data = new HashMap<>();
                    data.put(FirebaseID.postId,postId.toString());                          //문서id
                    data.put(FirebaseID.userId,mAuth.getCurrentUser().getUid());        //id값
                    data.put(FirebaseID.title,mTitle.getText().toString());                 //제목
                    data.put(FirebaseID.contents,mContents.getText().toString());           //내용
                    data.put(FirebaseID.img,stringurl);                                     //이미지url
                    data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());           //타임
                    data.put(FirebaseID.recommendation,"null");                         //추천
                    data.put(FirebaseID.tag,"null");                                    //태그
                    data.put(FirebaseID.commentnum,"null");               //태그

                    mStore.collection(FirebaseID.post).document(firstpath).collection(secondpath).document(postId).set(data, SetOptions.merge());  //값넣기
                    finish();
                }
            }
        });


    }

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