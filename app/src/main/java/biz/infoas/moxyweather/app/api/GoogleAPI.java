package biz.infoas.moxyweather.app.api;

import biz.infoas.moxyweather.domain.models.city.City;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by devel on 22.05.2017.
 */

public interface GoogleAPI {
    @GET("maps/api/place/autocomplete/json")
    Observable<City> getCity(@Query("input") String inputStr,
                             @Query("types") String types,
                             @Query("key") String key);
}
