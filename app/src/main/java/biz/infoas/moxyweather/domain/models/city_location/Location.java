
package biz.infoas.moxyweather.domain.models.city_location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;

    public Double getLat() {
        return lat;
    }
    public String getLatString() {
        return String.valueOf(lat);
    }


    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }
    public String getLngString() {
        return String.valueOf(lng);
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

}
