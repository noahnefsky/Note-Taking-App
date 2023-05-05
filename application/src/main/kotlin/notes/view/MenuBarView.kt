//Copyright (c) 2023. John Huang, Noah Nefsky, Jamie Levinson, Aryaman Goel

package notes.view

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.layout.VBox
import javafx.stage.Stage
import notes.model.Model

class MenuBarView(model: Model): IView, VBox() {
    private val noteNameTextField = TextField().apply {
        promptText = "Type Name Here"
    }
    private val createButton = Button("Create")
    private val sortLabel = Label("Sort:").apply {
        padding = Insets(5.0)
    }
    private val sortChoiceBox = ChoiceBox(FXCollections.observableArrayList("Last Modified", "Date Created", "Alphabetical"))
    private val searchTextField = TextField().apply {
        promptText = "Search"
    }
    val toolBar = ToolBar()

    var model = model // PUT THIS BECAUSE NOT APPEARING AS USABLE IN UPDATE ???

    init {
        searchTextField.textProperty().addListener { _, _, newValue ->
            if (searchTextField.text.isNotEmpty()) {
                model.setCurrentSearch(searchTextField.text)
            } else {
                model.setIsSearch(false)
            }
        }
        createButton.isFocusTraversable = false

        //set tooltip for the create button to show its shortcut
        val tooltip = Tooltip("Create new note (Ctrl + N)")
        createButton.tooltip = tooltip

        //set the shortcut for create button to be Ctrl+N
        val keyCodeCombination = KeyCodeCombination(KeyCode.N, KeyCodeCombination.SHORTCUT_DOWN)
        val sceneButton = model.getMS().scene
        sceneButton.setOnKeyPressed { event ->
            if (keyCodeCombination.match(event)) {
                createButton.fire()
                event.consume()
            }
        }

        //create a note and show it on screen when Ctrl N or clicked
        createButton.setOnAction {
            println("Created new note")
            val stage = Stage()
            stage.title = noteNameTextField.text
            val nv = NoteView(model, model.getNumOfNotes(), "")
            stage.scene = Scene(nv, 375.0, 300.0)
            stage.minWidth = 180.0
            stage.minHeight = 130.0
            stage.show()
            model.addSticky(noteNameTextField.text, stage)
        }

        //dropdown box for sorting
        sortChoiceBox.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            when (sortChoiceBox.value) {
                "Last Modified" -> model.sortOnModified()
                "Date Created" -> model.sortOnCreated()
                "Alphabetical" -> model.sortOnName()
            }
        }

        toolBar.items.addAll(noteNameTextField, createButton, Separator(), sortLabel,sortChoiceBox, searchTextField)
        this.children.add(toolBar)
    }

    override fun update() {
        this.children.clear()
        this.children.add(toolBar)
    }
}