package lazyideavim.whichkeylazy.ui

import lazyideavim.whichkeylazy.model.KeyNode
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class DescriptionOverridesTest : BasePlatformTestCase() {

    fun testAppliesDescriptionToActionNode() {
        val entries = mapOf(
            "(" to KeyNode.ActionNode(
                key = "(",
                description = "<Plug>(surround)",
                icon = null,
                actionId = ""
            )
        )

        val result = DescriptionOverrides.apply(entries, mapOf("s(" to "Add ( )"), "s")

        assertEquals("Add ( )", result["("]!!.description)
    }

    fun testAppliesDescriptionToGroupNode() {
        val entries = mapOf(
            "s" to KeyNode.GroupNode(
                key = "s",
                description = "+s",
                icon = null,
                children = emptyMap()
            )
        )

        val result = DescriptionOverrides.apply(entries, mapOf("ss" to "Search"), "s")

        assertEquals("Search", result["s"]!!.description)
    }

    fun testMissingDescriptionLeavesEntryUnchanged() {
        val node = KeyNode.ActionNode("(", "raw", null, "")
        val entries = mapOf("(" to node)

        val result = DescriptionOverrides.apply(entries, mapOf("s(" to "Add ( )"), "f")

        assertEquals("raw", result["("]!!.description)
    }

    fun testNullPrefixLeavesEntriesUnchanged() {
        val node = KeyNode.ActionNode("(", "raw", null, "")
        val entries = mapOf("(" to node)

        val result = DescriptionOverrides.apply(entries, mapOf("s(" to "Add ( )"), null)

        assertEquals("raw", result["("]!!.description)
    }
}
