package nz.rita.rcc.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_read_section.*
import nz.rita.rcc.R
import nz.rita.rcc.Repository
import nz.rita.rcc.events.OpenSubsectionEvent
import nz.rita.rcc.models.api.ApiContentItem
import nz.rita.rcc.models.api.ApiMainResponse
import nz.rita.rcc.utils.TypefaceUtil
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.sdk25.coroutines.onClick
import timber.log.Timber

class ReadSectionFragment : Fragment() {

    var apiObject: ApiMainResponse? = null
    var sectionTitle = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_read_section, container, false)
        TypefaceUtil.setAppDefaultTypeface(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sectionTitle = apiObject?.title ?: ""
        tvSectionTitle.text = sectionTitle

        if (apiObject?.title.equals(getString(R.string.section_advocate_training), true)) {
            tvSectionText.text = getString(R.string.section_text_advocate_training)
        } else {
            tvSectionText.text = ""
        }

        val size = apiObject?.listOfContents?.size ?: 0
        if (size > 0) {
            for(index in 0 until size){
                val item = View.inflate(activity.applicationContext,
                        R.layout.section_button_item, null) as Button
                item.text = apiObject?.listOfContents!![index].contentTitle
                item.onClick {
                    openSubsection(apiObject?.listOfContents!![index],index)
                }

                View.inflate(activity.applicationContext,
                        R.layout.section_empty_space, llSubsectionLinks)
                llSubsectionLinks.addView(item)
            }
        }

        TypefaceUtil.setAppDefaultTypeface(llSubsectionLinks)
    }

    private fun openSubsection(subsection: ApiContentItem, position: Int) {
        var nestingLevel: MutableList<Int> = mutableListOf()
        nestingLevel.add(position)
        Timber.d("index $position")
        EventBus.getDefault().post(OpenSubsectionEvent(sectionTitle, subsection, nestingLevel, apiObject!!))
    }

}
