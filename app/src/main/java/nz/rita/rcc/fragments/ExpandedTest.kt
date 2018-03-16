package nz.rita.rcc.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_expanded_menu.view.*
import nz.rita.rcc.R
import nz.rita.rcc.adapters.AdapterMenuItem
import nz.rita.rcc.api.ApiHelper
import nz.rita.rcc.models.api.ApiContentItem
import nz.rita.rcc.models.api.ApiMainResponse
import timber.log.Timber

/**
 * Created by Shvarev Mikhail on 1/26/2018.
 */

class ExpandedTest : Fragment() {

    var adapterMenu = AdapterMenuItem()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_expanded_menu, container, false)
        view.menu_recyclerview.adapter = adapterMenu;
        return view
    }

    private fun getExpandItem(gap: String, item: List<ApiContentItem>) {
        if (item!!.isNotEmpty()) {
            for (itemSub: ApiContentItem in item!!) {
                Timber.d("_" + gap + "title: " + itemSub.contentTitle)
                Timber.d("_" + gap + "count Sublist: " + itemSub.contentSubsectionList!!.size)
                getExpandItem(gap + "     ", itemSub.contentSubsectionList)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        // todo я не знаю что с этим делать :D
//        ApiHelper().initApi(object : ApiHelper.CallbackForApi {
//            override fun onResponseSuccess(amr: ApiMainResponse) {
////                Timber.d("language type: " + amr.settings!!.levelTitle!!.levelSecond!!)
////                Timber.d("language type: " + amr.listOfContents!![0].contentTitle!!)
//                //getExpandItem("", amr.listOfContents!!)
//                adapterMenu.setContents(amr.listOfContents!!)
//            }
//
//            override fun onResponseFailure(errorStr: String) {
//                Timber.d(errorStr)
//            }
//        })
    }
}
