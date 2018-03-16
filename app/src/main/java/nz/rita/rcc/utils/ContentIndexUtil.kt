package nz.rita.rcc.utils

import nz.rita.rcc.models.api.ApiContentItem

/**
 * Created by jgut on 08.02.2018.
 */

class SubsectionIndex(val nestingLevel: MutableList<Int>,
                      val item: ApiContentItem,
                      val position: Int)

class ContentIndexUtil {

    companion object {

        private fun cloneList(list: MutableList<Int>) : MutableList<Int> {
            val result = mutableListOf<Int>()
            for (item in list) {
                result.add(item)
            }
            return result
        }

        private fun parseApiForIndexes(branchContentItems: List<ApiContentItem>,
                                       nestingLevel: MutableList<Int>,
                                       level: Int,
                                       result: ArrayList<SubsectionIndex> ) {

            for ((index, item) in branchContentItems.withIndex()) {
                nestingLevel.add(level, index)
                val position = result.size
                result.add(SubsectionIndex(cloneList(nestingLevel), item, position))
                if (item.contentSubsectionList?.size ?: 0 > 0) {
                    parseApiForIndexes(item.contentSubsectionList!!, cloneList(nestingLevel),
                            level + 1, result)
                }
                nestingLevel.removeAt(level)
            }

        }

        fun generateIndexes(apiContentItems: List<ApiContentItem>) : ArrayList<SubsectionIndex> {
            val result = ArrayList<SubsectionIndex>()
            parseApiForIndexes(apiContentItems, mutableListOf<Int>(), 0, result)
            return result
        }

    }

}