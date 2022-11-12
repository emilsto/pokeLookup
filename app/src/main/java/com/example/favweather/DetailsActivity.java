package com.example.favweather;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();


        String pokeName = intent.getStringExtra("pokeName");
        String pokeWeight = intent.getStringExtra("pokeWeight");
        String pokeHeight = intent.getStringExtra("pokeHeight");
        String pokePic = intent.getStringExtra("pokePic");

        TextView name = (TextView) findViewById(R.id.nameText);
        TextView weight = (TextView) findViewById(R.id.weightText);
        TextView height = (TextView) findViewById(R.id.heightText);

        name.setText(pokeName);
        weight.setText(getString(R.string.weight) + " " + pokeWeight);
        height.setText(getString(R.string.height) + " " + pokeHeight);

        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(pokePic).into(imageView);

    }




    public void returnMain(View view){
        //move back to main
        Intent intent = new Intent(this, MainActivity.class);
        startActivity( intent );
    }
}