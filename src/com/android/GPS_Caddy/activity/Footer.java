package com.android.GPS_Caddy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.android.GPS_Caddy.R;
import org.holoeverywhere.widget.Button;

/**
 * Created with IntelliJ IDEA.
 * User: Seeholzer
 * Date: 4/21/13
 * Time: 4:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Footer extends RelativeLayout implements View.OnClickListener {

    private Button homeButton;
    private LayoutInflater inflater;

    public Footer (Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.footer, this, true);

        homeButton = (Button) findViewById(R.id.btnHome);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHome:
                Intent homeIntent = new Intent();
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(homeIntent);
                break;
        }
    }
}
