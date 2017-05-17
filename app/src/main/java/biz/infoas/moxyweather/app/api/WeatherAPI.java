package biz.infoas.moxyweather.app.api;

import biz.infoas.moxyweather.domain.Weather;
import dagger.Provides;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by devel on 16.05.2017.
 */

public interface WeatherAPI {


    @GET("data/2.5/forecast/daily")
    Observable<Weather> getWeatherFromServer(@Query("lat") String lat,
                                             @Query("lon") String lon,
                                             @Query("cnt") String cnt,
                                             @Query("units") String units,
                                             @Query("lang") String lang,
                                             @Query("appid") String appid);
}