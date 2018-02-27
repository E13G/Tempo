package com.weather.jpinto.tempo;

/**
 * Created by jcmor on 27/02/2018.
 **/

public class WeatherDay {

    private int woeid;
    private String city;
    private String country;
    private long sunrise;
    private long sunset;
    private Double lat;
    private Double lon;
    private String weather_state_name;
    private String weather_state_abbr;
    private String created;
    private String wind_direction_compass;
    private String applicable_date;
    private Double min_temp;
    private Double max_temp;
    private Double the_temp;
    private Double wind_speed;
    private Double wind_direction;
    private Double air_pressure;
    private int humidity;
    private Double visibility;
    private int predictability;

    public WeatherDay() {

    }

    public WeatherDay(int woeid, String city, Double lat, Double lon) {
        this.woeid = woeid;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
    }

    public WeatherDay(int woeid, String city, Double lat, Double lon, String weather_state_name, String created, Double min_temp, Double max_temp, Double the_temp) {
        this.woeid = woeid;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
        this.weather_state_name = weather_state_name;
        this.created = created;
        this.min_temp = min_temp;
        this.max_temp = max_temp;
        this.the_temp = the_temp;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public int getWoeid() {
        return woeid;
    }

    public void setWoeid(int woeid) {
        this.woeid = woeid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getWeather_state_name() {
        return weather_state_name;
    }

    public void setWeather_state_name(String weather_state_name) {
        this.weather_state_name = weather_state_name;
    }

    public String getWeather_state_abbr() {
        return weather_state_abbr;
    }

    public void setWeather_state_abbr(String weather_state_abbr) {
        this.weather_state_abbr = weather_state_abbr;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getWind_direction_compass() {
        return wind_direction_compass;
    }

    public void setWind_direction_compass(String wind_direction_compass) {
        this.wind_direction_compass = wind_direction_compass;
    }

    public String getApplicable_date() {
        return applicable_date;
    }

    public void setApplicable_date(String applicable_date) {
        this.applicable_date = applicable_date;
    }

    public Double getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(Double min_temp) {
        this.min_temp = min_temp;
    }

    public Double getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(Double max_temp) {
        this.max_temp = max_temp;
    }

    public Double getThe_temp() {
        return the_temp;
    }

    public void setThe_temp(Double the_temp) {
        this.the_temp = the_temp;
    }

    public Double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(Double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public Double getWind_direction() {
        return wind_direction;
    }

    public void setWind_direction(Double wind_direction) {
        this.wind_direction = wind_direction;
    }

    public Double getAir_pressure() {
        return air_pressure;
    }

    public void setAir_pressure(Double air_pressure) {
        this.air_pressure = air_pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Double getVisibility() {
        return visibility;
    }

    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    public int getPredictability() {
        return predictability;
    }

    public void setPredictability(int predictability) {
        this.predictability = predictability;
    }
}
