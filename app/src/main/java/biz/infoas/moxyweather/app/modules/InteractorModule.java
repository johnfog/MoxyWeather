package biz.infoas.moxyweather.app.modules;

import javax.inject.Singleton;

import biz.infoas.moxyweather.interactor.MapWeatherInteractor;
import biz.infoas.moxyweather.interactor.SearchWeatherInteractor;
import biz.infoas.moxyweather.interactor.WeatherInteractror;
import dagger.Module;
import dagger.Provides;

/**
 * Created by devel on 17.05.2017.
 */

@Module
public class InteractorModule {

    @Provides
    @Singleton
    public WeatherInteractror providWeatherInteractor() {
        return new WeatherInteractror();
    }

    @Provides
    @Singleton
    public SearchWeatherInteractor provideSearchWeatherInteractor() {
        return new SearchWeatherInteractor();
    }

    @Provides
    @Singleton
    public MapWeatherInteractor provideMapWeatherInteractor() {
        return new MapWeatherInteractor();
    }
}

