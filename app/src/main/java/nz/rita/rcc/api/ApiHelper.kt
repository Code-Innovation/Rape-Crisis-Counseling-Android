package nz.rita.rcc.api

import android.content.Context
import com.google.gson.Gson
import nz.rita.rcc.Repository
import nz.rita.rcc.models.Preferences

import nz.rita.rcc.models.api.ApiMainResponse
import nz.rita.rcc.models.api.ApiSection
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import timber.log.Timber
import java.util.*

/**
 * Created by Shvarev Mikhail on 1/25/2018.
 */

class ApiHelper(val context: Context) {

    companion object {
        private const val BASE_URL = "https://s3-us-west-2.amazonaws.com/"

        private const val SURVIVOR_RESOURCE_JSON = "/rcc-json/v1/english/survivor-resource.json"
        private const val ADVOCATE_RESOURCE_JSON = "/rcc-json/v1/english/advocate-resource.json"
        private const val ADVOCATE_TRAINING_JSON = "/rcc-json/v1/english/advocate-training.json"

        private const val JSON_AMOUNT = 3
    }

    private val retrofit: Retrofit
    private val apiService: ApiService
    private val client = OkHttpClient()
    private var jsonParseCount = 0

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    private fun getLastModified(jsonUrl: String) : Date {
        try {
            val request = Request.Builder()
                    .url(BASE_URL + jsonUrl)
                    .head()
                    .build()

            val response = client.newCall(request).execute()
            return response.headers().getDate("Last-Modified") ?: Date(0)
        } catch (e: Exception) {
            return Date(0)
        }
    }

    private fun getAssetsJsonName(apiSection: ApiSection) = when (apiSection) {
            ApiSection.ADVOCATE_TRAINING -> "advocate-training.json"
            ApiSection.ADVOCATE_RESOURCE -> "advocate-resource.json"
            ApiSection.SURVIVOR_RESOURCE -> "survivor-resource.json"
        }

    private fun putFromCache(cacheSection : Preferences.Section,
                             apiSection: ApiSection,
                             apiCallback: CallbackForApi) {

        val json = cacheSection.getJson()
        var data = Gson().fromJson(json, ApiMainResponse::class.java)
        if (data == null) {
            try {
                Timber.d("load data (%s) cacheDate from assets", apiSection.toString())
                val stream = context.assets.open(getAssetsJsonName(apiSection))
                val size = stream.available()
                val buffer = ByteArray(size)
                stream.read(buffer)
                stream.close()
                data = Gson().fromJson(String(buffer), ApiMainResponse::class.java)
                Repository.apiObjects.put(apiSection, data!!)
                jsonParseCount++
                checkResult(apiCallback)
            } catch (e: Exception) {
                apiCallback.onResponseFailure(
                        "Could not retrieve data from cache: " + e.message)
            }
        } else {
            Timber.d("load data (%s) cacheDate from config", apiSection.toString())
            Repository.apiObjects.put(apiSection, data)
            jsonParseCount++
            checkResult(apiCallback)
        }
    }

    private fun initApiObject(cacheSection : Preferences.Section,
                              jsonPath: String,
                              apiSection: ApiSection,
                              apiService: Call<ApiMainResponse>,
                              apiCallback: CallbackForApi) {
        val cacheDate = cacheSection.getDateModified()
        val serverDate = getLastModified(jsonPath)

        if (serverDate.after(cacheDate)) {
            Timber.d("load data (%s) serverDate > cacheDate", apiSection.toString())
            callJson(apiSection, apiService, cacheSection, apiCallback)
        } else {
            Timber.d("load data (%s) serverDate < cacheDate", apiSection.toString())
            putFromCache(cacheSection, apiSection, apiCallback)
        }
    }

    fun initApi(apiCallback: CallbackForApi) {
        doAsync {
            Repository().clear()
            jsonParseCount = 0

            with (Repository.prefObject?.Cache()!!) {
                initApiObject(SurvivorResource(), SURVIVOR_RESOURCE_JSON,
                        ApiSection.SURVIVOR_RESOURCE, apiService.survivorResource, apiCallback)

                initApiObject(AdvocateResource(), ADVOCATE_RESOURCE_JSON,
                        ApiSection.ADVOCATE_RESOURCE, apiService.advocateResource, apiCallback)

                initApiObject(AdvocateTraining(), ADVOCATE_TRAINING_JSON,
                        ApiSection.ADVOCATE_TRAINING, apiService.advocateTraining, apiCallback)
            }
        }
    }

    private fun checkResult(apiCallback: CallbackForApi) {
        if (Repository.apiObjects.size == JSON_AMOUNT) {
            if (jsonParseCount == JSON_AMOUNT) {
                apiCallback.onResponseSuccess()
            } else {
                apiCallback.onResponseFailure("")
            }
        }
    }

    private fun callJson(section: ApiSection, getJson: Call<ApiMainResponse>,
                         cacheSection : Preferences.Section, apiCallback: CallbackForApi) {
        getJson.enqueue(object : Callback<ApiMainResponse> {

            override fun onResponse(call: Call<ApiMainResponse>, response: Response<ApiMainResponse>) {
                if (response.code() == 200) {
                    Timber.d("load data (%s) from server", section.toString())
                    val data = response.body()!!
                    Repository.apiObjects.put(section, data)
                    cacheSection.setJson(Gson().toJson(data, ApiMainResponse::class.java))
                    cacheSection.setDateModified(response.headers().getDate("Last-Modified")!!)
                    jsonParseCount++
                    checkResult(apiCallback)
                } else {
                    Timber.d("load data (%s) from cache", section.toString())
                    putFromCache(cacheSection, section, apiCallback)
                }
            }

            override fun onFailure(call: Call<ApiMainResponse>, t: Throwable) {
                apiCallback.onResponseFailure(t.message ?: "")
            }
        })
    }

    interface CallbackForApi {
        fun onResponseSuccess()
        fun onResponseFailure(errorStr: String)
    }

    interface ApiService {
        @get:GET(SURVIVOR_RESOURCE_JSON)
        val survivorResource: Call<ApiMainResponse>

        @get:GET(ADVOCATE_RESOURCE_JSON)
        val advocateResource: Call<ApiMainResponse>

        @get:GET(ADVOCATE_TRAINING_JSON)
        val advocateTraining: Call<ApiMainResponse>
    }

}
