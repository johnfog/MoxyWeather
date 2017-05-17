package biz.infoas.moxyweather.ui.activity.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import biz.infoas.moxyweather.R;

/**
 * Created by devel on 17.05.2017.
 */

public class WeatherViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageTypeWeather;
    public TextView textViewWeekday;
    public TextView textViewTypeWeather;
    public TextView textViewTemperature1;
    public TextView textViewTemperature2;
    public TextView textViewHumidity;
    public TextView textViewtPressure;
    public TextView textViewWindSpeed;
    public TextView textViewWindDirection;

    public WeatherViewHolder(View itemView) {
        super(itemView);
        imageTypeWeather = (ImageView) itemView.findViewById(R.id.img_type_weather);
        textViewWeekday = (TextView) itemView.findViewById(R.id.tv_weekday);
        textViewTypeWeather = (TextView) itemView.findViewById(R.id.tv_type_weather);
        textViewTemperature1 = (TextView) itemView.findViewById(R.id.tv_temperature1);
        textViewTemperature2 = (TextView) itemView.findViewById(R.id.tv_temperature2);
        textViewHumidity = (TextView) itemView.findViewById(R.id.tv_humidity);
        textViewtPressure = (TextView) itemView.findViewById(R.id.tv_pressure);
        textViewWindSpeed = (TextView) itemView.findViewById(R.id.tv_wind_speed);
        textViewWindDirection = (TextView) itemView.findViewById(R.id.tv_wind_direction);
    }
}
