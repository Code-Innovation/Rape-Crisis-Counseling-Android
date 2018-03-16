package nz.rita.rcc.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Shvarev Mikhail on 1/25/2018.
 */

class ApiContentItem {
    @SerializedName("title")
    @Expose
    val contentTitle: String? = null

    @SerializedName("content")
    @Expose
    val contentContent: String? = null

    @SerializedName("subsections")
    @Expose
    val contentSubsectionList: List<ApiContentItem>? = null
}