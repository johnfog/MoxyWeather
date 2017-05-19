package biz.infoas.moxyweather.ui.activity.weather;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import biz.infoas.moxyweather.R;
import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.domain.util.models.WeatherFormated;
import biz.infoas.moxyweather.domain.util.Const;
import biz.infoas.moxyweather.ui.activity.weather.adapter.WeatherAdapter;
import biz.infoas.moxyweather.ui.activity.weather.base.BaseLocationActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends BaseLocationActivity implements WeatherView {

    private final static String TAG = "WeatherActivity";

    @InjectPresenter
    WeatherPresenter presenter;

    @BindView(R.id.recycler_weather)
    RecyclerView recyclerWeather;
    @BindView(R.id.progress_weather)
    ProgressBar progressBar;
    @BindView(R.id.refresh_weather)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private WeatherAdapter weatherAdapter;
    private boolean isPermissionLocationGranted = false;

    public WeatherActivity() {
        App.getAppComponent().inject(this);
    }

    private void initRecycler() {
        recyclerWeather.setLayoutManager(new LinearLayoutManager(this));
        recyclerWeather.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // Добавляем разделитель между элементами
        weatherAdapter = new WeatherAdapter(new WeatherAdapter.OnClickWeatherListener() {
            @Override
            public void onClickWeather(WeatherFormated weatherFormated, String city, int position) {
                presenter.openDetailActivity(WeatherActivity.this, weatherFormated, city, position);
            }
        });
        recyclerWeather.setAdapter(weatherAdapter); // Применяем адаптер для recyclerViewWeather
    }

    private void initFloatinActionButton() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isPermissionLocationGranted) {
                    presenter.updateLocation(WeatherActivity.this);
                } else {
                    getPermission();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initRecycler();
        initFloatinActionButton();
    }

    @Override
    public void showWeather(List<WeatherFormated> listWeather, final String city) {
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle("Место: " +city);
            }
        });
        weatherAdapter.updateWeatherList(listWeather, city);
    }

    @Override
    public void showLocationUser(Location locationUser) {
        presenter.getWeather(locationUser);
    }

    @Override
    public void showErrorLocationUser(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgress() {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        recyclerWeather.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        refreshLayout.setEnabled(false);
        recyclerWeather.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onLocationPermissionGranted() {
        isPermissionLocationGranted = true;
        presenter.isNeedUpdateWeather(WeatherActivity.this);
    }

    @Override
    protected void onLocationPermissionDenied() {
        isPermissionLocationGranted = false;
        hideProgress();
        Toast.makeText(this, "Разрешения не предоставлены", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Const.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        presenter.locationUser(WeatherActivity.this);
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, "Геолокация не включена", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

}
