//Copyright (c) 2023. John Huang, Noah Nefsky, Jamie Levinson, Aryaman Goel

package notes.view

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.util.Callback
import notes.model.Group
import notes.model.Model
import notes.model.Sticky


internal class GroupListTabView (private val model: Model) : IView, VBox() {
    private var currentGroups = mutableListOf<Group>()
    private val groupName = TextField().apply {
        promptText = "Type Name Here"
    }
    val toolBar = ToolBar()
    private val createB = Button("Create Group").apply{
        this.onMouseClicked = EventHandler {
            model.addGroup(groupName.text)
        }
    }
    //VBox of TitledPanes, of ListViews
    fun getCurrentGroups() {

        currentGroups.clear()
        if (model.getIsSearch()) {
            val searchCriteria = model.getCurrentSearch()
            val allGroups = model.getGroups()
            for (group in allGroups) {
                if (group.name.contains(searchCriteria)) {
                    currentGroups.add(group)
                }
            }
        } else {
            currentGroups.addAll(model.getGroups())
        }
    }
    fun createView() {
        this.children.add(toolBar)
        currentGroups.forEach {
            val element = TitledPane().apply {
                isExpanded = false
                alignment = Pos.CENTER
            }
            val titleContent = HBox().apply {
                padding = Insets(0.0, 10.0, 0.0, 35.0)
                minWidthProperty().bind(element.widthProperty())
                alignment = Pos.CENTER
            }
            val spacerRegion = HBox().apply {
                maxWidth = Double.MAX_VALUE
                HBox.setHgrow(this, Priority.ALWAYS)
            }

            val content = VBox()
            val stickyList = ListView<Sticky>()

            val stickies = it.getStickies()
            for (sticky in stickies) {
                stickyList.items.add(sticky)
            }
            val deleteButton = Button("Delete").apply {
                val gTemp = it
                setOnAction {
                    model.removeGroup(gTemp)
                }
            }
            titleContent.children.addAll(Label(it.name), spacerRegion, deleteButton)
            element.graphic = titleContent
            stickyList.cellFactory = Callback<ListView<Sticky>, ListCell<Sticky>> { stickyList ->
                object : ListCell<Sticky>() {
                    var rename = ""
                    val borderPaneItem = BorderPane()
                    override fun updateItem(item: Sticky?, empty: Boolean) {
                        super.updateItem(item, empty)
                        if (!empty) {
                            val text1 = Text(item!!.getName())
                            BorderPane.setAlignment(text1, Pos.CENTER_LEFT)
                            borderPaneItem.left = text1
                            val option1 = MenuItem("Delete").apply {
                                setOnAction {
                                    model.removeSticky(item.id)
                                }
                            }
                            val option2 = MenuItem("Rename").apply {
                                setOnAction {
                                    startEdit()

                                }
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
                            BorderPane.setAlignment(text1, Pos.CENTER_RIGHT)
                            borderPaneItem.right=dotsButton
                            graphic = borderPaneItem
                        }else{
                            graphic = null
                            text = null

                        }
                    }
                    override fun startEdit() {
                        val textField = TextField(text)
                        textField.setOnKeyPressed { event ->
                            if (event.code == KeyCode.ESCAPE) {
                                cancelEdit()
                            }
                            if (event.code == KeyCode.ENTER) {
                                rename = textField.text
                                model.renameSticky(item.id,rename)
                                rename = ""
                                graphic = borderPaneItem
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

            content.children.add(stickyList)
            element.content = content
            stickyList.onMouseClicked = EventHandler { event ->
                if (event.clickCount == 2 && stickyList.items.size>0) {
                    val selectedItem = stickyList.selectionModel.selectedItem
                    val id = selectedItem.id
                    println("You double-clicked ${selectedItem}")
                    // add your logic here to do something with the selected item
                    model.getStage(id).show()
                    model.getStage(id).toFront()
                }
            }
            this.children.add(element)
        }
    }
    override fun update() {
        children.clear()
        getCurrentGroups()
        println("in update: ${model.getGroups()}")
        println("in update: ${model.getGroups().size}")
        println("in update, numgroups: ${currentGroups.size}")
        createView()
        //nothing
    }
    init {
        toolBar.items.addAll(groupName, createB)

        getCurrentGroups()
        createView()
    }
}


