package biz.infoas.moxyweather.ui.search_weather;

import android.app.Activity;
import android.widget.TextView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.util.KeyboardUtil;
import biz.infoas.moxyweather.interactor.SearchWeatherInteractor;

/**
 * Created by devel on 22.05.2017.
 */
@InjectViewState
public class SearchWeatherPresenter extends MvpPresenter<SearchWeatherView> {

    @Inject
    SearchWeatherInteractor interactor;

    private boolean isDownloadListCites = false; // Переменная определяет, загружена ли список городов или нет
    private boolean isProcessDownload = false; // Если переменная true, значит идёт загрузка погоды из сети.
    private String nameCity = ""; // Переменная хранит значение введённое в EditText

    public SearchWeatherPresenter() {
        App.getAppComponent().inject(this);
    }

    public void textChangeListener(TextView searchTextView) {
        interactor.observableTextChange(searchTextView)
                .subscribe(string -> {
                    isDownloadListCites = false;
                    if (nameCity.equals(string)) { // Если введённый текст в совпадает с текстом, который там был введёт до этого, то выходим из метода, чтобы не выполнять сетевой запрос
                        return;
                    }
                    nameCity = string;
                    getViewState().showResultTextChange(string);
                }, error -> {
                    isDownloadListCites = false;
                    getViewState().showError(error.getMessage());
                });
    }

    public void getWeatherList(String nameCity) {
        if (isProcessDownload) {
            return;
        }
        if (isDownloadListCites) {
            return;
        }
        interactor.observableGetCites(nameCity).doOnSubscribe(() -> {
            isProcessDownload = true;
            getViewState().showProgress();
        }).subscribe(listCites -> {
            isProcessDownload = false;
            isDownloadListCites = true;
            getViewState().hideProgress();
            getViewState().showResult(listCites);
        }, error -> {
            isProcessDownload = false;
            getViewState().hideProgress();
            getViewState().showError(error.getMessage());
        });
    }

    public void setCityWeather(String nameCity, final Activity activity) {
        interactor.getLocationCityByName(nameCity).doOnSubscribe(() -> {
            KeyboardUtil.hideSoftKeyboard(activity);
            getViewState().showProgressLocation();
        }).subscribe(location -> {
            getViewState().hideProgressLocation();
            activity.finish();
        }, error -> {
            getViewState().hideProgressLocation();
            getViewState().showError(error.getMessage());
        });
    }


}
