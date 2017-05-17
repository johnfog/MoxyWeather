package biz.infoas.moxyweather.interactor;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.app.api.WeatherAPI;
import biz.infoas.moxyweather.domain.Weather;
import biz.infoas.moxyweather.domain.WeatherFormated;
import biz.infoas.moxyweather.domain.util.Const;
import rx.Observable;
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

    public Observable<List<WeatherFormated>> getWeather(String lat, String lon) {
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
}
