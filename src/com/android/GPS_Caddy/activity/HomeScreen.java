package com.android.GPS_Caddy.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import com.android.GPS_Caddy.R;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: Seeholzer
 * Date: 4/8/13
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class HomeScreen extends FragmentActivity implements View.OnClickListener {
    private Button clubsButton;
    private Button courseMapButton;
    private Button courseButton;
    private Button videoButton;
    private Typeface tf;
    private int test = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        initControls();
    }

    private void initControls() {
        tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        clubsButton = (Button) findViewById(R.id.clubsButton);
        clubsButton.setTypeface(tf);
        courseMapButton = (Button) findViewById(R.id.mapsButton);
        courseMapButton.setTypeface(tf);
        courseButton = (Button) findViewById(R.id.courseButton);
        courseButton.setTypeface(tf);
        videoButton = (Button) findViewById(R.id.videoButton);
        videoButton.setTypeface(tf);

        clubsButton.setOnClickListener(this);
        courseButton.setOnClickListener(this);
        courseMapButton.setOnClickListener(this);
        videoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.clubsButton:

                break;
            case R.id.mapsButton:
                Intent mapsIntent = new Intent(this, MapScreen.class);
                startActivity(mapsIntent);
                break;
            case R.id.courseButton:
                Intent courseIntent = new Intent(this, CourseScreen.class);
                startActivity(courseIntent);
                break;
            case R.id.videoButton:

                break;
        }
    }
}
