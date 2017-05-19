package biz.infoas.moxyweather.domain.db.dao;

import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import biz.infoas.moxyweather.app.App;
import biz.infoas.moxyweather.domain.util.models.WeatherFormated;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by devel on 17.05.2017.
 */

public class WeatherDAO extends BaseDaoImpl<WeatherFormated, Integer> {

    public WeatherDAO(ConnectionSource connectionSource, Class<WeatherFormated> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
    public Observable<List<WeatherFormated>> getAllWeather() {
        return Observable.fromCallable(new Callable<List<WeatherFormated>>() {
            public List<WeatherFormated> call() throws Exception {
                Log.d("WeatherDao", "Thread getAllWeather: "+ Thread.currentThread().getName());
                return WeatherDAO.this.queryForAll();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void addWeather(final List<WeatherFormated> weathers) throws SQLException {
        WeatherDAO.this.callBatchTasks(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                clearTable();
                Log.d("WeatherDao", "Thread addWeather: "+ Thread.currentThread().getName());
                for (WeatherFormated weather : weathers) {
                    WeatherDAO.this.create(weather);
                }
                return null;
            }
        });
    }

    private void clearTable() {
        try {
            Log.d("WeatherDao", "Thread clearTable: "+ Thread.currentThread().getName());
            TableUtils.clearTable(connectionSource, WeatherFormated.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
