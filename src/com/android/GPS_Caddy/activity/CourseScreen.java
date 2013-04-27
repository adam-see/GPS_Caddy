package com.android.GPS_Caddy.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import com.android.GPS_Caddy.R;
import org.holoeverywhere.internal._ViewGroup;

/**
 * Created with IntelliJ IDEA.
 * User: Seeholzer
 * Date: 4/21/13
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourseScreen extends Footer {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_selection_screen);
//        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.footer);
//        ViewGroup.inflate(CourseScreen.this, R.layout.course_selection_screen, viewGroup);
    }
}
