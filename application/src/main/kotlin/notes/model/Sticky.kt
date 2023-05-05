//Copyright (c) 2023. John Huang, Noah Nefsky, Jamie Levinson, Aryaman Goel

package notes.model

import javafx.stage.Stage
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
class Sticky(ID: Int, nameGiven: String, xGiven: Int, yGiven: Int, dataGiven: String,
             modifiedLast: LocalDateTime, created: LocalDateTime, stageGiven: Stage) {
    private val standardLength = 500 //CHANGE
    private val standardHeight = 600 //CHANGE

    private var x = yGiven
    private var y = xGiven
    private var data = dataGiven
    private var length = standardLength //length of the Sticky note
    private var height = standardHeight // height of the Sticky note
    private var isCollapsed = false
    private var isFullscreen = false
    private var name = nameGiven
    private var stage = stageGiven
    var id = ID
    var modified = modifiedLast
    var createdTime = created

    fun getName(): String {
        return name
    }

    fun setName(newName: String) {
        name = newName
        stage.title=newName
    }



    fun getXCoordinate(): Int {
        return x
    }

    fun setXCoordinate(newX: Int) {
        x = newX
    }

    fun getYCoordinate(): Int {
        return y
    }

    fun setYCoordinate(newY: Int) {
        y = newY
    }

    fun getData(): String {
        return data
    }

    fun setData(newData: String) {
        data = newData

    }

    fun getLength(): Int {
        return length
    }

    fun setLength(newLength: Int) {
        length = newLength
    }

    fun getHeight(): Int {
        return height
    }

    fun setHeight(newHeight: Int) {
        height = newHeight
    }


    fun getIsCollapsed(): Boolean {
        return isCollapsed
    }

    fun setIsCollapsed(newIsCollapsed: Boolean) {
        isCollapsed = newIsCollapsed
    }

    fun getIsFullscreen(): Boolean {
        return isFullscreen
    }

    fun setIsFullscreen(newIsFullscreen: Boolean) {
        isFullscreen = newIsFullscreen
    }

    fun getStage(): Stage{
        return stage
    }

    fun setDarkMode(darkMode: Boolean){
        if (darkMode){
            stage.scene.stylesheets.clear()
            stage.scene.stylesheets.add("dark-theme.css")
        }else{
            stage.scene.stylesheets.clear()
        }
    }

}