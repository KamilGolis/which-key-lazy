package lazyideavim.whichkeylazy.ui

import lazyideavim.whichkeylazy.model.KeyNode

/**
 * Applies g:WhichKeyDesc_ descriptions to entries from any mode.
 *
 * The prebuilt leader tree is normal-mode only, so visual and
 * operator-pending entries need this pass to pick up the same labels.
 */
object DescriptionOverrides {

    fun apply(
        entries: Map<String, KeyNode>,
        descriptions: Map<String, String>,
        postLeaderPrefix: String?
    ): Map<String, KeyNode> {
        if (postLeaderPrefix == null || descriptions.isEmpty()) return entries

        return entries.mapValues { (key, node) ->
            val description = descriptions[postLeaderPrefix + key]
                ?.takeIf { it.isNotBlank() }
                ?: return@mapValues node

            when (node) {
                is KeyNode.ActionNode -> node.copy(description = description)
                is KeyNode.GroupNode -> node.copy(
                    description = description,
                    icon = IconResolver.resolveGroupIcon(description)
                )
            }
        }
    }
}
