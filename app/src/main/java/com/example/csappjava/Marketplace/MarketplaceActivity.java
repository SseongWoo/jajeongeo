package com.example.csappjava.Marketplace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.csappjava.Chatting.ChatMainActivity;
import com.example.csappjava.Community.CommunityActivity;
import com.example.csappjava.Community.CommunityActivity2;
import com.example.csappjava.FirebaseID;
import com.example.csappjava.MainActivity;
import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.example.csappjava.Setting.SettingMain;
import com.example.csappjava.adapters.PostAdapterCommunity;
import com.example.csappjava.adapters.PostAdapterMarketplace;
import com.example.csappjava.models.DateConverter;
import com.example.csappjava.models.PostCommunity;
import com.example.csappjava.models.PostMarketplace;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
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

public class MarketplaceActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private RecyclerView mPostRecyclerView;
    private GridLayoutManager layoutManager;
    TextView nicknametv, schooltv;
    private String[] array;
    private String[] array2, array3;
    private String firstpath, secondpath;
    private PostAdapterMarketplace mAdapter;
    private List<PostMarketplace> mDataM;
    String spitem;
    ImageButton searchbt, searchbt2;
    EditText searchet;
    Spinner searchsp;
    int number = 0;
    int number2 = 0;
    ImageView profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);

       mPostRecyclerView = findViewById(R.id.marketplace_recycleview);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);

        mPostRecyclerView.setLayoutManager(layoutManager);


        findViewById(R.id.marketplace_post_edit).setOnClickListener(this);

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("중고장터");

        firstpath = Mydata.getFirstpath();
        secondpath = Mydata.getSecondpath();

        postlist(firstpath,secondpath,"","");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        String mynickname = Mydata.getMynickname();
        String myschool = Mydata.getMyschoolkr();
        View headerView = navigationView.getHeaderView(0);              //네비게이션 헤더 호출
        nicknametv = headerView.findViewById(R.id.nicknametv);              //네비게이션 헤더에 있는 아이템들 호출
        schooltv = headerView.findViewById(R.id.schooltv);
        profile = headerView.findViewById(R.id.profile);

        nicknametv.setText(mynickname);                                     //아이템 택스트 바꾸기
        schooltv.setText(myschool);

        searchsp = findViewById(R.id.marketplace_search_spinner);
        searchbt = findViewById(R.id.marketplace_search_button);
        searchbt2 = findViewById(R.id.marketplace_search_button2);
        searchet = findViewById(R.id.marketplace_search_edittext);

        searchsp.setVisibility(View.GONE);
        searchbt.setVisibility(View.GONE);
        searchbt2.setVisibility(View.GONE);
        searchet.setVisibility(View.GONE);

        Glide.with(getApplicationContext())
                .load(Mydata.getMyprofile())
                .error(R.drawable.ic_noimage)
                .into(profile);

        if(!Mydata.getNearschoolkr().equals("")){
            array3 = Mydata.getNearschoolkr().split(",");
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {    //네비게이션 메뉴 누를때 발생하는 이벤트
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                Intent intent;
                // 각 메뉴 클릭시 이뤄지는 이벤트
                switch (id){
                    case R.id.nav_home:
                        //Toast.makeText(MarketplaceActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                        intent = new Intent(MarketplaceActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_community:
                        //Toast.makeText(MarketplaceActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                        intent = new Intent(MarketplaceActivity.this, CommunityActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;

                    case R.id.nav_marketplace:
                        Toast.makeText(MarketplaceActivity.this, "이미 중고장터입니다.", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.nav_chatroome:
                        //Toast.makeText(CommunityActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                        intent = new Intent(MarketplaceActivity.this, ChatMainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;

                    case R.id.nav_settings:
                        intent = new Intent(MarketplaceActivity.this, SettingMain.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        searchsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
                    postlist(Mydata.getMyschool(), Mydata.getMyschoolkr() + " " + Mydata.getMycampus(),spitem,search);
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
    }

    @Override
    public void onClick(View view){                                                 //작성페이지 가는 이벤트
        ActionBar actionBar = getSupportActionBar();
        actionBar.getTitle();

        if(number2 == 0){
            Intent intentpost = new Intent(this, BarcoadActivity.class);
            startActivity(intentpost);
        }
        else{
            Intent intentpost2 = new Intent(this, MarketplacePostActivity2.class);
            startActivity(intentpost2);
        }

    }

    public String clock(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getTime = dateFormat.format(date);

        return getTime;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        menu.add(Menu.NONE,Menu.FIRST+10,Menu.NONE,"책 중고장터");
        menu.add(Menu.NONE,Menu.FIRST+20,Menu.NONE,"기타 중고장터");
        menu.add(Menu.NONE,Menu.FIRST+30,Menu.NONE,"내가 올린 게시물");
        if(!Mydata.getNearschool().equals("")){
            array2 = Mydata.getNearschool().split(",");
            for (int o = 0; o < array.length; o++){
                menu.add(Menu.NONE,Menu.FIRST+40+o,Menu.NONE, array[o] + " 중고장터");
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home:
                Intent intent = new Intent(MarketplaceActivity.this,CommunityActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                number2 = 0;
                finish();
                return true;
            case Menu.FIRST+10:
                postlist(firstpath, secondpath, "", "");
                number2 = 0;
                return true;
            case Menu.FIRST+20:
                postlist(firstpath, secondpath+"기타", "", "");
                number2 = 1;
                return true;
            case Menu.FIRST+30:
                postlist(firstpath, secondpath, "내닉네임", Mydata.getMynickname() );
                number2 = 0;
                Toast.makeText(getApplicationContext(), "내가 올린 게시물", Toast.LENGTH_SHORT).show();
                return true;
            case Menu.FIRST+40:
                number2 = 0;
                postlist(array2[0], array3[0], "", "");
                return true;
            case Menu.FIRST+41:
                number2 = 0;
                postlist(array2[1], array3[1], "", "");
                return true;
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
                return super.onOptionsItemSelected (item);
        }
    }

    void postlist(String path1, String path2, String sp, String search) {
        mDataM = new ArrayList<>();
        mStore.collection(FirebaseID.postMarket).document(path1).collection(path2)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {   //파이어베이스에서 목록을 실시간으로 불러오는 이벤트
                        if(queryDocumentSnapshots !=null){
                            mDataM.clear();
                            for(DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()){
                                Map<String, Object> shot = snap.getData();
                                String postId = String.valueOf((shot.get(FirebaseID.postId)));
                                String userId = String.valueOf((shot.get(FirebaseID.userId)));
                                String title = String.valueOf(shot.get(FirebaseID.title));
                                String contents = String.valueOf(shot.get(FirebaseID.contents));
                                String img = String.valueOf(shot.get(FirebaseID.img));
                                String price = String.valueOf(shot.get(FirebaseID.price));
                                String nick = String.valueOf(shot.get(FirebaseID.nickname));
                                String transaction = String.valueOf(shot.get(FirebaseID.transaction));
                                String lecture = String.valueOf(shot.get("강의정보_강의명"));
                                String professor = String.valueOf(shot.get("강의정보_교수명"));
                                String department = String.valueOf(shot.get("강의정보_학과명"));
                                String adapteryear = String.valueOf(shot.get("강의정보_년도"));
                                String adaptermonth = String.valueOf(shot.get("강의정보_학기"));
                                String bookname = String.valueOf(shot.get("책정보_책이름"));
                                String bookprice = String.valueOf(shot.get("책정보_책가격"));
                                String bookpublisher = String.valueOf(shot.get("책정보_출판사"));
                                String bookimg = String.valueOf(shot.get("책정보_이미지"));
                                String author = String.valueOf(shot.get("책정보_저자"));
                                String booklink = String.valueOf(shot.get("책정보_링크"));

                                String time = new String();
                                try {
                                    time = DateConverter.formatTimeString(((Timestamp) shot.get(FirebaseID.timestamp)).toDate().getTime());
                                } catch (Exception e) {
                                    Log.d("LOGTEST",  "오류오류" + title);
                                }

                                if(sp.equals("")&&search.equals("")){                   //검색할 데이터를 구분하는 작업
                                    PostMarketplace data = new PostMarketplace(postId, userId, title, contents, img, price, nick, time, transaction,
                                            lecture, professor, department, adapteryear, adaptermonth, bookname,bookprice, bookpublisher, bookimg, author, booklink);
                                    mDataM.add(data);
                                }
                                else if(sp.equals("책명")&&bookname.contains(search)){
                                    PostMarketplace data = new PostMarketplace(postId, userId, title, contents, img, price, nick, time, transaction,
                                            lecture, professor, department, adapteryear, adaptermonth, bookname,bookprice, bookpublisher, bookimg, author, booklink);
                                    mDataM.add(data);
                                }
                                else if(sp.equals("제목")&&title.contains(search)){
                                    PostMarketplace data = new PostMarketplace(postId, userId, title, contents, img, price, nick, time, transaction,
                                            lecture, professor, department, adapteryear, adaptermonth, bookname,bookprice, bookpublisher, bookimg, author, booklink);
                                    mDataM.add(data);
                                }
                                else if(sp.equals("교수명")&&professor.contains(search)){
                                    PostMarketplace data = new PostMarketplace(postId, userId, title, contents, img, price, nick, time, transaction,
                                            lecture, professor, department, adapteryear, adaptermonth, bookname,bookprice, bookpublisher, bookimg, author, booklink);
                                    mDataM.add(data);
                                }
                                else if(sp.equals("수업명")&&lecture.contains(search)){
                                    PostMarketplace data = new PostMarketplace(postId, userId, title, contents, img, price, nick, time, transaction,
                                            lecture, professor, department, adapteryear, adaptermonth, bookname,bookprice, bookpublisher, bookimg, author, booklink);
                                    mDataM.add(data);
                                }
                                else if(sp.equals("학과")&&department.contains(search)){
                                    PostMarketplace data = new PostMarketplace(postId, userId, title, contents, img, price, nick, time, transaction,
                                            lecture, professor, department, adapteryear, adaptermonth, bookname,bookprice, bookpublisher, bookimg, author, booklink);
                                    mDataM.add(data);
                                }
                                else if(sp.equals("내닉네임")&&nick.contains(search)){
                                    PostMarketplace data = new PostMarketplace(postId, userId, title, contents, img, price, nick, time, transaction,
                                            lecture, professor, department, adapteryear, adaptermonth, bookname,bookprice, bookpublisher, bookimg, author, booklink);
                                    mDataM.add(data);
                                }
                            }
                            mAdapter = new PostAdapterMarketplace(mDataM);

                            mAdapter.setOnItemClickListener(new PostAdapterMarketplace.OnItemClickListener() {         //아이템뷰에서 아이템 하나를 누르면 커뮤니티2액티비티로 데이터를 전송하며 이동하는 이벤트
                                @Override
                                public void onItemClick(int pos) {
                                    View header;
                                    ImageView ivTitle;
                                    header = getLayoutInflater().inflate(R.layout.marketplace_post,null,false);
                                    ivTitle = header.findViewById(R.id.marketplace_image);

                                    PostMarketplace hm = mDataM.get(pos);
                                    String hmpostid = hm.getPostId();
                                    String hmuserid = hm.getUserId();
                                    String hmtitle = hm.getTitle();
                                    String hmcontents = hm.getContents();
                                    String hmimg = hm.getImg();
                                    String hmtime = hm.getTime();
                                    String hmprice = hm.getPrice();
                                    String hmtransaction = hm.getTransaction();
                                    String hmlecture = hm.getLecture();
                                    String hmprofessor = hm.getProfessor();
                                    String hmdepartment = hm.getDepartment();
                                    String hmadapteryear = hm.getAdapteryear();
                                    String hmadaptermonth = hm.getAdaptermonth();
                                    String hmbookname = hm.getBookname();
                                    String hmbookprice = hm.getBookprice();
                                    String hmbookpublisher = hm.getBookpublisher();
                                    String hmbookimg = hm.getBookimg();
                                    String hmauthor = hm.getAuthor();
                                    String hmbooklink = hm.getBooklink();

                                    if(number2 == 1) {
                                        Intent intent2 = new Intent(MarketplaceActivity.this, MarketplaceActivity2_1.class);
                                        intent2.putExtra("postid",hmpostid);
                                        intent2.putExtra("userid",hmuserid);
                                        intent2.putExtra("title",hmtitle);
                                        intent2.putExtra("contents",hmcontents);
                                        intent2.putExtra("img",hmimg);
                                        intent2.putExtra("time",hmtime);
                                        intent2.putExtra("price",hmprice);
                                        intent2.putExtra("transaction",hmtransaction);
                                        startActivity(intent2);
                                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                                    }
                                    else{
                                        Intent intent = new Intent(MarketplaceActivity.this, MarketplaceActivity2.class);
                                        intent.putExtra("postid",hmpostid);
                                        intent.putExtra("userid",hmuserid);
                                        intent.putExtra("title",hmtitle);
                                        intent.putExtra("contents",hmcontents);
                                        intent.putExtra("img",hmimg);
                                        intent.putExtra("time",hmtime);
                                        intent.putExtra("price",hmprice);
                                        intent.putExtra("transaction",hmtransaction);
                                        intent.putExtra("lecture",hmlecture);
                                        intent.putExtra("professor",hmprofessor);
                                        intent.putExtra("department",hmdepartment);
                                        intent.putExtra("adapteryear",hmadapteryear);
                                        intent.putExtra("adaptermonth",hmadaptermonth);
                                        intent.putExtra("bookname",hmbookname);
                                        intent.putExtra("bookpublisher",hmbookpublisher);
                                        intent.putExtra("bookimg",hmbookimg);
                                        intent.putExtra("author",hmauthor);
                                        intent.putExtra("booklink",hmbooklink);
                                        intent.putExtra("bookprice",hmbookprice);

                                        Log.d("LOGTEST",  "IMG : " + hm.getImg() );

                                        startActivity(intent);
                                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                                    }
                                    //Toast.makeText(getApplicationContext(), hm.getContents() + "," + hm.getTitle(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            mAdapter.setOnLongItemClickListener(new PostAdapterMarketplace.OnLongItemClickListener() {             //아이템을 길게 눌렀을때 나오는 이벤트
                                @Override
                                public void onLongItemClick(int pos) {
                                    Toast.makeText(getApplicationContext(), "onLongItemClick position : " + pos, Toast.LENGTH_SHORT).show();
                                    mDataM.get(pos);
                                }
                            });
                            mPostRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(path2 + " 중고장터");
    }
}
