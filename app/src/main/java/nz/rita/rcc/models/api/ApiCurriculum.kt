package nz.rita.rcc.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Shvarev Mikhail on 1/25/2018.
 */

class ApiCurriculum {
    @SerializedName("title")
    @Expose
    val title: String? = null

    @SerializedName("settings")
    @Expose
    val apiSettings: ApiSettings? = null

    @SerializedName("content")
    @Expose
    val listContent: List<ApiContentItem>? = null
}
