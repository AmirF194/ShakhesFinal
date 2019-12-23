package fathi.shakhes;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import shakhes.R;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    final Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = FragmentOne.newInstance();
                break;
            case 1:
                fragment = FragmentTwo.newInstance();
                break;
            case 2:
                fragment = FragmentThree.newInstance();
                break;
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.tab1);
            case 1:
                return context.getResources().getString(R.string.tab2);
            case 2:
                return context.getResources().getString(R.string.tab3);
        }

        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
