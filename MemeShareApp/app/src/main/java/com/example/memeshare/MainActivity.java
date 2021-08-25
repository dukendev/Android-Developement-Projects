package com.example.memeshare;



import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.service.chooser.ChooserTargetService;
import android.view.View;


import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import com.bumptech.glide.Glide;


import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {



    private ImageView imageview;
    private TextView infotextview;
    private String imageUrl = null;
    private String subreddit = null;
    private String author = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageview = findViewById(R.id.MemeImageView);
        infotextview = findViewById(R.id.InfoTextView);

        loadMeme();
    }

    protected void loadMeme(){




        String url ="https://meme-api.herokuapp.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //
                        try {
                            imageUrl = response.getString("url");
                            subreddit = response.getString("subreddit");
                            author = response.getString("author");
                            //glide to display the image
                            Glide.with(MainActivity.this).load(imageUrl).into(imageview);
                            infotextview.setText("From r/" + subreddit + " by " + author);
                        }
                        catch (Exception e){
                            Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                        Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void shareActivity(View view) {
       Toast.makeText(this, "Showing sharing options", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Check out this dope meme " + imageUrl);
        Intent chooser = Intent.createChooser(intent,"Choose the app");
        startActivity(chooser);

    }

    public void nextActivity(View view) {
        loadMeme();
    }
}