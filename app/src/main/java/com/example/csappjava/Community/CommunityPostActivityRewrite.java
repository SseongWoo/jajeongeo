package com.example.csappjava.Community;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.csappjava.FirebaseID;
import com.example.csappjava.MainActivity;
import com.example.csappjava.MainLoadingActivity;
import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.example.csappjava.adapters.ImageSliderAdapter;
import com.example.csappjava.adapters.MultiImageAdapter;
import com.example.csappjava.adapters.MultiImageAdapter2;
import com.example.csappjava.adapters.PostAdapterCommunity;
import com.example.csappjava.adapters.PostAdapterCommunityComment;
import com.example.csappjava.models.PostCommunityComment;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityPostActivityRewrite extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    private EditText mTitle, mContents;
    private ImageView imageView, backbutton;
    private String stringurl;
    private int GALLERY_CODE = 10;
    private List<String> imglist = new ArrayList<>();
    private String postid, userid, title, contents, img, time, firstpath, secondpath;
    private String[] array;
    int count = 0;
    Uri imgUri;

    private static final String TAG = "MultiImageActivity";
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체

    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    private MultiImageAdapter adapter; // 리사이클러뷰에 적용시킬 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_post);

        mTitle = findViewById(R.id.community_post_title_edit);
        mContents = findViewById(R.id.community_post_contents_edit);
        imageView = findViewById(R.id.community_post_image);
        backbutton = findViewById(R.id.community_post_back_button);

        getintent();

        mTitle.setText(title);
        mContents.setText(contents);

        Button imagebt = (Button) findViewById(R.id.community_post_image_button);
        imagebt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
            }
        });

        recyclerView = findViewById(R.id.community_post_recyclerView);

        Button savebt = (Button) findViewById(R.id.community_post_save_button);
        savebt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                    String filename = sdf.format(new Date()) + ".png";//현재 시간과 사용자 고유id로 파일명 지정
                    String user = mAuth.getCurrentUser().getUid().toString();

                    String list = "";
                    for (int o = 0; o < imglist.size(); o++){
                        list = list + "," + imglist.get(o);
                    }
                    array = list.split(",");

                    for (int o = 0; o < imglist.size(); o++){
                        Uri u;
                        String su;

                        su = imglist.get(o).toString();
                        u = uriList.get(o);

                        StorageReference imgRef = firebaseStorage.getReference(su);
                        UploadTask uploadTask = imgRef.putFile(u);
                    }

                    Map<String, Object> data = new HashMap<>();
                    data.put(FirebaseID.postId, postid);                          //문서id
                    data.put(FirebaseID.userId, mAuth.getCurrentUser().getUid());        //id값
                    data.put(FirebaseID.title, mTitle.getText().toString());                 //제목
                    data.put(FirebaseID.contents, mContents.getText().toString());           //내용
                    if(imglist.isEmpty()){                //이미지url
                        imglist.add("null");
                        data.put(FirebaseID.img, imglist);
                    }
                    else{
                        data.put(FirebaseID.img, imglist);
                    }

                    data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());           //타임
                    data.put(FirebaseID.recommendation, "null");                         //추천
                    data.put(FirebaseID.tag, "null");                                    //태그
                    data.put(FirebaseID.commentnum, "null");               //태그
                    data.put(FirebaseID.nickname, Mydata.getMynickname());           //닉네임

                    mStore.collection(FirebaseID.post).document(firstpath).collection(secondpath).document(postid).set(data, SetOptions.merge());  //값넣기

                    Loadingstart();
                }
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        adapter = new MultiImageAdapter(uriList, getApplicationContext());

        for (int o = 0; o < array.length; o++){
            String suri;
            Uri uuri;

            if(!array[o].equals("null")) {
                suri = array[o];
                uuri = Uri.parse(suri);

                uriList.add(uuri);  //uri를 list에 담는다.
                imglist.add(suri);

                recyclerView.setAdapter(adapter);   // 리사이클러뷰에 어댑터 세팅
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));     // 리사이클러뷰 수평 스크롤 적용
            }
            else{

            }

        }

        adapter.setOnLongItemClickListener(new MultiImageAdapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(int pos) {
                commentdeleteDialog(pos);
            }
        });
    }

    // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String filename = sdf.format(new Date());//현재 시간과 사용자 고유id로 파일명 지정
        String user = mAuth.getCurrentUser().getUid().toString();


        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }
        else{   // 이미지를 하나라도 선택한 경우
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);

                adapter = new MultiImageAdapter(uriList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

                stringurl = "images/community/" + user + filename + ".png";
                imglist.add(stringurl);
            }
            else{      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 10){   // 선택한 이미지가 11장 이상인 경우
                    Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                    Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                        try {
                            uriList.add(imageUri);  //uri를 list에 담는다.
                            stringurl = "images/community/" + user + filename + "_" + count + ".png";
                            imglist.add(stringurl);
                            count++;
                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                    adapter = new MultiImageAdapter(uriList, getApplicationContext());
                    recyclerView.setAdapter(adapter);   // 리사이클러뷰에 어댑터 세팅
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));     // 리사이클러뷰 수평 스크롤 적용
                }
            }
        }
    }

    void commentdeleteDialog(int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(CommunityPostActivityRewrite.this)
                .setTitle("이미지 삭제")
                .setMessage("이미지를 삭제하시겠습니까?")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CommunityPostActivityRewrite.this, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        uriList.remove(pos);
                        adapter.notifyItemRemoved(pos);
                        imglist.remove(pos);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    void getintent() {
        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        userid = intent.getStringExtra("userid");
        title = intent.getStringExtra("title");
        contents = intent.getStringExtra("contents");
        img = intent.getStringExtra("img");
        time = intent.getStringExtra("time");
        firstpath = Mydata.getFirstpath();
        secondpath = Mydata.getSecondpath();

        img = img.replace("[", "");
        img = img.replace("]", "");
        img = img.replaceAll(" ", "");
        array = img.split(",");
    }

    private void Loadingstart(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                String nextimg = "";
                for (int o = 0; o < imglist.size(); o++) {
                    nextimg = nextimg + imglist.get(o) + ",";
                }

                Intent intent = new Intent(CommunityPostActivityRewrite.this, CommunityActivity2.class);
                intent.putExtra("postid", postid);
                intent.putExtra("userid", userid);
                intent.putExtra("title", title);
                intent.putExtra("contents", contents);
                intent.putExtra("img", nextimg);
                intent.putExtra("time", time);
                startActivity(intent);
                finish();
            }
        },500);
    }
}