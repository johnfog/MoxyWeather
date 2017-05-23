package biz.infoas.moxyweather.ui.activity.search_weather;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import biz.infoas.moxyweather.R;
import biz.infoas.moxyweather.ui.activity.search_weather.adapter.SearchWeatherAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchWeatherActivity extends MvpAppCompatActivity implements SearchWeatherView{

    @InjectPresenter
    SearchWeatherPresenter presenter;
    @BindView(R.id.recycler_search_weather)
    RecyclerView recyclerSearchWeather;
    @BindView(R.id.edt_name_search_city)
    EditText edtNameSearchCity;
    @BindView(R.id.progress_search_weather)
    ProgressBar progressSearchWeather;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private SearchWeatherAdapter searchWeatherAdapter;

    private void initRecycler() {
        recyclerSearchWeather.setLayoutManager(new LinearLayoutManager(this));
        recyclerSearchWeather.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        searchWeatherAdapter = new SearchWeatherAdapter(new SearchWeatherAdapter.OnClickCityNameListener() {
            @Override
            public void OnClickCity(String nameCity) {
                Toast.makeText(SearchWeatherActivity.this, nameCity, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerSearchWeather.setAdapter(searchWeatherAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_weather);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        presenter.textChangeListener(edtNameSearchCity);
        initRecycler();
    }

    @Override
    public void showResult(List<String> strings) {
        searchWeatherAdapter.updateSearchList(strings);

    }

    @Override
    public void showResultTextChange(String textChange) {
        presenter.getWeatherList(textChange);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressSearchWeather.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressSearchWeather.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
