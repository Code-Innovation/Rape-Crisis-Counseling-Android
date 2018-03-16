package nz.rita.rcc.utils

import android.widget.TextView
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.text.SpannableString



/**
 * Created by maletin on 2/6/2018.
 */

class FormatTextViewUtil {

    companion object {

        fun setUnderline(textView : TextView) {
            val text = textView.text
            val ss = SpannableString(text)
            ss.setSpan(UnderlineSpan(), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            textView.text = ss
        }

        fun setRegular(textView : TextView) {
            val text = textView.text
            textView.text = text.toString()
        }

    }

}