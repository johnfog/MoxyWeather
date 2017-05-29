package biz.infoas.moxyweather.interactor;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.app.api.WeatherAPI;
import biz.infoas.moxyweather.domain.db.dao.WeatherDAO;
import biz.infoas.moxyweather.interactor.transformer.AsyncTransformer;
import biz.infoas.moxyweather.util.TimeUtils;
import biz.infoas.moxyweather.domain.models.Weather;
import biz.infoas.moxyweather.domain.models.WeatherFormated;
import biz.infoas.moxyweather.domain.models.WeatherWithCityName;
import biz.infoas.moxyweather.util.Const;
import biz.infoas.moxyweather.interactor.observable.LocationObservable;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by devel on 16.05.2017.
 */

public class WeatherInteractror extends LocationObservable {

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

                        return weatherToFormat(weather);
                    }
                }).doOnNext(weatherFormated -> {
                    try {
                        weatherDAO.addWeather(weatherFormated.weatherFormatedList);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }).compose(new AsyncTransformer<>());
    }

    public Observable<List<WeatherFormated>> getWeatherDB() {
        return weatherDAO.getAllWeather();
    }

    private Observable<WeatherWithCityName> weatherToFormat(Weather weather) {
        return Observable.zip(cityObserver(weather), weatherFormatedObservable(weather), (s, weatherFormateds) -> {
            WeatherWithCityName newWeather = new WeatherWithCityName();
            newWeather.weatherFormatedList = weatherFormateds;
            newWeather.cityName = s;
            return newWeather;
        });
    }

    private Observable<String> cityObserver(Weather weather) {
        return Observable.just(weather).flatMap(new Func1<Weather, Observable<String>>() {
            @Override
            public Observable<String> call(Weather weather) {
                return Observable.just(weather.city.name);
            }
        });
    }


    // Метод заполняет myWeather
    private Observable<List<WeatherFormated>> weatherFormatedObservable(Weather weather) {
        return Observable.just(weather
        ).flatMap(weatherToList -> Observable.from(weatherToList.list))
                .flatMap(listWeather -> {
                        Date date = new Date();
                        date.setTime((long) listWeather.dt * 1000);
                        String drawableName = "weather" + listWeather.weather.get(0).icon;
                        //получаем из имени ресурса идентификатор картинки
                        int weatherIcon = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMMM", Locale.getDefault());
                        String weatherDay = String.valueOf(listWeather.temp.morn.intValue()) + "°";
                        String weatherNight = String.valueOf(listWeather.temp.night.intValue()) + "°";
                        WeatherFormated weatherFormated = new WeatherFormated();
                        weatherFormated.setImage(weatherIcon);
                        weatherFormated.setDay(dateFormat.format(date));
                        weatherFormated.setTypeWeather(String.valueOf(listWeather.weather.get(0).description));
                        weatherFormated.setTemperatureDay(weatherDay);
                        weatherFormated.setTemperatureNight(weatherNight);
                        weatherFormated.setHumidity(String.valueOf(listWeather.humidity));
                        weatherFormated.setPressure(String.valueOf(listWeather.pressure));
                        weatherFormated.setWindSpeed(String.valueOf(listWeather.speed));
                        weatherFormated.setDirection(String.valueOf(listWeather.deg));
                        return Observable.just(weatherFormated);
                }).toList();
    }

    public Observable<List<WeatherFormated>> isNeedUpdateWeatherFromServer() {
        return Observable.zip(Observable.just(TimeUtils.isNecessaryUpdateWeather(TimeUtils.getDatetimeNow(), sharedPreferences.getString(Const.SHARED_PREFERENCE_TIME_UPDATE, ""))),
                weatherDAO.getAllWeather(), (aBoolean, weatherFormateds) -> {
                    List<WeatherFormated> weather = new ArrayList<>();
                    if (aBoolean || weatherFormateds.size() == 0) {
                        return weather;
                    } else {
                        return weatherFormateds;
                    }
                });
    }
}
