package com.vanistudio.a1_instagramviewer;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by thuynh6 on 3/13/2016.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotoAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        //TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.ivAvatar);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
        TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
        ListView lvComments = (ListView) convertView.findViewById(R.id.lvComments);

        //tvCaption.setText(item.caption);
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(item.imageUrl).placeholder(R.drawable.loading).into(ivPhoto);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderWidthDp(0)
                //.cornerRadiusDp(30)
                //.oval(false)
                .oval(true)
                .build();

        Picasso.with(getContext()).load(item.userAvatar).fit().transform(transformation).into(ivAvatar);

        tvUsername.setText(item.username);
        Date dCreatedTime = new Date(item.timestamp);
        Calendar cCreatedTime = Calendar.getInstance();
        cCreatedTime.setTime(dCreatedTime);
        Calendar now = Calendar.getInstance();
        long diffMillis= Math.abs(now.getTimeInMillis() - cCreatedTime.getTimeInMillis());
        long differenceInTime = TimeUnit.MINUTES.convert(diffMillis, TimeUnit.MILLISECONDS);
        String timestamp;
        if (differenceInTime < 60) {
            timestamp = "<font color='#218380'>" + String.valueOf(differenceInTime) + " min ago \u2022 " + "</font>";
        }
        else {
            differenceInTime = differenceInTime/60;
            if (differenceInTime < 24) {
                timestamp = "<font color='#218380'>" + String.valueOf(differenceInTime) + " hr ago \u2022 " + "</font>";
            }
            else {
                differenceInTime = differenceInTime/24;
                timestamp = "<font color='#218380'>" + String.valueOf(differenceInTime) + " days ago \u2022 " + "</font>";
            }
        }
        tvTimeStamp.setText(Html.fromHtml(timestamp + item.caption));

        tvLikesCount.setText("\u2764 " + String.valueOf(item.likesCount) + " likes");

        ArrayAdapter<Spanned> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,item.comments);
        lvComments.setAdapter(arrayAdapter);
        return convertView;
    }
}
