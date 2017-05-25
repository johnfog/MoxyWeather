package biz.infoas.moxyweather.ui.activity.weather.base;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

import biz.infoas.moxyweather.app.App;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by devel on 18.05.2017.
 */

public abstract class BaseWeatherActivity extends MvpAppCompatActivity {

    public BaseWeatherActivity() {
        App.getAppComponent().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPermission();
    }

    public void getPermission() {
        new RxPermissions(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            onLocationPermissionGranted();
                        } else {
                            onLocationPermissionDenied();
                        }
                    }
                });
    }

    protected abstract void onLocationPermissionGranted();
    protected abstract void onLocationPermissionDenied();
}