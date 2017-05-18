package biz.infoas.moxyweather.ui.activity.weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.domain.Weather;
import biz.infoas.moxyweather.domain.WeatherFormated;
import biz.infoas.moxyweather.domain.util.Const;
import biz.infoas.moxyweather.interactor.WeatherInteractror;
import biz.infoas.moxyweather.ui.activity.detail.DetailActivity;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by devel on 16.05.2017.
 */

@InjectViewState
public class WeatherPresenter extends MvpPresenter<WeatherView> {

    @Inject
    WeatherInteractror weatherInteractror;
    @Inject
    Context context;

    private Location lastLocationUser;

    public WeatherPresenter() {
        App.getAppComponent().inject(this);
    }

    public void getWeather(Location locationUser) {
        getViewState().showProgress();
        weatherInteractror.getWeather(locationUser.getLatitude(),locationUser.getLongitude()).subscribe(new Subscriber<List<WeatherFormated>>() {
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

    public void openDetailActivity(Activity activity, WeatherFormated weatherFormated, int position) {
        Intent intentDetail = new Intent(activity, DetailActivity.class);
        intentDetail.putExtra(Const.INTENT_WEATHER_FORMATED, weatherFormated);
        activity.startActivity(intentDetail);
    }

    public void locationUser(Activity activity) {
        if (lastLocationUser != null) {
            return; // Если есть координаты, то повторно их не запрашиваем
        }
        getLocation(activity);
    }

    public void updateLocation(Activity activity) {
        getLocation(activity);
    }

    private void getLocation(Activity activity) {
        getViewState().showProgress();
        weatherInteractror.getUserLocation(activity).subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                lastLocationUser = location;
                getViewState().hideProgress();
                getViewState().showLocationUser(location);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                getViewState().hideProgress();
                getViewState().showErrorLocationUser("Не удаётся получить координаты");
            }
        });
    }
}
