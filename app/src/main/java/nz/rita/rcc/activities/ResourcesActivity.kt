package nz.rita.rcc.activities

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import kotlinx.android.synthetic.main.activity_resources.*
import nz.rita.rcc.R
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import nz.rita.rcc.R.string.resources_title
import nz.rita.rcc.utils.MakeLinksUtil
import nz.rita.rcc.utils.TypefaceUtil

class ResourcesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resources)

        supportActionBar?.title = getString(resources_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        with (tvResourcesText) {
            var html = getString(R.string.resources_text)
            text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
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
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
