package com.example.firstmultiscreenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText name;
    //to pass in next activity
    public static final String EXTRA_NAME = "com.example.firstmultiscreenapp.extra.name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void openActivity(View v){
        Toast.makeText(MainActivity.this, "I am working", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,MainActivity2.class);
        name = findViewById(R.id.nameId); //in R.id. is the id o the text view
        // i have just named the variable and id to same name
        String nameText = name.getText().toString();
        intent.putExtra(EXTRA_NAME,nameText); //key, value pair for sending
        startActivity(intent);

    }
}