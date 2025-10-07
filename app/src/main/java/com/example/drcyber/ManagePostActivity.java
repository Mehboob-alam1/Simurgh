package com.example.drcyber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ManagePostActivity extends AppCompatActivity {

    private Spinner firstSpinner, secondSpinner;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_post);

        toolbar = findViewById(R.id.toolbar);
        firstSpinner = findViewById(R.id.firstSpinner);
        secondSpinner = findViewById(R.id.secondSpinner);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Manage Posts");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setSpinner();
    }

    private void setSpinner() {
        String[] firstSpinnerItems = {"Home", "About", "Services", "Contact"};
        ArrayAdapter<String> firstAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, firstSpinnerItems);
        firstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstSpinner.setAdapter(firstAdapter);

        String[] servicesItems = {"Cyber", "Web3", "XR", "IoT", "Charity"};
        ArrayAdapter<String> secondAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, servicesItems);
        secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Services")) {
                    secondSpinner.setVisibility(View.VISIBLE);
                    secondSpinner.setAdapter(secondAdapter);
                    
                    secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String subCategory = parent.getItemAtPosition(position).toString();
                            openServicesBlogActivity(selectedItem, subCategory);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                } else {
                    secondSpinner.setVisibility(View.GONE);
                    openServicesBlogActivity(selectedItem, null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                secondSpinner.setVisibility(View.GONE);
            }
        });
    }

    private void openServicesBlogActivity(String parentNode, String childNode) {
        Intent intent = new Intent(ManagePostActivity.this, ServicesBlogActivity.class);
        intent.putExtra("PN", parentNode);
        intent.putExtra("CN", childNode != null ? childNode : "null");
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}