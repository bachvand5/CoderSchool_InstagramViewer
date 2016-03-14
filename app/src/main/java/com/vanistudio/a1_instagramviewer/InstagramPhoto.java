package com.vanistudio.a1_instagramviewer;

import android.text.Spanned;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by thuynh6 on 3/13/2016.
 */
public class InstagramPhoto {
    public String username;
    public String userAvatar;
    public String caption;
    public String imageUrl;
    public int imageHeight;
    public Long timestamp;
    public Long likesCount;
    public String type;
    public ArrayList<Spanned> comments = new ArrayList<>();

}
