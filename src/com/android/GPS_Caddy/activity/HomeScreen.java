package com.android.GPS_Caddy.activity;


import android.app.Activity;
import android.os.Bundle;
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
public class HomeScreen extends Activity {
    private Button clubsButton;
    private Button courseMapButton;
    private Button courseButton;
    private Button videoButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        initControls();
    }

    private void initControls() {
        clubsButton = (Button) findViewById(R.id.clubsButton);
        courseMapButton = (Button) findViewById(R.id.mapsButton);
        courseButton = (Button) findViewById(R.id.courseButton);
        videoButton = (Button) findViewById(R.id.videoButton);
    }

    public void onBtnClick(View v) {
        switch(v.getId()) {
            case R.id.clubsButton:
                Toast.makeText(this, "Clubs button pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mapsButton:

                break;
            case R.id.courseButton:

                break;
            case R.id.videoButton:

                break;
        }
    }
}
