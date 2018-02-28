package com.weather.jpinto.tempo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 13;
    private FusedLocationProviderClient mFusedLocationClient;
    private final String api = "https://www.metaweather.com/api/";
    private Boolean isDay = true;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.weather_layout)
    ConstraintLayout weather_layout;
    @BindView(R.id.weather_bar)
    ProgressBar weather_bar;
    @BindView(R.id.weather_location)
    TextView weather_location;
    @BindView(R.id.weather_status)
    TextView weather_status;
    @BindView(R.id.weather_temp)
    TextView weather_temp;
    @BindView(R.id.min_temp)
    TextView min_temp;
    @BindView(R.id.max_temp)
    TextView max_temp;
    @BindView(R.id.weather_image)
    ImageView weather_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupActionBarHomeButton();
        setupDrawerListeners();


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();
    }

    private void setupActionBarHomeButton() {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_action_menu);
        }
    }

    private void setupDrawerListeners() {
        drawer_layout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object

                                Double lat = location.getLatitude();
                                Double lon = location.getLongitude();
                                whereOnWorld(lat, lon);
                            } else {
                                whereOnWorld("Lisbon");
                            }
                        }
                    });
        }else{
            whereOnWorld("Lisbon");
        }
    }

    private void whereOnWorld(double lat, double lon) {
        Ion.with(this)
                .load(api + "location/search/?lattlong=" + lat + "," + lon)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        whereOnWorldResultParse(result);
                    }
                });
    }

    private void whereOnWorld(String city) {
        Ion.with(this)
                .load(api + "location/search/?query=" + city)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        whereOnWorldResultParse(result);
                    }
                });
    }

    private void whereOnWorldResultParse(String result) {
        try {
            JSONArray citiesList = new JSONArray(result);
            JSONObject city = citiesList.getJSONObject(0);
            WeatherActivity.this.setTitle(city.getString("title"));
            loadWeatherTodayData(city.getInt("woeid"));

        } catch (JSONException je) {
            Log.e("JsonParse", je.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    checkLocationPermission();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("Location", "Denied");
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void loadWeatherTodayData(int woeid) {
        Ion.with(this)
                .load(api + "location/" + woeid + "/")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {

                            WeatherDay today = new WeatherDay();
                            JSONObject report = new JSONObject(result);
                            JSONArray weatherReportList = report.getJSONArray("consolidated_weather");
                            JSONObject weatherLocation = report.getJSONObject("parent");
                            JSONObject todayWeatherDescription = weatherReportList.getJSONObject(0);

                            String[] coordinates = report.getString("latt_long").split(",");
                            today.setCity(report.getString("title"));
                            today.setCountry(weatherLocation.getString("title"));
                            today.setWoeid(report.getInt("woeid"));
                            today.setLat(Double.parseDouble(coordinates[0]));
                            today.setLon(Double.parseDouble(coordinates[1]));
                            today.setWeather_state_name(todayWeatherDescription.getString("weather_state_name"));
                            today.setWeather_state_abbr(todayWeatherDescription.getString("weather_state_abbr"));
                            today.setThe_temp(todayWeatherDescription.getDouble("the_temp"));
                            today.setMin_temp(todayWeatherDescription.getDouble("min_temp"));
                            today.setMax_temp(todayWeatherDescription.getDouble("max_temp"));

                            setupNavigationDrawerText(today);

                            today.setSunrise(parseDateString(report.getString("sun_rise")));
                            today.setSunset(parseDateString(report.getString("sun_rise")));
                            checkDayCycler(today);

                            addImageWeather(today.getWeather_state_abbr());

                            populateWeatherLayout(today);

                            changeWeatherLayout();

                        } catch (Exception io) {
                            Log.d("onComplete", io.toString());
                        }
                    }

                });
    }

    private long parseDateString(String time) {

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
        try {
            Date d = f.parse(time.split("T")[0]);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private void checkDayCycler(WeatherDay today) {

        long time = System.currentTimeMillis() / 1000;
        long sunrise = today.getSunrise();
        long sunset = today.getSunset();

        if (sunrise <= time && time <= sunset) {
            Log.d("Weather", "onCompleted: isDay");
            isDay = true;
        } else {
            Log.d("Weather", "onCompleted: isNight");
            isDay = false;
        }
    }

    private void populateWeatherLayout(WeatherDay today) {


        weather_location.setText(today.getCity() + ", " + today.getCountry());
        weather_status.setText(today.getWeather_state_name());
        weather_temp.setText(today.getThe_temp().intValue() + " ºC");
        min_temp.setText(today.getMin_temp().intValue() + " ºC");
        Double Max = Math.ceil(today.getMax_temp());
        max_temp.setText(Max.intValue() + " ºC");
    }

    private void changeWeatherLayout() {

        if (weather_bar.getVisibility() == View.VISIBLE) {
            weather_layout.setVisibility(View.VISIBLE);
            weather_bar.setVisibility(View.GONE);
        } else {
            weather_layout.setVisibility(View.GONE);
            weather_bar.setVisibility(View.VISIBLE);
        }
    }

    public void addImageWeather(String weather) {
        int imageUrl = R.drawable.sun;
        switch (weather) {
            case "sn":
                imageUrl = R.drawable.snow;
                break;
            case "sl":
                imageUrl = R.drawable.sleet;
                break;
            case "h":
                imageUrl = R.drawable.sleet;
                break;
            case "t":
                imageUrl = R.drawable.thunderstorm;
                break;
            case "hr":
                imageUrl = R.drawable.rain;
                break;
            case "lr":
                imageUrl = isDay ? R.drawable.drizzle_day : R.drawable.drizzle_night;
                break;
            case "s":
                imageUrl = isDay ? R.drawable.drizzle_day : R.drawable.drizzle_night;
                break;
            case "hc":
                imageUrl = R.drawable.clouds;
                break;
            case "lc":
                imageUrl = isDay ? R.drawable.lc_day : R.drawable.lc_night;
                break;
            case "c":
                imageUrl = isDay ? R.drawable.drizzle_day : R.drawable.drizzle_night;
                break;
        }
        loadImage(imageUrl);
    }

    private void loadImage(int imageUrl) {
        Picasso.with(getApplicationContext())
                .load(imageUrl)
                .resize(125, 125)
                .centerCrop()
                .into(weather_image);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                drawer_layout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerText(WeatherDay today){
        TextView header_title = findViewById(R.id.header_title);
        TextView header_weather = findViewById(R.id.header_weather);
        TextView header_temp = findViewById(R.id.header_temp);
        TextView search_localization = findViewById(R.id.search_localization);

        search_localization.setText(today.getCity());
        header_title.setText(today.getCity());
        header_weather.setText(today.getWeather_state_name());
        header_temp.setText(today.getThe_temp().intValue() + " ºC");
    }

    public void navLocalizationChange(View view){
        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void navNotificationsChange(View view){
        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void navCityChange(View view){
        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void navRefresh(View view){
        drawer_layout.closeDrawer(Gravity.START);
        changeWeatherLayout();
        checkLocationPermission();
    }

}
