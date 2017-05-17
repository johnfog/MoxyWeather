package biz.infoas.moxyweather.ui.activity.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import biz.infoas.moxyweather.R;
import biz.infoas.moxyweather.domain.WeatherFormated;

/**
 * Created by devel on 17.05.2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {

    private List<WeatherFormated> weatherList = new ArrayList<>();

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_weather, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        WeatherFormated weatherFormated = weatherList.get(position);
        holder.imageTypeWeather.setImageResource(weatherFormated.getImage());
        holder.textViewWeekday.setText(weatherFormated.getDay());
        holder.textViewTypeWeather.setText(weatherFormated.getTypeWeather());
        holder.textViewTemperature1.setText(weatherFormated.getTemperatureDay());
        holder.textViewTemperature2.setText(weatherFormated.getTemperatureNight());
        holder.textViewHumidity.setText(weatherFormated.getHumidity());
        holder.textViewtPressure.setText(weatherFormated.getPressure());
        holder.textViewWindSpeed.setText(weatherFormated.getWindSpeed());
        holder.textViewWindDirection.setText(weatherFormated.getWindDirection());
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public void updateWeatherList(List<WeatherFormated> listWeather) {
        weatherList.clear();
        weatherList.addAll(listWeather);
        this.notifyDataSetChanged();
    }
}
