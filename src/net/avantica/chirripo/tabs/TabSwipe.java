package net.avantica.chirripo.tabs;

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
            // Top Rated fragment activity
            return new MyRoutes();
        case 1:
            // Games fragment activity
            return new Route();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        return 2;
    }
 
}
