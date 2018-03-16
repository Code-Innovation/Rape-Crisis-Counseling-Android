package nz.rita.rcc.events

import nz.rita.rcc.models.api.ApiContentItem
import nz.rita.rcc.models.api.ApiMainResponse

/**
 * Created by jgut on 26.01.2018.
 */
class OpenSubsectionEvent(val title: String,
                          val subsection: ApiContentItem,
                          val nestingLevel: MutableList<Int>,
                          val sectionApiObject: ApiMainResponse)