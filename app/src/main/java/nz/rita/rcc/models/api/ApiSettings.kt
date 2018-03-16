package nz.rita.rcc.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Shvarev Mikhail on 1/25/2018.
 */

class ApiSettings {
    @SerializedName("language")
    @Expose
    val typeLang: String? = null
    @SerializedName("level_titles")
    @Expose
    val levelTitle: ApiLevelTitle? = null
}
