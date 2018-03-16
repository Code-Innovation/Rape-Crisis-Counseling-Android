package nz.rita.rcc.adapters

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_list.view.*
import java.util.ArrayList

import nz.rita.rcc.R
import nz.rita.rcc.events.ClickInSideMenuEvent
import nz.rita.rcc.models.api.ApiContentItem
import nz.rita.rcc.utils.TypefaceUtil
import org.greenrobot.eventbus.EventBus
import android.widget.RelativeLayout
import android.widget.TextView
import nz.rita.rcc.utils.FormatTextViewUtil


/**
 * Created by Shvarev Mikhail on 1/26/2018.
 */

class AdapterMenuItem : RecyclerView.Adapter<AdapterMenuItem.ViewHolderItem>() {

    companion object {
        var currentItem : String? = null
        var lastUnderlineTextView : TextView? = null
    }

    private var listOfContentItems: List<ApiContentItem> = ArrayList()
    private var level: Int = 1
    private var nestingLevel: MutableList<Int> = mutableListOf<Int>()

    private fun setNestingLevel(nestingLevel: MutableList<Int>) {
        this.nestingLevel = nestingLevel
    }

    private fun setLevel(level: Int) {
        this.level = level
    }

    fun setContents(listOfContentItems: List<ApiContentItem>) {
        this.listOfContentItems = listOfContentItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)

        return ViewHolderItem(view)
    }

    private fun onClickItem(holder: ViewHolderItem, position: Int) {
        if (level == 1) {
            nestingLevel = mutableListOf<Int>()
        }
        try {
            while (nestingLevel.size >= level) {
                nestingLevel.removeAt(nestingLevel.size - 1)
            }
            nestingLevel.add(level - 1, position)
        } catch (indexEx: IndexOutOfBoundsException) {
            nestingLevel.add(position)
        }

        if (listOfContentItems[position].contentSubsectionList!!.isNotEmpty()) {
            if (holder.itemView.item_rv.visibility == View.GONE) {
                holder.itemView.item_ib_sub.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.minus))
                var adapterSub = AdapterMenuItem()
                adapterSub.setLevel(level + 1)
                adapterSub.setNestingLevel(nestingLevel)
                holder.itemView.item_rv.adapter = adapterSub
                adapterSub.setContents(listOfContentItems[position].contentSubsectionList!!)
                holder.itemView.item_rv.visibility = View.VISIBLE
            } else {
                holder.itemView.item_ib_sub.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.plus))
                holder.itemView.item_rv.visibility = View.GONE
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
        TypefaceUtil.setAppDefaultTypeface(holder.itemView)
        holder.itemView.item_background.setBackgroundColor(getColorBackground(holder.itemView.resources))
        holder.itemView.item_tv_title.text = listOfContentItems[position].contentTitle

        if (currentItem == listOfContentItems[position].contentTitle) {
            FormatTextViewUtil.setUnderline(holder.itemView.item_tv_title)
        }

        val layoutParams = holder.itemView.item_tv_title.layoutParams as RelativeLayout.LayoutParams
        layoutParams.leftMargin = layoutParams.leftMargin * level

        holder.itemView.item_ib_sub.setImageDrawable(holder.itemView.resources.getDrawable(R.drawable.plus))
        if (listOfContentItems[position].contentSubsectionList!!.isNotEmpty()) {
            holder.itemView.item_ib_sub.visibility = View.VISIBLE
        } else {
            holder.itemView.item_ib_sub.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onClickItem(holder, position)
            EventBus.getDefault().post(
                    ClickInSideMenuEvent(listOfContentItems[position], nestingLevel))
            lastUnderlineTextView?.text = lastUnderlineTextView?.text.toString()
            FormatTextViewUtil.setUnderline(holder.itemView.item_tv_title)
            currentItem = listOfContentItems[position].contentTitle
            lastUnderlineTextView = holder.itemView.item_tv_title
        }

        holder.itemView.item_ib_sub.setOnClickListener {
            onClickItem(holder, position)
        }
    }

    private fun getColorBackground(resources: Resources): Int {
        when (level) {
            1 -> return resources.getColor(R.color.menu_level_1)
            2 -> return resources.getColor(R.color.menu_level_2)
            3 -> return resources.getColor(R.color.menu_level_3)
            4 -> return resources.getColor(R.color.menu_level_4)
        }
        return resources.getColor(R.color.menu_level_1)
    }

    override fun getItemCount(): Int {
        return listOfContentItems.size
    }

    class ViewHolderItem(itemView: View) : RecyclerView.ViewHolder(itemView)

}
