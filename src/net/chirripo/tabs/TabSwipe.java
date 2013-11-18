package net.chirripo.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabSwipe extends FragmentPagerAdapter {
    public TabSwipe(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            return new MyRoutes();
        case 1:
            return new Route();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        return 2;
    }
 
}
