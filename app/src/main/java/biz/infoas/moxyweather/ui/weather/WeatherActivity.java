package biz.infoas.moxyweather.ui.weather;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import biz.infoas.moxyweather.R;
import biz.infoas.moxyweather.domain.models.WeatherFormated;
import biz.infoas.moxyweather.ui.map_weather.MapWeatherActivity;
import biz.infoas.moxyweather.ui.search_weather.SearchWeatherActivity;
import biz.infoas.moxyweather.ui.weather.adapter.WeatherAdapter;
import biz.infoas.moxyweather.ui.weather.base.BaseWeatherActivity;
import butterknife.BindView;

public class WeatherActivity extends BaseWeatherActivity implements WeatherView {

    private final static String TAG = "WeatherActivity";

    @InjectPresenter
    WeatherPresenter presenter;

    @BindView(R.id.recycler_weather)
    RecyclerView recyclerWeather;
    @BindView(R.id.progress_weather)
    ProgressBar progressBar;
    @BindView(R.id.refresh_weather)
    SwipeRefreshLayout refreshLayout;

    private WeatherAdapter weatherAdapter;

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
                presenter.updateWeather(WeatherActivity.this);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecycler();
        initFloatinActionButton();
    }


    @Override
    public void showWeather(List<WeatherFormated> listWeather, final String city) {
        toolbar.post(() -> toolbar.setTitle("Место: " + city));
        weatherAdapter.updateWeatherList(listWeather, city);
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
        presenter.isNeedUpdateWeather(WeatherActivity.this);
    }

    @Override
    protected void onLocationPermissionDenied() {
        Toast.makeText(this, "Разрешения не предоставлены", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void enabledGPS() {
        presenter.updateWeather(WeatherActivity.this);
    }

    @Override
    protected void disabledGPS() {
        Toast.makeText(this, "Геолокация не включена", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                presenter.openActivity(WeatherActivity.this,SearchWeatherActivity.class);
                break;
            case R.id.action_location:
                // Тут апдейтим погоду и надо сохранить координаты в sharedPrefernce, которые
                presenter.updateLocation(WeatherActivity.this);
                break;
            case R.id.action_map:
                presenter.openActivity(WeatherActivity.this, MapWeatherActivity.class);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
