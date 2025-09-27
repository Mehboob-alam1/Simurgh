package com.example.simurgh;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new CyberHygieneFragment();
            case 1: return new CyberDietFragment();
            case 2: return new CyberStudyFragment();
            case 3: return new CyberTodayFragment();
            case 4: return new CyberDoctorFragment();
            default: return new CyberHygieneFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5; // Number of tabs
    }
}

