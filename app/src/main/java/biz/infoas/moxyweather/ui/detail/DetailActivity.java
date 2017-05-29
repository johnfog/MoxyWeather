package biz.infoas.moxyweather.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;

import biz.infoas.moxyweather.R;
import biz.infoas.moxyweather.domain.models.WeatherFormated;
import biz.infoas.moxyweather.util.Const;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends MvpAppCompatActivity {

    @SuppressWarnings("unchecked")
    private WeatherFormated weatherFormated;
    private String city;

    @BindView(R.id.tv_detail_temp1)
    TextView tvTemp1;
    @BindView(R.id.tv_detail_temp2)
    TextView tvTemp2;
    @BindView(R.id.tv_detail_wind_speed)
    TextView tvWindSpeed;
    @BindView(R.id.tv_detail_direction_wind)
    TextView tvDirectionWind;
    @BindView(R.id.tv_detail_pressure)
    TextView tvPressure;
    @BindView(R.id.tv_detail_humidity)
    TextView tvHumidity;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_weather)
    ImageView imgWeather;
    @BindView(R.id.tv_detail_data)
    TextView tvData;
    @BindView(R.id.tv_detail_type_weather)
    TextView tvTypeWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent intentWeather = getIntent();
        Bundle bundle = intentWeather.getExtras();
        if (bundle != null) {
            weatherFormated = (WeatherFormated) bundle.getSerializable(Const.INTENT_WEATHER_FORMATED);
            city = bundle.getString(Const.INTENT_WEATHER_CITY);
            setupTextViews();
        }

    }

    private void setupTextViews() {
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle("Место: " +city);
            }
        });
        tvData.setText(weatherFormated.getDay());
        tvTypeWeather.setText(weatherFormated.getTypeWeather());
        imgWeather.setImageResource(weatherFormated.getImage());
        tvTemp1.setText("Днём: " +weatherFormated.getTemperatureDay() + "C");
        tvTemp2.setText("Ночью: " +weatherFormated.getTemperatureNight() + "C");
        tvWindSpeed.setText(weatherFormated.getWindSpeed()+" м/с");
        tvDirectionWind.setText(weatherFormated.getWindDirection());
        tvPressure.setText(weatherFormated.getPressure()+ " мм рт.ст.");
        tvHumidity.setText(weatherFormated.getHumidity()+"%");
    }
}
