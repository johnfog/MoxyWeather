package biz.infoas.moxyweather.app;

import android.content.Context;

import javax.inject.Singleton;

import biz.infoas.moxyweather.app.modules.ContextModule;
import biz.infoas.moxyweather.app.modules.DBModule;
import biz.infoas.moxyweather.app.modules.HttpModule;
import biz.infoas.moxyweather.app.modules.InteractorModule;
import biz.infoas.moxyweather.app.modules.SharedPreferenceModule;
import biz.infoas.moxyweather.interactor.MapWeatherInteractor;
import biz.infoas.moxyweather.interactor.SearchWeatherInteractor;
import biz.infoas.moxyweather.interactor.WeatherInteractror;
import biz.infoas.moxyweather.ui.map_weather.MapWeatherPresenter;
import biz.infoas.moxyweather.ui.search_weather.SearchWeatherPresenter;
import biz.infoas.moxyweather.ui.weather.WeatherPresenter;
import biz.infoas.moxyweather.ui.weather.base.BaseWeatherActivity;
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
	void inject(BaseWeatherActivity baseLocationActivity);
	void inject(SearchWeatherInteractor searchWeatherInteractor);
	void inject(SearchWeatherPresenter searchWeatherPresenter);
	void inject(MapWeatherPresenter mapWeatherPresenter);
	void inject(MapWeatherInteractor mapWeatherInteractor);
}
