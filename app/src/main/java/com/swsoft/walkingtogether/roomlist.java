package com.swsoft.walkingtogether;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class roomlist extends AppCompatActivity {

    long pressedTime;

    ArrayList<roomlist_item> items = new ArrayList<>();

    RecyclerView recyclerView;
    roomlist_apater adapter;
    FloatingActionButton roomlistfab;
    BottomNavigationView roomlistbottomnavigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roomlist);

        Toolbar roomlistToolbar = findViewById(R.id.roomlistToolbar);
        setSupportActionBar(roomlistToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

//임시데이터
        items.add(new roomlist_item("Test01", "00:00", "주소"));
        items.add(new roomlist_item("Test02", "00:00", "주소"));
        items.add(new roomlist_item("Test03", "00:00", "주소"));
        items.add(new roomlist_item("Test04", "00:00", "주소"));
        items.add(new roomlist_item("Test05", "00:00", "주소"));
        items.add(new roomlist_item("Test06", "00:00", "주소"));
        items.add(new roomlist_item("Test07", "00:00", "주소"));
        items.add(new roomlist_item("Test08", "00:00", "주소"));
        items.add(new roomlist_item("Test09", "00:00", "주소"));
        items.add(new roomlist_item("Test01", "00:00", "주소"));
        items.add(new roomlist_item("Test02", "00:00", "주소"));
        items.add(new roomlist_item("Test03", "00:00", "주소"));
        items.add(new roomlist_item("Test04", "00:00", "주소"));
        items.add(new roomlist_item("Test05", "00:00", "주소"));
        items.add(new roomlist_item("Test06", "00:00", "주소"));
        items.add(new roomlist_item("Test07", "00:00", "주소"));
        items.add(new roomlist_item("Test08", "00:00", "주소"));
        items.add(new roomlist_item("Test09", "00:00", "주소"));
        items.add(new roomlist_item("Test01", "00:00", "주소"));
        items.add(new roomlist_item("Test02", "00:00", "주소"));
        items.add(new roomlist_item("Test03", "00:00", "주소"));
        items.add(new roomlist_item("Test04", "00:00", "주소"));
        items.add(new roomlist_item("Test05", "00:00", "주소"));
        items.add(new roomlist_item("Test06", "00:00", "주소"));
        items.add(new roomlist_item("Test07", "00:00", "주소"));
        items.add(new roomlist_item("Test08", "00:00", "주소"));
        items.add(new roomlist_item("Test09", "00:00", "주소"));

        recyclerView = findViewById(R.id.roomlist_recyclerview);
        adapter = new roomlist_apater(this, items);
        recyclerView.setAdapter(adapter);

        roomlistfab = findViewById(R.id.roomlistfloating);
        roomlistfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(roomlist.this, createroom.class);
                startActivity(intent);
                finish();

            }
        });

        roomlistbottomnavigation = findViewById(R.id.roomlistbottomnavigation);
        roomlistbottomnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){

                    case R.id.notice:
                        Intent intentnotice = new Intent(roomlist.this, notice.class);
                        roomlist.this.startActivity(intentnotice);
                        finish();

                        break;
                    case R.id.myinfo:
                        Intent intentmyinfo = new Intent(roomlist.this, myinfo.class);
                        roomlist.this.startActivity(intentmyinfo);
                        finish();

                        break;
                    case R.id.howto:
                        Intent intenthowto = new Intent(roomlist.this, howto.class);
                        roomlist.this.startActivity(intenthowto);

                        break;
                    case R.id.setting:
                        Intent intentsetting = new Intent(roomlist.this, setting.class);
                        roomlist.this.startActivity(intentsetting);
                        finish();
                        break;

                }

                return false;
            }
        });



    }//oncreate

    @Override
    public void onBackPressed() {
        if ( pressedTime == 0 ) {
            Toast.makeText(roomlist.this, " 한 번 더 누르면 로그아웃 됩니다." , Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis();
        }
        else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if ( seconds > 2000 ) {
                Toast.makeText(roomlist.this, " 한 번 더 누르면 로그아웃 됩니다." , Toast.LENGTH_LONG).show();
                pressedTime = 0 ;
            }
            else {
                Intent intentsetting = new Intent(roomlist.this, login.class);
                roomlist.this.startActivity(intentsetting);
                super.onBackPressed();
//                finish(); // app 종료 시키기
            }
        }
    }
}//roomlist
