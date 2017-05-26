package biz.infoas.moxyweather.ui.activity.map_weather;

import android.app.Activity;
import android.location.Location;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.interactor.MapWeatherInteractor;
import rx.functions.Action1;

/**
 * Created by devel on 26.05.2017.
 */

@InjectViewState
public class MapWeatherPresenter extends MvpPresenter<MapWeatherView> {

     @Inject
    MapWeatherInteractor interactor;

    public MapWeatherPresenter() {
        App.getAppComponent().inject(this);
    }

    public void setOnMapClick(MapView mapView) {
        interactor.setOnMapClickInteractor(mapView).subscribe(new Action1<LatLng>() {
            @Override
            public void call(LatLng latLng) {
                getViewState().showLatLngClickMap(latLng);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                getViewState().showErrorLatLngClickMap(throwable.getMessage());
            }
        });
    }

    public void getLocationUser(Activity activity) {
        interactor.getUserLocationLatLng(activity).subscribe(new Action1<LatLng>() {
            @Override
            public void call(LatLng latLng) {
                getViewState().showLatLngUserLocation(latLng);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
               getViewState().showErrorLatLngUserLocation(throwable.getMessage());
            }
        });
    }
}
