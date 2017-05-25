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
import biz.infoas.moxyweather.domain.models.WeatherFormated;
import biz.infoas.moxyweather.domain.models.WeatherWithCityName;
import biz.infoas.moxyweather.domain.util.Const;
import biz.infoas.moxyweather.interactor.WeatherInteractror;
import biz.infoas.moxyweather.ui.activity.detail.DetailActivity;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

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

    private boolean isDownloadWeather = false; // Переменная определяет, загружена ли погода или нет (не важно из БД или из сети)
    private boolean isProcessDownload = false; // Если переменная true, значит идёт загрузка погоды из сети.
    private boolean isPermissionLocationGranted = false;
    private String lat = "lat";
    private String lon = "lon";


    public WeatherPresenter() {
        App.getAppComponent().inject(this);
        updateLocSharedPref();
    }

    private void updateLocSharedPref() {
        lat = sharedPreferences.getString(Const.SHARED_PREFERENCE_LNG, "");
        lon = sharedPreferences.getString(Const.SHARED_PREFERENCE_LON, "");
    }

    public void getWeather() {
        updateLocSharedPref();
        if (isProcessDownload) {
            return; // Идёт загрузка из сети, следовательно не надо делать повторный запрос, выходим из метода.
        }
        isProcessDownload = true;
        isDownloadWeather = false;
        getViewState().showProgress();

        weatherInteractror.getWeather(Double.parseDouble(lat), Double.parseDouble(lon)).subscribe(new Subscriber<WeatherWithCityName>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                isProcessDownload = false;
                getViewState().hideProgress();
                getViewState().showError(e.getMessage());
            }

            @Override
            public void onNext(WeatherWithCityName weatherWithCityName) {
                isProcessDownload = false;
                isDownloadWeather = true;
                getViewState().hideProgress();
                getViewState().showWeather(weatherWithCityName.weatherFormatedList, weatherWithCityName.cityName);
                sharedPreferences.edit().putString(Const.SHARED_PREFERENCE_CITY, weatherWithCityName.cityName).apply();
            }
        });
    }

    public void openDetailActivity(Activity activity, WeatherFormated weatherFormated, String city, int position) {
        Intent intentDetail = new Intent(activity, DetailActivity.class);
        intentDetail.putExtra(Const.INTENT_WEATHER_FORMATED, weatherFormated);
        intentDetail.putExtra(Const.INTENT_WEATHER_CITY, city);
        activity.startActivity(intentDetail);
    }


    public void updateWeather(WeatherActivity activity) {
        if (isPermissionLocationGranted) {
            if (lat.equals("") || lon.equals("")) {
                getLocation(activity);
            } else {
                getWeather();
            }
        } else {
            activity.getPermission();
        }
    }

    public void updateLocation(WeatherActivity activity) {
        if (isPermissionLocationGranted) {
            getLocation(activity);
        } else {
            activity.getPermission();
        }
    }

    private void getLocation(Activity activity) {
        getViewState().showProgress();
        weatherInteractror.getUserLocation(activity).subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                sharedPreferences.edit().putString(Const.SHARED_PREFERENCE_LNG, lat).apply();
                sharedPreferences.edit().putString(Const.SHARED_PREFERENCE_LON, lon).apply();
                getViewState().hideProgress();
                getWeather();
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
        isPermissionLocationGranted = true;
        // Проверяем, если координаты изменились, то обновляем погоду
        if (!lat.equals(sharedPreferences.getString(Const.SHARED_PREFERENCE_LNG, "")) || !lon.equals(sharedPreferences.getString(Const.SHARED_PREFERENCE_LON, ""))) {
            getWeather();
            return;
        }
        if (isDownloadWeather) {
            return; // Если у нас уже загружена погода, то повторно загружать не надо, выходим из метода
        }
        if (isProcessDownload) {
            return; // В процессе загрузки, значит выходим с метода
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
                    getLocation(activity);
                } else {
                    isDownloadWeather = true;
                    getViewState().showWeather(weatherFormateds, sharedPreferences.getString(Const.SHARED_PREFERENCE_CITY, ""));
                }
            }
        });
    }

}
