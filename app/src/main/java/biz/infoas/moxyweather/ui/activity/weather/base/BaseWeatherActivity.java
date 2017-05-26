package biz.infoas.moxyweather.ui.activity.weather.base;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

import biz.infoas.moxyweather.R;
import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.domain.util.Const;
import biz.infoas.moxyweather.ui.activity.BaseActivity;
import biz.infoas.moxyweather.ui.activity.weather.WeatherActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

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