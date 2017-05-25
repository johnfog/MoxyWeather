package biz.infoas.moxyweather.interactor;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.app.api.WeatherAPI;
import biz.infoas.moxyweather.domain.db.dao.WeatherDAO;
import biz.infoas.moxyweather.domain.util.TimeUtils;
import biz.infoas.moxyweather.domain.models.Weather;
import biz.infoas.moxyweather.domain.models.WeatherFormated;
import biz.infoas.moxyweather.domain.models.WeatherWithCityName;
import biz.infoas.moxyweather.domain.util.Const;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by devel on 16.05.2017.
 */

public class WeatherInteractror {

    @Inject
    WeatherAPI apiWeather;
    @Inject
    Context context;
    @Inject
    WeatherDAO weatherDAO;
    @Inject
    SharedPreferences sharedPreferences;

    public WeatherInteractror() {
        App.getAppComponent().inject(this);
    }

    public Observable<WeatherWithCityName> getWeather(Double lat, Double lon) {
        return apiWeather.getWeatherFromServer(lat, lon, Const.CNT, Const.UTILS, Const.LANG, Const.WEATHER_API)
                .flatMap(new Func1<Weather, Observable<WeatherWithCityName>>() {
                    @Override
                    public Observable<WeatherWithCityName> call(Weather weather) {

                        return Observable.just(weatherToFormat(weather));
                    }
                }).doOnNext(new Action1<WeatherWithCityName>() {
                    @Override
                    public void call(WeatherWithCityName weatherFormated) {
                        try {
                            weatherDAO.addWeather(weatherFormated.weatherFormatedList);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<WeatherFormated>> getWeatherDB() {
        return weatherDAO.getAllWeather();
    }

    // Метод заполняет myWeather
    private WeatherWithCityName weatherToFormat(Weather weather) {
        String weatherDay;
        String weatherNight;
        Date date;
        WeatherWithCityName newWeather = new WeatherWithCityName();
        List<WeatherFormated> weatherFormatedList = new ArrayList<>();
        for (int i = 0; i < weather.list.size(); i++) {
            date = new Date();
            date.setTime((long) weather.list.get(i).dt * 1000);
            String drawableName = "weather" + weather.list.get(i).weather.get(0).icon;
            //получаем из имени ресурса идентификатор картинки
            int weatherIcon = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMMM", Locale.getDefault());
            weatherDay = String.valueOf(weather.list.get(i).temp.morn.intValue()) + "°";
            weatherNight = String.valueOf(weather.list.get(i).temp.night.intValue()) + "°";

            WeatherFormated weatherFormated = new WeatherFormated();
            weatherFormated.setImage(weatherIcon);
            weatherFormated.setDay(dateFormat.format(date));
            weatherFormated.setTypeWeather(String.valueOf(weather.list.get(i).weather.get(0).description));
            weatherFormated.setTemperatureDay(weatherDay);
            weatherFormated.setTemperatureNight(weatherNight);
            weatherFormated.setHumidity(String.valueOf(weather.list.get(i).humidity));
            weatherFormated.setPressure(String.valueOf(weather.list.get(i).pressure));
            weatherFormated.setWindSpeed(String.valueOf(weather.list.get(i).speed));
            weatherFormated.setDirection(String.valueOf(weather.list.get(i).deg));
            weatherFormatedList.add(weatherFormated);
        }
        newWeather.weatherFormatedList = weatherFormatedList;
        newWeather.cityName = weather.city.name;
        return newWeather;
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

    public Observable<List<WeatherFormated>> isNeedUpdateWeatherFromServer() {
        return Observable.zip(Observable.just(TimeUtils.isNecessaryUpdateWeather(TimeUtils.getDatetimeNow(), sharedPreferences.getString(Const.SHARED_PREFERENCE_TIME_UPDATE, ""))),
                weatherDAO.getAllWeather(), new Func2<Boolean, List<WeatherFormated>, List<WeatherFormated>>() {
                    @Override
                    public List<WeatherFormated> call(Boolean aBoolean, List<WeatherFormated> weatherFormateds) {
                        List<WeatherFormated> weather = new ArrayList<>();
                        if (aBoolean || weatherFormateds.size() == 0) {
                            return weather;
                        } else {
                            return weatherFormateds;
                        }
                    }
                });
    }
}
