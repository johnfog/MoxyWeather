package biz.infoas.moxyweather.interactor;


import android.content.SharedPreferences;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.app.api.WeatherAPI;
import biz.infoas.moxyweather.domain.models.city.City;
import biz.infoas.moxyweather.domain.models.city.Prediction;
import biz.infoas.moxyweather.domain.models.city_location.CityLocation;
import biz.infoas.moxyweather.domain.models.city_location.Location;
import biz.infoas.moxyweather.interactor.transformer.AsyncTransformer;
import biz.infoas.moxyweather.util.Const;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by devel on 22.05.2017.
 */

public class SearchWeatherInteractor {

    @Inject
    WeatherAPI weatherAPI;
    @Inject
    SharedPreferences sharedPreferences;

    public SearchWeatherInteractor() {
        App.getAppComponent().inject(this);
    }

    public Observable<String> observableTextChange(TextView searchTextView) {
        return RxTextView.textChanges(searchTextView).skip(1) // Пропускаем первый вызов
                .throttleLast(100, TimeUnit.MILLISECONDS)
                .debounce(300, TimeUnit.MILLISECONDS) // Задержка
                .onBackpressureLatest()
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<CharSequence, Observable<String>>() {
                    @Override
                    public Observable<String> call(CharSequence charSequence) {
                        return Observable.just(charSequence.toString());
                    }
                });
    }

    public Observable<List<String>> observableGetCites(String nameChars) {
       return weatherAPI.getCity(nameChars, "(cities)", Const.GOOGLE_KEY_AUTOCOMPLETE).flatMap(new Func1<City, Observable<Prediction>>() {
            @Override
            public Observable<Prediction> call(City city) {
                return Observable.from(city.predictions);
            }
        }).flatMap(new Func1<Prediction, Observable<String>>() {
                   @Override
                   public Observable<String> call(Prediction prediction) {
                       return Observable.just(prediction.description);
                   }
               }).toList().compose(new AsyncTransformer<>());
    }

    public Observable<Location> getLocationCityByName(String nameCity) {
        return weatherAPI.getLocationByCity(nameCity, Const.GOOGLE_KEY_GEOCODER)
                .flatMap((Func1<CityLocation, Observable<Location>>) cityLocation -> {
                    Observable observable;
                    if (cityLocation.getResults().size() != 1) {
                        observable = Observable.error(new Throwable("Не могу найти координаты"));
                    } else {
                        Location loc = cityLocation.getResults().get(0).getGeometry().getLocation();
                        sharedPreferences.edit().putString(Const.SHARED_PREFERENCE_LNG, loc.getLatString()).apply();
                        sharedPreferences.edit().putString(Const.SHARED_PREFERENCE_LON, loc.getLngString()).apply();
                        observable = Observable.just(loc);
                    }
                   return observable;
                }).compose(new AsyncTransformer<>());
    }
}
