package biz.infoas.moxyweather.app.modules;

import android.content.Context;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import javax.inject.Singleton;

import biz.infoas.moxyweather.domain.models.WeatherFormated;
import biz.infoas.moxyweather.domain.db.DBHelper;
import biz.infoas.moxyweather.domain.db.dao.WeatherDAO;
import dagger.Module;
import dagger.Provides;

/**
 * Created by devel on 19.05.2017.
 */
@Module
public class DBModule {

    @Provides
    @Singleton
    public DBHelper provideDBHelper(Context context) {
        return new DBHelper(context);
    }

    @Provides
    @Singleton
    ConnectionSource provideConnectionSource(DBHelper dbHelper) {
        return dbHelper.getConnectionSource();
    }

    @Provides
    @Singleton
    public WeatherDAO provideWeatherDAO(ConnectionSource connectionSource) {
        WeatherDAO weatherDAO = null;
        try {
            weatherDAO = new WeatherDAO(connectionSource, WeatherFormated.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weatherDAO;
    }
}
