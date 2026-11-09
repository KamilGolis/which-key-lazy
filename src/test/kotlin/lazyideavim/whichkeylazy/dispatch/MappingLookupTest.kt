package lazyideavim.whichkeylazy.dispatch

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.maddyhome.idea.vim.api.injector
import com.maddyhome.idea.vim.command.MappingMode
import com.maddyhome.idea.vim.key.MappingOwner
import com.maddyhome.idea.vim.vimscript.model.datatypes.VimString
import javax.swing.KeyStroke

class MappingLookupTest : BasePlatformTestCase() {

    override fun tearDown() {
        try {
            injector.keyGroup.removeKeyMapping(MappingOwner.Plugin.get(OWNER))
            injector.variableService.clear()
        } finally {
            super.tearDown()
        }
    }

    fun testConfigOrderIsPreservedInLookup() {
        injector.variableService.storeGlobalVariable("mapleader", VimString(" "))
        listOf('z', 'a', 'm').forEach { key ->
            injector.keyGroup.putKeyMapping(
                setOf(MappingMode.NORMAL),
                listOf(KeyStroke.getKeyStroke(' '), KeyStroke.getKeyStroke(key)),
                MappingOwner.Plugin.get(OWNER),
                listOf(KeyStroke.getKeyStroke('x')),
                false
            )
        }

        val result = MappingLookup.getNestedEntries(
            listOf(KeyStroke.getKeyStroke(' ')),
            MappingMode.NORMAL
        )

        assertNotNull(result)
        assertEquals(listOf("z", "a", "m"), result!!.entries.keys.toList())
    }

    private companion object {
        const val OWNER = "whichkey-lookup-test"
    }
}
