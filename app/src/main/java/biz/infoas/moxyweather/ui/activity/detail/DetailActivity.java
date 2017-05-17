package biz.infoas.moxyweather.ui.activity.detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import biz.infoas.moxyweather.R;
import biz.infoas.moxyweather.domain.WeatherFormated;
import biz.infoas.moxyweather.domain.util.Const;

public class DetailActivity extends AppCompatActivity {

    @SuppressWarnings("unchecked")
    private WeatherFormated listPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intentWeather = getIntent();
        Bundle bundle = intentWeather.getExtras();
        if (bundle != null) {
            listPoints = (WeatherFormated) bundle.getSerializable(Const.INTENT_WEATHER_FORMATED);
        }
    }
}
