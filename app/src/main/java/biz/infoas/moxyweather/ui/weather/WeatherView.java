package biz.infoas.moxyweather.ui.weather;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import biz.infoas.moxyweather.domain.models.WeatherFormated;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface WeatherView extends MvpView {

    void showWeather(List<WeatherFormated> listWeather, String city);


    void showErrorLocationUser(String error);

    void showError(String error);

    void hideProgress();
    void showProgress();
}
