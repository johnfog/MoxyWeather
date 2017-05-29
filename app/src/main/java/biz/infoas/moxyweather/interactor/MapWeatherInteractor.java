package biz.infoas.moxyweather.interactor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdoward.rxgooglemap.MapObservableProvider;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.interactor.observable.LocationObservable;
import biz.infoas.moxyweather.util.Const;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by devel on 26.05.2017.
 */

public class MapWeatherInteractor extends LocationObservable {

    @Inject
    SharedPreferences sharedPreferences;

    public MapWeatherInteractor() {
        App.getAppComponent().inject(this);
    }
    public Observable<LatLng> setOnMapClickInteractor(MapView mapView) {
        return new MapObservableProvider(mapView).getMapClickObservable();
    }

    public Observable<LatLng> getUserLocationLatLng(Activity activity) {
       return getUserLocation(activity).flatMap(location ->
               Observable.just(new LatLng(location.getLatitude(),location.getLongitude())));
    }

    public Observable<Boolean> setWeatherWithCoordinates(LatLng latLng) {
        return Observable.just(latLng).flatMap(loc -> {
            try {
                sharedPreferences.edit().putString(Const.SHARED_PREFERENCE_LNG, String.valueOf(loc.latitude)).apply();
                sharedPreferences.edit().putString(Const.SHARED_PREFERENCE_LON, String.valueOf(loc.longitude)).apply();
                return Observable.just(true);
            } catch (Exception e) {
                return Observable.error(new Throwable("Ошибка сохранения координат"));
            }
        });
    }
}
