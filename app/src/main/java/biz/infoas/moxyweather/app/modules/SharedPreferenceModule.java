package biz.infoas.moxyweather.app.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by devel on 19.05.2017.
 */

@Module
public class SharedPreferenceModule {

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
