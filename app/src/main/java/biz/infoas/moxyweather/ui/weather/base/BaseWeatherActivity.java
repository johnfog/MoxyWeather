package biz.infoas.moxyweather.ui.weather.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import biz.infoas.moxyweather.R;
import biz.infoas.moxyweather.ui.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by devel on 18.05.2017.
 */

public abstract class BaseWeatherActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }
}