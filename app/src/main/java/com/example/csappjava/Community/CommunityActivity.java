package com.example.csappjava.Community;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csappjava.Chatting.ChatMainActivity;
import com.example.csappjava.FirebaseID;
import com.example.csappjava.Login.RegisterActivity3;
import com.example.csappjava.MainActivity;
import com.example.csappjava.Marketplace.MarketplaceActivity;
import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.example.csappjava.Marketplace.BookApiActivity;
import com.example.csappjava.Marketplace.BarcoadActivity;
import com.example.csappjava.Setting.SettingMain;
import com.example.csappjava.Test1;
import com.example.csappjava.adapters.PostAdapterCommunity;
import com.example.csappjava.image.MultiImageActivity;
import com.example.csappjava.models.DateConverter;
import com.example.csappjava.models.PostCommunity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private RecyclerView mPostRecyclerView;

    private PostAdapterCommunity mAdapter;
    private List<PostCommunity> mDatas;
    TextView nicknametv, pointtv, schooltv;
    ImageButton searchbt, searchbt2;
    EditText searchet;
    Spinner searchsp;
    int number, count;
    String menumyschool,menumycampus,menumydepartment,menumyaffiliation, spitem;
    Timer timer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        mPostRecyclerView = findViewById(R.id.community_recycleview);

        findViewById(R.id.community_post_editbt).setOnClickListener(this);
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);


        //final Mydata mdata = (Mydata)getApplication();
        //mdata.Init();

        String uid = mAuth.getCurrentUser().getUid();                                   //자신의 정보 불러오기
        DocumentReference docRef = mStore.collection(FirebaseID.user).document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        /*myemail = document.getData().get("email").toString();
                        myimg = document.getData().get("img").toString();
                        mynickname = document.getData().get("nickname").toString();
                        mypoint = document.getData().get("point").toString();
                        myschool = document.getData().get("school").toString();
                        myschoolkr = document.getData().get("schoolKR").toString();
                        mycampus = document.getData().get("campus").toString();
                        mydepartment = document.getData().get("department").toString();
                        myaffiliation = document.getData().get("affiliation").toString();*/

                        //Log.d("LOGTEST",  "테스트 : " + Mydata.getMycampus());

                        String postcampus = document.getData().get("campus").toString();
                        String postschool = document.getData().get("school").toString();
                        String postschoolkr = document.getData().get("schoolKR").toString();
                        menudata(Mydata.getMyschool(), Mydata.getMycampus(), Mydata.getMydepartment(), Mydata.getMyaffiliation());


                        if(postcampus.equals("본교")){
                            Mydata.setFirstpath(postschool);
                            Mydata.setSecondpath(postcampus);
                        }
                        else{
                            Mydata.setFirstpath(postschool);
                            Mydata.setSecondpath(postschoolkr + " " + postcampus);
                        }
                        postlist(Mydata.getFirstpath(),Mydata.getSecondpath(),"","");
                        number = 0;

                        //Log.d("LOGTEST",  myaffiliation);

                        nicknametv.setText(Mydata.getMynickname());                                     //아이템 택스트 바꾸기
                        //pointtv.setText();
                        schooltv.setText(Mydata.getMyschoolkr());

                        //Toast.makeText(CommunityActivity.this, document.getData().get("nickname") + ";", Toast.LENGTH_SHORT).show();
                    } else {

                    }
                } else {

                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("커뮤니티");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        View headerView = navigationView.getHeaderView(0);              //네비게이션 헤더 호출
        nicknametv = headerView.findViewById(R.id.nicknametv);              //네비게이션 헤더에 있는 아이템들 호출
        pointtv = headerView.findViewById(R.id.pointtv);
        schooltv = headerView.findViewById(R.id.schooltv);

        searchsp = findViewById(R.id.community_search_spinner);
        searchbt = findViewById(R.id.community_search_button);
        searchbt2 = findViewById(R.id.community_search_button2);
        searchet = findViewById(R.id.community_search_edittext);
        searchsp = (Spinner) findViewById(R.id.community_search_spinner);

        searchsp.setVisibility(View.GONE);
        searchbt.setVisibility(View.GONE);
        searchbt2.setVisibility(View.GONE);
        searchet.setVisibility(View.GONE);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {    //네비게이션 메뉴 누를때 발생하는 이벤트
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                Intent intent;
                // 각 메뉴 클릭시 이뤄지는 이벤트
                switch (id) {
                    case R.id.nav_home:
                        //Toast.makeText(CommunityActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                        intent = new Intent(CommunityActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_community:
                        Toast.makeText(CommunityActivity.this, "이미 커뮤니티입니다.", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.nav_marketplace:
                        //Toast.makeText(CommunityActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                        intent = new Intent(CommunityActivity.this, MarketplaceActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;

                    case R.id.nav_chatroome:
                        //Toast.makeText(CommunityActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                        intent = new Intent(CommunityActivity.this, ChatMainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;

                    case R.id.nav_settings:
                        intent = new Intent(CommunityActivity.this, SettingMain.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        searchsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(CommunityActivity.this,"선택된 아이템 : "+searchsp.getItemAtPosition(i),Toast.LENGTH_SHORT).show();
                spitem = searchsp.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchet.length()>1){
                    String search = searchet.getText().toString();
                    if(number == 0){
                        if (Mydata.getMycampus().equals("본교")) {
                            postlist(Mydata.getMyschool(), Mydata.getMyschoolkr(),spitem,search);
                        } else {
                            postlist(Mydata.getMyschool(), Mydata.getMyschoolkr() + " " + Mydata.getMycampus(),spitem,search);
                        }
                    }
                    else if(number == 1){
                        if (Mydata.getMycampus().equals("본교")) {
                            postlist(Mydata.getMyschool(), Mydata.getMydepartment(),spitem,search);
                        } else {
                            postlist(Mydata.getMyschool(), Mydata.getMycampus() + " " + Mydata.getMydepartment(),spitem,search);
                        }
                    }
                    else if(number == 2){
                        postlist("계열커뮤니티", Mydata.getMyaffiliation(),spitem,search);
                    }
                    searchet.setText(null);
                }
                else{
                    Toast.makeText(getApplicationContext(), "두글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        searchbt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number == 0){
                    if (Mydata.getMycampus().equals("본교")) {
                        postlist(Mydata.getMyschool(), Mydata.getMyschoolkr(),"","");
                    } else {
                        postlist(Mydata.getMyschool(), Mydata.getMyschoolkr() + " " + Mydata.getMycampus(),"","");
                    }
                }
                else if(number == 1){
                    if (Mydata.getMycampus().equals("본교")) {
                        postlist(Mydata.getMyschool(), Mydata.getMydepartment(),"","");
                    } else {
                        postlist(Mydata.getMyschool(), Mydata.getMycampus() + " " + Mydata.getMydepartment(),"","");
                    }
                }
                else if(number == 2){
                    postlist("계열커뮤니티", Mydata.getMyaffiliation(),"","");
                }
            }
        });

        TimerTask TT = new TimerTask() {
            @Override
            public void run() {
                // 반복실행할 구문
                if(count != 0){
                    //Intent intent = new Intent(CommunityActivity.this, CommunityActivity.class);
                    //startActivity(intent);
                    //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    //finish();
                    //Log.d("LOGTEST",  "짜잔?" + count );
                }
                //count++;
                //Log.d("LOGTEST",  "이게 실행되는거임?" + count );
            }

        };
        timer = new Timer();
        timer.schedule(TT, 0, 10000); //Timer 실행

        Log.d("LOGTEST",  "테스트 : " + Mydata.getMyschoolkr());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();//타이머 종료
    }

    @Override
    public void onClick(View view) {                                                 //작성페이지 가는 이벤트
        Intent intentpost = new Intent(this, CommunityPostActivity.class);
        //Log.d("LOGTEST",  "이동 테스트 : " + number);
        startActivity(intentpost);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_community_menu, menu);

        MenuItem item1 = menu.findItem(R.id.option_community1);
        MenuItem item2 = menu.findItem(R.id.option_community2);
        MenuItem item3 = menu.findItem(R.id.option_community3);

        if(Mydata.getMycampus().equals("본교")){
            item1.setTitle(Mydata.getMyschoolkr() +" 커뮤니티");
        }
        else{
            item1.setTitle(Mydata.getMyschoolkr() + " " + Mydata.getMycampus() + " 커뮤니티");
        }
        item2.setTitle(Mydata.getMydepartment() + " 커뮤니티");
        item3.setTitle(Mydata.getMyaffiliation() + " 커뮤니티");

        return super.onCreateOptionsMenu(menu);

        /*

        if(Mydata.getMycampus().equals("본교")){
            menu.add(Menu.NONE,Menu.FIRST,Menu.NONE,Mydata.getMyschoolkr() +" 커뮤니티");
        }
        else{
            menu.add(Menu.NONE,Menu.FIRST,Menu.NONE,Mydata.getMyschoolkr() + " " + Mydata.getMycampus() + " 커뮤니티");
        }
        menu.add(Menu.NONE,Menu.FIRST+10,Menu.NONE,Mydata.getMydepartment() + " 커뮤니티");
        menu.add(Menu.NONE,Menu.FIRST+20,Menu.NONE,Mydata.getMyaffiliation() + " 커뮤니티");
        menu.add(Menu.NONE,Menu.FIRST+30,Menu.NONE,"테스트페이지");


        Log.d("LOGTEST", Mydata.getMyschoolkr() + ", " + Mydata.getMydepartment() + ", " + Mydata.getMyaffiliation());
        return true;*/
    }

    //앱바(App Bar)에 표시된 액션 또는 오버플로우 메뉴가 선택되면
    //액티비티의 onOptionsItemSelected() 메서드가 호출
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.option_community1:
                if (number == 0) {                            //학교커뮤니티
                    Toast.makeText(getApplicationContext(), "해당 영역입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (Mydata.getMycampus().equals("본교")) {
                        postlist(Mydata.getMyschool(), Mydata.getMyschoolkr(),"","");
                    } else {
                        postlist(Mydata.getMyschool(), Mydata.getMyschoolkr() + " " + Mydata.getMycampus(),"","");
                    }
                    number = 0;
                }
                return true;
            case R.id.option_community2:
                if (number == 1) {                                //학과커뮤니티
                    Toast.makeText(getApplicationContext(), "해당 영역입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (Mydata.getMycampus().equals("본교")) {
                        postlist(Mydata.getMyschool(), Mydata.getMydepartment(),"","");
                    } else {
                        postlist(Mydata.getMyschool(), Mydata.getMycampus() + " " + Mydata.getMydepartment(),"","");
                    }
                    number = 1;
                }
                return true;

            case R.id.option_community3:
                if (number == 2) {                                //학과커뮤니티
                    Toast.makeText(getApplicationContext(), "해당 영역입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    postlist("계열커뮤니티", Mydata.getMyaffiliation(),"","");
                    number = 2;
                    Log.d("LOGTEST",  "테스트 : " + Mydata.getMyaffiliation());
                }
                return true;

            /*case R.id.option_community4:
                Intent intent = new Intent(CommunityActivity.this, MultiImageActivity.class);
                startActivity(intent);
                return true;*/

            case R.id.item1:
                if(searchsp.getVisibility() == View.GONE){
                    searchsp.setVisibility(View.VISIBLE);
                    searchbt.setVisibility(View.VISIBLE);
                    searchbt2.setVisibility(View.VISIBLE);
                    searchet.setVisibility(View.VISIBLE);
                }
                else{
                    searchsp.setVisibility(View.GONE);
                    searchbt.setVisibility(View.GONE);
                    searchbt2.setVisibility(View.GONE);
                    searchet.setVisibility(View.GONE);
                }



            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommunityActivity.this)
                .setTitle("Test")
                .setMessage("Test")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CommunityActivity.this, "아니오", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CommunityActivity.this, "네", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    void postlist(String path1, String path2, String menu, String search) {     //menu엔 작성자 닉네임 내용 중에 하나
        mDatas = new ArrayList<>();
        Mydata.setPostpath1(path1);
        Mydata.setPostpath2(path2);

        mStore.collection(FirebaseID.post).document(path1).collection(path2)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {   //파이어베이스에서 목록을 실시간으로 불러오는 이벤트
                        if (queryDocumentSnapshots != null) {
                            mDatas.clear();
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String postId = String.valueOf((shot.get(FirebaseID.postId)));
                                String userId = String.valueOf((shot.get(FirebaseID.userId)));
                                String title = String.valueOf(shot.get(FirebaseID.title));
                                String contents = String.valueOf(shot.get(FirebaseID.contents));
                                String img = String.valueOf(shot.get(FirebaseID.img));
                                String recommendation = String.valueOf(shot.get(FirebaseID.img));
                                String tag = String.valueOf(shot.get(FirebaseID.img));
                                String commentnum = String.valueOf(shot.get(FirebaseID.img));
                                String nick = String.valueOf(shot.get(FirebaseID.nickname));
                                //String time = String.valueOf(shot.get(FirebaseID.timestamp));
                                //String time = String.valueOf(shot.get(FirebaseID.timestamp));
                                //String time = DateConverter.formatTimeString(((Timestamp) shot.get(FirebaseID.timestamp)).toDate().getTime());
                                String time = new String();
                                try {
                                    time = DateConverter.formatTimeString(((Timestamp) shot.get(FirebaseID.timestamp)).toDate().getTime());
                                } catch (Exception e) {
                                    Log.d("LOGTEST",  "오류오류" + title);
                                }

                                //String time = String.valueOf(shot.get(FirebaseID.timestamp));
                                //Date date = Date.valueOf(shot.get(FirebaseID.timestamp));
                                //data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());

                                if(menu.equals("")&&search.equals("")){
                                    //PostCommunity data = new PostCommunity(postId, userId, title, contents, img, recommendation, tag, commentnum, nick, time);
                                    PostCommunity data = new PostCommunity(postId, userId, title, contents, img, recommendation, tag, commentnum, nick, time);
                                    mDatas.add(data);
                                }
                                else if(menu.equals("닉네임")&&nick.contains(search)){
                                    PostCommunity data = new PostCommunity(postId, userId, title, contents, img, recommendation, tag, commentnum, nick, time);
                                    mDatas.add(data);
                                }
                                else if(menu.equals("내용")&&contents.contains(search)){
                                    PostCommunity data = new PostCommunity(postId, userId, title, contents, img, recommendation, tag, commentnum, nick, time);
                                    mDatas.add(data);
                                }
                                else if(menu.equals("제목")&&title.contains(search)){
                                    PostCommunity data = new PostCommunity(postId, userId, title, contents, img, recommendation, tag, commentnum, nick, time);
                                    mDatas.add(data);
                                }
                            }

                            mAdapter = new PostAdapterCommunity(mDatas);

                            mAdapter.setOnItemClickListener(new PostAdapterCommunity.OnItemClickListener() {         //아이템뷰에서 아이템 하나를 누르면 커뮤니티2액티비티로 데이터를 전송하며 이동하는 이벤트
                                @Override
                                public void onItemClick(int pos) {
                                    //Toast.makeText(getApplicationContext(), "onItemClick position : " + pos, Toast.LENGTH_SHORT).show();

                                    PostCommunity hm = mDatas.get(pos);
                                    String hmuserId = hm.getUserId();
                                    String hmtitle = hm.getTitle();
                                    String hmcontents = hm.getContents();
                                    String hmimg = hm.getImg();
                                    String hmtime = hm.getTime();
                                    String hmpostid = hm.getPostId();

                                    Intent intent = new Intent(CommunityActivity.this, CommunityActivity2.class);
                                    intent.putExtra("postid", hmpostid);
                                    intent.putExtra("userid", hmuserId);
                                    intent.putExtra("title", hmtitle);
                                    intent.putExtra("contents", hmcontents);
                                    intent.putExtra("img", hmimg);
                                    intent.putExtra("time", hmtime);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    //Toast.makeText(getApplicationContext(), hm.getContents() + "," + hm.getTitle(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            mAdapter.setOnLongItemClickListener(new PostAdapterCommunity.OnLongItemClickListener() {             //아이템을 길게 눌렀을때 나오는 이벤트
                                @Override
                                public void onLongItemClick(int pos) {
                                    //Toast.makeText(getApplicationContext(), "onLongItemClick position : " + pos, Toast.LENGTH_SHORT).show();
                                    mDatas.get(pos);
                                    showDialog();
                                }
                            });
                            mPostRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(path2 + " 커뮤니티");
    }

    void menudata(String myschooldata, String mycampusdata, String mydepartmentdata, String myaffiliationdata){
        menumyschool = myschooldata;
        menumycampus = mycampusdata;
        menumydepartment = mydepartmentdata;
        menumyaffiliation = myaffiliationdata;
    }






}
