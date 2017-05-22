
package biz.infoas.moxyweather.domain.models.city;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainTextMatchedSubstring {

    @SerializedName("length")
    @Expose
    public Integer length;
    @SerializedName("offset")
    @Expose
    public Integer offset;

}
