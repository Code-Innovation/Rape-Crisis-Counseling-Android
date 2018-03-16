package nz.rita.rcc

import android.content.Context
import nz.rita.rcc.models.Preferences
import nz.rita.rcc.models.api.ApiMainResponse
import nz.rita.rcc.models.api.ApiSection

/**
 * Created by jgut on 26.01.2018.
 */

class Repository {

    companion object {
        var apiObjects = HashMap<ApiSection, ApiMainResponse?>()
        var prefObject : Preferences? = null
    }

    fun addApiObject(section: ApiSection, asd: ApiMainResponse) {
        apiObjects.put(section, asd)
    }

    fun clear() {
        apiObjects = HashMap<ApiSection, ApiMainResponse?>()
    }

    fun initPref(context: Context) {
        prefObject = Preferences(context)
    }

}