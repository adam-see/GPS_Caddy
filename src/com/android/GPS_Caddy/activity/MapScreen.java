package com.android.GPS_Caddy.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import com.android.GPS_Caddy.R;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.maps.GeoPoint;
import org.holoeverywhere.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Seeholzer
 * Date: 4/10/13
 * Time: 11:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapScreen extends FragmentActivity implements View.OnClickListener, LocationListener {
    private GoogleMap mMap;
    private Button btnHome;
    private Button btnCenter;
    private Button btnStartPoint;
    private Button btnAverage;
    private LocationManager locationManager;
    private boolean gpsServiceEnabled = false;
    private Typeface tf;
    private String provider;
    private LatLng currLatLng;
    private Location location;
    private Criteria criteria;
    private Marker setMarker;
    private Location currLocation;
    private Location placedLocation;
    private Location holeLocation;
    private Polyline placedPolyLine;
    private TextView distanceTextView;
    ScheduledExecutorService worker;

    private static final int DEF_ZOOM = 17;
    private static final int MIN_TIME = 100;
    private static final int MIN_DISTANCE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_screen);
        initControls();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        mMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, this);
        mMap.setMyLocationEnabled(true);
    }

    private void initControls() {
        tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        btnHome = (Button) findViewById(R.id.btnHome);
        btnAverage = (Button) findViewById(R.id.btnAverage);
        btnCenter = (Button) findViewById(R.id.btnCenter);
        btnStartPoint = (Button) findViewById(R.id.btnStartPnt);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        currLocation = new Location("curr_location");
        placedLocation = new Location("placed_location");
        holeLocation = new Location("hole_location");
        worker = Executors.newSingleThreadScheduledExecutor();

        btnHome.setTypeface(tf);
        btnAverage.setTypeface(tf);
        btnCenter.setTypeface(tf);
        btnStartPoint.setTypeface(tf);
        distanceTextView.setTypeface(tf);

        btnHome.setOnClickListener(this);
        btnAverage.setOnClickListener(this);
        btnCenter.setOnClickListener(this);
        btnStartPoint.setOnClickListener(this);

        setUpMapIfNeeded();
        isServiceEnabled();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not have been
     * completely destroyed during this process (it is likely that it would only be stopped or
     * paused), {@link #onCreate(Bundle)} may not be called again so we should call this method in
     * {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsServiceEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location in provider -> use default
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            currLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, DEF_ZOOM));
        }
        else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), DEF_ZOOM));
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setTrafficEnabled(false);

        //add map listener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (setMarker != null) {
                    //Get the two locations for distance
                    placedLocation.setLatitude(latLng.latitude);
                    placedLocation.setLongitude(latLng.longitude);
                    double distance = getDistance(placedLocation, currLocation);
                    String distanceFormat = String.format("%.1f", distance);
                    setMarker.setPosition(latLng);
                    setMarker.setSnippet(distanceFormat);
                    setMarker.showInfoWindow();

                    distanceTextView.setText("Marker: " + distanceFormat + " Yds");

                    //Move the line with the points
                    List<LatLng> pointsToDestination = new ArrayList<LatLng>();
                    pointsToDestination.add(new LatLng(placedLocation.getLatitude(), placedLocation.getLongitude()));
                    pointsToDestination.add(new LatLng(currLocation.getLatitude(), currLocation.getLongitude()));
                    placedPolyLine.setPoints(pointsToDestination);

                    //Rotate the map to the placed location as north, then adjust the map position
                    rotateMap(currLocation, placedLocation, currLatLng);
                    moveCenterPointDown();
                }
                else {
                    if (currLocation.getLatitude() == 0.0 && currLocation.getLongitude() == 0.0) {
                        currLocation = location;
                    }
                    //Get the two locations for distance
                    placedLocation.setLatitude(latLng.latitude);
                    placedLocation.setLongitude(latLng.longitude);

                    //Calculate the distance in yards and set the marker
                    double distance = getDistance(placedLocation, currLocation);
                    String distanceFormat = String.format("%.1f", distance);
                    setMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Yards").snippet(distanceFormat));
                    setMarker.showInfoWindow();

                    distanceTextView.setText("Marker: " + distanceFormat + " Yds");

                    //Draw a line between the points
                    placedPolyLine = mMap.addPolyline(new PolylineOptions().
                            add(latLng, new LatLng(currLocation.getLatitude(), currLocation.getLongitude())).width(3).color(Color.RED));

                    //Rotate the map to the placed location as north, then adjust the map position
                    rotateMap(currLocation, placedLocation, currLatLng);
                    moveCenterPointDown();
                }
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    /**
     * Rotate the map to place the target location as north
     * @param targetLocation
     * @param bearingTo
     * @param targetLatLng
     */
    private void rotateMap(Location targetLocation, Location bearingTo, LatLng targetLatLng) {
        //Find the Bearing from current location to next location
        float targetBearing = targetLocation.bearingTo(bearingTo);
        CameraPosition currentPlace = new CameraPosition.Builder()
                .target(targetLatLng)
                .bearing(targetBearing).zoom(DEF_ZOOM).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(currentPlace));
    }

    private void moveCenterPointDown() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mMap.animateCamera(CameraUpdateFactory.scrollBy(0, -400));
            }
        }, 1200);
    }

    /**
     * Calculate the distance between two raw locations
     * @param location1 The raw location
     * @param location2 The raw location
     * @return The distance in yards between the two points
     */
    private double getDistance(Location location1, Location location2)
    {
        double distance;
        final double METERS_IN_YARD = 1.0936133;
        GeoPoint point1 = new GeoPoint((int) (location1.getLatitude() * 1E6), (int) (location1.getLongitude() * 1E6));
        GeoPoint point2 = new GeoPoint((int) (location2.getLatitude() * 1E6), (int) (location2.getLongitude() * 1E6));

        //Set the lat and long to the new E6 values
        location1.setLatitude(point1.getLatitudeE6() / 1E6);
        location1.setLongitude(point1.getLongitudeE6() / 1E6);
        location2.setLatitude(point2.getLatitudeE6() / 1E6);
        location2.setLongitude(point2.getLongitudeE6() / 1E6);

        distance = (location1.distanceTo(location2)) * METERS_IN_YARD;
        return distance;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHome:
                Intent intent = new Intent(this, HomeScreen.class);
                startActivity(intent);
                break;
            case R.id.btnAverage:

                break;
            case R.id.btnCenter:
                mMap.animateCamera(CameraUpdateFactory.newLatLng(currLatLng));
                break;
            case R.id.btnStartPnt:

                break;
        }
    }

    /**
     * Check to see if the GPS is enabled
     */
    private void isServiceEnabled() {
        if (!gpsServiceEnabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        currLocation = location;

        double distance = getDistance(location, placedLocation);
        String distanceFormat = String.format("%.1f", distance);
        if (setMarker != null) {
            setMarker.remove();
            setMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(placedLocation.getLatitude(), placedLocation.getLongitude()))
                    .title("Yards").snippet(distanceFormat));
            setMarker.showInfoWindow();

            //Move the line with the points
            List<LatLng> pointsToDestination = new ArrayList<LatLng>();
            pointsToDestination.add(new LatLng(placedLocation.getLatitude(), placedLocation.getLongitude()));
            pointsToDestination.add(currLatLng);
            placedPolyLine.setPoints(pointsToDestination);

            distanceTextView.setText("Marker: " + distanceFormat + " Yds");
        }

        //TODO: adjust the polyline to move with location changes
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(currLatLng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderEnabled(String provider) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderDisabled(String provider) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
