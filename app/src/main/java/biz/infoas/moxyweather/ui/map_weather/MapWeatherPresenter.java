package biz.infoas.moxyweather.ui.map_weather;

import android.app.Activity;

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
        interactor.setOnMapClickInteractor(mapView).subscribe(latLng -> {
            getViewState().showLatLngClickMap(latLng);
        }, throwable -> {
            getViewState().showErrorLatLngClickMap(throwable.getMessage());
        });
    }

    public void getLocationUser(Activity activity) {
        interactor.getUserLocationLatLng(activity).subscribe(latLng -> {
            getViewState().showLatLngUserLocation(latLng);
        }, throwable -> {
           getViewState().showErrorLatLngUserLocation(throwable.getMessage());
        });
    }

    public void setWeather(LatLng latLng, Activity activity) {
        interactor.setWeatherWithCoordinates(latLng).subscribe(b -> {
            activity.finish();
        }, error -> getViewState().showErrorSetWeather(error.getMessage()));
    }
}
