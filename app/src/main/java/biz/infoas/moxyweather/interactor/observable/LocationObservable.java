package biz.infoas.moxyweather.interactor.observable;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.concurrent.TimeUnit;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.util.Const;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by devel on 26.05.2017.
 */

public class LocationObservable {

    public Observable<Location> getUserLocation(final Activity activity) {
        final LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        final ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(App.appComponent.getContext());
        return locationProvider.checkLocationSettings(
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(request)
                        .setAlwaysShow(true)  //Refrence: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                        .build()
        )
                .doOnNext(locationSettingsResult -> {
                    Status status = locationSettingsResult.getStatus();
                    if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            status.startResolutionForResult(activity, Const.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException th) {
                            Log.e("MainActivity", "Error opening settings activity.", th);
                        }
                    }
                }).flatMap(locationSettingsResult -> {
                        return locationProvider.getUpdatedLocation(request)
                                .timeout(Const.LOCATION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS, Observable.just(null), AndroidSchedulers.mainThread())
                                .first()
                                .observeOn(AndroidSchedulers.mainThread());
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }
}
