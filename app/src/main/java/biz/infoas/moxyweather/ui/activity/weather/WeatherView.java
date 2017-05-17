package biz.infoas.moxyweather.ui.activity.weather;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import biz.infoas.moxyweather.domain.Weather;
import biz.infoas.moxyweather.domain.WeatherFormated;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface WeatherView extends MvpView {

    @StateStrategyType(SingleStateStrategy.class)
    void showWeatherList(List<WeatherFormated> listWeather);

    void showError(String error);

    void hideProgress();
    void showProgress();
}
