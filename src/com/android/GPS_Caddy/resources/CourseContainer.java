package com.android.GPS_Caddy.resources;

/**
 * Created with IntelliJ IDEA.
 * User: Seeholzer
 * Date: 5/1/13
 * Time: 11:11 PM
 * To change this template use File | Settings | File Templates.
 */

import java.util.List;

public class CourseContainer {
    private Course course;
    // Add/generate getters and setters.

    public static class Course {
        private List<Hole> holes;
        private String courseName;
        private int numHoles;
        // Add/generate getters and setters.
    }

    public static class Hole {
        private int holeNumber;
        private String lat;
        private String lng;
        // Add/generate getters and setters.
    }
}
