package biz.infoas.moxyweather.ui.activity.search_weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import biz.infoas.moxyweather.R;

/**
 * Created by devel on 22.05.2017.
 */

public class SearchWeatherAdapter extends RecyclerView.Adapter<SearchWeatherHolder> {

    public interface OnClickCityNameListener {
        void OnClickCity(String nameCity);
    }

    private OnClickCityNameListener onClickCityNameListener;
    private List<String> listCites = new ArrayList<>();

    public SearchWeatherAdapter(OnClickCityNameListener onClickCityNameListener) {
        this.onClickCityNameListener = onClickCityNameListener;
    }

    @Override
    public SearchWeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_search_weather, parent, false);
        return new SearchWeatherHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchWeatherHolder holder, int position) {
        final String nameCity = listCites.get(position);
        holder.tvNameCitySearch.setText(nameCity);
        holder.layoutNameCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickCityNameListener != null) {
                    onClickCityNameListener.OnClickCity(nameCity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCites.size();
    }

    public void updateSearchList(List<String> listCites) {
        this.listCites.clear();
        this.listCites.addAll(listCites);
        this.notifyDataSetChanged();

    }
}
