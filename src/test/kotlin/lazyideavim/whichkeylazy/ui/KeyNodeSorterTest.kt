package lazyideavim.whichkeylazy.ui

import lazyideavim.whichkeylazy.model.KeyNode
import lazyideavim.whichkeylazy.model.WhichKeySettings
import org.junit.Assert.assertEquals
import org.junit.Test

class KeyNodeSorterTest {

    private val input = listOf<KeyNode>(
        action("z"),
        group("b"),
        action("a"),
        group("c")
    )

    @Test
    fun `config order is preserved when alphabetical sorting is off`() {
        val sorted = KeyNodeSorter.sort(input, WhichKeySettings(sortAlphabetically = false))
        assertEquals(listOf("z", "b", "a", "c"), sorted.map { it.key })
    }

    @Test
    fun `groups come first when alphabetical sorting is on`() {
        val sorted = KeyNodeSorter.sort(
            input,
            WhichKeySettings(sortAlphabetically = true, sortGroupsFirst = true)
        )
        assertEquals(listOf("b", "c", "a", "z"), sorted.map { it.key })
    }

    @Test
    fun `all entries are alphabetical when groups-first is off`() {
        val sorted = KeyNodeSorter.sort(
            input,
            WhichKeySettings(sortAlphabetically = true, sortGroupsFirst = false)
        )
        assertEquals(listOf("a", "b", "c", "z"), sorted.map { it.key })
    }

    private fun group(key: String) = KeyNode.GroupNode(
        key = key,
        description = key,
        icon = null,
        children = emptyMap()
    )

    private fun action(key: String) = KeyNode.ActionNode(
        key = key,
        description = key,
        icon = null,
        actionId = key
    )
}
