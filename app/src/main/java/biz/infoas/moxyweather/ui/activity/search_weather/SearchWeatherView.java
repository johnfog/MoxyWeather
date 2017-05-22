package biz.infoas.moxyweather.ui.activity.search_weather;

import com.arellomobile.mvp.MvpView;

import java.util.List;

/**
 * Created by devel on 22.05.2017.
 */

public interface SearchWeatherView extends MvpView {

    void showResult(List<String> strings);
    void showError(String error);

    void showProgress();
    void hideProgress();
}
