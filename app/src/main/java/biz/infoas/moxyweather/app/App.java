package biz.infoas.moxyweather.app;

import android.app.Application;
import android.content.Context;

import biz.infoas.moxyweather.app.modules.ContextModule;
import biz.infoas.moxyweather.app.modules.HttpModule;

/**
 * Created by devel on 16.05.2017.
 */

public class App extends Application {

    public static AppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        initInjector();
    }

    private void initInjector() {
        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .httpModule(new HttpModule())
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
