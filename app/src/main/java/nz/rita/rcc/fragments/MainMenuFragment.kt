package nz.rita.rcc.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main_menu.*
import nz.rita.rcc.R
import nz.rita.rcc.events.MainMenuEvent
import nz.rita.rcc.utils.TypefaceUtil
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainMenuFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_main_menu, container, false)
        TypefaceUtil.setAppDefaultTypeface(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btMainMenuResForSurvivors.onClick {
            EventBus.getDefault().post(MainMenuEvent(R.id.menu_resource_for_survivors))
        }

        btMainMenuResForAdvocates.onClick {
            EventBus.getDefault().post(MainMenuEvent(R.id.menu_resource_for_advocates))
        }

        btMainMenuAdvocateTraining.onClick {
            EventBus.getDefault().post(MainMenuEvent(R.id.menu_advocate_training))
        }
    }

}
