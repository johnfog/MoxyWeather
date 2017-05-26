package biz.infoas.moxyweather.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

import biz.infoas.moxyweather.domain.util.Const;
import rx.functions.Action1;

/**
 * Created by devel on 26.05.2017.
 */

public abstract class BaseActivity extends MvpAppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        getPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Const.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        enabledGPS();
                        break;
                    case RESULT_CANCELED:
                        disabledGPS();
                        break;
                    default:
                        break;
                }
                break;
        }
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

    protected abstract void enabledGPS();

    protected abstract void disabledGPS();
}
