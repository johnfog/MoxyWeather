package biz.infoas.moxyweather.ui.activity.search_weather;

import android.widget.TextView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.interactor.SearchWeatherInteractor;
import rx.Subscriber;
import rx.Subscription;

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
    private Subscription subTextChange;
    private Subscription subGetCitesList;

    public SearchWeatherPresenter() {
        App.getAppComponent().inject(this);
    }

    public void textChangeListener(TextView searchTextView) {
        subTextChange = interactor.observableTextChange(searchTextView).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                isDownloadListCites = false;
                getViewState().showError(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                if (nameCity.equals(s)) { // Если введённый текст в совпадает с текстом, который там был введёт до этого, то выходим из метода, чтобы не выполнять сетевой запрос
                    return;
                }
                nameCity = s;
                isDownloadListCites = false;
                getViewState().showResultTextChange(s);
            }
        });
    }

    public void getWeatherList(String nameCity) {
        if (isProcessDownload) {
            return;
        }
        if (isDownloadListCites) {
            return;
        }
        isProcessDownload = true;
        getViewState().showProgress();
        subGetCitesList = interactor.observableGetCites(nameCity).subscribe(new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                isProcessDownload = false;
                getViewState().hideProgress();
                getViewState().showError(e.getMessage());
            }

            @Override
            public void onNext(List<String> strings) {
                isDownloadListCites = true;
                isProcessDownload = false;
                getViewState().hideProgress();
                getViewState().showResult(strings);
            }
        });
    }

    public void onDestroy() {
        if (subTextChange != null) {
            subTextChange.unsubscribe();
        }
        if (subGetCitesList != null) {
            subGetCitesList.unsubscribe();
        }
    }
}
