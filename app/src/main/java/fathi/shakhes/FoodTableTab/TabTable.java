package fathi.shakhes.FoodTableTab;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import fathi.shakhes.AppSessionManager;
import fathi.shakhes.FoodActivity;
import shakhes.R;

public class TabTable extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String token;
    AppSessionManager s ;
    Typeface typeface ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        new MyTask().execute("Trigger");
        s = new AppSessionManager(getApplicationContext());
        token = s.getDiningData().get("access_token");
        typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.tab_activity_table);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        init();
        changeTabsFont();
    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    private void init(){
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(final ViewPager viewPager) {
        PagerAdapterTable viewPagerAdapter = new PagerAdapterTable(getSupportFragmentManager(), this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(2);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(viewPagerAdapter);
    }
    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                    ((TextView) tabViewChild).setTextSize(10);
                    ((TextView) tabViewChild).setTextColor(Color.BLACK);
                }
            }
        }
    }
    @Override
    public void  onBackPressed() {
        Intent myIntent = new Intent(TabTable.this, FoodActivity.class);
        startActivity(myIntent);
        finish();
    }

}