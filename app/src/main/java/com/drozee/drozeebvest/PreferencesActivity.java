package com.drozee.drozeebvest;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PreferencesActivity extends AppCompatActivity {
    ArrayList<String> pref = new ArrayList<String>();
    RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(this, pref);
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mListReference;
    FirebaseAuth mAuth;
    String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        mAuth =  FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mListReference = mFirebaseDatabase.getReference();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.prefRV);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(viewAdapter);
        user = mAuth.getUid();
        Button add = (Button)findViewById(R.id.addBTN);
        final Button submit = (Button)findViewById(R.id.submitBTN);
        final EditText prefET = (EditText)findViewById(R.id.prefET);
        final EditText authET = (EditText)findViewById(R.id.authorET);
        submit.setEnabled(false);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(authET.getText().toString()!= "" && prefET.getText().toString()!= ""){
                pref.add(prefET.getText().toString() + "," + authET.getText());
                prefET.setText("");
                authET.setText("");
                viewAdapter.notifyDataSetChanged();
                if(viewAdapter.getItemCount() >= 5)
                {
//                    submit.setBackgroundResource(R.drawable.edittext);
                    submit.setEnabled(true);
                if (prefET.getText().toString().equals("") || authET.getText().toString().equals("")) {
                    Toast.makeText(PreferencesActivity.this, "Either field cannot be blank", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    pref.add(prefET.getText().toString() + "," + authET.getText().toString());
                    prefET.setText("");
                    authET.setText("");
                    viewAdapter.notifyDataSetChanged();
                    if (viewAdapter.getItemCount() >= 5)
                    {
                        submit.setBackgroundResource(R.drawable.edittext);
                    }
                }
            }}
        }});
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewAdapter.getItemCount() >= 5)
                {
              if(viewAdapter.getItemCount() == 1)
                        mListReference.child("Books").child(user).push().setValue(pref);
                    else
                        mListReference.child("Books").child(user).setValue(pref);
                    Toast.makeText(PreferencesActivity.this, "Uploading Data", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(PreferencesActivity.this, "Please enter at least 5 books", Toast.LENGTH_SHORT).show();
                }
            }
        });





        //Set Custom Font
        //Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "CaviarDreams_Bold.ttf");

    }
}
