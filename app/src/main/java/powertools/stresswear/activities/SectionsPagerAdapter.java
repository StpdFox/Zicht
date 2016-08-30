package powertools.stresswear.activities;

/**
 * Created by Surface Pro 3 on 29-8-2016.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import powertools.stresswear.fragments.sensors.SensorsFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SensorsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

}