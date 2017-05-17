package biz.infoas.moxyweather.ui.activity.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import biz.infoas.moxyweather.R;
import butterknife.BindView;

/**
 * Created by devel on 17.05.2017.
 */

public class WeatherViewHolder extends RecyclerView.ViewHolder {

    LinearLayout layoutWeather;
    ImageView imageTypeWeather;
    TextView textViewWeekday;
    TextView textViewTypeWeather;
    TextView textViewTemperature1;
    TextView textViewTemperature2;
    TextView textViewHumidity;
    TextView textViewtPressure;
    TextView textViewWindSpeed;
    TextView textViewWindDirection;

    public WeatherViewHolder(View itemView) {
        super(itemView);
        layoutWeather = (LinearLayout) itemView.findViewById(R.id.layout_weather);
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
