package biz.infoas.moxyweather.interactor;

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdoward.rxgooglemap.MapObservableProvider;

import biz.infoas.moxyweather.interactor.observable.LocationObservable;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by devel on 26.05.2017.
 */

public class MapWeatherInteractor extends LocationObservable {


    public Observable<LatLng> setOnMapClickInteractor(MapView mapView) {
        return new MapObservableProvider(mapView).getMapClickObservable();
    }

    public Observable<LatLng> getUserLocationLatLng(Activity activity) {
       return getUserLocation(activity).flatMap(new Func1<Location, Observable<LatLng>>() {
            @Override
            public Observable<LatLng> call(Location location) {
                return Observable.just(new LatLng(location.getLatitude(),location.getLongitude()));
            }
        });
    }
}
