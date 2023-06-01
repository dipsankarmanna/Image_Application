package com.example.imageapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.imageapplication.databinding.ActivityImageBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {

    ActivityImageBinding binding;

    ArrayList<ImageModel> list;
    ImageApapter adapter;
    String url="https://jsonplaceholder.typicode.com/photos/";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog=new ProgressDialog(ImageActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        list=new ArrayList<>();
        adapter=new ImageApapter(list,ImageActivity.this);
        binding.img.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        binding.img.setAdapter(adapter);
        s_Response();
    }
    public void s_Response(){
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(ImageActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        // we are getting each json object.
                        JSONObject responseObj = response.getJSONObject(i);
                        String imgurl = responseObj.getString("url");
                        list.add(new ImageModel(imgurl));
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ImageActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }
}