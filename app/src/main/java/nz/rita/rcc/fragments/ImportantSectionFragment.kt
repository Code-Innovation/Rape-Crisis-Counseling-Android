package nz.rita.rcc.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_important_section.*

import nz.rita.rcc.R
import org.jetbrains.anko.sdk25.coroutines.onClick


class ImportantSectionFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_important_section, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rvImportantSectionBack.onClick {
            activity.supportFragmentManager.popBackStack()
        }
    }

}
