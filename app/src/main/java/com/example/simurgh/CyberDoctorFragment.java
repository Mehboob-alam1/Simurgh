package com.example.simurgh;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CyberDoctorFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cyber_doctor, container, false);
        
        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);
        
        title.setText("Cyber Doctor");
        description.setText("24/7 Support. Constant vigilance for unwavering protection. Our rapid response teams operate around the clock to neutralize threats swiftly.");
        
        return view;
    }
}
