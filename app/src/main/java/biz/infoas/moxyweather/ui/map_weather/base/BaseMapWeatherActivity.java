package biz.infoas.moxyweather.ui.map_weather.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import biz.infoas.moxyweather.R;
import biz.infoas.moxyweather.ui.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by devel on 26.05.2017.
 */

public abstract class BaseMapWeatherActivity extends BaseActivity implements OnMapReadyCallback {

    @BindView(R.id.map_view_new)
    public MapView mapView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_weather);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mapView.onCreate(savedInstanceState);
        if (mapView != null) {
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
