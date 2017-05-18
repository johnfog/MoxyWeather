package biz.infoas.moxyweather.ui.activity.detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import biz.infoas.moxyweather.R;
import biz.infoas.moxyweather.domain.WeatherFormated;
import biz.infoas.moxyweather.domain.WeatherWithCityName;
import biz.infoas.moxyweather.domain.util.Const;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends MvpAppCompatActivity {

    @SuppressWarnings("unchecked")
    private WeatherFormated weatherFormated;
    private String city;

    @BindView(R.id.tv_detail_city_name)
    TextView tvCityName;
    @BindView(R.id.tv_detail_temp)
    TextView tvTemp;
    @BindView(R.id.tv_detail_wind_speed)
    TextView tvWindSpeed;
    @BindView(R.id.tv_detail_direction_wind)
    TextView tvDirectionWind;
    @BindView(R.id.tv_detail_pressure)
    TextView tvPressure;
    @BindView(R.id.tv_detail_humidity)
    TextView tvHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intentWeather = getIntent();
        Bundle bundle = intentWeather.getExtras();
        if (bundle != null) {
            weatherFormated = (WeatherFormated) bundle.getSerializable(Const.INTENT_WEATHER_FORMATED);
            city = bundle.getString(Const.INTENT_WEATHER_CITY);
            setupTextViews();
        }

    }

    private void setupTextViews() {
        tvCityName.setText(city);
        tvTemp.setText(weatherFormated.getTemperatureDay());
        tvWindSpeed.setText(weatherFormated.getWindSpeed());
        tvDirectionWind.setText(weatherFormated.getWindDirection());
        tvPressure.setText(weatherFormated.getPressure());
        tvHumidity.setText(weatherFormated.getHumidity());
    }
}
