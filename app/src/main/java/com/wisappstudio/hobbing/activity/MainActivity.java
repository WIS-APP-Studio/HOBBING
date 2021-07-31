package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hobbing.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wisappstudio.hobbing.fragment.MainPageFragment;
import com.wisappstudio.hobbing.fragment.MyPageFragment;
import com.wisappstudio.hobbing.fragment.ServicePageFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNV;
    private String userId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        if(userId.equals("admin")) {
            Toast.makeText(getApplicationContext(),userId+"으로 메인 접근",Toast.LENGTH_SHORT).show();

        }
        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent = getIntent();
                String userId = intent.getStringExtra("user_id");
                BottomNavigate(menuItem.getItemId(), userId);
                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.main);
    }

    private void BottomNavigate(int id, String userId) {  //BottomNavigation 페이지 변경
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if (id == R.id.main) {
                fragment = new MainPageFragment(userId);
            } else if (id == R.id.profile){
                fragment = new MyPageFragment(userId);
            } else {
                fragment = new ServicePageFragment();
            }
            fragmentTransaction.add(R.id.content_layout, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }
}
