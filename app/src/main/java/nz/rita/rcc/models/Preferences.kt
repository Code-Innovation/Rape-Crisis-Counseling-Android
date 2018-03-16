package nz.rita.rcc.models

import android.content.Context
import android.preference.PreferenceManager
import java.util.*

class Preferences(context : Context) {

    private var pref = PreferenceManager.getDefaultSharedPreferences(context)

    interface Section {

        fun getJson() : String
        fun setJson(data: String)
        fun getDateModified() : Date
        fun setDateModified(date: Date)

    }

    inner class Cache {

        inner class SurvivorResource : Section {

            private val sectionName = "survivor_resource"
            private val json = "cache_${sectionName}_json"
            private val dateModified = "cache_${sectionName}_date_modified"

            override fun getJson() = pref.getString(json, "")

            override fun setJson(data: String) {
                val editor = pref.edit()
                editor.putString(json, data)
                editor.apply()
            }

            override fun getDateModified() = Date(pref.getLong(dateModified, 0))

            override fun setDateModified(date: Date) {
                val editor = pref.edit()
                editor.putLong(dateModified, date.time)
                editor.apply()
            }

        }

        inner class AdvocateResource : Section {

            private val sectionName = "advocate_resource"
            private val json = "cache_${sectionName}_json"
            private val dateModified = "cache_${sectionName}_date_modified"

            override fun getJson() = pref.getString(json, "")

            override fun setJson(data: String) {
                val editor = pref.edit()
                editor.putString(json, data)
                editor.apply()
            }

            override fun getDateModified() = Date(pref.getLong(dateModified, 0))

            override fun setDateModified(date: Date) {
                val editor = pref.edit()
                editor.putLong(dateModified, date.time)
                editor.apply()
            }

        }

        inner class AdvocateTraining : Section {

            private val sectionName = "advocate_training"
            private val json = "cache_${sectionName}_json"
            private val dateModified = "cache_${sectionName}_date_modified"

            override fun getJson() = pref.getString(json, "")

            override fun setJson(data: String) {
                val editor = pref.edit()
                editor.putString(json, data)
                editor.apply()
            }

            override fun getDateModified() = Date(pref.getLong(dateModified, 0))

            override fun setDateModified(date: Date) {
                val editor = pref.edit()
                editor.putLong(dateModified, date.time)
                editor.apply()
            }

        }

    }

}
