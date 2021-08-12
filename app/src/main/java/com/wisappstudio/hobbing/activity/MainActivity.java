package com.wisappstudio.hobbing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hobbing.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wisappstudio.hobbing.fragment.MainPageFragment;
import com.wisappstudio.hobbing.fragment.MyPageFragment;
import com.wisappstudio.hobbing.fragment.ServicePageFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNV;
    private String userId;
    public static Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 메인 페이지 엑티비티
        activity = MainActivity.this;
        userId = getIntent().getStringExtra("user_id");

        // 하단 네비게이션
        BottomNavigate(userId);
    }

    private void BottomNavigate(String userId) {  //BottomNavigation 페이지 변경
        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // 클릭한 버튼의 id 값을 보내 화면을 전환함
                changeActivity(menuItem.getItemId());
                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.main);
    }

    private void changeActivity(int id) {
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        switch (id) {
            case R.id.main : {
                fragment = new MainPageFragment(userId);
                break;
            }
            case R.id.profile : {
                fragment = new MyPageFragment(userId);
                break;
            }
        }
        fragmentTransaction.add(R.id.content_layout, fragment, tag);
        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }
}
