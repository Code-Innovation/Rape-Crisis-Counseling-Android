package nz.rita.rcc.activities

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_terms_of_use.*
import nz.rita.rcc.R
import nz.rita.rcc.utils.MakeLinksUtil
import nz.rita.rcc.utils.TypefaceUtil

class TermsOfUseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_use)

        supportActionBar?.title = getString(R.string.terms_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        with (tvTermsText) {
            val html = getString(R.string.terms_text)
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
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
