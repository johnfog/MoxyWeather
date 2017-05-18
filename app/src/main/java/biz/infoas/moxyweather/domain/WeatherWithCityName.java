package biz.infoas.moxyweather.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by devel on 18.05.2017.
 */

public class WeatherWithCityName implements Serializable {

    public String cityName;
    public List<WeatherFormated> weatherFormatedList;
}
