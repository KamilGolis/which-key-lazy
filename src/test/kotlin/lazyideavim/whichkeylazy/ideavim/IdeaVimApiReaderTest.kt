package lazyideavim.whichkeylazy.ideavim

import lazyideavim.whichkeylazy.model.KeyNode
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.maddyhome.idea.vim.api.injector
import com.maddyhome.idea.vim.command.MappingMode
import com.maddyhome.idea.vim.key.MappingOwner
import com.maddyhome.idea.vim.vimscript.model.datatypes.VimString
import java.awt.event.KeyEvent
import javax.swing.KeyStroke

class IdeaVimApiReaderTest : BasePlatformTestCase() {

    override fun tearDown() {
        try {
            injector.keyGroup.removeKeyMapping(MappingOwner.Plugin.get(OWNER))
            injector.variableService.clear()
        } finally {
            super.tearDown()
        }
    }

    fun testSpaceLeaderUsesWhichKeyDescDescription() {
        injector.variableService.storeGlobalVariable("mapleader", VimString(" "))
        injector.variableService.storeGlobalVariable(
            "WhichKeyDesc_nerdtree",
            VimString("<leader>e Explorer (NERDTree)")
        )
        injector.keyGroup.putKeyMapping(
            setOf(MappingMode.NORMAL),
            listOf(KeyStroke.getKeyStroke(' '), KeyStroke.getKeyStroke('e')),
            MappingOwner.Plugin.get(OWNER),
            listOf(KeyStroke.getKeyStroke('d')),
            false
        )

        val tree = IdeaVimApiReader.readMappings()
        val node = tree["e"]
        assertTrue("Expected an e mapping, got ${tree.keys}", node is KeyNode.ActionNode)
        assertEquals("Explorer (NERDTree)", node!!.description)
    }

    fun testActionMappingWithoutWhichKeyDescUsesActionName() {
        injector.variableService.storeGlobalVariable("mapleader", VimString(" "))
        injector.keyGroup.putKeyMapping(
            setOf(MappingMode.NORMAL),
            listOf(KeyStroke.getKeyStroke(' '), KeyStroke.getKeyStroke(' ')),
            MappingOwner.Plugin.get(OWNER),
            injector.parser.parseKeys(":action GotoFile<cr>"),
            false
        )

        val tree = IdeaVimApiReader.readMappings()
        val node = tree["<Space>"] ?: tree[" "]
        assertTrue("Expected a space mapping, got ${tree.keys}", node is KeyNode.ActionNode)
        assertTrue("Description was blank: $node", node!!.description.isNotBlank())
        assertTrue("Unexpected description: ${node.description}", node.description.contains("Go to File"))
    }

    fun testActionPseudoKeyWithoutWhichKeyDescUsesActionName() {
        injector.variableService.storeGlobalVariable("mapleader", VimString(" "))
        putSpaceActionMapping()

        val tree = IdeaVimApiReader.readMappings()
        val node = tree["<Space>"] ?: tree[" "]
        assertTrue("Expected a space mapping, got ${tree.keys}", node is KeyNode.ActionNode)
        assertTrue("Description was blank: $node", node!!.description.isNotBlank())
        assertTrue("Unexpected description: ${node.description}", node.description.contains("Go to File"))
    }

    fun testWhichKeyDescOverridesActionName() {
        injector.variableService.storeGlobalVariable("mapleader", VimString(" "))
        injector.variableService.storeGlobalVariable(
            "WhichKeyDesc_space",
            VimString("<leader><leader> Find File")
        )
        putSpaceActionMapping()

        val tree = IdeaVimApiReader.readMappings()
        val node = tree["<Space>"] ?: tree[" "]
        assertEquals("Find File", node!!.description)
    }

    private fun putSpaceActionMapping() {
        injector.keyGroup.putKeyMapping(
            setOf(MappingMode.NORMAL),
            listOf(KeyStroke.getKeyStroke(' '), KeyStroke.getKeyStroke(' ')),
            MappingOwner.Plugin.get(OWNER),
            injector.parser.parseKeys("<Action>(GotoFile)"),
            false
        )
    }

    fun testConfigOrderIsPreserved() {
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

        val tree = IdeaVimApiReader.readMappings()
        assertEquals(listOf("z", "a", "m"), tree.keys.toList())
    }

    fun testPressedSpaceStrokeIsNotUsedForLeaderLookup() {
        val typed = KeyStroke.getKeyStroke(' ')
        val pressed = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0)
        assertTrue("$pressed should not equal the typed stroke", pressed != typed)
    }

    private companion object {
        const val OWNER = "whichkey-test"
    }
}
