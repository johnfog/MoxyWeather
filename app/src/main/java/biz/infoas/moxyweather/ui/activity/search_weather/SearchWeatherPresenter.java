package biz.infoas.moxyweather.ui.activity.search_weather;

import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.interactor.SearchWeatherInteractor;
import rx.Subscriber;

/**
 * Created by devel on 22.05.2017.
 */
@InjectViewState
public class SearchWeatherPresenter extends MvpPresenter<SearchWeatherView> {

    @Inject
    SearchWeatherInteractor interactor;

    public SearchWeatherPresenter() {
        App.getAppComponent().inject(this);
    }

    public void textChangeListener(TextView searchTextView) {
        getViewState().showProgress();
        interactor.observableTextChange(searchTextView).subscribe(new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {
               String ed = "sdf";
            }

            @Override
            public void onError(Throwable e) {
                getViewState().hideProgress();
                getViewState().showError(e.getMessage());
            }

            @Override
            public void onNext(List<String> strings) {
                getViewState().hideProgress();
                getViewState().showResult(strings);
            }
        });
    }
}
