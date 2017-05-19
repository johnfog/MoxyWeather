package biz.infoas.moxyweather.domain.util.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import biz.infoas.moxyweather.domain.db.dao.WeatherDAO;

/**
 * Created by devel on 17.05.2017.
 */

@DatabaseTable(tableName = WeatherFormated.TABLE_NAME_WEATHER, daoClass = WeatherDAO.class)
public class WeatherFormated implements Serializable {
    public static final String TABLE_NAME_WEATHER = "weather_table";

    public static final String FIELD_NAME_ID = "id";
    public static final String IMAGE = "image";
    public static final String DAY = "day";
    public static final String TYPE_WEATHER = "type_weather";
    public static final String TEMPERATURE_DAY = "temperature_day";
    public static final String TEMPERATURE_NIGHT = "temperature_night";
    public static final String HUMIDITY = "humidity";
    public static final String PRESSURE = "pressure";
    public static final String WIND_SPEED = "wind_speed";
    public static final String WIND_DIRECTION = "wind_direction";




    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = IMAGE)
    private int image;

    @DatabaseField(columnName = DAY)
    private String day;

    @DatabaseField(columnName = TYPE_WEATHER)
    private String typeWeather;

    @DatabaseField(columnName = TEMPERATURE_DAY)
    private String temperatureDay;

    @DatabaseField(columnName = TEMPERATURE_NIGHT)
    private String temperatureNight;

    @DatabaseField(columnName = HUMIDITY)
    private String humidity;

    @DatabaseField(columnName = PRESSURE)
    private String pressure;

    @DatabaseField(columnName = WIND_SPEED)
    private String windSpeed;

    @DatabaseField(columnName = WIND_DIRECTION)
    private String direction;


    public WeatherFormated() {

    }

    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getDay() {
        return day;
    }

    public String getTypeWeather() {
        return typeWeather;
    }

    public String getTemperatureDay() {
        return temperatureDay;
    }

    public String getTemperatureNight() {
        return temperatureNight;
    }

    public String getHumidity() {
        return "Влажность: " + humidity;
    }

    public String getPressure() {
        return "Давление: " + pressure;
    }

    public String getWindSpeed() {
        return "Скорость ветра: " + windSpeed;
    }

    public String getWindDirection() {
        return "Направление ветра: " + direction+"°";
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTypeWeather(String typeWeather) {
        this.typeWeather = typeWeather;
    }

    public void setTemperatureDay(String temperatureDay) {
        this.temperatureDay = temperatureDay;
    }

    public void setTemperatureNight(String temperatureNight) {
        this.temperatureNight = temperatureNight;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
