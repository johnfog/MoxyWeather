package biz.infoas.moxyweather.app;

import android.content.Context;

import javax.inject.Singleton;

import biz.infoas.moxyweather.app.modules.ContextModule;
import biz.infoas.moxyweather.app.modules.DBModule;
import biz.infoas.moxyweather.app.modules.HttpModule;
import biz.infoas.moxyweather.app.modules.InteractorModule;
import biz.infoas.moxyweather.app.modules.SharedPreferenceModule;
import biz.infoas.moxyweather.domain.db.dao.WeatherDAO;
import biz.infoas.moxyweather.interactor.SearchWeatherInteractor;
import biz.infoas.moxyweather.interactor.WeatherInteractror;
import biz.infoas.moxyweather.ui.activity.search_weather.SearchWeatherPresenter;
import biz.infoas.moxyweather.ui.activity.weather.WeatherActivity;
import biz.infoas.moxyweather.ui.activity.weather.WeatherPresenter;
import dagger.Component;

@Singleton
@Component(modules = {ContextModule.class,
		HttpModule.class,
		InteractorModule.class,
		DBModule.class,
		SharedPreferenceModule.class})
public interface AppComponent {
	 Context getContext();
	//WeatherAPI getWeatherAPI();
	void inject(WeatherPresenter weatherPresenter);
	void inject(WeatherInteractror weatherInteractror);
	void inject(WeatherActivity weatherActivity);

	void inject(SearchWeatherInteractor searchWeatherInteractor);
	void inject(SearchWeatherPresenter searchWeatherPresenter);
}
