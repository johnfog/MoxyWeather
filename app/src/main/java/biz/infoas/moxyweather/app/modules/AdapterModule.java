package biz.infoas.moxyweather.app.modules;

import javax.inject.Singleton;

import biz.infoas.moxyweather.ui.activity.weather.adapter.WeatherAdapter;
import dagger.Module;
import dagger.Provides;

/**
 * Created by devel on 17.05.2017.
 */

@Module
public class AdapterModule {

    @Provides
    @Singleton
    public WeatherAdapter weatherAdapter() {
        return new WeatherAdapter();
    }
}
