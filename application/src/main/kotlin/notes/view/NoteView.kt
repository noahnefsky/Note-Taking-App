//Copyright (c) 2023. John Huang, Noah Nefsky, Jamie Levinson, Aryaman Goel

package notes.view

import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.input.KeyEvent
import javafx.scene.layout.*
import javafx.scene.web.*
import notes.model.Model
import java.time.LocalDateTime

internal class NoteView (private val model: Model, id: Int, dataGiven: String): VBox(), IView {
    var textInputBox = HTMLEditor()
    var newId = id
    var data = dataGiven

    init {
        //retrieve and apply data from model for the given id
        val dataGotten = model.getStickyData(id)
        if (dataGotten == "") textInputBox.htmlText = data
        else textInputBox.htmlText = dataGotten

        //add event handlers for textInputBox to save the text as data whenever edited
        textInputBox.addEventHandler(KeyEvent.ANY, EventHandler<Event?> {
            model.changeModified(id, LocalDateTime.now())
            model.updateStickyData(id, textInputBox.htmlText)
            println("$id was modified!")
        })
        textInputBox.addEventHandler(ActionEvent.ANY, EventHandler<Event?> {
            model.changeModified(id, LocalDateTime.now())
            model.updateStickyData(id, textInputBox.htmlText)
            println("$id was modified!")
        })

        //design specifications
        VBox.setVgrow(textInputBox, Priority.ALWAYS)
        HBox.setHgrow(textInputBox, Priority.ALWAYS)
        this.children.addAll(
            textInputBox
        )
    }
    override fun update() {
    }
}
