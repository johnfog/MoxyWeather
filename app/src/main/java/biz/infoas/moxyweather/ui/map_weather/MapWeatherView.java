package biz.infoas.moxyweather.ui.map_weather;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by devel on 26.05.2017.
 */
@StateStrategyType(SingleStateStrategy.class)
public interface MapWeatherView extends MvpView {


    void showLatLngClickMap(LatLng latLng);
    void showErrorLatLngClickMap(String error);

    void showLatLngUserLocation(LatLng latLng);
    void showErrorLatLngUserLocation(String error);

    void showErrorSetWeather(String error);
}
