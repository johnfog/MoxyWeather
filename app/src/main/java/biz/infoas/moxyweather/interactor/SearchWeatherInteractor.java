package biz.infoas.moxyweather.interactor;

import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.app.api.WeatherAPI;
import biz.infoas.moxyweather.domain.models.city.City;
import biz.infoas.moxyweather.domain.models.city.Prediction;
import biz.infoas.moxyweather.domain.util.Const;
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
       return weatherAPI.getCity(nameChars, "(cities)", Const.GOOGLE_KEY).flatMap(new Func1<City, Observable<Prediction>>() {
            @Override
            public Observable<Prediction> call(City city) {
                return Observable.from(city.predictions);
            }
        }).flatMap(new Func1<Prediction, Observable<String>>() {
                   @Override
                   public Observable<String> call(Prediction prediction) {
                       return Observable.just(prediction.description);
                   }
               }).toList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
