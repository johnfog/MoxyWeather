package biz.infoas.moxyweather.ui.activity.weather;

import android.location.Location;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import biz.infoas.moxyweather.domain.models.WeatherFormated;

@StateStrategyType(SingleStateStrategy.class)
public interface WeatherView extends MvpView {

    void showWeather(List<WeatherFormated> listWeather, String city);


    void showLocationUser(Location locationUser);
    void showErrorLocationUser(String error);

    void showError(String error);

    void hideProgress();
    void showProgress();
}
