package lazyideavim.whichkeylazy

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class ActionDescriptionsTest : BasePlatformTestCase() {

    fun testGotoFileHasPresentationText() {
        val text = ActionDescriptions.lookup("GotoFile")
        assertTrue(text.isNotBlank())
        assertFalse(text == "???")
    }
}
