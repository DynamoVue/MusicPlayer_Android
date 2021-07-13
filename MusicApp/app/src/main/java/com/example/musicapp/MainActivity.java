package com.example.musicapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.musicapp.Adapter.MainViewPagerAdapter;
import com.example.musicapp.Adapter.MainViewPagerAdapter;
import com.example.musicapp.Fragment.HomeFragment;
import com.example.musicapp.Fragment.PlaylistFragment;
import com.google.android.material.tabs.TabLayout;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    private void printKeyHash() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        PackageInfo info = getPackageManager().getPackageInfo("com.example.musicapp", PackageManager.GET_SIGNATURES);
        for (Signature signature: info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("keyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            printKeyHash();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        createPage();
        init();
    }

    public void init() {
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mainViewPagerAdapter.addFragment(new HomeFragment(), "HOME");
        mainViewPagerAdapter.addFragment(new PlaylistFragment(), "SEARCH");
        viewPager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.icontrangchu);
        tabLayout.getTabAt(1).setIcon(R.drawable.iconsearch);
    }

    public void createPage() {
        tabLayout = findViewById(R.id.myTabLayout);
        viewPager = findViewById(R.id.myViewPager);
    }
}