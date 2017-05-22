
package biz.infoas.moxyweather.domain.models.city;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("predictions")
    @Expose
    public List<Prediction> predictions = null;
    @SerializedName("status")
    @Expose
    public String status;

}
