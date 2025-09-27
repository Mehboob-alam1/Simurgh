package com.example.simurgh;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ServicesFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_services, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPagerS);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);



        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Cyber Hygiene");
                    break;
                case 1:
                    tab.setText("Cyber Diet");
                    break;
                case 2:
                    tab.setText("Cyber Study");
                    break;
                case 3:
                    tab.setText("Cyber Today");
                    break;
                case 4:
                    tab.setText("Cyber Doctor");
                    break;
            }
        }).attach();
        return  view;

    }
}