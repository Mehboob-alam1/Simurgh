package com.example.simurgh;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {


    private DatabaseReference databaseReference;
    private TextView txtUserName,txtEmail,txtPhoneNumber;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        txtUserName=view.findViewById(R.id.txtUserName);
        txtEmail=view.findViewById(R.id.txtEmail);
        txtPhoneNumber=view.findViewById(R.id.txtPhoneNumber);
 databaseReference= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

 databaseReference.addValueEventListener(new ValueEventListener() {
     @Override
     public void onDataChange(@NonNull DataSnapshot snapshot) {
         if (snapshot.exists()){

           User user=  snapshot.getValue(User.class);
           txtUserName.setText(user.getUserName().toUpperCase());
             txtEmail.setText(user.getEmail());
             txtPhoneNumber.setText(user.getPhoneNumber());
         }
     }

     @Override
     public void onCancelled(@NonNull DatabaseError error) {

     }
 });
        return view;
    }
}