package nz.rita.rcc.activities

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_about.*
import nz.rita.rcc.BuildConfig
import nz.rita.rcc.R
import nz.rita.rcc.R.string.about_text
import nz.rita.rcc.R.string.about_title
import nz.rita.rcc.utils.MakeLinksUtil
import nz.rita.rcc.utils.TypefaceUtil

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        supportActionBar?.title = getString(about_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        with (tvAboutText) {
            val html = getString(about_text).replace("VERSION_NAME", BuildConfig.VERSION_NAME)
            text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(html)
            }
            linksClickable = true
            movementMethod = LinkMovementMethod.getInstance()

            if (text is Spannable) {
                text = MakeLinksUtil().reformatText(text)
            }
        }

        TypefaceUtil.setAppDefaultTypeface(window.decorView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
