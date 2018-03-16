package nz.rita.rcc.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Shvarev Mikhail on 1/25/2018.
 */

class ApiLevelTitle {
    @SerializedName("1")
    @Expose
    val levelFirst: String? = null

    @SerializedName("2")
    @Expose
    val levelSecond: String? = null

    @SerializedName("3")
    @Expose
    val levelThird: String? = null

    @SerializedName("4")
    @Expose
    val levelFourth: String? = null
}

