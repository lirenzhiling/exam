package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class BottomList extends AppCompatActivity implements View.OnClickListener {
    private BottomNavigationView bottom;
    private HomeFragment homeFragment;
    private AddFragment addFragment;
    private MeFragment meFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        bottom = findViewById(R.id.bottom);
        selectFragment(0);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    selectFragment(0);
                }else if (item.getItemId() == R.id.add) {
                    selectFragment(1);
                } else {
                    selectFragment(2);
                }
                return true;
            }
        });

    }

    private void selectFragment(int position) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(fragmentTransaction);
        if (position == 0) {
            if (homeFragment == null) {
                homeFragment = new HomeFragment();
                fragmentTransaction.add(R.id.allcontent, homeFragment);
            } else {
                fragmentTransaction.show(homeFragment);
            }
            homeFragment.mounted();
        }  else if (position == 1) {
            if (addFragment == null) {
                addFragment = new AddFragment();
                fragmentTransaction.add(R.id.allcontent, addFragment);
            } else {
                fragmentTransaction.show(addFragment);
            }

        } else {
            if (meFragment == null) {
                meFragment = new MeFragment();
                fragmentTransaction.add(R.id.allcontent, meFragment);
            } else {
                fragmentTransaction.show(meFragment);
            }
        }
        fragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
        }
        if (addFragment != null) {
            fragmentTransaction.hide(addFragment);
        }
        if (meFragment != null) {
            fragmentTransaction.hide(meFragment);
        }
    }

    @Override
    public void onClick(View v) {
    }
}