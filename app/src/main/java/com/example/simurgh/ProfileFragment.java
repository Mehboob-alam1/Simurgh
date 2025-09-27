package com.example.simurgh;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    private TextView txtUserName, txtEmail, txtPhoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        txtUserName = view.findViewById(R.id.txtUserName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);

        // Set default Doctor Cyber profile information
        txtUserName.setText("DOCTOR CYBER USER");
        txtEmail.setText("user@doctorcyber.com");
        txtPhoneNumber.setText("+1 (555) 123-4567");

        return view;
    }
}