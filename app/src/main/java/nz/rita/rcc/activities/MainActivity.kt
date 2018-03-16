package nz.rita.rcc.activities

import android.app.FragmentManager
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import nz.rita.rcc.R
import nz.rita.rcc.Repository
import nz.rita.rcc.events.MainMenuEvent
import nz.rita.rcc.events.OpenSubsectionEvent
import nz.rita.rcc.fragments.MainMenuFragment
import nz.rita.rcc.fragments.ReadSectionFragment
import nz.rita.rcc.fragments.ReadSubsectionFragment
import nz.rita.rcc.models.api.ApiSection
import nz.rita.rcc.utils.TypefaceUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.sdk25.coroutines.onClick
import timber.log.Timber

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var currentMode : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportActionBar?.title = getString(R.string.app_name)

        supportFragmentManager.apply {
            if (fragments.size == 0) {
                beginTransaction().apply {
                    add(R.id.frameForFragment, MainMenuFragment())
                    commit()
                }
            }
        }

        tvToolbarLogo.onClick {
            supportFragmentManager.apply {
                beginTransaction().apply {
                    replace(R.id.frameForFragment, MainMenuFragment())
                    popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    commit()
                }
            }
        }

        ivDrawerMenu.onClick {
            dlMain.openDrawer(GravityCompat.END)
        }

        nav_view.setNavigationItemSelectedListener(this)
        TypefaceUtil.setAppDefaultTypeface(window.decorView)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        if (dlMain.isDrawerOpen(GravityCompat.END)) {
            dlMain.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        EventBus.getDefault().post(MainMenuEvent(item.itemId))
        dlMain.closeDrawer(GravityCompat.END)
        return true
    }

    private fun openSection(section: ApiSection) {
        supportFragmentManager.apply {
            beginTransaction().apply {
                replace(R.id.frameForFragment, ReadSectionFragment().apply {
                    apiObject = Repository.apiObjects[section]
                })
                addToBackStack(null)
                commit()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event : OpenSubsectionEvent) {
        supportFragmentManager.apply {
            beginTransaction().apply {
                replace(R.id.frameForFragment,
                        ReadSubsectionFragment().apply {
                            toolbarTitle = event.title
                            subsection = event.subsection
                            nesting = event.nestingLevel
                            sectionApiObject = event.sectionApiObject
                        })
                addToBackStack(null)
                commit()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event : MainMenuEvent) {
        when (event.itemId) {
            R.id.menu_resources -> {
                val intent = Intent(baseContext, ResourcesActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_about -> {
                val intent = Intent(baseContext, AboutActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_privacy_policy -> {
                val intent = Intent(baseContext, PrivacyPolicyActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_terms_of_use -> {
                val intent = Intent(baseContext, TermsOfUseActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_resource_for_survivors -> openSection(ApiSection.SURVIVOR_RESOURCE)
            R.id.menu_resource_for_advocates -> openSection(ApiSection.ADVOCATE_RESOURCE)
            R.id.menu_advocate_training -> openSection(ApiSection.ADVOCATE_TRAINING)
        }

        Timber.d("size Menu${nav_view.menu.size()}")
    }

}
