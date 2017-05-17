package biz.infoas.moxyweather.ui.activity.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import javax.inject.Inject;

import biz.infoas.moxyweather.R;
import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.domain.Weather;
import biz.infoas.moxyweather.domain.WeatherFormated;
import biz.infoas.moxyweather.ui.activity.weather.adapter.WeatherAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends MvpAppCompatActivity implements WeatherView {

    @InjectPresenter
    WeatherPresenter presenter;

    @Inject
    WeatherAdapter weatherAdapter;

    @BindView(R.id.recycler_weather)
    RecyclerView recyclerWeather;
    @BindView(R.id.progress_weather)
    ProgressBar progressBar;

    public WeatherActivity() {
        App.getAppComponent().inject(this);
    }

    private void initRecycler() {
        recyclerWeather.setLayoutManager(new LinearLayoutManager(this));
        recyclerWeather.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // Добавляем разделитель между элементами
        recyclerWeather.setAdapter(weatherAdapter); // Применяем адаптер для recyclerViewWeather
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
      //  getMvpDelegate().onAttach();

        initRecycler();
       // presenter.getWeather("53.536639","49.393022");
    }

    @Override
    public void showWeatherList(List<WeatherFormated> listWeather) {
        weatherAdapter.updateWeatherList(listWeather);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgress() {
        recyclerWeather.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        recyclerWeather.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }


}
