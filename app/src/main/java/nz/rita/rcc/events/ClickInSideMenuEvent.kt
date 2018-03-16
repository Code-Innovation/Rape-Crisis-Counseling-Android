package nz.rita.rcc.events

import nz.rita.rcc.models.api.ApiContentItem

/**
 * Created by jgut on 27.01.2018.
 */
class ClickInSideMenuEvent(val subsection: ApiContentItem,
                           val nestingLevel: MutableList<Int>)