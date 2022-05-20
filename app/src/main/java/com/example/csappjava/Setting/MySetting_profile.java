package com.example.csappjava.Setting;

import static com.example.csappjava.LoginActivity.context2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.csappjava.LoginActivity;
import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.example.csappjava.models.UserProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MySetting_profile extends AppCompatActivity {

    private ImageView profile;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cs");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private String userUid,sch,times;
    Uri imgUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysetting_profile);

        sch = Mydata.getMyschool();
        userUid = mAuth.getCurrentUser().getUid();
        times = String.valueOf(System.currentTimeMillis());
        profile = findViewById(R.id.myprofile_photo);

        Toolbar toolbar = findViewById (R.id.setoolbar);
        setSupportActionBar (toolbar); //액티비티의 앱바(App Bar)로 지정
        ActionBar actionBar = getSupportActionBar (); //앱바 제어를 위해 툴바 액세스
        actionBar.setDisplayHomeAsUpEnabled (true);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MySetting_profile.this) // TestActivity 부분에는 현재 Activity의 이름 입력.
                        .setMessage("선택하세요")     // 제목 부분 (직접 작성)
                        .setPositiveButton("갤러리", new DialogInterface.OnClickListener() {      // 버튼1 (직접 작성)
                            public void onClick(DialogInterface dialog, int which){

                                Intent gallery= new Intent();
                                gallery.setAction(gallery.ACTION_GET_CONTENT);
                                gallery.setType("image/*");
                                startActivityForResult(gallery,10);
                                profile.setEnabled(false);

                            }
                        })

                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {     // 버튼2 (직접 작성)
                            public void onClick(DialogInterface dialog, int which){
                                Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show(); // 실행할 코드
                            }
                        })
                        .show();
            }
        });

        Button btn_upload_profile = findViewById(R.id.mysetting_profile_upload);
        btn_upload_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgUri!=null){
                    uploadImage(imgUri);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate (R.menu.mainoption, menu);

        return true;
    }

    //앱바(App Bar)에 표시된 액션 또는 오버플로우 메뉴가 선택되면
    //액티비티의 onOptionsItemSelected() 메서드가 호출
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home:
                finish ();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;

            default:
                return super.onOptionsItemSelected (item);
        }
    }

    private void uploadImage(Uri uri){
        StorageReference fileRef = storageReference.child("/profile/profile" + userUid+ times+".png");
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        reference.child(sch+"/user").child(userUid).child("profile").setValue("profile/profile"+userUid+times+".png");
                        Toast.makeText(MySetting_profile.this,"변경 완료",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

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
                    Glide.with(this).load(imgUri).into(profile);
                }
                break;
        }
    }

}
