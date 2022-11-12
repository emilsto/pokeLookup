package com.example.favweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //Aktiviteetin tila (state) == aktiviteetin tietojasenten data
    private String mUrl = "https://pokeapi.co/api/v2/pokemon/";
    private RequestQueue mQueue;
    String pokemonType = "";
    String pokemonId = "";
    String searchItem = "";
    String pokeHeight = "";
    String pokeWeight = "";
    String pokeImage = "";
    String pokemonName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);

        if(savedInstanceState != null) {
            pokemonId = savedInstanceState.getString("pokemonId");
            pokemonType = savedInstanceState.getString("pokemonType");
            pokemonName = savedInstanceState.getString("pokemonName");
        }

        TextView pokeName = (TextView) findViewById(R.id.pokeName);
        TextView pokeId = (TextView) findViewById(R.id.pokeWeight);
        TextView pokeType = (TextView) findViewById(R.id.pokeType);

        pokeName.setText(pokemonName);
        pokeId.setText("PokeDexID" + pokemonId);
        pokeType.setText(getString(R.string.poke_type) + pokemonType);

    }

    //Talletetaan olion tila
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putString("pokemonType", pokemonType);
        savedInstanceState.putString("pokemonId", pokemonId);
        savedInstanceState.putString("pokemonName", pokemonName);
        super.onSaveInstanceState(savedInstanceState);
    }


    public void fetchPoke(View view){
        //check if search field is empty, handle if yes
        EditText searchField = (EditText) findViewById(R.id.pokemonNameInput);
        searchItem = searchField.getText().toString();
        if(searchItem.equals("")){
            Toast.makeText(this, getString(R.string.noinput_error), Toast.LENGTH_SHORT).show();
        }else{
        getSearchItem(view);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        parseJsonAndUpdateUi( response );
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //if no internet connection, show error message
                        Toast.makeText(getApplicationContext(), getString(R.string.nointernet_error), Toast.LENGTH_LONG).show();
                    }
                });
        mQueue.add(jsonObjectRequest);
        }
    }

    private void parseJsonAndUpdateUi(JSONObject pokeObject){
        TextView pokeName = (TextView) findViewById(R.id.pokeName);
        TextView pokeId = (TextView) findViewById(R.id.pokeWeight);
        TextView pokeType = (TextView) findViewById(R.id.pokeType);

        try {
            pokemonName = pokeObject.getString("name");
            pokeName.setText(pokemonName);
            pokemonId = pokeObject.getString("id");
            pokeId.setText("PokeDexID: " + pokemonId);
            pokemonType = pokeObject.getJSONArray("types").getJSONObject(0).getJSONObject("type").getString("name");
            pokeType.setText(getString(R.string.poke_type) + pokemonType);
            pokeHeight = pokeObject.getString("height");
            pokeWeight = pokeObject.getString("weight");
            pokeImage = pokeObject.getJSONObject("sprites").getString("front_default");
        } catch (JSONException e) {
            e.printStackTrace();
        } 

    }

    public void getSearchItem(View view) {
        //reset url
        mUrl = "https://pokeapi.co/api/v2/pokemon/";
        EditText pokemonNameInput = findViewById(R.id.pokemonNameInput);
        String pokeName = (pokemonNameInput.getText().toString());
        //format url string to include the name
        mUrl += pokeName;
        searchItem = pokeName;
    }

    public void viewDetails(View view){
        //check if data has been fetched, if not, do not move to details
        if (searchItem.equals("")){
            Toast.makeText(getApplicationContext(), getString(R.string.nosearch_error), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("pokeName", searchItem);
            intent.putExtra("pokeWeight", pokeWeight);
            intent.putExtra("pokeHeight", pokeHeight);
            intent.putExtra("pokePic", pokeImage);
            startActivity(intent);
        }
    }
}