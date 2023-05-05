//Copyright (c) 2023. John Huang, Noah Nefsky, Jamie Levinson, Aryaman Goel

package notes.model

import javafx.scene.Scene
import javafx.stage.Stage
import notes.view.IView
import notes.view.NoteView
import org.apache.pdfbox.pdmodel.PDDocument
import org.fit.pdfdom.PDFDomTree
import java.io.File
import java.io.PrintWriter
import java.io.Writer
import java.time.LocalDateTime
import kotlin.collections.ArrayList

class Model (ms: Stage){
    private var groups = mutableListOf<Group>()
    private val stickies = mutableListOf<Sticky>()
    private val views = ArrayList<IView>()
    private var numOfNotes = 0
    private var isSearch = false
    private var currentSearch = ""
    private var darkMode = false
    var currentSort = 0 // 1 = modified, 2 = created, 3 = alphabetical (name)
    private var mainStage = ms

    fun setMS(stage: Stage) {
        mainStage = stage
    }
    fun getMS(): Stage {
        return mainStage
    }
    fun getStickies(): MutableList<Sticky> {
        return stickies
    }

    fun getGroups(): MutableList<Group> {
        return groups
    }

    fun getIsSearch(): Boolean {
        return isSearch
    }

    fun changeModified(id: Int, modifiedLast: LocalDateTime) {
        for (sticky in stickies) {
            if (sticky.id == id) {
                sticky.modified = modifiedLast
            }
        }
        for (group in groups) {
            val groupStickies = group.getStickies()
            for (sticky in groupStickies) {
                if (sticky.id == id) {
                    group.modified = modifiedLast
                }
            }
        }

        when (currentSort) {
            1 -> {
                sortOnModified()
            }
            2 -> {
                sortOnCreated()
            }
            3 -> {
                sortOnName()
            }
        }
    }

    fun updateStickyData(id: Int, data: String) {
        //updates data string for given id
        for (sticky in stickies) {
            if (sticky.id == id) {
                sticky.setData(data)
            }
        }
        notifyView()
    }


    fun getStickyData(id: Int): String {
        //retrieves data string for given id
        for (sticky in stickies) {
            if (sticky.id == id) {
                return sticky.getData()
            }
        }
        return ""
    }

    fun setIsSearch(isSearchGiven: Boolean) {
        isSearch = isSearchGiven
        notifyView()
    }
    fun getCurrentSearch(): String {
        return currentSearch
    }
    fun setCurrentSearch(searchCriteria: String) {
        currentSearch = searchCriteria
        isSearch = true
        notifyView()
    }

    fun addGroup(name: String) {
        groups.add(Group(name, LocalDateTime.now(), LocalDateTime.now()))
        println("In model, groups.size: ${groups.size}")
        notifyView()
    }

    fun addGroup(name: String, newModified: LocalDateTime, newCreated: LocalDateTime): Group {
        var newGroup = Group(name, newModified, newCreated)
        groups.add(newGroup)
        println("In model, groups.size: ${groups.size}")
        notifyView()
        return newGroup
    }

    fun sortOnModified() {
        currentSort = 1

        stickies.sortByDescending { it.modified }
        groups.sortByDescending { it.modified }
        notifyView()
    }

    fun sortOnCreated() {

        stickies.sortBy { it.createdTime }
        groups.sortBy { it.createdTime }
        notifyView()
    }

    fun sortOnName() {
        stickies.sortBy { it.getName() }
        groups.sortBy{ it.name }
        println(stickies)
        notifyView()
    }

    fun addSticky(name: String, stage: Stage, data: String, modified: LocalDateTime,
    created: LocalDateTime) {
        val newSticky = Sticky(numOfNotes, name, 0, 0, "", modified, created, stage)
        newSticky.setData(data)
        println(LocalDateTime.now())
        // numOfNotes = id
        // 0 and 0 are x and y coordinates to save stickies being up but will add later
        // dataGiven gets modified by person and will change everytime someone closes the sticky
        //--> gets saved to file
        stickies.add(newSticky)
        numOfNotes++
        notifyView()
    }
    fun addSticky(name: String, stage: Stage) {
        val newSticky = Sticky(numOfNotes, name, 0, 0, "", LocalDateTime.now(), LocalDateTime.now(), stage)
        println(LocalDateTime.now())
        // numOfNotes = id
        // 0 and 0 are x and y coordinates to save stickies being up but will add later
        // dataGiven gets modified by person and will change everytime someone closes the sticky
        //--> gets saved to file
        stickies.add(newSticky)
        numOfNotes++
        notifyView()
    }


    fun removeSticky(id: Int){
        for (i in 0 until stickies.size) {
            if (stickies[i].id == id) {
                stickies.removeAt(i)
                break
            }
        }
        for (group in groups) {
            val g_stickies = group.getStickies()
            for (i in 0 until g_stickies.size) {
                if (g_stickies[i].id == id) {
                    g_stickies.removeAt(i)
                    break
                }
            }
        }
        notifyView()
    }
    fun removeGroup(group: Group){
        groups.remove(group)
        notifyView()
    }
    // view management
    fun addView(view: IView) {
        views.add(view)
    }
    fun removeView(view: IView) {
        views.remove(view)
    }
    fun notifyView() {
        for (view in views) {
            view.update()
        }
    }
    // NEED TO ADD OTHER FUNCTIONS LIKE REMOVE
    fun getNumOfNotes() : Int{
        return numOfNotes
    }
    fun addNumOfNotes(){
        numOfNotes++
    }
    fun getStage(id: Int): Stage{
        for (sticky in stickies) {
            if (sticky.id == id) return sticky.getStage()
        }
        return Stage()
    }

    fun renameSticky(i: Int, name: String){
        for (sticky in stickies) {
            if (sticky.id == i) {
                sticky.setName(name)
                break
            }
        }
        notifyView()
    }

    fun addToGroup(id: Int, group: Group) {
        val gStickies = group.getStickies()
        for (sticky in gStickies) {
            if (sticky.id == id) {
                return
            }
        }
        for (sticky in stickies) {
            if (sticky.id == id) {
                group.addSticky(sticky)
                break
            }
        }
        notifyView()
    }
    fun removeFromGroup(sticky: Sticky, group: Group) {
        group.getStickies().remove(sticky)
        notifyView()
    }
    fun addToGroupNoNotify(id: Int, group: Group) {
        val g_stickies = group.getStickies()
        for (sticky in g_stickies) {
            if (sticky.id == id) {
                return
            }
        }
        for (sticky in stickies) {
            if (sticky.id == id) {
                group.addSticky(sticky)
                break
            }
        }
    }
    fun importPDF(file: File) {
        val pdf: PDDocument = PDDocument.load(file)
        val output: Writer = PrintWriter("temp.html", "utf-8")
        PDFDomTree().writeText(pdf, output)
        output.close()
        pdf.close()
        val noteContent = File("temp.html").readText()
        File("temp.html").delete()

        val stage = Stage()
        stage.setTitle(file.name)
        stage.scene = Scene(
            NoteView(this, this.getNumOfNotes(), noteContent),
            375.0,
            300.0
        )
        stage.minWidth = 180.0
        stage.minHeight = 130.0
        addSticky(file.name, stage, noteContent, LocalDateTime.now(), LocalDateTime.now())
    }
    fun importTXT(file: File) {

        val noteContent = file.readText()
        val stage = Stage()
        stage.setTitle(file.name)
        stage.scene = Scene(
            NoteView(this, this.getNumOfNotes(), noteContent),
            375.0,
            300.0
        )
        stage.minWidth = 180.0
        stage.minHeight = 130.0
        addSticky(file.name, stage, noteContent, LocalDateTime.now(), LocalDateTime.now())
    }

    fun setStickyDarkMode(darkMode1: Boolean){
        for (sticky in stickies) {
            sticky.setDarkMode(darkMode1)
        }
    }
}
