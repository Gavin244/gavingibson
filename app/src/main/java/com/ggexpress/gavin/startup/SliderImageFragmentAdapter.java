package com.ggexpress.gavin.startup;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 10/07/18.
 */

public class SliderImageFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public SliderImageFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragmentList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }
}
