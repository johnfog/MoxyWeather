package biz.infoas.moxyweather.ui.activity.search_weather;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

/**
 * Created by devel on 22.05.2017.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface SearchWeatherView extends MvpView {

    void showResult(List<String> strings);
    void showResultTextChange(String textChange);
    void showError(String error);
    void showErrorLocation(String errorLoc);

    void showProgress();
    void hideProgress();

    void showProgressLocation();
    void hideProgressLocation();
}
