package biz.infoas.moxyweather.ui.activity.weather;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.domain.Weather;
import biz.infoas.moxyweather.domain.WeatherFormated;
import biz.infoas.moxyweather.domain.util.Const;
import biz.infoas.moxyweather.interactor.WeatherInteractror;
import rx.Subscriber;

/**
 * Created by devel on 16.05.2017.
 */

@InjectViewState
public class WeatherPresenter extends MvpPresenter<WeatherView> {

    @Inject
    WeatherInteractror weatherInteractror;
    @Inject
    Context context;

    public WeatherPresenter() {
        App.getAppComponent().inject(this);
        getWeather("53.536639","49.393022");
    }

    public void getWeather(String lat, String lon) {
        getViewState().showProgress();
        weatherInteractror.getWeather(lat,lon).subscribe(new Subscriber<List<WeatherFormated>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getViewState().hideProgress();
                getViewState().showError(e.getMessage());
            }

            @Override
            public void onNext(List<WeatherFormated> weatherFormateds) {
                getViewState().hideProgress();
                getViewState().showWeatherList(weatherFormateds);
            }
        });
    }
}
