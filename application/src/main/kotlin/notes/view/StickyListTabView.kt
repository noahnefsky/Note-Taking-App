//Copyright (c) 2023. John Huang, Noah Nefsky, Jamie Levinson, Aryaman Goel

package notes.view

import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.util.Callback
import notes.model.Model
import notes.model.Sticky


internal class StickyListTabView (private val model: Model) : IView, VBox()  {
    private val stickyList = ListView<Sticky>()
    fun showNormal() {
        val stickies = model.getStickies()
        for (sticky in stickies) {
            println("show normal: " + sticky.getName())
            stickyList.items.add(sticky)
        }
    }


    fun showFromSearch() {
        val searchCriteria = model.getCurrentSearch()
        val stickies = model.getStickies()
        for (sticky in stickies) {
            if (sticky.getName().contains(searchCriteria)) {
                stickyList.items.add(sticky)
            }
        }
    }
    init {
        //this adds a ... button to each cell in listview for renaming, deleting and grouping
        stickyList.cellFactory = Callback<ListView<Sticky>, ListCell<Sticky>> { stickyList ->
            object : ListCell<Sticky>() {
                var rename = ""
                val borderPane = BorderPane()
                override fun updateItem(item: Sticky?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (!empty) {
                        val text1 = Text(item!!.getName())
                        BorderPane.setAlignment(text1, javafx.geometry.Pos.CENTER_LEFT)
                        borderPane.left = text1
                        val option1 = MenuItem("Delete").apply {
                            setOnAction {
                                model.removeSticky(item.id)
                            }
                            accelerator = KeyCodeCombination(KeyCode.D, KeyCodeCombination.SHORTCUT_DOWN)
                        }
                        val option2 = MenuItem("Rename").apply {
                            setOnAction {
                                startEdit()

                            }
                            accelerator = KeyCodeCombination(KeyCode.R, KeyCodeCombination.SHORTCUT_DOWN)
                        }

                        val option3 = Menu("Add to group")
                        val subOptions3 = mutableListOf<MenuItem>()
                        val groups = model.getGroups()
                        val option4 = Menu("Remove from group")
                        val subOptions4 = mutableListOf<MenuItem>()
                        groups.forEach { g ->
                            if (g.getStickies().contains(item)) {
                                subOptions4.add(MenuItem(g.name).apply {
                                    setOnAction {
                                        model.removeFromGroup(item, g)
                                    }
                                })
                            } else {
                                subOptions3.add(MenuItem(g.name).apply {
                                    setOnAction {
                                        model.addToGroup(item.id, g)
                                    }
                                })
                            }
                        }
                        option3.items.addAll(subOptions3)
                        option4.items.addAll(subOptions4)

                        val dotsButton = MenuButton("...").apply {
                            items.addAll(option1, option2, option3, option4) }
                        BorderPane.setAlignment(text1, javafx.geometry.Pos.CENTER_RIGHT)
                        borderPane.right=dotsButton
                        graphic = borderPane
                    }else{
                        graphic = null
                        text = null

                    }
                }
                //this is for renaming the note title
                override fun startEdit() {
                    val textField = TextField(text)
                    textField.setOnAction {
                    }
                    textField.setOnKeyPressed { event ->
                        if (event.code == KeyCode.ESCAPE) {
                            cancelEdit()
                        }
                        if (event.code == KeyCode.ENTER) {
                            rename = textField.text
                            model.renameSticky(item.id,rename)
                            rename = ""
                            graphic = borderPane
                            cancelEdit()
                        }
                    }
                    text = null
                    graphic = textField
                    textField.requestFocus()
                    textField.selectAll()
                }
            }
        }

        this.children.add(stickyList)


        stickyList.setOnKeyPressed { event ->
            when (event.code) {
                KeyCode.ENTER -> {
                    val selectedItemIndex = stickyList.selectionModel.selectedIndex
                    if (selectedItemIndex >= 0) {
                        val selectedItem = stickyList.selectionModel.selectedItem
                        val id = selectedItem.id
                        println("You double-clicked ${selectedItem}")
                        // add your logic here to do something with the selected item
                        model.getStage(id).show()
                        model.getStage(id).toFront()
                    }
                }
            }
        }
        //this opens the selected note from the listview
        stickyList.onMouseClicked = EventHandler { event ->
            val selectedItemIndex = stickyList.selectionModel.selectedIndex
            if (event.clickCount == 2 && selectedItemIndex>=0) {
                val selectedItem = stickyList.selectionModel.selectedItem
                val id = selectedItem.id
                println("You double-clicked ${selectedItem}")
                // add your logic here to do something with the selected item
                model.getStage(id).show()
                model.getStage(id).toFront()
            }
        }
    }
    override fun update() {
        stickyList.items.clear()
        if (model.getIsSearch()) {
            showFromSearch()
        } else {
            showNormal()
        }
        stickyList.refresh()
    }


}