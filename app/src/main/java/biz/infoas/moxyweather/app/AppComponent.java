package biz.infoas.moxyweather.app;

import android.content.Context;

import javax.inject.Singleton;

import biz.infoas.moxyweather.app.api.WeatherAPI;
import biz.infoas.moxyweather.app.modules.AdapterModule;
import biz.infoas.moxyweather.app.modules.ContextModule;
import biz.infoas.moxyweather.app.modules.HttpModule;
import biz.infoas.moxyweather.app.modules.InteractorModule;
import biz.infoas.moxyweather.interactor.DetailInteractor;
import biz.infoas.moxyweather.interactor.WeatherInteractror;
import biz.infoas.moxyweather.ui.activity.weather.WeatherActivity;
import biz.infoas.moxyweather.ui.activity.weather.WeatherPresenter;
import biz.infoas.moxyweather.ui.activity.weather.adapter.WeatherAdapter;
import dagger.Component;

@Singleton
@Component(modules = {ContextModule.class, HttpModule.class, InteractorModule.class, AdapterModule.class})
public interface AppComponent {
	 Context getContext();
	//WeatherAPI getWeatherAPI();
	void inject(WeatherPresenter weatherPresenter);
	void inject(WeatherInteractror weatherInteractror);
	void inject(WeatherActivity weatherActivity);
}
