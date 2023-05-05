//Copyright (c) 2023. John Huang, Noah Nefsky, Jamie Levinson, Aryaman Goel

package notes.view

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import notes.model.Model

internal class StickyListView (model: Model) : IView, TabPane() {
    private val stickyListTab = StickyListTabView(model)
    private val groupListTab = GroupListTabView(model)
    override fun update() {
        stickyListTab.update()
        groupListTab.update()
    }
    init {
        tabs.add(Tab("Standard", stickyListTab))
        tabs.add(Tab("Groups", groupListTab))
    }


}