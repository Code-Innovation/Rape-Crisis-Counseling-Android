package nz.rita.rcc.fragments

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import kotlinx.android.synthetic.main.content_subsection.*
import kotlinx.android.synthetic.main.fragment_read_subsection.*
import nz.rita.rcc.R
import nz.rita.rcc.adapters.AdapterMenuItem
import nz.rita.rcc.events.ClickInSideMenuEvent
import nz.rita.rcc.models.api.ApiContentItem
import nz.rita.rcc.models.api.ApiMainResponse
import nz.rita.rcc.utils.ContentIndexUtil
import nz.rita.rcc.utils.SubsectionIndex
import nz.rita.rcc.utils.TypefaceUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.sdk25.coroutines.onClick
import timber.log.Timber
import kotlin.properties.Delegates

class ReadSubsectionFragment : Fragment() {

    private var adapterMenu: AdapterMenuItem? = null
    var subsection: ApiContentItem by Delegates.notNull()
    var nesting: MutableList<Int> by Delegates.notNull()
    var toolbarTitle: String by Delegates.notNull()
    var indexes: ArrayList<SubsectionIndex> by Delegates.notNull()
    
    var sectionApiObject: ApiMainResponse? = null
        set(value) {
            indexes = ContentIndexUtil.generateIndexes(value?.listOfContents!!)
            adapterMenu = AdapterMenuItem()
            val list = value.listOfContents
            if (list != null) {
                adapterMenu?.setContents(list)
            }
        }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_read_subsection, container, false)
        TypefaceUtil.setAppDefaultTypeface(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ivSubsectionMenu.onClick {
            dlSubsection.openDrawer(GravityCompat.START)
        }

        rvContentSideMenu.adapter = adapterMenu
        tvSubsectionTitle.text = toolbarTitle

        Timber.d("size nesting ${nesting.size}")
        setPageContent(subsection, nesting)
    }

    override fun onResume() {
        super.onResume()
        val list = sectionApiObject?.listOfContents
        if (list != null) {
            adapterMenu?.setContents(list)
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ClickInSideMenuEvent) {
        dlSubsection.closeDrawer(GravityCompat.START)

        Timber.d("size nesting: " + event.nestingLevel.size)
        Timber.d("default test level")
        for (itemNest: Int in event.nestingLevel) {
            Timber.d("item: ${itemNest + 1}")
        }
        setPageContent(event.subsection, event.nestingLevel)
    }

    private fun setPageContent(content: ApiContentItem,
                               nestingLevel: MutableList<Int>) {
        sv_main_scroll.fullScroll(ScrollView.FOCUS_UP)

        var listAllContent = sectionApiObject?.listOfContents
        tvMainTitle.text = listAllContent?.get(nestingLevel[0])?.contentTitle ?: ""

        if (nestingLevel.size > 1) {
            tvSubTitle.text = content.contentTitle
            tvSubTitle.visibility = View.VISIBLE
        } else {
            tvSubTitle.visibility = View.GONE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvSubsectionText.text = Html.fromHtml(Html.fromHtml(content.contentContent, Html.FROM_HTML_MODE_LEGACY).toString(), Html.FROM_HTML_MODE_LEGACY)
        } else {
            tvSubsectionText.text = Html.fromHtml(Html.fromHtml(content.contentContent).toString())
        }

        val position = indexes.find {it.item == content}?.position ?: 0

        val nextPosition = position + 1
        if (nextPosition <= indexes.size - 1) {
            var nextItemWithIndex = indexes[nextPosition]
            rlSubsectionNextPoint.visibility = View.VISIBLE
            tvSubsectionNextTitle.text = nextItemWithIndex.item.contentTitle
            rlSubsectionNextPoint.setOnClickListener {
                sv_main_scroll.fullScroll(ScrollView.FOCUS_UP)
                EventBus.getDefault().post(
                        ClickInSideMenuEvent(nextItemWithIndex.item, nextItemWithIndex.nestingLevel))
            }
        } else {
            rlSubsectionNextPoint.visibility = View.GONE
        }

        val backPosition = position - 1
        if (backPosition >= 0) {
            var backItemWithIndex = indexes[backPosition]
            rlSubsectionBackPoint.visibility = View.VISIBLE
            tvSubsectionBackTitle.text = backItemWithIndex.item.contentTitle
            rlSubsectionBackPoint.setOnClickListener {
                sv_main_scroll.fullScroll(ScrollView.FOCUS_UP)
                EventBus.getDefault().post(
                        ClickInSideMenuEvent(backItemWithIndex.item, backItemWithIndex.nestingLevel))
            }
        } else {
            rlSubsectionBackPoint.visibility = View.GONE
        }

        TypefaceUtil.setAppDefaultTypeface(sv_main_scroll)
    }

}
