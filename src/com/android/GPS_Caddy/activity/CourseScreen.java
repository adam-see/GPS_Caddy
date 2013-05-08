package com.android.GPS_Caddy.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import com.android.GPS_Caddy.R;
import com.android.GPS_Caddy.service.RestClient;
import org.holoeverywhere.internal._ViewGroup;
import org.holoeverywhere.widget.Switch;

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Seeholzer
 * Date: 4/21/13
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourseScreen extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_selection_screen);
        CourseLoader courseLoader = new CourseLoader();
        courseLoader.execute();
    }

    private void loadCourses() {
        RestClient restClient = new RestClient();
        restClient.parseJson("https://dl.dropboxusercontent.com/u/60424262/courses.json");
    }

    private class CourseLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            loadCourses();
            return null;
        }
    }
}
