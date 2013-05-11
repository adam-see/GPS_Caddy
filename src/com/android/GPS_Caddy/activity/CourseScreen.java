package com.android.GPS_Caddy.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.android.GPS_Caddy.R;
import com.android.GPS_Caddy.resources.CourseListings;
import com.android.GPS_Caddy.service.RestClient;

/**
 * Created with IntelliJ IDEA.
 * User: Seeholzer
 * Date: 4/21/13
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourseScreen extends FragmentActivity {

    private TableLayout table;
    private CourseListings courseListings;
    private Typeface tf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_selection_screen);
        tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        table = (TableLayout) findViewById(R.id.courseTableLayout);
        CourseLoader courseLoader = new CourseLoader();
        courseLoader.execute();
    }

    private void loadCourses() {
        RestClient restClient = new RestClient();
//        CourseContainer course = restClient.parseJson("https://dl.dropboxusercontent.com/u/60424262/Courses/birch_creek.json", CourseContainer.class);
        courseListings = restClient.parseJson("https://dl.dropboxusercontent.com/u/60424262/Courses/courses.json", CourseListings.class);
    }

    private void displayCourses() {
        table.removeAllViews();
        TableLayout.LayoutParams params= new TableLayout.LayoutParams (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5,30,5,30);

        for (String courseNames : courseListings.getCourseNames()){
            TextView view = new TextView(getApplication());
            TableRow row = new TableRow(getApplication());
            row.setLayoutParams(params);
            view.setText(courseNames);
            view.setTypeface(tf);
            view.setTextColor(Color.WHITE);
            view.setTextSize(30f);
            view.setGravity(Gravity.CENTER);
            row.addView(view);

            table.addView(row, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        }
    }

    private class CourseLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            loadCourses();
//            return course;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            displayCourses();
        }
    }
}
