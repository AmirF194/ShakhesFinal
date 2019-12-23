package fathi.shakhes.FoodMessageTab ;

import android.content.Intent;
import android.content.res.AssetManager;
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

import fathi.shakhes.FoodActivity;
import shakhes.R;

public class TabMessage extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.tab_activity_message);

        tabLayout = (TabLayout) findViewById(R.id.tabs_foodmessagetab);
        viewPager = (ViewPager) findViewById(R.id.viewPager_foodmessagetab);

        init();
        changeTabsFont();
    }

    private void init(){
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(final ViewPager viewPager) {
        PagerAdapterMessage viewPagerAdapter = new PagerAdapterMessage(getSupportFragmentManager(), this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(1);
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
                    AssetManager mgr = getAssets();
                    Typeface tf = Typeface.createFromAsset(mgr, "fonts/IRANSans.ttf");//Font file in /assets
                    ((TextView) tabViewChild).setTypeface(tf);
                    ((TextView) tabViewChild).setTextColor(Color.BLACK);
                }
            }
        }
    }
    @Override
    public void  onBackPressed() {
        finish();
        Intent myIntent = new Intent(TabMessage.this, FoodActivity.class);
        startActivity(myIntent);
    }

}
