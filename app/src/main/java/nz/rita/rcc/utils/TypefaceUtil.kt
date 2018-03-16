package nz.rita.rcc.utils

/**
 * Created by Mihail on 30.01.2018.
 */

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

object TypefaceUtil {
    private val TAG_TITLE = "TITLE"
    private var tf_noto_regular: Typeface? = null
    private var tf_noto_bold: Typeface? = null


    fun initialize(context: Context) {
        tf_noto_regular = Typeface.createFromAsset(context.assets, "fonts/noto_sans_regular.ttf")
        tf_noto_bold = Typeface.createFromAsset(context.assets, "fonts/noto_sans_bold.ttf")
    }

    /**
     * Recursively sets typeface of textvievs inside this view
     * to open sans
     *
     *
     * Use whenever new view is created: activity, fragment, adapter, etc.
     *
     * @param view
     */
    fun setAppDefaultTypeface(view: View) {
        if (view is ViewGroup) {
            val childCount = view.childCount
            for (childIndex in 0 until childCount) {
                val curChild = view.getChildAt(childIndex)

                if (curChild is TextView) {
                    setTypeFace(curChild)
                }

                if (curChild is ViewGroup) {
                    setAppDefaultTypeface(curChild)
                }
            }
        } else {
            if (view is TextView) {
                setTypeFace(view)
            }
        }
    }

    private fun setTypeFace(textView: TextView) {
        if (textView.typeface != null && textView.typeface.style == Typeface.BOLD) {
            textView.typeface = tf_noto_bold
        } else {
            textView.typeface = tf_noto_regular
        }
    }

    fun setBoldTypeface(view: View, bold: Boolean) {
        if (view is ViewGroup) {
            val childCount = view.childCount
            for (childIndex in 0 until childCount) {
                val curChild = view.getChildAt(childIndex)

                if (curChild is TextView) {
                    setBold(curChild, bold)
                }

                if (curChild is ViewGroup) {
                    setBoldTypeface(curChild, bold)
                }
            }
        } else {
            if (view is TextView) {
                setBold(view, bold)
            }
        }
    }

    private fun setBold(textView: TextView, bold: Boolean) {
        if (bold) {
            textView.typeface = tf_noto_bold
        } else {
            textView.typeface = tf_noto_regular
        }

    }

}
