package nz.rita.rcc.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Shvarev Mikhail on 1/25/2018.
 */

class ApiMainResponse {
    @SerializedName("curriculum")
    @Expose
    protected val main: ApiCurriculum? = null

    val settings: ApiSettings?
        get() = main!!.apiSettings

    val listOfContents: List<ApiContentItem>?
        get() = main!!.listContent

    val title: String?
        get() = main!!.title
}
