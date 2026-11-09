package lazyideavim.whichkeylazy.ui

import lazyideavim.whichkeylazy.model.KeyNode
import lazyideavim.whichkeylazy.model.WhichKeySettings

/**
 * Orders entries for display.
 *
 * When [WhichKeySettings.sortAlphabetically] is off, the original mapping
 * order is preserved (IdeaVim/config-file insertion order).
 */
object KeyNodeSorter {

    fun sort(nodes: List<KeyNode>, settings: WhichKeySettings): List<KeyNode> {
        if (!settings.sortAlphabetically) return nodes

        return if (settings.sortGroupsFirst) {
            val groups = nodes.filterIsInstance<KeyNode.GroupNode>().sortedBy { it.key }
            val actions = nodes.filterIsInstance<KeyNode.ActionNode>().sortedBy { it.key }
            groups + actions
        } else {
            nodes.sortedBy { it.key }
        }
    }
}
