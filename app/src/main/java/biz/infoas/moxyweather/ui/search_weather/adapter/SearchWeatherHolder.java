package biz.infoas.moxyweather.ui.search_weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import biz.infoas.moxyweather.R;

/**
 * Created by devel on 22.05.2017.
 */

public class SearchWeatherHolder extends RecyclerView.ViewHolder {

    LinearLayout layoutNameCity;
    TextView tvNameCitySearch;

    public SearchWeatherHolder(View itemView) {
        super(itemView);
        layoutNameCity = (LinearLayout) itemView.findViewById(R.id.layout_city_name);
        tvNameCitySearch = (TextView) itemView.findViewById(R.id.tv_name_city_search);
    }
}
