package nz.rita.rcc.utils

import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View

/**
 * Created by maletin on 1/31/2018.
 */

class MakeLinksUtil {

    class CustomerTextClick(internal var mUrl: String) : ClickableSpan() {

        override fun onClick(widget: View) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(mUrl)
            widget.context.startActivity(i)
        }

    }

    fun reformatText(text: CharSequence): SpannableStringBuilder {
        val end = text.length
        val sp = text as Spannable
        val urls = sp.getSpans(0, end, URLSpan::class.java)
        val style = SpannableStringBuilder(text)
        for (url in urls) {
            style.removeSpan(url)
            val click = MakeLinksUtil.CustomerTextClick(url.url)
            style.setSpan(click, sp.getSpanStart(url), sp.getSpanEnd(url),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return style
    }
}