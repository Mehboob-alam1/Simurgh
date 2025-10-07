package com.example.drcyber;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ServicesBlogActivity extends AppCompatActivity implements AdminAdapter.OnEditClickListener, AdminAdapter.OnDeleteClickListener {
    private AdminAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ArrayList<Blog> list;
    private String PN, CN;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_blog);

        PN = getIntent().getStringExtra("PN");
        CN = getIntent().getStringExtra("CN");
        
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerServices);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(PN + (CN != null && !CN.equals("null") ? " - " + CN : ""));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (CN.equals("null")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("blogs").child(PN);
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference("blogs").child(PN).child(CN);
        }

        list = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Blog blog = snap.getValue(Blog.class);
                        list.add(blog);
                    }

                    adapter = new AdminAdapter(ServicesBlogActivity.this, list);
                    adapter.setOnDeleteClickListener(ServicesBlogActivity.this);
                    adapter.setOnEditClickListener(ServicesBlogActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ServicesBlogActivity.this));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onEditClick(int position) {
        Gson gson = new Gson();
        String data = gson.toJson(list.get(position));
        Intent i = new Intent(ServicesBlogActivity.this, EditPostActivity.class);
        i.putExtra("PN", PN);
        i.putExtra("CN", CN);
        i.putExtra("data", data);
        startActivity(i);
    }

    @Override
    public void onDeleteClick(int position) {
        databaseReference.child(list.get(position).getPushId()).removeValue((error, ref) -> {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            Toast.makeText(this, "Blog removed", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

