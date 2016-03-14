package com.vanistudio.a1_instagramviewer;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotoActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotoAdapter photoAdapter;
    private SwipeRefreshLayout slPhotos;
    private static int latestComments = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        photos = new ArrayList<>();
        photoAdapter = new InstagramPhotoAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(photoAdapter);
        FetchPopularPhotos();*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        photos = new ArrayList<>();
        photoAdapter = new InstagramPhotoAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(photoAdapter);
        slPhotos = (SwipeRefreshLayout) findViewById(R.id.slPhotos);
        slPhotos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchPopularPhotos();
            }
        });

        slPhotos.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        FetchPopularPhotos();
    }

    public void FetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            //worked (return code 200)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.i("DEBUG", response.toString());
                photoAdapter.clear();
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        JSONObject dump = photoJSON.getJSONObject("user");

                        photo.username = dump.getString("username");
                        photo.userAvatar = dump.getString("profile_picture");

                        dump = photoJSON.getJSONObject("caption");
                        photo.caption = dump.getString("text");

                        dump = photoJSON.getJSONObject("likes");
                        photo.likesCount = dump.getLong("count");

                        dump = photoJSON.getJSONObject("images").getJSONObject("standard_resolution");
                        photo.imageUrl = dump.getString("url");
                        photo.imageHeight = dump.getInt("height");

                        photo.timestamp = photoJSON.getLong("created_time")*1000;
                        photo.type = photoJSON.getString("type");

                        JSONArray commentsJSON = photoJSON.getJSONObject("comments").getJSONArray("data");

                        for (int j = 1; j <= latestComments; j++) {
                            JSONObject dump1 = commentsJSON.getJSONObject(commentsJSON.length() - j);
                            photo.comments.add(Html.fromHtml("<font color='#218380'><b>" + dump1.getJSONObject("from").getString("username") + "</b></font>  " + dump1.getString("text")));
                        }
                        photos.add(photo);
                    }

                }
                catch (JSONException e) {
                    e.getStackTrace();
                }
                //Log.i("INFO", photos.toString() );

                photoAdapter.notifyDataSetChanged();
                slPhotos.setRefreshing(false);
            }

            //failed
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });

    }
}
