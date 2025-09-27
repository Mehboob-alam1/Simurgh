package com.example.simurgh;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CyberTodayFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cyber_today, container, false);
        
        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);
        
        title.setText("Cyber Today");
        description.setText("Proactive Defense. Stay ahead of threats with predictive AI technology and real-time security updates for today's digital landscape.");
        
        return view;
    }
}
