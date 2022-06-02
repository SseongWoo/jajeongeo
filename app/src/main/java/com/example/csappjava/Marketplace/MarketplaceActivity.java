package com.example.csappjava.Marketplace;

import android.annotation.SuppressLint;
import android.content.Intent;
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
    TextView nicknametv, pointtv, schooltv;

    private String firstpath, secondpath;
    private PostAdapterMarketplace mAdapter;
    private List<PostMarketplace> mDataM;
    String spitem;
    ImageButton searchbt, searchbt2;
    EditText searchet;
    Spinner searchsp;
    int number = 0;

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
        String myschool = Mydata.getMyschool();
        View headerView = navigationView.getHeaderView(0);              //네비게이션 헤더 호출
        nicknametv = headerView.findViewById(R.id.nicknametv);              //네비게이션 헤더에 있는 아이템들 호출
        pointtv = headerView.findViewById(R.id.pointtv);
        schooltv = headerView.findViewById(R.id.schooltv);

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
        Intent intentpost = new Intent(this, BarcoadActivity.class);
        startActivity(intentpost);
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
        inflater.inflate(R.menu.option_market_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //앱바(App Bar)에 표시된 액션 또는 오버플로우 메뉴가 선택되면
    //액티비티의 onOptionsItemSelected() 메서드가 호출
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home:
                Intent intent = new Intent(MarketplaceActivity.this,CommunityActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            case R.id.option_delete:
                Toast.makeText(getApplicationContext(), "삭제", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_report:
                Toast.makeText(getApplicationContext(), "신고", Toast.LENGTH_SHORT).show();
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
                                String time = new String();
                                try {
                                    time = DateConverter.formatTimeString(((Timestamp) shot.get(FirebaseID.timestamp)).toDate().getTime());
                                } catch (Exception e) {
                                    Log.d("LOGTEST",  "오류오류" + title);
                                }
                                Log.d("LOGTEST",  "여기?" + time);
                                //String time = String.valueOf(shot.get(clock()));
                                //String time = String.valueOf(shot.get(FirebaseID.timestamp));
                                //Date date = Date.valueOf(shot.get(FirebaseID.timestamp));
                                //data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());

                                Log.d("MARKET",  "SP : " + sp + " SEARCH : " + search );

                                if(sp.equals("")&&search.equals("")){
                                    PostMarketplace data = new PostMarketplace(postId, userId, title, contents, img, price, nick, time, transaction);
                                    mDataM.add(data);
                                }
                                else if(sp.equals("닉네임")&&nick.contains(search)){
                                    PostMarketplace data = new PostMarketplace(postId, userId, title, contents, img, price, nick, time, transaction);
                                    mDataM.add(data);
                                }
                                else if(sp.equals("책명")&&contents.contains(search)){
                                    PostMarketplace data = new PostMarketplace(postId, userId, title, contents, img, price, nick, time, transaction);
                                    mDataM.add(data);
                                }
                                else if(sp.equals("제목")&&title.contains(search)){
                                    PostMarketplace data = new PostMarketplace(postId, userId, title, contents, img, price, nick, time, transaction);
                                    mDataM.add(data);
                                }
                                else if(sp.equals("교수명")&&title.contains(search)){
                                    PostMarketplace data = new PostMarketplace(postId, userId, title, contents, img, price, nick, time, transaction);
                                    mDataM.add(data);
                                }
                                else if(sp.equals("수업명")&&title.contains(search)){
                                    PostMarketplace data = new PostMarketplace(postId, userId, title, contents, img, price, nick, time, transaction);
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

                                    Intent intent = new Intent(MarketplaceActivity.this, MarketplaceActivity2.class);
                                    intent.putExtra("postid",hmpostid);
                                    intent.putExtra("userid",hmuserid);
                                    intent.putExtra("title",hmtitle);
                                    intent.putExtra("contents",hmcontents);
                                    intent.putExtra("img",hmimg);
                                    intent.putExtra("time",hmtime);
                                    intent.putExtra("price",hmprice);
                                    intent.putExtra("transaction",hmtransaction);

                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

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
