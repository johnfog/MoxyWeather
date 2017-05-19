package biz.infoas.moxyweather.ui.activity.weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.domain.util.models.WeatherFormated;
import biz.infoas.moxyweather.domain.util.models.WeatherWithCityName;
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
    @Inject
    SharedPreferences sharedPreferences;

    private Location lastLocationUser;
    private boolean isDownloadWeather = false; // Переменная определяет, загружена ли погода или нет (не важно из БД или из сети)
    private boolean isProcessDownload = false; // Если переменная true, значит идёт загрузка погоды из сети.

    public WeatherPresenter() {
        App.getAppComponent().inject(this);
    }

    public void getWeather(Location locationUser) {
        if (isProcessDownload) {
            return; // Идёт загрузка из сети, следовательно не надо делать повторный запрос, выходим из метода.
        }
        isProcessDownload = true;
        isDownloadWeather = false;
        getViewState().showProgress();
        weatherInteractror.getWeather(locationUser.getLatitude(), locationUser.getLongitude()).subscribe(new Subscriber<WeatherWithCityName>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getViewState().hideProgress();
                getViewState().showError(e.getMessage());
            }

            @Override
            public void onNext(WeatherWithCityName weatherWithCityName) {
                isProcessDownload = false;
                isDownloadWeather = true;
                getViewState().hideProgress();
                getViewState().showWeather(weatherWithCityName.weatherFormatedList, weatherWithCityName.cityName);
            }
        });
    }

    public void openDetailActivity(Activity activity, WeatherFormated weatherFormated, String city, int position) {
        Intent intentDetail = new Intent(activity, DetailActivity.class);
        intentDetail.putExtra(Const.INTENT_WEATHER_FORMATED, weatherFormated);
        intentDetail.putExtra(Const.INTENT_WEATHER_CITY, city);
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

    public void isNeedUpdateWeather(final Activity activity) {
        if (isDownloadWeather) {
            return; // Если у нас уже загружена погода, то повторно загружать не надо, выходим из метода
        }
        weatherInteractror.isNeedUpdateWeatherFromServer().subscribe(new Subscriber<List<WeatherFormated>>() {
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
                if (weatherFormateds.size() == 0) {
                    locationUser(activity);
                } else {
                    isDownloadWeather = true;
                    getViewState().showWeather(weatherFormateds, sharedPreferences.getString(Const.SHARED_PREFERENCE_CITY, ""));
                }
            }
        });
    }
}
