//Copyright (c) 2023. John Huang, Noah Nefsky, Jamie Levinson, Aryaman Goel
package notes.view


import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import notes.model.Model

class BottomBarView(model: Model): IView, VBox() {
    private val toolBar = ToolBar()
    private val fileChooser = FileChooser()

    //this imports a pdf  file or txt file into a note
    private val importButton = Button("Import File (.pdf or .txt)").apply {
        setOnAction{
            val file = fileChooser.showOpenDialog(Stage())
            if (file != null) {
                if (file.name.endsWith(".pdf")) {
                    model.importPDF(file)
                } else {
                    model.importTXT(file)
                }
            }
        }
    }
    //this will set the mode to dark or light
    private val toggleDarkMode = ToggleButton("Dark Mode").apply {
        setOnAction {
            val ms = model.getMS()
            if (isSelected) {
                ms?.scene.stylesheets.clear()
                ms?.scene.stylesheets.add("dark-theme.css")
                model.setStickyDarkMode(true)
                text = "Light Mode"
            } else {
                ms.scene.stylesheets.clear()
                model.setStickyDarkMode(false)
                text = "Dark Mode"
            }
        }
    }

    init {
        val extFilter = FileChooser.ExtensionFilter("PDF or TEXT Files (*.txt or *.pdf)", "*.txt", "*.pdf")
        fileChooser.extensionFilters.add(extFilter)
        toolBar.items.addAll(toggleDarkMode, importButton)
        this.children.add(toolBar)
    }

    override fun update() {
        this.children.clear()
        this.children.add(toolBar)
    }

}