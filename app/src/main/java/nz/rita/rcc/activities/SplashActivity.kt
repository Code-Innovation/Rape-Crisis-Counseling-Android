package nz.rita.rcc.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import kotlinx.android.synthetic.main.activity_splash.*
import nz.rita.rcc.R
import nz.rita.rcc.Repository
import nz.rita.rcc.api.ApiHelper

class SplashActivity : AppCompatActivity() {

    private fun initApi() {

        ApiHelper(this).initApi(object : ApiHelper.CallbackForApi {
            override fun onResponseSuccess() {
                val intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onResponseFailure(errorStr: String) {

                Snackbar.make(clSplash, "Unknown Error", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", View.OnClickListener {
                            initApi()
                        }).show()

            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Repository().initPref(this)
        initApi()
    }
}
