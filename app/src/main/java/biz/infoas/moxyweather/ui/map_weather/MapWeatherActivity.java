package biz.infoas.moxyweather.ui.map_weather;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import biz.infoas.moxyweather.R;
import biz.infoas.moxyweather.ui.map_weather.base.BaseMapWeatherActivity;
import biz.infoas.moxyweather.ui.search_weather.SearchWeatherActivity;
import biz.infoas.moxyweather.ui.weather.WeatherActivity;

public class MapWeatherActivity extends BaseMapWeatherActivity implements OnMapReadyCallback, MapWeatherView {

    private LatLng latLng; // Запоминаем координаты маркера

    @InjectPresenter
    MapWeatherPresenter presenter;

    public GoogleMap googleMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        presenter.setOnMapClick(mapView);
    }

    @Override
    protected void onLocationPermissionGranted() {
        presenter.getLocationUser(MapWeatherActivity.this);
    }

    @Override
    protected void onLocationPermissionDenied() {
        Toast.makeText(this, "Разрешения не предоставлены", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void enabledGPS() {
        presenter.getLocationUser(MapWeatherActivity.this);
    }

    @Override
    protected void disabledGPS() {
        Toast.makeText(this, "Геолокация не включена", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLatLngClickMap(LatLng latLng) {
        setMarker(latLng, true);
    }

    @Override
    public void showErrorLatLngClickMap(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLatLngUserLocation(LatLng latLng) {
        setMarker(latLng, false);
    }

    @Override
    public void showErrorLatLngUserLocation(String error) {

    }

    @Override
    public void showErrorSetWeather(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    private void setMarker(LatLng latLng, boolean isClick) {
        this.latLng = latLng;
        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng)); // Наводим камеру на место клика
        googleMap.addMarker(new MarkerOptions().position(latLng)); // Добавляем маркер
        //Двигаем камеру на место, где расположено устройство
        if (!isClick) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_select:
                presenter.setWeather(latLng, MapWeatherActivity.this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
