package com.example.android.udacityforum;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Admin on 17-Apr-18.
 */

public class SliderAdapter extends PagerAdapter {
    public String[] description = {

            "פורום שאלות ותשובות לתלמידים מכל השכבות.",
            "תשאלו שאלות בכל נושא ורמה.",
            "תקבלו סיוע ומענה מתלמידים בוגרים ומנוסים.",
            "על ידי מענה על שאלות תוכלו לצבור נקודות ולהעלות את ציוניכם."
    };
    private int[] Slide_images = {
            R.drawable.app_icon,
            R.drawable.ask,
            R.drawable.get,
            R.drawable.comments
    };
    private String[] slide_headings = {
            "ברוכים הבאים לפורום",
            "תוכלו לשאול שאלות!",
            "לקבל תשובות!",
            "או לענות בעצמכם ולהרוויח."
    };
    // Arrays
    private Context context;

    SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slideimage);
        TextView slideheading = (TextView) view.findViewById(R.id.heading);
        TextView slidedescription = (TextView) view.findViewById(R.id.description);

        slideImageView.setImageResource(Slide_images[position]);
        slideheading.setText(slide_headings[position]);
        slidedescription.setText(description[position]);

        container.addView(view);


        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public void cleanUp() {
        context = null;
        description = null;
        slide_headings = null;
        Slide_images = null;
    }

}
