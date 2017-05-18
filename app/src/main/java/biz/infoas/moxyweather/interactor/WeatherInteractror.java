package biz.infoas.moxyweather.interactor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.app.api.WeatherAPI;
import biz.infoas.moxyweather.domain.Weather;
import biz.infoas.moxyweather.domain.WeatherFormated;
import biz.infoas.moxyweather.domain.util.Const;
import biz.infoas.moxyweather.ui.activity.weather.WeatherActivity;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by devel on 16.05.2017.
 */

public class WeatherInteractror {

    @Inject
    WeatherAPI apiWeather;
    @Inject
    Context context;

    public WeatherInteractror() {
        App.getAppComponent().inject(this);
    }

    public Observable<List<WeatherFormated>> getWeather(Double lat, Double lon) {
        return apiWeather.getWeatherFromServer(lat, lon, Const.CNT, Const.UTILS, Const.LANG, Const.WEATHER_API)
                .flatMap(new Func1<Weather, Observable<WeatherFormated>>() {
                    @Override
                    public Observable<WeatherFormated> call(Weather weather) {
                        return Observable.from(weatherToFormat(weather.list));
                    }
                }).doOnNext(new Action1<WeatherFormated>() {
                    @Override
                    public void call(WeatherFormated weatherFormated) {
                        //TODO тут сейвить в БД
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).toList();
    }

    // Метод заполняет myWeather
    private List<WeatherFormated> weatherToFormat(List<Weather.ListWeather> listWeather) {
        String weatherDay;
        String weatherNight;
        Date date;
        List<WeatherFormated> myWeatherList = new ArrayList<>();
        for (int i = 0; i < listWeather.size(); i++) {
            date = new Date();
            date.setTime((long) listWeather.get(i).dt * 1000);
            String drawableName = "weather" + listWeather.get(i).weather.get(0).icon;
            //получаем из имени ресурса идентификатор картинки
            int weatherIcon = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMMM", Locale.getDefault());
            weatherDay = String.valueOf(listWeather.get(i).temp.morn.intValue()) + "°";
            weatherNight = String.valueOf(listWeather.get(i).temp.night.intValue()) + "°";

            WeatherFormated weather = new WeatherFormated();
            weather.setImage(weatherIcon);
            weather.setDay(dateFormat.format(date));
            weather.setTypeWeather(String.valueOf(listWeather.get(i).weather.get(0).description));
            weather.setTemperatureDay(weatherDay);
            weather.setTemperatureNight(weatherNight);
            weather.setHumidity(String.valueOf(listWeather.get(i).humidity));
            weather.setPressure(String.valueOf(listWeather.get(i).pressure));
            weather.setWindSpeed(String.valueOf(listWeather.get(i).speed));
            weather.setDirection(String.valueOf(listWeather.get(i).deg));
            myWeatherList.add(weather);
        }
        return myWeatherList;
    }

    public Observable<Location> getUserLocation(final Activity activity) {
        final LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        final ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context);
       return locationProvider.checkLocationSettings(
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(request)
                        .setAlwaysShow(true)  //Refrence: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                        .build()
        )
                .doOnNext(new Action1<LocationSettingsResult>() {
                    @Override
                    public void call(LocationSettingsResult locationSettingsResult) {
                        Status status = locationSettingsResult.getStatus();
                        if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                            try {
                                status.startResolutionForResult(activity, Const.REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException th) {
                                Log.e("MainActivity", "Error opening settings activity.", th);
                            }
                        }
                    }
                })
                .flatMap(new Func1<LocationSettingsResult, Observable<Location>>() {
                    @Override
                    public Observable<Location> call(LocationSettingsResult locationSettingsResult) {
                        return locationProvider.getUpdatedLocation(request)
                                .timeout(Const.LOCATION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS, Observable.just((Location) null), AndroidSchedulers.mainThread())
                                .first()
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                });

    }
}
