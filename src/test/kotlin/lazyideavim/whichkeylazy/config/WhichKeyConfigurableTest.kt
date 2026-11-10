package lazyideavim.whichkeylazy.config

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.awt.Component
import java.awt.Container
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JSpinner

class WhichKeyConfigurableTest : BasePlatformTestCase() {

    fun testBuildsPlacementGapAndSortingControls() {
        val panel = WhichKeyConfigurable().createComponent()

        assertEquals(1, find(panel) { it is JComboBox<*> }.size)
        assertEquals(2, find(panel) { it is JSpinner }.size)
        assertEquals(
            1,
            find(panel) { it is JCheckBox && it.text == "Sort alphabetically" }.size
        )
    }

    private fun find(root: Component, predicate: (Component) -> Boolean): List<Component> {
        val result = mutableListOf<Component>()
        fun visit(component: Component) {
            if (predicate(component)) result.add(component)
            if (component is Container) {
                component.components.forEach(::visit)
            }
        }
        visit(root)
        return result
    }
}
